package cn.labelnet.bletooth.core.bean;

import android.bluetooth.BluetoothGattService;

import java.util.List;

/**
 * @Package cn.labelnet.bletooth.core.bean
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 8:22 PM 2/9/2017
 * @Desc Desc
 * <p>
 * BluetoothGattService bean
 */

public class BleService {

    private String UUID;
    private BluetoothGattService service;
    private List<BleCharacteristic> characteristics;

    public BleService(String UUID, BluetoothGattService service, List<BleCharacteristic> characteristics) {
        this.UUID = UUID;
        this.service = service;
        this.characteristics = characteristics;
    }

    public String getUUID() {
        return UUID;
    }

    public BluetoothGattService getService() {
        return service;
    }

    public List<BleCharacteristic> getCharacteristics() {
        return characteristics;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BleService)) return false;

        BleService that = (BleService) o;

        if (!UUID.equals(that.UUID)) return false;
        return service.equals(that.service);

    }

    @Override
    public int hashCode() {
        int result = UUID.hashCode();
        result = 31 * result + service.hashCode();
        return result;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public String toString() {
        String characts = (characteristics == null || characteristics.isEmpty() || characteristics.size() == 0) ? "[]" : characteristics.toString();
        return "{ \"ServiceUUID\":\"" + UUID + "\" , \"Characteristics\":" + characts + " }";
    }

    /**
     * builder
     */
    public static final class Builder {

        private String UUID;
        private BluetoothGattService service;
        private List<BleCharacteristic> characteristics;

        public Builder setUUID(String UUID) {
            this.UUID = UUID;
            return this;
        }

        public Builder setService(BluetoothGattService service) {
            this.service = service;
            return this;
        }

        public Builder setCharacteristics(List<BleCharacteristic> characteristics) {
            this.characteristics = characteristics;
            return this;
        }

        public BleService build() {
            return new BleService(UUID, service, characteristics);
        }
    }

}
