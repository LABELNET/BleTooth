package cn.labelnet.bletooth.core.bean;

import android.bluetooth.BluetoothGattCharacteristic;

import java.util.List;

/**
 * @Package cn.labelnet.bletooth.core.bean
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 8:27 PM 2/9/2017
 * @Desc
 * BleCharacteristic bean
 */

public class BleCharacteristic {

    private String UUID;
    private BluetoothGattCharacteristic characteristic;
    private List<BleDescriptor> descriptors;

    public BleCharacteristic(String UUID, BluetoothGattCharacteristic characteristic, List<BleDescriptor> descriptors) {
        this.UUID = UUID;
        this.characteristic = characteristic;
        this.descriptors = descriptors;
    }

    public String getUUID() {
        return UUID;
    }

    public BluetoothGattCharacteristic getCharacteristic() {
        return characteristic;
    }

    public List<BleDescriptor> getDescriptors() {
        return descriptors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BleCharacteristic)) return false;

        BleCharacteristic that = (BleCharacteristic) o;

        if (!UUID.equals(that.UUID)) return false;
        return characteristic.equals(that.characteristic);

    }

    @Override
    public String toString() {
        String descStr = (descriptors == null || descriptors.isEmpty() || descriptors.size() == 0) ? "[]" : descriptors.toString();
        return "{\"CharacteristicUUID\":\"" + UUID + "\",\"Descriptors\":" + descStr + "}";
    }

    @Override
    public int hashCode() {
        int result = UUID.hashCode();
        result = 31 * result + characteristic.hashCode();
        return result;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String UUID;
        private BluetoothGattCharacteristic characteristic;
        private List<BleDescriptor> descriptors;

        public Builder setUUID(String UUID) {
            this.UUID = UUID;
            return this;
        }

        public Builder setCharacteristic(BluetoothGattCharacteristic characteristic) {
            this.characteristic = characteristic;
            return this;
        }

        public Builder setDescriptors(List<BleDescriptor> descriptors) {
            this.descriptors = descriptors;
            return this;
        }

        public BleCharacteristic build() {
            return new BleCharacteristic(UUID, characteristic, descriptors);
        }

    }

}
