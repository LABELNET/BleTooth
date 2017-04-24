package cn.labelnet.bletooth.core.scan;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import cn.labelnet.bletooth.core.bean.BleDevice;
import cn.labelnet.bletooth.core.bean.BleService;
import cn.labelnet.bletooth.core.conn.BleConnStatus;
import cn.labelnet.bletooth.core.conn.BleToothBleGattCallBack;
import cn.labelnet.bletooth.core.BleScanCallBack;
import cn.labelnet.bletooth.core.scan.ble.BleScanFilter;
import cn.labelnet.bletooth.core.scan.ble.BleScanStatus;
import cn.labelnet.bletooth.core.scan.ble.BleToothBleScanCallback;
import cn.labelnet.bletooth.core.simple.SimpleBleScanResultCallback;
import cn.labelnet.bletooth.util.ClsBleUtil;
import cn.labelnet.bletooth.util.LogUtil;

/**
 * @Package cn.labelnet.bletooth.ble
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 3:27 PM 2/5/2017
 *
 * (1) 只允许一个BluetoothGattCallback
 * (2)
 *
 * 4.3
 * Test : scan , conn
 *
 */

public class BleBlueToothTest implements BleToothBleScanCallback.OnScanCompleteListener
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
    //conn
    private int connCount = 0;
    private int timeOutCount = 0;
    private AtomicBoolean isAutoConn = new AtomicBoolean(false);
    private BleToothBleGattCallBack gattCallBack;

    //control
    private Handler handler = new Handler(Looper.getMainLooper());
    private AtomicBoolean isConnBle = new AtomicBoolean(false);

    private static BleBlueToothTest mInstance;

    public static BleBlueToothTest getInstance(Context context) {
        if (mInstance == null) {
            synchronized (BleBlueToothTest.class) {
                if (mInstance == null) {
                    mInstance = new BleBlueToothTest(context);
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
    public BleBlueToothTest(Context context) {
        this.mContext = context.getApplicationContext();
        initBlueTooth();
    }

    private void initBlueTooth() {
        mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();

    }

    //==================================== SCAN ===================================================
    /**
     * start scan
     *
     * @param callback LeCallback
     */
    public void startScan(final BleToothBleScanCallback callback) {
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
     * stop scan
     * @param callback BleToothBleScanCallback
     */
    public void stopScan(BleToothBleScanCallback callback) {
        callback.onStopTimmer();
        mBluetoothAdapter.stopLeScan(callback);
    }

    @Override
    public void onScanFinish() {
        LogUtil.v("Over Scan Finish !");
        stopScan(scanCallback);
    }

    //==================================== CONN ===================================================

    /**
     * conn blue
     *
     * @param bleDevice             chip bean
     * @param isAutoConn            isAuto
     * @param bluetoothGattCallback callback
     */
    public synchronized void connect(BleDevice bleDevice, boolean isAutoConn, BleToothBleGattCallBack bluetoothGattCallback) {
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


    /**
     * scan and conn
     *
     * @param mac
     * @param isAutoConn
     * @param bluetoothGattCallback
     */
    public void scanAndConnect(final String mac, final boolean isAutoConn, final BleToothBleGattCallBack bluetoothGattCallback) {

        if (mac == null || mac.split(":").length != 6) {
            throw new IllegalArgumentException("MAC is null or error! ");
        }

        startScan(new SimpleBleScanResultCallback(new BleScanCallBack(3000) {
            @Override
            public void onScanDevicesData(List<BleDevice> bleDevices) {
                LogUtil.v("Ble : "+bleDevices);
                if (bleDevices.size() > 0) {
                    mBleDevice = bleDevices.get(0);
                    mBlueToothDevice = mBleDevice.getBluetoothDevice();
                    connect(mBleDevice, isAutoConn, bluetoothGattCallback);
                }
            }

            @Override
            public void onScanProcess(float process) {

            }

            @Override
            public void onScanStatus(BleScanStatus status) {

            }

            @Override
            public List<BleScanFilter> getScanFilter() {
                List<BleScanFilter> bleScanFilters = new ArrayList<>();
                bleScanFilters.add(new BleScanFilter.Builder().setDeviceMac(mac).build());
                return bleScanFilters;
            }
        }));
    }


}
