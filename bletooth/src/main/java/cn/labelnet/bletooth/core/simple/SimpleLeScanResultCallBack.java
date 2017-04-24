package cn.labelnet.bletooth.core.simple;

import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;

import java.util.List;

import cn.labelnet.bletooth.core.BleScanCallBack;
import cn.labelnet.bletooth.core.bean.BleDevice;
import cn.labelnet.bletooth.core.scan.ble.BleScanStatus;
import cn.labelnet.bletooth.core.scan.le.LeScanResultCallBack;

/**
 * @Package cn.labelnet.bletooth.core
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 5:16 PM 2/9/2017
 * @Desc Desc
 */

public class SimpleLeScanResultCallBack extends LeScanResultCallBack {

    private BleScanCallBack mBleScanCallBack;

    public SimpleLeScanResultCallBack(BleScanCallBack bleScanCallBack) {
        super(bleScanCallBack.getTimeoutmills());
        this.mBleScanCallBack = bleScanCallBack;
    }

    @Override
    protected void onScanDevicesData(List<BleDevice> bleDevices) {
        mBleScanCallBack.onScanDevicesData(bleDevices);
    }

    @Override
    protected void onNotifyBleToothDeviceRssi(int position, int rssi) {
        mBleScanCallBack.onNotifyBleToothDeviceRssi(position, rssi);
    }

    @Override
    protected void onScanStatus(int errorCode) {
        BleScanStatus scanStatus;
        switch (errorCode) {
            case 100:
                scanStatus = BleScanStatus.scaning;
                break;
            case 101:
                scanStatus = BleScanStatus.disscan;
                break;
            case 102:
                scanStatus = BleScanStatus.stopscan;
                break;
            case 103:
                scanStatus = BleScanStatus.timeout;
                break;
            default:
                scanStatus = BleScanStatus.scannererror;
                break;
        }
        mBleScanCallBack.onScanStatus(scanStatus);
    }

    @Override
    protected void bleToothScanProcess(float process) {
        mBleScanCallBack.onScanProcess(process);
    }

    @Override
    public ScanSettings getScanSettings(ScanSettings.Builder builder) {
        return mBleScanCallBack.getScanSettings(builder);
    }

    @Override
    public List<ScanFilter> getScanFilters() {
        return mBleScanCallBack.getScanFilters();
    }
}
