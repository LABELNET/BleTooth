package cn.labelnet.bletooth_peripheral.config.setting;

import android.bluetooth.le.AdvertiseSettings;

import static android.bluetooth.le.AdvertiseSettings.ADVERTISE_MODE_LOW_POWER;
import static android.bluetooth.le.AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM;

/**
 * @Package cn.labelnet.bletooth_peripheral.config.setting
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 5:09 PM 2/15/2017
 * @Desc Desc
 */

public abstract class BaseAdSettings {


    //default value
    private int mPowerMode = ADVERTISE_MODE_LOW_POWER;
    private int mTxPowerLevel = ADVERTISE_TX_POWER_MEDIUM;
    private int mTimeoutMillis = 5000;
    private boolean mConnectable = true;

    /**
     * Advertise Settings ： 广告设置
     * (1) power mode ：功耗 : low , balance , high
     * (2) (TX) power level ：信号强度 : ultra , low , medium , high
     * (3) Connectable : 可连接性
     * (4) timeoutmills ： 超时时间
     */
    private AdvertiseSettings mAdvertiseSettings;

    public AdvertiseSettings build() {
        AdvertiseSettings.Builder builder = new AdvertiseSettings.Builder();
        builder.setAdvertiseMode(getAdPowerMode());
        builder.setTxPowerLevel(getAdTxPowerMode());
        builder.setConnectable(getAdConnectable());
        builder.setTimeout(getAdTimeOutMills());
        return mAdvertiseSettings = builder.build();
    }

    protected int getAdTimeOutMills() {
        return mTimeoutMillis;
    }

    protected boolean getAdConnectable() {
        return mConnectable;
    }

    protected int getAdTxPowerMode() {
        return mTxPowerLevel;
    }

    protected int getAdPowerMode() {
        return mPowerMode;
    }
}
