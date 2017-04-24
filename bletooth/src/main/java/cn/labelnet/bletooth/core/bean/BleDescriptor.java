package cn.labelnet.bletooth.core.bean;

import android.bluetooth.BluetoothGattDescriptor;

/**
 * @Package cn.labelnet.bletooth.core.bean
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 8:31 PM 2/9/2017
 * @Desc Desc
 * Descriptor  bean
 */

public class BleDescriptor {
    private String UUID;
    private BluetoothGattDescriptor descriptor;

    public BleDescriptor(String UUID, BluetoothGattDescriptor descriptor) {
        this.UUID = UUID;
        this.descriptor = descriptor;
    }

    public String getUUID() {
        return UUID;
    }

    public BluetoothGattDescriptor getDescriptor() {
        return descriptor;
    }

    @Override
    public String toString() {
        return "{ \" DescUUID \" : \" " + UUID + " \" } ";
    }
}
