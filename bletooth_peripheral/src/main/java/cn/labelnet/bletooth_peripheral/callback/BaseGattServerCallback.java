package cn.labelnet.bletooth_peripheral.callback;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattServerCallback;

/**
 * @Package cn.labelnet.bletooth_peripheral.callback
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 6:45 PM 2/15/2017
 * @Desc Desc
 *
 */

public abstract class BaseGattServerCallback extends BluetoothGattServerCallback{

    private OnGattServerListener onGattServerListener;


    public void setOnGattServerListener(OnGattServerListener onGattServerListener) {
        this.onGattServerListener = onGattServerListener;
    }

    @Override
    public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
        super.onConnectionStateChange(device, status, newState);
        if(newState == BluetoothGatt.STATE_CONNECTED){
            onGattServerListener.onConnectSuccess();
        }

        if(newState == BluetoothGatt.STATE_DISCONNECTED){
            onGattServerListener.onConnectFail();
        }
    }


    public interface OnGattServerListener{

        void onConnectSuccess();

        void onConnectFail();
    }
}
