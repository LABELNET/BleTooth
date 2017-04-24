package cn.labelnet.bletooth.core.conn;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;

import java.util.List;

import cn.labelnet.bletooth.core.bean.BleService;
import cn.labelnet.bletooth.util.LogUtil;

/**
 * @Package cn.labelnet.bletooth.core.conn
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 4:08 PM 2/6/2017
 * @Desc Desc
 * conn bluetooth call back
 * (1)conn status
 * (2)control
 */

public abstract class BleToothBleGattCallBack extends BluetoothGattCallback {

    protected OnConnStatusListener onConnStatusListener;

    public void setOnConnStatusListener(OnConnStatusListener onConnStatusListener) {
        this.onConnStatusListener = onConnStatusListener;
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        LogUtil.v("Status : " + status + "　NewState : " + newState);

        if (status == 133 && newState == 0) {
            onConnStatusListener.onTimeOut();
            return;
        }
        if (newState == BluetoothGatt.STATE_CONNECTED) {
            setBleConnStatus(BleConnStatus.success);
            onConnStatusListener.onSuccess();
            gatt.discoverServices();
        } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
            onConnStatusListener.onFail();
        } else if (newState == BluetoothGatt.STATE_CONNECTING) {
            setBleConnStatus(BleConnStatus.conning);
        } else if (newState == BluetoothGatt.STATE_DISCONNECTING) {
            setBleConnStatus(BleConnStatus.disconning);
        }
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            setBleConnStatus(BleConnStatus.gatt_success);
            initFilterGatt(gatt);
        } else {
            setBleConnStatus(BleConnStatus.gatt_fail);
        }
    }

    public abstract void setBleConnStatus(BleConnStatus status);

    /**
     * 过滤有效的 Gatt Service
     * @param gatt
     */
    protected void initFilterGatt(BluetoothGatt gatt) {
    }

    //conn listener
    public interface OnConnStatusListener {

        void onFail();

        void onSuccess();

        void onTimeOut();

        void onFilterGattService(List<BleService> bleServices);
    }

}
