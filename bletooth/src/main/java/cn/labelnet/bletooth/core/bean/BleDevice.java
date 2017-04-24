package cn.labelnet.bletooth.core.bean;

import android.bluetooth.BluetoothDevice;

/**
 * @Package cn.labelnet.bletooth.core.bean
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 6:15 PM 2/5/2017
 * @Desc Desc
 * 蓝牙实体 bean
 */

public class BleDevice {

    private BluetoothDevice bluetoothDevice;
    private String deviceName;
    private String deviceMac;
    private int rssi;

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceMac() {
        return deviceMac;
    }

    public void setDeviceMac(String deviceMac) {
        this.deviceMac = deviceMac;
    }

    public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }


    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BleDevice)) return false;

        BleDevice bleDevice = (BleDevice) o;
        return (deviceName.equals(bleDevice.deviceName)
                && deviceMac.equals(bleDevice.deviceMac));
    }

    @Override
    public int hashCode() {
        int result = deviceName.hashCode();
        result = 31 * result + deviceMac.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "BleDevice{" +
                "rssi=" + rssi +
                ", deviceName='" + deviceName + '\'' +
                ", deviceMac='" + deviceMac + '\'' +
                '}';
    }
}
