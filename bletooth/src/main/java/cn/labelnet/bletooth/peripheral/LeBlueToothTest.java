package cn.labelnet.bletooth.peripheral;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.os.Build;
import android.os.ParcelUuid;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import cn.labelnet.bletooth.core.bean.BleDevice;
import cn.labelnet.bletooth.core.bean.BleService;
import cn.labelnet.bletooth.core.conn.BleConnStatus;
import cn.labelnet.bletooth.core.conn.BleToothBleGattCallBack;
import cn.labelnet.bletooth.core.scan.BleBlueToothTest;
import cn.labelnet.bletooth.core.scan.le.BleToothLeScanCallBack;
import cn.labelnet.bletooth.peripheral.uuid.TestUUID;
import cn.labelnet.bletooth.util.ClsBleUtil;
import cn.labelnet.bletooth.util.LogUtil;

/**
 * @Package cn.labelnet.bletooth.le
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 9:51 AM 2/6/2017
 * @Desc Desc
 *
 * 5.0 :
 * Test : scan , conn , peripheral
 */

public class LeBlueToothTest implements BleToothLeScanCallBack.OnScanCompleteListener
        , BleToothBleGattCallBack.OnConnStatusListener {

    private Context mContext;

    //bluetooth
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothManager mBluetoothManager;
    private BluetoothGatt mBluetoothGatt;

    //scan
    private BluetoothLeScanner mBluetoothLeScaner;
    private BleToothLeScanCallBack leScanCallBack;

    //conn
    private BluetoothDevice mBlueToothDevice;
    private BleDevice mBleDevice;
    private int connCount = 0;
    private int timeOutCount = 0;
    private AtomicBoolean isAutoConn = new AtomicBoolean(false);
    private BleToothBleGattCallBack gattCallBack;
    private AtomicBoolean isConnBle = new AtomicBoolean(false);

    /**
     * use BluetoothGattServer init BluetoothGatt
     */
    private BluetoothGattServer mBluetoothGattServer;

    //Advertiser
    private BluetoothLeAdvertiser mBluetoothLeAdvertiser;


    private static LeBlueToothTest mInstance;

    /**
     * init
     *
     * @param context applicationContext
     * @return instance
     */
    public static LeBlueToothTest getInstance(Context context) {
        if (mInstance == null) {
            synchronized (BleBlueToothTest.class) {
                if (mInstance == null) {
                    mInstance = new LeBlueToothTest(context);
                }
            }
        }
        return mInstance;
    }

    public LeBlueToothTest(Context context) {
        this.mContext = context.getApplicationContext();
        initBlueTooth();
    }

    /**
     * System VERSION
     *
     * @return API is >=21
     */
    private boolean isBuildLOLLIPOP() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    private void initBlueTooth() {
        mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (isBuildLOLLIPOP()) {
            mBluetoothLeScaner = mBluetoothAdapter.getBluetoothLeScanner();
            mBluetoothLeAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
        }
    }

    /**
     * Support BLE Perpheral
     *
     * @return is support
     */
    public boolean isCheckSupportPerpheral() {
        if (isBuildLOLLIPOP()) {
            return mBluetoothAdapter.isMultipleAdvertisementSupported();
        }
        return false;
    }

    //========================================== SCAN ==============================================

    /**
     * start scan ble
     * filter need override @see BleToothLeScanCallBack#getScanFilters() method
     * setting need override @see BleToothLeScanCallBack#getScanSettings(ScanSettings) method
     *
     * @param leScanCallBack result
     */
    public void startScan(final BleToothLeScanCallBack leScanCallBack) {
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

    @Override
    public void onScanFinish() {
        LogUtil.v("LeBlueTooth Scan Finish");
        stopScan(leScanCallBack);
    }

    /**
     * stop scan
     *
     * @param leScanCallBack
     */
    public void stopScan(final BleToothLeScanCallBack leScanCallBack) {
        if (mBluetoothLeScaner != null) {
            leScanCallBack.onStopTimmer();
            mBluetoothLeScaner.stopScan(leScanCallBack);
        }
    }

    //========================================== CONN ==============================================

    /**
     *
     */
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

    public BluetoothGatt getmBluetoothGatt() {
        return mBluetoothGatt;
    }

    public BleDevice getmBleDevice() {
        return mBleDevice;
    }

    public Context getmContext() {
        return mContext;
    }

    public BluetoothAdapter getmBluetoothAdapter() {
        return mBluetoothAdapter;
    }


//========================================== 外围设备 Advertiser Init GattServer==============


    public void initGATTServer() {
        AdvertiseSettings settings = new AdvertiseSettings.Builder()
                .setConnectable(true)
                .build();

        AdvertiseData advertiseData = new AdvertiseData.Builder()
                .setIncludeDeviceName(true)
                .setIncludeTxPowerLevel(true)
                .build();

        AdvertiseData scanResponseData = new AdvertiseData.Builder()
                .addServiceUuid(new ParcelUuid(TestUUID.UUID_MOV_SERV))
                .setIncludeTxPowerLevel(true)
                .build();


        AdvertiseCallback callback = new AdvertiseCallback() {

            @Override
            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                LogUtil.v("BLE advertisement added successfully : initGATTServer success");
                initServices();
            }

            @Override
            public void onStartFailure(int errorCode) {
                LogUtil.e("Failed to add BLE advertisement, reason: " + errorCode);
            }
        };

        BluetoothLeAdvertiser bluetoothLeAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
        bluetoothLeAdvertiser.startAdvertising(settings, advertiseData, scanResponseData, callback);
    }

    private void initServices() {
        mBluetoothGattServer = mBluetoothManager.openGattServer(mContext, bluetoothGattServerCallback);
        BluetoothGattService service = new BluetoothGattService(TestUUID.UUID_MOV_SERV, BluetoothGattService.SERVICE_TYPE_PRIMARY);

        //Read : Add data Characteristic
        BluetoothGattCharacteristic characteristicDataRead = new BluetoothGattCharacteristic(TestUUID.UUID_MOV_DATA, BluetoothGattCharacteristic.PROPERTY_READ, BluetoothGattCharacteristic.PERMISSION_READ);
        BluetoothGattDescriptor descriptor = new BluetoothGattDescriptor(TestUUID.UUID_MOV__DESCRIPTOR, BluetoothGattCharacteristic.PERMISSION_WRITE);
        characteristicDataRead.addDescriptor(descriptor);
        service.addCharacteristic(characteristicDataRead);

        //Write :Add conf Characteristic
        BluetoothGattCharacteristic characteristicConfWrite = new BluetoothGattCharacteristic(TestUUID.UUID_MOV_CONF,
                BluetoothGattCharacteristic.PROPERTY_WRITE |
                        BluetoothGattCharacteristic.PROPERTY_READ |
                        BluetoothGattCharacteristic.PROPERTY_NOTIFY,
                BluetoothGattCharacteristic.PERMISSION_WRITE);
        service.addCharacteristic(characteristicConfWrite);

        //Period
        BluetoothGattCharacteristic characteristicPeriodWrite = new BluetoothGattCharacteristic(TestUUID.UUID_MOV_CONF,
                BluetoothGattCharacteristic.PROPERTY_WRITE |
                        BluetoothGattCharacteristic.PROPERTY_READ |
                        BluetoothGattCharacteristic.PROPERTY_NOTIFY,
                BluetoothGattCharacteristic.PERMISSION_WRITE);
        service.addCharacteristic(characteristicPeriodWrite);

        mBluetoothGattServer.addService(service);
        LogUtil.v("initServices ok");
    }

    private BluetoothGattServerCallback bluetoothGattServerCallback = new BluetoothGattServerCallback() {

        private int index = 10;

        @Override
        public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
            super.onConnectionStateChange(device, status, newState);
            LogUtil.v("onConnectionStateChange() : " + newState);
        }

        @Override
        public void onServiceAdded(int status, BluetoothGattService service) {
            super.onServiceAdded(status, service);
            LogUtil.v("onServiceAdded() : " + status);
        }

        @Override
        public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic) {
//            super.onCharacteristicReadRequest(device, requestId, offset, characteristic);
            LogUtil.v("onCharacteristicReadRequest() : " + device.getName() + " | " + device.getAddress());

            mBluetoothGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, characteristic.getValue());
        }

        @Override
        public void onCharacteristicWriteRequest(BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded, int offset, byte[] requestBytes) {
//            super.onCharacteristicWriteRequest(device, requestId, characteristic, preparedWrite, responseNeeded, offset, value);
            LogUtil.v("onCharacteristicWriteRequest() : " + device.getName() + " | " + device.getAddress());
            mBluetoothGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, requestBytes);
            //处理响应内容
            onResponseToClient(requestBytes, device, requestId, characteristic);
        }

        private void onResponseToClient(byte[] requestBytes, BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic) {
            LogUtil.e("onResponseToClient : 收到的数据 ： " + Arrays.toString(requestBytes));
            index++;
            characteristic.setValue("toClient : " + index);
            //重新相应
            mBluetoothGattServer.notifyCharacteristicChanged(device, characteristic, false);
        }

        @Override
        public void onDescriptorWriteRequest(BluetoothDevice device, int requestId, BluetoothGattDescriptor descriptor, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
            // super.onDescriptorWriteRequest(device, requestId, descriptor, preparedWrite, responseNeeded, offset, value);

            LogUtil.v("onDescriptorWriteRequest() : " + device.getName() + " | " + device.getAddress());
            mBluetoothGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, value);
        }

        @Override
        public void onDescriptorReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattDescriptor descriptor) {
//            super.onDescriptorReadRequest(device, requestId, offset, descriptor);
            LogUtil.v("onDescriptorReadRequest() : " + device.getName() + " | " + device.getAddress());
            mBluetoothGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, null);
        }

        @Override
        public void onNotificationSent(BluetoothDevice device, int status) {
//            super.onNotificationSent(device, status);
            LogUtil.v("onNotificationSent() : " + device.getName() + " | " + device.getAddress() + " STATUS : " + status);
        }

        @Override
        public void onExecuteWrite(BluetoothDevice device, int requestId, boolean execute) {
//            super.onExecuteWrite(device, requestId, execute);
            LogUtil.v("onExecuteWrite() : " + device.getName() + " | " + device.getAddress() + " requestId : " + requestId);
        }

        @Override
        public void onMtuChanged(BluetoothDevice device, int mtu) {
//            super.onMtuChanged(device, mtu);
            LogUtil.v("onMtuChanged() : " + device.getName() + " | " + device.getAddress() + " mtu : " + mtu);
        }


    };

    private void stopAdvertise() {
        if (mBluetoothLeAdvertiser != null) {
//            mBluetoothLeAdvertiser.stopAdvertising(m);
            mBluetoothLeAdvertiser = null;
        }
    }

}
