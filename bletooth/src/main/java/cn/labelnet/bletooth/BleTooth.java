package cn.labelnet.bletooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.os.Build;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import cn.labelnet.bletooth.async.AsyncCenter;
import cn.labelnet.bletooth.core.BleGattCallback;
import cn.labelnet.bletooth.core.BleScanCallBack;
import cn.labelnet.bletooth.core.bean.BleDevice;
import cn.labelnet.bletooth.core.bean.BleService;
import cn.labelnet.bletooth.core.conn.BleConnStatus;
import cn.labelnet.bletooth.core.conn.BleToothBleGattCallBack;
import cn.labelnet.bletooth.core.scan.BleBlueToothTest;
import cn.labelnet.bletooth.core.scan.ble.BleToothBleScanCallback;
import cn.labelnet.bletooth.core.scan.le.BleToothLeScanCallBack;
import cn.labelnet.bletooth.core.simple.SimpleBleScanResultCallback;
import cn.labelnet.bletooth.core.simple.SimpleLeScanResultCallBack;
import cn.labelnet.bletooth.core.simple.SimpleScanAndConnCallBack;
import cn.labelnet.bletooth.data.BaseOperation;
import cn.labelnet.bletooth.data.OnOperationListener;
import cn.labelnet.bletooth.data.notify.GattNotifyCharacteristicOperation;
import cn.labelnet.bletooth.data.notify.GattNotifyDescriptorOperation;
import cn.labelnet.bletooth.data.read.GattReadCharacteristicOperation;
import cn.labelnet.bletooth.data.read.GattReadDescriptorOpertation;
import cn.labelnet.bletooth.data.read.GattReadRemoteRSSI;
import cn.labelnet.bletooth.data.write.GattWriteCharacteristicOperation;
import cn.labelnet.bletooth.data.write.GattWriteDescriptorOperation;
import cn.labelnet.bletooth.util.ClsBleUtil;
import cn.labelnet.bletooth.util.LogUtil;

/**
 * @Package cn.labelnet
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 5:58 PM 2/5/2017
 * @Desc
 *
 */

