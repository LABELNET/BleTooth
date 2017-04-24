package cn.labelnet.bletooth_peripheral.callback;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattService;

/**
 * @Package cn.labelnet.bletooth_peripheral.callback
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 6:53 PM 2/15/2017
 * @Desc Desc
 */

public class BleGattServerCallback extends BaseGattServerCallback implements BaseGattServerCallback.OnGattServerListener {

    private AdCallBack callBack;
    private BluetoothGattServer mBluetoothGattServer;

    public BluetoothGattServer getBluetoothGattServer() {
        return mBluetoothGattServer;
    }

    public void setBluetoothGattServer(BluetoothGattServer mBluetoothGattServer) {
        this.mBluetoothGattServer = mBluetoothGattServer;
    }

    public BleGattServerCallback(AdCallBack callBack) {
        this.callBack = callBack;
    }

    /**
     * conn success
     */
    @Override
    public void onConnectSuccess() {
        callBack.onConnectSuccess();
    }

    /**
     * conn fail
     */
    @Override
    public void onConnectFail() {
        callBack.onConnectSuccess();
    }

    @Override
    public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
        super.onConnectionStateChange(device, status, newState);
        callBack.onConnectionStateChange(device, status, newState);
    }

    /**
     * gatt service add
     *
     * @param status
     * @param service
     */
    @Override
    public void onServiceAdded(int status, BluetoothGattService service) {
        callBack.onServiceAdded(status, service);
        if (status == BluetoothGatt.GATT_SUCCESS) {
            callBack.onServiceAddSuccess();
        } else {
            callBack.onServiceAddFail(service);
        }
    }

    @Override
    public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic) {
        callBack.onCharacteristicReadRequest(getBluetoothGattServer(), device, requestId, offset, characteristic);
    }

    @Override
    public void onCharacteristicWriteRequest(BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
        callBack.onCharacteristicWriteRequest(getBluetoothGattServer(), device, requestId, characteristic, preparedWrite, responseNeeded, offset, value);
    }

    @Override
    public void onDescriptorReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattDescriptor descriptor) {
        callBack.onDescriptorReadRequest(getBluetoothGattServer(), device, requestId, offset, descriptor);
    }

    @Override
    public void onDescriptorWriteRequest(BluetoothDevice device, int requestId, BluetoothGattDescriptor descriptor, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
        callBack.onDescriptorWriteRequest(getBluetoothGattServer(), device, requestId, descriptor, preparedWrite, responseNeeded, offset, value);
    }

    @Override
    public void onExecuteWrite(BluetoothDevice device, int requestId, boolean execute) {
        callBack.onExecuteWrite(getBluetoothGattServer(), device, requestId, execute);
    }

    @Override
    public void onNotificationSent(BluetoothDevice device, int status) {
        callBack.onNotificationSent(device, status);
    }

    @Override
    public void onMtuChanged(BluetoothDevice device, int mtu) {
        callBack.onMtuChanged(device, mtu);
    }
}
