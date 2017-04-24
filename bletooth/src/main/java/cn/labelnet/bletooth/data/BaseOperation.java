package cn.labelnet.bletooth.data;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

/**
 * @Package cn.labelnet.bletooth.data
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 3:03 PM 2/10/2017
 * @Desc Desc
 * Operation base
 * (1) check null
 * (2) status
 */

public abstract class BaseOperation implements Runnable {


    protected BluetoothGatt mBluetoothGatt;
    private OnOperationListener operationListener;


    public void setmBluetoothGatt(BluetoothGatt mBluetoothGatt) {
        this.mBluetoothGatt = mBluetoothGatt;
    }

    public void setOperationListener(OnOperationListener operationListener) {
        this.operationListener = operationListener;
    }

    public BaseOperation() {
    }

    protected void onOperationStatus(OnOperationListener.OperationStatus status, String msg) {
        if (operationListener != null) {
            operationListener.onOperationStatus(status, msg);
        }
    }


    /**
     * check BluetoothGatt is null
     *
     * @return true : null
     */
    protected boolean isCheckGattNull() {
        if (mBluetoothGatt == null) {
            onOperationStatus(OnOperationListener.OperationStatus.error, "BluetoothGatt is null!");
            return true;
        }
        return false;
    }

    /**
     * check BluetoothGattCharacteristic is not null
     *
     * @param characteristic write and read
     * @return true :not null
     */
    protected boolean isNotCheckCharacteristicNull(BluetoothGattCharacteristic characteristic) {

        if (isCheckGattNull()) {
            return false;
        }

        if (characteristic == null) {
            onOperationStatus(OnOperationListener.OperationStatus.error, "BluetoothGattCharacteristic is null!");
            return false;
        }
        return true;
    }

    /**
     * check BluetoothGattDescriptor is not null
     *
     * @param descriptor write and read
     * @return true : not null
     */
    protected boolean isNotCheckDescriptorNull(BluetoothGattDescriptor descriptor) {

        if (isCheckGattNull()) {
            return false;
        }

        if (descriptor == null) {
            onOperationStatus(OnOperationListener.OperationStatus.error, "BluetoothGattDescriptor is null!");
            return false;
        }
        return true;
    }

}
