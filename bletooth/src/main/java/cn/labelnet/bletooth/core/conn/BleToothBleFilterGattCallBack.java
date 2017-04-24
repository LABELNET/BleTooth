package cn.labelnet.bletooth.core.conn;

import android.bluetooth.BluetoothGatt;

import java.util.List;

import cn.labelnet.bletooth.async.AsyncCenter;
import cn.labelnet.bletooth.core.bean.BleService;
import cn.labelnet.bletooth.core.bean.BleServiceUUID;
import cn.labelnet.bletooth.data.filter.BleBluetoothGattFilter;
import cn.labelnet.bletooth.data.filter.BleBluetoothGattStatus;
import cn.labelnet.bletooth.data.filter.BleBluetoothUUIDFilter;

/**
 * @Package cn.labelnet.bletooth.core.conn
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 11:03 AM 2/10/2017
 * @Desc Desc
 * filter BlueToothGatt Service
 */

public abstract class BleToothBleFilterGattCallBack extends BleToothBleGattCallBack
        implements BleBluetoothGattFilter.OnDataCenterListener {


    @Override
    protected void initFilterGatt(BluetoothGatt gatt) {
        super.initFilterGatt(gatt);
        BleBluetoothGattFilter bleDataCenter = new BleBluetoothGattFilter();
        bleDataCenter.setBluetoothGatt(gatt);
        bleDataCenter.setOnDataCenterListener(this);
        AsyncCenter.getInstance().postRunnable(bleDataCenter);
    }

    @Override
    public void OnFilterBleService(List<BleService> bleServices) {
        onFilterBluetoothGattResult(bleServices);
        onConnStatusListener.onFilterGattService(bleServices);
    }

    @Override
    public void onBleDataStatus(BleBluetoothGattStatus status, String msg) {
        onFilterBluetoothGattStatus(status, msg);
    }

    @Override
    public BleBluetoothUUIDFilter getBleFilterUUID() {
        BleBluetoothUUIDFilter.Builder builder = new BleBluetoothUUIDFilter.Builder();
        return onFilterBluetoothGattService(builder);
    }

    /**
     * add filter
     *
     * @param builder filter bean
     * @return filter uuid bean
     */
    protected BleBluetoothUUIDFilter onFilterBluetoothGattService(BleBluetoothUUIDFilter.Builder builder) {
        return builder.build();
    }

    /**
     * filter result
     * can use BluetoothGatt Service
     *
     * @param bleServices BleService
     */
    protected void onFilterBluetoothGattResult(List<BleService> bleServices) {

    }

    /**
     * filter status
     *
     * @param status BleDataStatus
     * @param msg    msg
     */
    protected void onFilterBluetoothGattStatus(BleBluetoothGattStatus status, String msg) {

    }


}
