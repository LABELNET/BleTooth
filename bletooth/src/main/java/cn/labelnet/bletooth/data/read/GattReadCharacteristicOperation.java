package cn.labelnet.bletooth.data.read;

import android.bluetooth.BluetoothGattCharacteristic;

import cn.labelnet.bletooth.data.BaseOperation;
import cn.labelnet.bletooth.data.OnOperationListener;

/**
 * @Package cn.labelnet.bletooth.data.read
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 3:40 PM 2/10/2017
 * @Desc Desc
 * Characteristic read
 */

public class GattReadCharacteristicOperation extends BaseOperation {

    private BluetoothGattCharacteristic readBluetoothGattCharacteristic;

    public GattReadCharacteristicOperation(BluetoothGattCharacteristic readBluetoothGattCharacteristic) {
        this.readBluetoothGattCharacteristic = readBluetoothGattCharacteristic;
    }

    @Override
    public void run() {
        if (isNotCheckCharacteristicNull(readBluetoothGattCharacteristic)) {
            boolean isSuccess = mBluetoothGatt.readCharacteristic(readBluetoothGattCharacteristic);
            if (isSuccess) {
                onOperationStatus(OnOperationListener.OperationStatus.readSuccess, "Characteristic read success");
            } else {
                onOperationStatus(OnOperationListener.OperationStatus.readFail, "Characteristic read fail");
            }
        }
    }
}
