package cn.labelnet.bletooth.data.filter;

import android.bluetooth.BluetoothGatt;

import java.util.List;

import cn.labelnet.bletooth.core.bean.BleService;
import cn.labelnet.bletooth.core.bean.BleServiceUUID;

/**
 * @Package cn.labelnet.bletooth.data
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 7:23 PM 2/9/2017
 * @Desc Desc
 */

public class BleBluetoothGattFilter extends BaseBleBluetoothGattFilter {

    private OnDataCenterListener onDataCenterListener;

    public void setOnDataCenterListener(OnDataCenterListener onDataCenterListener) {
        this.onDataCenterListener = onDataCenterListener;
    }

    public void setBluetoothGatt(BluetoothGatt gatt) {
        super.mGatt = gatt;
    }

    @Override
    protected void onBleDataStatus(BleBluetoothGattStatus status, String msg) {
        onDataCenterListener.onBleDataStatus(status, msg);
        if (status.equals(BleBluetoothGattStatus.InitAllSuccess) || status.equals(BleBluetoothGattStatus.InitFilterSuccess)) {
            onDataCenterListener.OnFilterBleService(getCurrBleServices());
        }
    }

    @Override
    protected List<BleServiceUUID> getBleFilterUUID() {
        return onDataCenterListener.getBleFilterUUID().getBleServiceUUIDs();
    }

    public interface OnDataCenterListener {

        //返回全部的数据
        void OnFilterBleService(List<BleService> bleServices);

        //filter 状态
        void onBleDataStatus(BleBluetoothGattStatus status, String msg);

        //filter UUID
        BleBluetoothUUIDFilter getBleFilterUUID();
    }

}
