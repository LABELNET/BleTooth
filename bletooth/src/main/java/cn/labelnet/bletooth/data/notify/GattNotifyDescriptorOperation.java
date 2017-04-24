package cn.labelnet.bletooth.data.notify;

import android.bluetooth.BluetoothGattDescriptor;

import cn.labelnet.bletooth.data.BaseOperation;
import cn.labelnet.bletooth.data.OnOperationListener;

/**
 * @Package cn.labelnet.bletooth.data.notify
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 4:00 PM 2/10/2017
 * @Desc Desc
 * Notification Descriptor
 */

public class GattNotifyDescriptorOperation extends BaseOperation {

    private BluetoothGattDescriptor notifyBluetoothGattDescriptor;
    private boolean enable;

    public GattNotifyDescriptorOperation(BluetoothGattDescriptor notifyBluetoothGattDescriptor, boolean enable) {
        this.notifyBluetoothGattDescriptor = notifyBluetoothGattDescriptor;
        this.enable = enable;
    }

    @Override
    public void run() {
        if (isNotCheckDescriptorNull(notifyBluetoothGattDescriptor)) {
            if (enable) {
                notifyBluetoothGattDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            } else {
                notifyBluetoothGattDescriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
            }
            boolean isSuccess = mBluetoothGatt.writeDescriptor(notifyBluetoothGattDescriptor);
            if (isSuccess) {
                onOperationStatus(OnOperationListener.OperationStatus.notifySuccess, "Descriptor is success");
            } else {
                onOperationStatus(OnOperationListener.OperationStatus.notifyFail, "Descriptor is fail");
            }
        }
    }
}
