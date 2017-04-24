package cn.labelnet.bletooth.data.read;

import android.bluetooth.BluetoothGattDescriptor;

import cn.labelnet.bletooth.data.BaseOperation;
import cn.labelnet.bletooth.data.OnOperationListener;

/**
 * @Package cn.labelnet.bletooth.data.read
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 3:54 PM 2/10/2017
 * @Desc Desc
 * Descriptor read
 */

public class GattReadDescriptorOpertation extends BaseOperation {

    private BluetoothGattDescriptor readBluetoothGattDescriptor;

    public GattReadDescriptorOpertation(BluetoothGattDescriptor readBluetoothGattDescriptor) {
        this.readBluetoothGattDescriptor = readBluetoothGattDescriptor;
    }

    @Override
    public void run() {
        if (isNotCheckDescriptorNull(readBluetoothGattDescriptor)) {
            boolean isSuccess = mBluetoothGatt.readDescriptor(readBluetoothGattDescriptor);
            if (isSuccess) {
                onOperationStatus(OnOperationListener.OperationStatus.readSuccess, "Descriptor read success");
            } else {
                onOperationStatus(OnOperationListener.OperationStatus.readFail, "Descriptor read fail");
            }
        }
    }
}
