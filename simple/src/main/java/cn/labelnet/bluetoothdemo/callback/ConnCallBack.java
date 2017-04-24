package cn.labelnet.bluetoothdemo.callback;


import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;

import java.util.Arrays;

import cn.labelnet.bletooth.core.BleGattCallback;
import cn.labelnet.bletooth.core.conn.BleConnStatus;
import cn.labelnet.bletooth.core.conn.BleToothBleGattCallBack;
import cn.labelnet.bletooth.util.LogUtil;

public class ConnCallBack extends BleGattCallback {

    @Override
    public void setBleConnStatus(BleConnStatus status) {
        LogUtil.v("=========== : 连接状态 ： " + status);
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        super.onServicesDiscovered(gatt, status);
        printServices(gatt);
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        super.onCharacteristicChanged(gatt, characteristic);
        LogUtil.v("onCharacteristicChanged 收到的数据 ： " + characteristic.getValue());
    }

    public static void printServices(BluetoothGatt gatt) {
        if (gatt != null) {
            for (BluetoothGattService service : gatt.getServices()) {
                LogUtil.e("service: " + service.getUuid());
                for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                    LogUtil.e("  characteristic: " + characteristic.getUuid() + " value: " + Arrays.toString(characteristic.getValue()));
                    for (BluetoothGattDescriptor descriptor : characteristic.getDescriptors()) {
                        LogUtil.e("  descriptor: " + descriptor.getUuid() + " value: " + Arrays.toString(descriptor.getValue()));
                    }
                }
            }
        }
    }

}