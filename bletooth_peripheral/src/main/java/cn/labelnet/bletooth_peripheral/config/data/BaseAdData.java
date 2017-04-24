package cn.labelnet.bletooth_peripheral.config.data;

import android.bluetooth.le.AdvertiseData;

/**
 * @Package cn.labelnet.bletooth_peripheral.config.data
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 5:30 PM 2/15/2017
 * @Desc Desc
 * 向外广播的数据包
 */

public abstract class BaseAdData {

    /**
     * AdvertiseData ： 广告数据包，设置要向外广播的出的内容
     * (1) max data length : 31bytes
     * (2) setIncludeTxPowerLevel ： rssi 信号强度
     * (3) setIncludeDeviceName : 设备名称
     * (4) addManufacturerData() : 添加厂商特定数据 Bluetooth SIG
     * (5) addServiceData() : 添加Gatt Service UUID and byte data
     */
    private AdvertiseData mAdvertiseData;

    //default
    private boolean isShowDeviceName = true;
    private boolean isShowTxPowerLevel = false;


    public AdvertiseData build() {
        AdvertiseData.Builder builder = new AdvertiseData.Builder();
        builder.setIncludeDeviceName(getIsShowDeviceName());
        builder.setIncludeTxPowerLevel(getIsShowTxPowerLevel());
        return addOperation(builder);
    }

    protected abstract AdvertiseData addOperation(AdvertiseData.Builder builder);

    protected boolean getIsShowTxPowerLevel() {
        return isShowTxPowerLevel;
    }

    protected boolean getIsShowDeviceName() {
        return isShowDeviceName;
    }

}
