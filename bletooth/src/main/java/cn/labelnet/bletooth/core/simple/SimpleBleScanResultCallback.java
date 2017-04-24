package cn.labelnet.bletooth.core.simple;

import java.util.List;

import cn.labelnet.bletooth.core.scan.ble.BleScanFilter;
import cn.labelnet.bletooth.core.BleScanCallBack;
import cn.labelnet.bletooth.core.bean.BleDevice;
import cn.labelnet.bletooth.core.scan.ble.BleScanResultCallback;
import cn.labelnet.bletooth.core.scan.ble.BleScanStatus;

/**
 * @Package cn.labelnet.bletooth.core
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 5:12 PM 2/9/2017
 * @Desc Desc
 */

public class SimpleBleScanResultCallback extends BleScanResultCallback {

    private BleScanCallBack mBleScanCallBack;

    public SimpleBleScanResultCallback(BleScanCallBack bleScanCallBack) {
        super(bleScanCallBack.getTimeoutmills());
        this.mBleScanCallBack = bleScanCallBack;
    }

    @Override
    protected void onNotifyBleToothDeviceRssi(int position, int rssi) {
        mBleScanCallBack.onNotifyBleToothDeviceRssi(position, rssi);
    }

    @Override
    protected void onScanDevicesData(List<BleDevice> bleDevices) {
        mBleScanCallBack.onScanDevicesData(bleDevices);
    }

    @Override
    public void setBleToothScanStatus(BleScanStatus status) {
        mBleScanCallBack.onScanStatus(status);
    }

    @Override
    protected void bleToothScanProcess(float process) {
        mBleScanCallBack.onScanProcess(process);
    }

    @Override
    protected List<BleScanFilter> getScanFilter() {
        return mBleScanCallBack.getScanFilter();
    }
}
