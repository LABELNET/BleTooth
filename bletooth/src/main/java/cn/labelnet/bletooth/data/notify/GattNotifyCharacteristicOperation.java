package cn.labelnet.bletooth.data.notify;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.os.SystemClock;

import java.util.UUID;

import cn.labelnet.bletooth.data.BaseOperation;
import cn.labelnet.bletooth.data.OnOperationListener;

/**
 * @Package cn.labelnet.bletooth.data.notify
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 3:59 PM 2/10/2017
 * @Desc Desc
 * Notification Characteristic
 */

public class GattNotifyCharacteristicOperation extends BaseOperation {

    private BluetoothGattCharacteristic notifyBluetoothGattCharacteristic;
    private boolean enable;
    private BluetoothGattDescriptor notifyBluetoothGattDescriptor;

    public GattNotifyCharacteristicOperation(BluetoothGattCharacteristic notifyBluetoothGattCharacteristic, boolean enable) {
        this.notifyBluetoothGattCharacteristic = notifyBluetoothGattCharacteristic;
        this.enable = enable;
    }

    public GattNotifyCharacteristicOperation(BluetoothGattCharacteristic notifyBluetoothGattCharacteristic, boolean enable, UUID configDescriptorUUID) {
        this.notifyBluetoothGattCharacteristic = notifyBluetoothGattCharacteristic;
        this.enable = enable;
        notifyBluetoothGattDescriptor = this.notifyBluetoothGattCharacteristic.getDescriptor(configDescriptorUUID);
    }

    public GattNotifyCharacteristicOperation(BluetoothGattCharacteristic notifyBluetoothGattCharacteristic, boolean enable, String configDescriptorUUID) {
        this(notifyBluetoothGattCharacteristic, enable, UUID.fromString(configDescriptorUUID));
    }


    @Override
    public void run() {
        if (isNotCheckCharacteristicNull(notifyBluetoothGattCharacteristic)) {

            boolean isSuccess = mBluetoothGatt.setCharacteristicNotification(notifyBluetoothGattCharacteristic, enable);
            SystemClock.sleep(200);

            if (isNotCheckDescriptorNull(notifyBluetoothGattDescriptor)) {
                if (enable) {
                    notifyBluetoothGattDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                } else {
                    notifyBluetoothGattDescriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
                }
                mBluetoothGatt.writeDescriptor(notifyBluetoothGattDescriptor);
                SystemClock.sleep(200);
            }

            if (isSuccess) {
                onOperationStatus(OnOperationListener.OperationStatus.notifySuccess, " Characteristic notify is success");
            } else {
                onOperationStatus(OnOperationListener.OperationStatus.notifyFail, " Characteristic notify is fail");
            }
        }
    }
}
