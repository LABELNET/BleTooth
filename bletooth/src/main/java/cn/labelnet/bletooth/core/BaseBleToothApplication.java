package cn.labelnet.bletooth.core;

import android.app.Application;

import cn.labelnet.bletooth.BleTooth;
import cn.labelnet.bletooth.core.bean.BleDevice;

/**
 * @Package cn.labelnet.bletooth
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 1:23 PM 2/15/2017
 * @Desc Desc
 * 中心设备进行操作蓝牙
 * use application init
 * (1) init bletooth
 * (2) bletooth connect and disconnect
 *
 * example :
 * @see cn.labelnet.bletooth.core.simple.SimpleBleToothApplication
 */

public abstract class BaseBleToothApplication extends Application {

    protected BleTooth mBleTooth;
    private BleGattCallback mBleGattCallback;

    public BleTooth getBleTooth() {
        return mBleTooth;
    }

    /**
     * init ble
     */
    public void initBleTooth() {
        mBleTooth = BleTooth.getInstance(getApplicationContext());
        mBleGattCallback = getBleGattCallback();
    }

    /**
     * init callback
     *
     * @return
     */
    protected abstract BleGattCallback getBleGattCallback();


    /**
     * conn ble
     *
     * @param bleDevice ble bean
     * @param isAuto    is auto conn
     */
    public void connect(BleDevice bleDevice, boolean isAuto) {
        mBleTooth.connect(bleDevice, isAuto, mBleGattCallback);
    }

    /**
     * conn ble
     *
     * @param bleDevice ble bean
     */
    public void connect(BleDevice bleDevice) {
        mBleTooth.connect(bleDevice, false, mBleGattCallback);
    }

    /**
     * dis conn ble
     */
    public void disConnect() {
        if (mBleTooth.isConnBleTooth()) {
            mBleTooth.disconnect();
        }
    }


}
