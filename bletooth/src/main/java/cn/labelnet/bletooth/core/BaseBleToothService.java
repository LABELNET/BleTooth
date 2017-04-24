package cn.labelnet.bletooth.core;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import cn.labelnet.bletooth.BleTooth;
import cn.labelnet.bletooth.core.bean.BleDevice;

/**
 * @Package cn.labelnet.bletooth
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 7:03 PM 2/8/2017
 * @Desc Desc
 * <p>
 * 中心设备进行操作蓝牙
 * use service init
 * (1)initBind
 * (2)initService
 *
 * example :
 * @see cn.labelnet.bletooth.core.simple.SimpleBleToothService
 * @see cn.labelnet.bletooth.core.simple.SimpleServiceBleApplication
 */

public abstract class BaseBleToothService extends Service {

    protected BleTooth mBleTooth;
    private BleGattCallback mBleGattCallback;

    private IBinder binder = new LocalBinder();

    public BleTooth getBleTooth() {
        return mBleTooth;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBleTooth = BleTooth.getInstance(getApplicationContext());
        mBleGattCallback = getBleGattCallback();
    }

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

    /**
     * get gatt callback
     *
     * @return conn callback
     */
    protected abstract BleGattCallback getBleGattCallback();

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        disConnect();
        return super.onUnbind(intent);
    }

    /**
     * bind
     */
    public class LocalBinder extends Binder {
        public BaseBleToothService getBleService() {
            return getBaseBleToothService();
        }
    }

    protected abstract BaseBleToothService getBaseBleToothService();

}
