package cn.labelnet.bletooth.core.bean;

import java.util.List;

/**
 * @Package cn.labelnet.bletooth.core.bean
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 7:45 PM 2/9/2017
 * @Desc Desc
 */

public class BleCharacteristicUUID {

    private String characteristicUUID;

    private List<String> descriptorUUIDs;

    public BleCharacteristicUUID(String characteristicUUID, List<String> descriptorUUIDs) {
        this.characteristicUUID = characteristicUUID;
        this.descriptorUUIDs = descriptorUUIDs;
    }

    public String getCharacteristicUUID() {
        return characteristicUUID;
    }

    public void setCharacteristicUUID(String characteristicUUID) {
        this.characteristicUUID = characteristicUUID;
    }

    public List<String> getDescriptorUUIDs() {
        return descriptorUUIDs;
    }

    public void setDescriptorUUIDs(List<String> descriptorUUIDs) {
        this.descriptorUUIDs = descriptorUUIDs;
    }

    @Override
    public String toString() {
        return "{ \" CharacteristicUUID\" : " + characteristicUUID + ", \"DescriptorUUIDs\" : " + descriptorUUIDs +"}";
    }
}
