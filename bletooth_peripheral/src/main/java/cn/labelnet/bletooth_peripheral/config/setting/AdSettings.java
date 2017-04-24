package cn.labelnet.bletooth_peripheral.config.setting;

/**
 * @Package cn.labelnet.bletooth_peripheral.config.setting
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 5:15 PM 2/15/2017
 * @Desc Desc
 * Example :
 * override BaseAdSettings Method !
 * use new AdSetting().build()
 */

public class AdSettings extends BaseAdSettings {


    @Override
    protected int getAdTimeOutMills() {
        return 5000;
    }

}
