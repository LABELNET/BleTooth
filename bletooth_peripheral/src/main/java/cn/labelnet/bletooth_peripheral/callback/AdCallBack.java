package cn.labelnet.bletooth_peripheral.callback;

import android.bluetooth.BluetoothDevice;
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
 * @Date Created in 5:56 PM 2/15/2017
 * @Desc Desc
 *
 * all
 */

public abstract class AdCallBack {


    /**
     * notify msg
     *
     * @param msg maybe error msg and msg
     */
    public void showMsg(String msg) {

    }

    public void onConnectSuccess() {
        
    }


    public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
        
    }

    public void onServiceAdded(int status, BluetoothGattService service) {
        
    }

    public void onServiceAddSuccess() {
        
    }

    public void onServiceAddFail(BluetoothGattService service) {
        
    }

    public void onCharacteristicReadRequest(BluetoothGattServer mBluetoothGattServer, BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic) {
        
    }

    public void onCharacteristicWriteRequest(BluetoothGattServer mBluetoothGattServer, BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
        
    }

    public void onDescriptorReadRequest(BluetoothGattServer mBluetoothGattServer, BluetoothDevice device, int requestId, int offset, BluetoothGattDescriptor descriptor) {
        
    }

    public void onDescriptorWriteRequest(BluetoothGattServer mBluetoothGattServer, BluetoothDevice device, int requestId, BluetoothGattDescriptor descriptor, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
    }

    public void onExecuteWrite(BluetoothGattServer mBluetoothGattServer, BluetoothDevice device, int requestId, boolean execute) {
    }


    public void onNotificationSent(BluetoothDevice device, int status) {
    }

    public void onMtuChanged(BluetoothDevice device, int mtu) {

    }

    public void onStartSuccess() {

    }
}