public class BleTooth implements BleToothBleScanCallback.OnScanCompleteListener
        , BleToothLeScanCallBack.OnScanCompleteListener
        , BleToothBleGattCallBack.OnConnStatusListener {

    private static final String TAG = BleBlueToothTest.class.getSimpleName();

    private Context mContext;

    //bluetooth
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothManager mBluetoothManager;
    private BluetoothGatt mBluetoothGatt;
    private BluetoothDevice mBlueToothDevice;
    private BleDevice mBleDevice;
    //scan
    private BleToothBleScanCallback scanCallback;
    //scan  5.0 scanner
    private BluetoothLeScanner mBluetoothLeScaner;
    private BleToothLeScanCallBack leScanCallBack;
    //conn
    private int connCount = 0;
    private int timeOutCount = 0;
    private AtomicBoolean isAutoConn = new AtomicBoolean(false);
    private BleGattCallback gattCallBack;
    //bluetooth gatt service filter result
    List<BleService> bleServices;

    //control
    private AtomicBoolean isConnBle = new AtomicBoolean(false);
    private AsyncCenter mAsyncCenter;

    private static BleTooth mInstance;

    public static BleTooth getInstance(Context context) {
        if (mInstance == null) {
            synchronized (BleBlueToothTest.class) {
                if (mInstance == null) {
                    mInstance = new BleTooth(context);
                }
            }
        }
        return mInstance;
    }


    /**
     * init bluetooth
     *
     * @param context Application Context
     */
    public BleTooth(Context context) {
        this.mContext = context.getApplicationContext();
        mAsyncCenter = AsyncCenter.getInstance();
        initBlueTooth();
    }

    private void initBlueTooth() {
        mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (isBuildLOLLIPOP()) {
            mBluetoothLeScaner = mBluetoothAdapter.getBluetoothLeScanner();
        }
    }

    //==================================== SCAN ===================================================


    /**
     * start scan must be params is BleScanCallBack
     *
     * @param bleScanCallBack 5.0 and 4.3 scan callback
     */
    public void startScan(final BleScanCallBack bleScanCallBack) {

        if (bleScanCallBack == null) {
            throw new IllegalArgumentException("StartScan BleScanCallBack is Null");
        }

        if (isBuildLOLLIPOP()) {
            //5.0
            LogUtil.v("start scan use android 5.0 Le");
            leScanCallBack = new SimpleLeScanResultCallBack(bleScanCallBack);
            startScan(leScanCallBack);
        } else {
            LogUtil.v("start scan use android 4.3 BLe");
            scanCallback = new SimpleBleScanResultCallback(bleScanCallBack);
            startScan(scanCallback);
        }
    }

    /**
     * start scan
     *
     * @param callback LeCallback
     */
    private void startScan(final BleToothBleScanCallback callback) {
        this.scanCallback = callback;
        boolean startResult = mBluetoothAdapter.startLeScan(scanCallback);
        if (startResult) {
            callback.setOnScanCompleteListener(this);
            scanCallback.onStartTimmer();
        } else {
            stopScan(scanCallback);
        }
    }

    /**
     * android 5.0 scan
     * start scan ble
     * filter need override @see BleToothLeScanCallBack#getScanFilters() method
     * setting need override @see BleToothLeScanCallBack#getScanSettings(ScanSettings) method
     *
     * @param leScanCallBack android 5.0
     */
    private void startScan(final BleToothLeScanCallBack leScanCallBack) {
        if (leScanCallBack == null) {
            throw new IllegalArgumentException("start Scan callback is null");
        }
        this.leScanCallBack = leScanCallBack;
        startScan(leScanCallBack.getScanFilters()
                , leScanCallBack.getScanSettings(new ScanSettings.Builder())
                , leScanCallBack);
        leScanCallBack.setOnScanCompleteListener(this);
        leScanCallBack.onStartTimmer();
    }


    private void startScan(List<ScanFilter> filters, ScanSettings settings,
                           final ScanCallback callback) {
        if (mBluetoothLeScaner != null) {
            mBluetoothLeScaner.startScan(filters, settings, callback);
        } else {
            throw new IllegalArgumentException("Don't Support BLE BlueToothLeScanner");
        }
    }

    /**
     * stop scan
     *
     * @param callback BleToothBleScanCallback
     */
    private void stopScan(BleToothBleScanCallback callback) {
        callback.onStopTimmer();
        mBluetoothAdapter.stopLeScan(callback);
    }


    /**
     * android 5.0
     * stop scan
     *
     * @param leScanCallBack
     */
    private void stopScan(final BleToothLeScanCallBack leScanCallBack) {
        if (mBluetoothLeScaner != null) {
            leScanCallBack.onStopTimmer();
            mBluetoothLeScaner.stopScan(leScanCallBack);
        }
    }

    @Override
    public void onScanFinish() {
        LogUtil.v("Over Scan Finish !");
        if (isBuildLOLLIPOP()) {
            if (leScanCallBack != null) {
                stopScan(leScanCallBack);
            }
        } else {
            if (scanCallback != null) {
                stopScan(scanCallback);
            }
        }
    }

    //==================================== CONN ===================================================

    /**
     * conn blue
     *
     * @param bleDevice             chip bean
     * @param isAutoConn            isAuto
     * @param bluetoothGattCallback callback
     */
    public synchronized void connect(BleDevice bleDevice, boolean isAutoConn, BleGattCallback bluetoothGattCallback) {
        if (bleDevice == null) {
            throw new IllegalArgumentException("BleDevice Bean is null!");
        }

        if (bluetoothGattCallback == null) {
            throw new IllegalArgumentException("BleToothBleGattCallBack is null!");
        }

        this.mBleDevice = bleDevice;
        this.mBlueToothDevice = bleDevice.getBluetoothDevice();
        this.isAutoConn.set(isAutoConn);
        this.gattCallBack = bluetoothGattCallback;
        bluetoothGattCallback.setOnConnStatusListener(this);
        mBluetoothGatt = mBlueToothDevice.connectGatt(mContext, isAutoConn, bluetoothGattCallback);
    }

    @Override
    public void onFail() {
        connCount++;
        if (connCount == 5) {
            gattCallBack.setBleConnStatus(BleConnStatus.fail);
            isConnBle.set(false);
            return;
        }
        disconnect();
        connect(mBleDevice, isAutoConn.get(), gattCallBack);
    }

    @Override
    public void onSuccess() {
        connCount = 0;
        timeOutCount = 0;
        isConnBle.set(true);
    }

    @Override
    public void onTimeOut() {
        timeOutCount++;
        if (timeOutCount == 5) {
            gattCallBack.setBleConnStatus(BleConnStatus.conntimeout);
            ClsBleUtil.cancelBondProcess(mBlueToothDevice);
            isConnBle.set(false);
            return;
        }
        disconnect();
        connect(mBleDevice, isAutoConn.get(), gattCallBack);
    }

    @Override
    public void onFilterGattService(List<BleService> bleServices) {
        this.bleServices = bleServices;
    }

    /**
     * disconnect, refresh and close bluetooth gatt.
     */
    public void disconnect() {
        if (mBluetoothGatt != null) {
            mBluetoothGatt.disconnect();
            ClsBleUtil.refreshDeviceCache(mBluetoothGatt);
            mBluetoothGatt.close();
        }
    }

    /**
     * is conn ble tooth
     *
     * @return
     */
    public boolean isConnBleTooth() {
        return isConnBle.get();
    }

    //==================================== Scan and Conn =========================================


    /**
     * scan conn
     *
     * @param mac                   mac address
     * @param bluetoothGattCallback conn callback
     * @param onScanAndConnListener scan listener
     */
    public void scanAndConnect(String mac
            , final BleGattCallback bluetoothGattCallback
            , SimpleScanAndConnCallBack.OnScanAndConnListener onScanAndConnListener) {
        scanAndConnect(mac, false, bluetoothGattCallback, onScanAndConnListener);
    }

    /**
     * scan conn
     *
     * @param mac                   mac address
     * @param isAutoConn            auto conn
     * @param bluetoothGattCallback conn callback
     * @param onScanAndConnListener scan listener
     */
    public void scanAndConnect(final String mac
            , final boolean isAutoConn
            , final BleGattCallback bluetoothGattCallback
            , SimpleScanAndConnCallBack.OnScanAndConnListener onScanAndConnListener) {

        if (mac == null || mac.split(":").length != 6) {
            throw new IllegalArgumentException("MAC is null or error! ");
        }
        this.gattCallBack = bluetoothGattCallback;
        SimpleScanAndConnCallBack scanAndConnCallBack = new SimpleScanAndConnCallBack(mac) {
            @Override
            protected void onConnect(BleDevice bleDevice) {
                mBleDevice = bleDevice;
                mBlueToothDevice = bleDevice.getBluetoothDevice();
                connect(bleDevice, isAutoConn, bluetoothGattCallback);
            }
        };
        scanAndConnCallBack.setOnScanAndConnListener(onScanAndConnListener);
        startScan(scanAndConnCallBack);
    }

    public BluetoothGatt getBluetoothGatt() {
        return mBluetoothGatt;
    }

    public BleDevice getBleDevice() {
        return mBleDevice;
    }

    public Context getContext() {
        return mContext;
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return mBluetoothAdapter;
    }

    public List<BleService> getBleServices() {
        return bleServices;
    }

    /**
     * support Android L 5.0
     *
     * @return is support package android.bluetooth.le
     */
    private boolean isBuildLOLLIPOP() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * ======================================= Operation：read , write notify =====================
     */

    /**
     * post operation : do write read notify
     *
     * @param onOperationListener
     * @param baseOperation
     */
    private synchronized void postOperation(final OnOperationListener onOperationListener, BaseOperation baseOperation) {
        baseOperation.setmBluetoothGatt(getBluetoothGatt());
        baseOperation.setOperationListener(new OnOperationListener() {
            @Override
            public void onOperationStatus(OperationStatus status, String msg) {
                LogUtil.v("do operation : status" + status + " >> msg : " + msg);
                if (onOperationListener != null) {
                    onOperationListener.onOperationStatus(status, msg);
                }
            }
        });
        mAsyncCenter.postRunnable(baseOperation);
    }

    /**
     * write Characteristic
     *
     * @param mBluetoothGattCharacteristic need write
     * @param onOperationListener          status listener
     * @param values                       values( byte[] )
     */
    public void writeCharacteristic(BluetoothGattCharacteristic mBluetoothGattCharacteristic, OnOperationListener onOperationListener, byte... values) {
        GattWriteCharacteristicOperation writeCharacteristicOperation = new GattWriteCharacteristicOperation(mBluetoothGattCharacteristic, values);
        postOperation(onOperationListener, writeCharacteristicOperation);
    }

    public void writeCharacteristic(BluetoothGattCharacteristic mBluetoothGattCharacteristic, byte... values) {
        writeCharacteristic(mBluetoothGattCharacteristic, null, values);
    }

    /**
     * write Descriptor
     *
     * @param mBluetoothGattDescriptor need write
     * @param onOperationListener      status listener
     * @param values                   values(byte[])
     */
    public void writeDescriptor(BluetoothGattDescriptor mBluetoothGattDescriptor, OnOperationListener onOperationListener, byte... values) {
        GattWriteDescriptorOperation writeDescriptorOperation = new GattWriteDescriptorOperation(mBluetoothGattDescriptor, values);
        postOperation(onOperationListener, writeDescriptorOperation);
    }

    public void writeDescriptor(BluetoothGattDescriptor mBluetoothGattDescriptor, byte... values) {
        writeDescriptor(mBluetoothGattDescriptor, null, values);
    }

    /**
     * read Characteristic
     *
     * @param mBluetoothGattCharacteristic need read
     * @param onOperationListener          status listener
     */
    public void readCharacteristic(BluetoothGattCharacteristic mBluetoothGattCharacteristic, final OnOperationListener onOperationListener) {
        final GattReadCharacteristicOperation readCharacteristicOperation = new GattReadCharacteristicOperation(mBluetoothGattCharacteristic);
        //1. disable notification
        disableNotificationCharacteristic(mBluetoothGattCharacteristic, new OnOperationListener() {
            @Override
            public void onOperationStatus(OperationStatus status, String msg) {
                if(status.equals(OperationStatus.notifySuccess)){
                    //2. readCharacteristic
                    postOperation(onOperationListener, readCharacteristicOperation);
                }
            }
        });
//        postOperation(onOperationListener, readCharacteristicOperation);
    }

    public void readCharacteristic(BluetoothGattCharacteristic mBluetoothGattCharacteristic) {
        readCharacteristic(mBluetoothGattCharacteristic, null);
    }

    /**
     * read Descriptor
     *
     * @param mBluetoothGattDescriptor need read Descriptor
     * @param onOperationListener      status listener
     */
    public void readDescriptor(BluetoothGattDescriptor mBluetoothGattDescriptor, OnOperationListener onOperationListener) {
        GattReadDescriptorOpertation readDescriptorOpertation = new GattReadDescriptorOpertation(mBluetoothGattDescriptor);
        postOperation(onOperationListener, readDescriptorOpertation);
    }

    public void readDescriptor(BluetoothGattDescriptor mBluetoothGattDescriptor) {
        readDescriptor(mBluetoothGattDescriptor, null);
    }

    /**
     * read rssi
     *
     * @param onOperationListener
     */
    public void readRemoteRSSI(OnOperationListener onOperationListener) {
        GattReadRemoteRSSI readRemoteRSSI = new GattReadRemoteRSSI();
        postOperation(onOperationListener, readRemoteRSSI);
    }

    public void readRemoteRSSI() {
        readRemoteRSSI(null);
    }


    /**
     * setNotification for Characteristic
     *
     * @param mBluetoothGattCharacteristic need characteristic
     * @param enable                       open
     * @param uuid                         Descriptor UUID ENABLE_NOTIFICATION
     * @param onOperationListener          status listener
     */
    private void setNotificationCharacteristic(BluetoothGattCharacteristic mBluetoothGattCharacteristic
            , boolean enable, String uuid, OnOperationListener onOperationListener) {
        GattNotifyCharacteristicOperation notifyCharacteristicOperation = null;
        if (uuid == null || uuid.length() == 0) {
            notifyCharacteristicOperation = new GattNotifyCharacteristicOperation(mBluetoothGattCharacteristic, enable);
        } else {
            notifyCharacteristicOperation = new GattNotifyCharacteristicOperation(mBluetoothGattCharacteristic, enable, uuid);
        }
        postOperation(onOperationListener, notifyCharacteristicOperation);
    }

    /**
     * open Characteristic notification
     *
     * @param mBluetoothGattCharacteristic need
     * @param uuid                         descriptor uuid
     * @param onOperationListener          status
     */
    public void enableNotificationCharacteristic(BluetoothGattCharacteristic mBluetoothGattCharacteristic
            , String uuid, OnOperationListener onOperationListener) {
        setNotificationCharacteristic(mBluetoothGattCharacteristic, true, uuid, onOperationListener);
    }

    public void enableNotificationCharacteristic(BluetoothGattCharacteristic mBluetoothGattCharacteristic
            , String uuid) {
        enableNotificationCharacteristic(mBluetoothGattCharacteristic, uuid, null);
    }

    public void enableNotificationCharacteristic(BluetoothGattCharacteristic mBluetoothGattCharacteristic, OnOperationListener onOperationListener) {
        enableNotificationCharacteristic(mBluetoothGattCharacteristic, null, onOperationListener);
    }

    public void enableNotificationCharacteristic(BluetoothGattCharacteristic mBluetoothGattCharacteristic) {
        enableNotificationCharacteristic(mBluetoothGattCharacteristic, null, null);
    }

    /**
     * close Characteristic notification
     *
     * @param mBluetoothGattCharacteristic need
     * @param uuid                         descriptor uuid
     * @param onOperationListener          status listener
     */
    public void disableNotificationCharacteristic(BluetoothGattCharacteristic mBluetoothGattCharacteristic
            , String uuid, OnOperationListener onOperationListener) {
        setNotificationCharacteristic(mBluetoothGattCharacteristic, false, uuid, onOperationListener);
    }

    public void disableNotificationCharacteristic(BluetoothGattCharacteristic mBluetoothGattCharacteristic
            , String uuid) {
        disableNotificationCharacteristic(mBluetoothGattCharacteristic, uuid, null);
    }

    public void disableNotificationCharacteristic(BluetoothGattCharacteristic mBluetoothGattCharacteristic, OnOperationListener onOperationListener) {
        disableNotificationCharacteristic(mBluetoothGattCharacteristic, null, onOperationListener);
    }

    public void disableNotificationCharacteristic(BluetoothGattCharacteristic mBluetoothGattCharacteristic) {
        disableNotificationCharacteristic(mBluetoothGattCharacteristic, null, null);
    }

    /**
     * setNotification Descriptor enable/disable
     *
     * @param mBluetoothGattDescriptor need
     * @param enable                   open
     * @param onOperationListener      status
     */
    private void setNotificationDescriptor(BluetoothGattDescriptor mBluetoothGattDescriptor, boolean enable, OnOperationListener onOperationListener) {
        GattNotifyDescriptorOperation gattNotifyDescriptorOperation = new GattNotifyDescriptorOperation(mBluetoothGattDescriptor, enable);
        postOperation(onOperationListener, gattNotifyDescriptorOperation);
    }

    /**
     * enable Descriptor Notification
     *
     * @param mBluetoothGattDescriptor need
     * @param onOperationListener      status
     */
    public void enableNotificationDescriptor(BluetoothGattDescriptor mBluetoothGattDescriptor, OnOperationListener onOperationListener) {
        setNotificationDescriptor(mBluetoothGattDescriptor, true, onOperationListener);
    }

    public void enableNotificationDescriptor(BluetoothGattDescriptor mBluetoothGattDescriptor) {
        enableNotificationDescriptor(mBluetoothGattDescriptor, null);
    }

    /**
     * disable Descriptor Notification
     *
     * @param mBluetoothGattDescriptor need
     * @param onOperationListener      status
     */
    public void disableNotificationDescriptor(BluetoothGattDescriptor mBluetoothGattDescriptor, OnOperationListener onOperationListener) {
        setNotificationDescriptor(mBluetoothGattDescriptor, false, onOperationListener);
    }

    public void disableNotificationDescriptor(BluetoothGattDescriptor mBluetoothGattDescriptor) {
        disableNotificationDescriptor(mBluetoothGattDescriptor, null);
    }


}
