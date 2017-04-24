package cn.labelnet.bletooth.core.bean;

import java.util.List;

/**
 * @Package cn.labelnet.bletooth.core.bean
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 7:44 PM 2/9/2017
 * @Desc Desc
 */

public class BleServiceUUID {

    private String serviceUUID;

    private List<BleCharacteristicUUID> bleCharacteristicUUIDs;

    public BleServiceUUID(String serviceUUID, List<BleCharacteristicUUID> bleCharacteristicUUIDs) {
        this.serviceUUID = serviceUUID;
        this.bleCharacteristicUUIDs = bleCharacteristicUUIDs;
    }

    public String getServiceUUID() {
        return serviceUUID;
    }

    public void setServiceUUID(String serviceUUID) {
        this.serviceUUID = serviceUUID;
    }

    public List<BleCharacteristicUUID> getBleCharacteristicUUIDs() {
        return bleCharacteristicUUIDs;
    }

    public void setBleCharacteristicUUIDs(List<BleCharacteristicUUID> bleCharacteristicUUIDs) {
        this.bleCharacteristicUUIDs = bleCharacteristicUUIDs;
    }

    @Override
    public String toString() {
        return "{ \"ServiceUUID\" : " + serviceUUID + ", \"CharacteristicUUIDs\" : " + bleCharacteristicUUIDs + "}";
    }
}
