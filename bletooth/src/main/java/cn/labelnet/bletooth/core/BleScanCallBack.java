package cn.labelnet.bletooth.core;

import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;

import java.util.ArrayList;
import java.util.List;

import cn.labelnet.bletooth.core.scan.ble.BleScanFilter;
import cn.labelnet.bletooth.core.scan.ble.BleScanStatus;
import cn.labelnet.bletooth.core.bean.BleDevice;

/**
 * @Package cn.labelnet.bletooth.core
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 4:00 PM 2/9/2017
 * @Desc Desc
 *
 * android 4.3 and android 5.0 up
 * general scan callback
 *
 */

public abstract class BleScanCallBack {

    /**
     * timeout mills
     */
    private long timeoutmills;

    public BleScanCallBack(long timeoutmills) {
        this.timeoutmills = timeoutmills;
    }

    public long getTimeoutmills() {
        return timeoutmills;
    }


    /**
     * scan result
     *
     * @param bleDevices scan result list
     */
    public abstract void onScanDevicesData(List<BleDevice> bleDevices);


    /**
     * scan process
     * According to timeout mills get it!
     *
     * @param process process 0 ~ 1
     */
    public abstract void onScanProcess(float process);

    /**
     * scan status
     *
     * @param status scan status
     */
    public abstract void onScanStatus(BleScanStatus status);

    /**
     * scan result update rssi value
     *
     * @param position scan result list BleDevice position
     * @param rssi     rssi value
     */
    public void onNotifyBleToothDeviceRssi(int position, int rssi) {
    }


    /**
     * scan filter BleScanFilter
     * BleScanFilter.Builder#build()
     * According to DeviceName and DeicceMac filter
     *
     * @return BleScanFilter list
     */
    public List<BleScanFilter> onScanFilter() {
        List<BleScanFilter> filters = new ArrayList<>();
        //new BleScanFilter.Builder().setDeviceName().build()
        return filters;
    }

    public List<BleScanFilter> getScanFilter() {
        return onScanFilter();
    }

    /**
     * android 5.0 need ScanFilter
     *
     * @return filter
     */
    public final List<ScanFilter> getScanFilters() {

        List<ScanFilter> scanFilters = new ArrayList<>();

        List<BleScanFilter> bleScanFilters = onScanFilter();

        if (bleScanFilters == null || bleScanFilters.size() == 0) {
            return scanFilters;
        }

        for (BleScanFilter bleScanFilter : bleScanFilters) {
            ScanFilter scannerFilter = new ScanFilter
                    .Builder()
                    .setDeviceName(bleScanFilter.getDeviceName())
                    .setDeviceAddress(bleScanFilter.getDeviceMac())
                    .build();
            scanFilters.add(scannerFilter);
        }
        return scanFilters;
    }

    /**
     * android 5.0 need ScanFilter
     * else no need
     * @param builder scansetting
     * @return setting
     */
    public ScanSettings getScanSettings(ScanSettings.Builder builder) {
        return builder.build();
    }

}
