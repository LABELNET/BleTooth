package cn.labelnet.bletooth.core.scan.ble;

/**
 * @Package cn.labelnet.bletooth.core
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 4:20 PM 2/9/2017
 * @Desc Desc
 *  ble scan filter
 */

public class BleScanFilter {

    private String deviceName;
    private String deviceMac;

    public BleScanFilter(String deviceName, String deviceMac) {
        this.deviceName = deviceName;
        this.deviceMac = deviceMac;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getDeviceMac() {
        return deviceMac;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BleScanFilter)) return false;

        BleScanFilter that = (BleScanFilter) o;

        if (!deviceName.equals(that.deviceName)) return false;
        return deviceMac.equals(that.deviceMac);

    }

    @Override
    public int hashCode() {
        int result = deviceName.hashCode();
        result = 31 * result + deviceMac.hashCode();
        return result;
    }

    public static final class Builder{

        private String mDeviceName;
        private String mDeviceMac;

        public Builder setDeviceName(String deviceName){
            this.mDeviceName=deviceName;
            return this;
        }

        public Builder setDeviceMac(String deviceMac){
            this.mDeviceMac=deviceMac;
            return this;
        }

        public BleScanFilter build(){
            return new BleScanFilter(mDeviceName,mDeviceMac);
        }

    }

}
