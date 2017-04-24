package cn.labelnet.bletooth_peripheral;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.os.Build;

import cn.labelnet.bletooth_peripheral.callback.AdCallBack;
import cn.labelnet.bletooth_peripheral.callback.BleAdvertiseCallback;
import cn.labelnet.bletooth_peripheral.callback.BleGattServerCallback;
import cn.labelnet.bletooth_peripheral.config.data.AdData;
import cn.labelnet.bletooth_peripheral.config.setting.AdSettings;
import cn.labelnet.bletooth_peripheral.util.LogUtil;

/**
 * @Package cn.labelnet.bletooth_peripheral
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 4:09 PM 2/15/2017
 * @Desc Desc
 * Android Ble Peripheral
 * (1) Android 5.0 support BlueTooth Ble Peripheral，so System Version must be >= 21
 * (2)
 */

public class BlePeripheral implements BleAdvertiseCallback.OnBleAdListener {

    //init bluetooth
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothManager mBluetoothManager;
    //init ad
    private BluetoothLeAdvertiser mBluetoothLeAdvertiser;
    private BleAdvertiseCallback mBleAdvertiseCallback;

    private Context mContext;
    //init server
    private BluetoothGattServer mBluetoothGattServer;
    private BleGattServerCallback mBleGattServerCallback;

    // all callback
    private AdCallBack adCallBack;

    /**

     * ble peripheral
     *
     * @param applicationContext application context
     * @param isShowLog          true : show log
     */
    public BlePeripheral(Context applicationContext, boolean isShowLog, AdCallBack adCallBack) {
        if (adCallBack == null) {
            throw new IllegalArgumentException("AdCallBack is null");
        }
        this.mContext = applicationContext;
        this.adCallBack = adCallBack;
        // init log
        LogUtil.init(isShowLog);
        initBle();
    }

    private void initBle() {
        if (!isBuildLOLLIPOP()) {
            throw new IllegalStateException("Your android system version too low , must be >= 21 (Android 5.0)");
        }

        mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        mBluetoothLeAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();

        if (mBluetoothLeAdvertiser == null) {
            try {
                throw new Exception("Your Android Device not support ble peripheral , BluetoothLeAdvertiser is null !");
            } catch (Exception e) {
                e.printStackTrace();
                adCallBack.showMsg("Your Android Device not support ble peripheral , BluetoothLeAdvertiser is null !");
            }
        }
    }

    /**
     * System VERSION
     *
     * @return API is >=21
     */
    private boolean isBuildLOLLIPOP() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
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

    /**
     * start peripheral
     *
     * @param settings
     * @param adData
     */
    public void startPeripheral(AdSettings settings, AdData adData) {
        startPeripheral(settings, adData, null);
    }

    public void startPeripheral(AdSettings settings, AdData adData, AdData scanResponseAdData) {

        if (settings == null) {
            throw new IllegalArgumentException("startPeripheral method AdSettings is null");
        }

        if (adData == null) {
            throw new IllegalArgumentException("startPeripheral method AdData is null");
        }

        AdvertiseSettings advertiseSettings = settings.build();
        AdvertiseData advertiseData = adData.build();
        AdvertiseData scanAdData = null;
        if (scanResponseAdData != null) {
            scanAdData = scanResponseAdData.build();
        }
        mBleAdvertiseCallback = new BleAdvertiseCallback();
        mBleAdvertiseCallback.setOnBleAdListener(this);
        if (scanAdData != null) {
            mBluetoothLeAdvertiser.startAdvertising(advertiseSettings, advertiseData, scanAdData, mBleAdvertiseCallback);
        } else {
            mBluetoothLeAdvertiser.startAdvertising(advertiseSettings, advertiseData, mBleAdvertiseCallback);
        }
    }

    /**
     * stop peripheral
     */
    public void stopPeripheral() {
        if (mBleAdvertiseCallback != null) {
            mBluetoothLeAdvertiser.stopAdvertising(mBleAdvertiseCallback);
        }
    }

    @Override
    public void onStartSuccess() {
        initServer();
    }

    @Override
    public void onStartFailure(String errorMsg) {
        LogUtil.e(errorMsg);
        if (adCallBack != null) {
            adCallBack.showMsg(errorMsg);
        }
    }


    public void initServer() {
        mBleGattServerCallback = new BleGattServerCallback(adCallBack);
        mBluetoothGattServer = mBluetoothManager.openGattServer(mContext, mBleGattServerCallback);
        mBleGattServerCallback.setBluetoothGattServer(mBluetoothGattServer);
        adCallBack.onStartSuccess();
    }

    /**
     * add bluetooth service
     *
     * @param gattService gatt
     */
    public void addGattService(BluetoothGattService gattService) {
        if (gattService != null) {
            mBluetoothGattServer.addService(gattService);
        } else {
            throw new IllegalArgumentException("addGattService method params is null!");
        }
    }




}
