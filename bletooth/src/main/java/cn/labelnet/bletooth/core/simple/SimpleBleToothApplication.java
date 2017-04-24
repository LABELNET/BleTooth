package cn.labelnet.bletooth.core.simple;

import cn.labelnet.bletooth.core.BaseBleToothApplication;
import cn.labelnet.bletooth.core.BleGattCallback;

/**
 * @Package cn.labelnet.bletooth.core.simple
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 1:28 PM 2/15/2017
 * @Desc Desc
 * use application init type : example
 */

public class SimpleBleToothApplication extends BaseBleToothApplication {


    //example


    @Override
    public void onCreate() {
        super.onCreate();
        //initBle();
    }

    /**
     * (1)initBle in MainActivity OnCreate() method
     * (2)initBle in Application onCreate() method
     */
    public void initBle() {
        initBleTooth();
    }

    /**
     * You need override getBleGattCallBack method, support BleToothGattCallback !
     *
     * @return
     * @see SimpleBleToothGattCallback
     */
    @Override
    protected BleGattCallback getBleGattCallback() {
        return new SimpleBleToothGattCallback(getApplicationContext());
    }
}
