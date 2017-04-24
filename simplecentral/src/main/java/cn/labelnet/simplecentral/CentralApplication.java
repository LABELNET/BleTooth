package cn.labelnet.simplecentral;

import android.content.Context;

import cn.labelnet.bletooth.core.BaseBleToothApplication;
import cn.labelnet.bletooth.core.BleGattCallback;
import cn.labelnet.simplecentral.ble.CentralBleGattCallback;

/**
 * @Package cn.labelnet.simplecentral
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 2:05 PM 2/15/2017
 * @Desc Desc
 */

public class CentralApplication extends BaseBleToothApplication {

    private Context mContext;

    private static CentralApplication instance;

    public static CentralApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        instance = (CentralApplication) mContext;
    }

    @Override
    protected BleGattCallback getBleGattCallback() {
        return new CentralBleGattCallback(mContext);
    }
}
