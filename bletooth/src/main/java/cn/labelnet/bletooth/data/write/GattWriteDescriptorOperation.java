package cn.labelnet.bletooth.data.write;

import android.bluetooth.BluetoothGattDescriptor;
import android.os.SystemClock;

import cn.labelnet.bletooth.data.BaseOperation;
import cn.labelnet.bletooth.data.OnOperationListener;

/**
 * @Package cn.labelnet.bletooth.data.write
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 3:32 PM 2/10/2017
 * @Desc Desc
 * write descriptor
 */

public class GattWriteDescriptorOperation extends BaseOperation {


    private BluetoothGattDescriptor mBluetoothGattDescriptor;

    public GattWriteDescriptorOperation(BluetoothGattDescriptor mBluetoothGattDescriptor, byte... data) {
        this.mBluetoothGattDescriptor = mBluetoothGattDescriptor;
        this.mBluetoothGattDescriptor.setValue(data);
    }

    @Override
    public void run() {
        if (isNotCheckDescriptorNull(mBluetoothGattDescriptor)) {
            boolean isSuccess = mBluetoothGatt.writeDescriptor(mBluetoothGattDescriptor);
            SystemClock.sleep(200);
            if (isSuccess) {
                onOperationStatus(OnOperationListener.OperationStatus.writerSuccess, "Descriptor write success");
            } else {
                onOperationStatus(OnOperationListener.OperationStatus.writerFail, "Descriptor write fail");
            }
        }
    }
}
