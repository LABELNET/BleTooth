package cn.labelnet.bletooth.data.filter;

import java.util.ArrayList;
import java.util.List;

import cn.labelnet.bletooth.core.bean.BleCharacteristicUUID;
import cn.labelnet.bletooth.core.bean.BleServiceUUID;

/**
 * @Package cn.labelnet.bletooth.data
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 7:23 PM 2/9/2017
 * @Desc Desc
 */

public class BleBluetoothUUIDFilter {

    List<BleServiceUUID> bleServiceUUIDs;

    public BleBluetoothUUIDFilter(List<BleServiceUUID> bleServiceUUIDs) {
        this.bleServiceUUIDs = bleServiceUUIDs;
    }

    public List<BleServiceUUID> getBleServiceUUIDs() {
        return bleServiceUUIDs;
    }

    /**
     * build list serivce
     */
    public static final class Builder {
        List<BleServiceUUID> bleServiceUUIDs;
        BuilderService mBuilderService;

        public Builder() {
            bleServiceUUIDs = new ArrayList<>();
        }

        public Builder addService(String serviceUUID) {
            mBuilderService.setServiceUUID(serviceUUID);
            return this;
        }

        public Builder addCharacteristic(String characterUUID, String... descriptorUUIDs) {
            mBuilderService.addBleCharacteristicUUIDs(characterUUID, descriptorUUIDs);
            return this;
        }

        public Builder addCharacteristic(String characterUUID) {
            mBuilderService.addBleCharacteristicUUIDs(characterUUID);
            return this;
        }

        //end
        public Builder endBuilderService() {
            bleServiceUUIDs.add(mBuilderService.build());
            return this;
        }

        //start
        public Builder startBuilderService() {
            mBuilderService = new BuilderService();
            return this;
        }

        public BleBluetoothUUIDFilter build() {
            return new BleBluetoothUUIDFilter(bleServiceUUIDs);
        }
    }


    /**
     * build ServiceUUID
     */
    private static final class BuilderService {

        private String serviceUUID;
        private List<BleCharacteristicUUID> bleCharacteristicUUIDs;

        public BuilderService() {
            bleCharacteristicUUIDs = new ArrayList<>();
        }

        public BuilderService setServiceUUID(String serviceUUID) {
            this.serviceUUID = serviceUUID;
            return this;

        }

        public BuilderService addBleCharacteristicUUIDs(String characteristicUUID, String... descriptorUUIDs) {
            BuilderCharacteristic builderCharacteristic = newBuilderCharacteristic();
            builderCharacteristic.setCharacteristicUUID(characteristicUUID);
            for (String descUUID : descriptorUUIDs) {
                builderCharacteristic.addDescriptorUUIDs(descUUID);
            }
            bleCharacteristicUUIDs.add(builderCharacteristic.build());
            return this;
        }

        public BuilderService addBleCharacteristicUUIDs(String characteristicUUID) {
            BuilderCharacteristic builderCharacteristic = newBuilderCharacteristic();
            builderCharacteristic.setCharacteristicUUID(characteristicUUID);
            bleCharacteristicUUIDs.add(builderCharacteristic.build());
            return this;
        }

        private BuilderCharacteristic newBuilderCharacteristic() {
            return new BuilderCharacteristic();
        }

        public BleServiceUUID build() {
            return new BleServiceUUID(serviceUUID, bleCharacteristicUUIDs);
        }
    }

    /**
     * build CharacteristicUUID
     */
    private static final class BuilderCharacteristic {

        private String characteristicUUID;
        private List<String> descriptorUUIDs;

        public BuilderCharacteristic() {
            descriptorUUIDs = new ArrayList<>();
        }

        public BuilderCharacteristic setCharacteristicUUID(String characteristicUUID) {
            this.characteristicUUID = characteristicUUID;
            return this;
        }

        public BuilderCharacteristic addDescriptorUUIDs(String descriptorUUID) {
            descriptorUUIDs.add(descriptorUUID);
            return this;
        }

        public BleCharacteristicUUID build() {
            return new BleCharacteristicUUID(characteristicUUID, descriptorUUIDs);
        }
    }

}
