package cn.labelnet.simplecentral.ble;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.content.Intent;

import cn.labelnet.bletooth.core.BleGattCallback;
import cn.labelnet.bletooth.core.conn.BleConnStatus;

/**
 * @Package cn.labelnet.simplecentral.ble
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 2:06 PM 2/15/2017
 * @Desc Desc
 */

public class CentralBleGattCallback extends BleGattCallback {

    private Context mContext;

    public CentralBleGattCallback(Context mContext) {
        this.mContext = mContext;
    }

    private void sendBroadCast(String action) {
        final Intent intent = new Intent(action);
        mContext.sendBroadcast(intent);
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        super.onCharacteristicChanged(gatt, characteristic);
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        super.onServicesDiscovered(gatt, status);
    }

    @Override
    public void setBleConnStatus(BleConnStatus status) {

    }
}
