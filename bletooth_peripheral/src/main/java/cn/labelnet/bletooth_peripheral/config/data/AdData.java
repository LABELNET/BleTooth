package cn.labelnet.bletooth_peripheral.config.data;

import android.bluetooth.le.AdvertiseData;

/**
 * @Package cn.labelnet.bletooth_peripheral.config.data
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 5:47 PM 2/15/2017
 * @Desc Desc
 * Example :
 */

public class AdData extends BaseAdData {
    @Override
    protected AdvertiseData addOperation(AdvertiseData.Builder builder) {
        return builder.build();
    }
}
