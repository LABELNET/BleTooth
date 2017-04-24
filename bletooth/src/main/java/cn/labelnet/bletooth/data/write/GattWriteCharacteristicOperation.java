package cn.labelnet.bletooth.data.write;

import android.bluetooth.BluetoothGattCharacteristic;
import android.os.SystemClock;

import cn.labelnet.bletooth.data.BaseOperation;
import cn.labelnet.bletooth.data.OnOperationListener;

/**
 * @Package cn.labelnet.bletooth.data.write
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 3:06 PM 2/10/2017
 * @Desc Desc
 * Write Characteristic
 */

public class GattWriteCharacteristicOperation extends BaseOperation {


    private BluetoothGattCharacteristic mBluetoothGattCharacteristic;

    public GattWriteCharacteristicOperation(BluetoothGattCharacteristic mBluetoothGattCharacteristic, byte... values) {
        this.mBluetoothGattCharacteristic = mBluetoothGattCharacteristic;
        this.mBluetoothGattCharacteristic.setValue(values);
    }

    @Override
    public void run() {
        if (isNotCheckCharacteristicNull(mBluetoothGattCharacteristic)) {
            boolean isSuccess = mBluetoothGatt.writeCharacteristic(mBluetoothGattCharacteristic);
            SystemClock.sleep(200);
            if (isSuccess) {
                onOperationStatus(OnOperationListener.OperationStatus.writerSuccess, "Characteristic write success!");
            } else {
                onOperationStatus(OnOperationListener.OperationStatus.writerFail, "Characteristic write fail!");
            }
        }
    }
}
