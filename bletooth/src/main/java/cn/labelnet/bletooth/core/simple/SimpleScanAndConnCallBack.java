package cn.labelnet.bletooth.core.simple;

import java.util.ArrayList;
import java.util.List;

import cn.labelnet.bletooth.core.scan.ble.BleScanFilter;
import cn.labelnet.bletooth.core.BleScanCallBack;
import cn.labelnet.bletooth.core.bean.BleDevice;
import cn.labelnet.bletooth.core.scan.ble.BleScanStatus;
import cn.labelnet.bletooth.util.LogUtil;

/**
 * @Package cn.labelnet.bletooth.core
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 6:08 PM 2/9/2017
 * @Desc Desc
 * Scan And Conn
 * Scan Callback
 */

public abstract class SimpleScanAndConnCallBack extends BleScanCallBack {

    private String mac;
    private List<BleDevice> bleDevices;
    private OnScanAndConnListener onScanAndConnListener;

    public void setOnScanAndConnListener(OnScanAndConnListener onScanAndConnListener) {
        this.onScanAndConnListener = onScanAndConnListener;
    }

    public SimpleScanAndConnCallBack(String mac) {
        super(3000);
        this.mac = mac;
    }

    @Override
    public void onScanDevicesData(List<BleDevice> bleDevices) {
        this.bleDevices = bleDevices;
    }

    private BleDevice getBleDevice() {
        if (bleDevices == null || bleDevices.size() == 0) {
            return null;
        }
        return bleDevices.get(0);
    }

    @Override
    public void onScanProcess(float process) {
        if (onScanAndConnListener != null) {
            onScanAndConnListener.onScanProcess(process);
        }
    }

    @Override
    public void onScanStatus(BleScanStatus status) {

        if (onScanAndConnListener != null) {
            ScanAndConnStatus scanStatus = ScanAndConnStatus.scannererror;
            switch (status) {
                case scaning:
                    scanStatus = ScanAndConnStatus.scaning;
                    break;
                case disscan:
                    scanStatus = ScanAndConnStatus.disscan;
                    break;
                case timeout:
                    scanStatus = ScanAndConnStatus.timeout;
                    break;
                case stopscan:
                    scanStatus = ScanAndConnStatus.stopscan;
                    break;
                case scannererror:
                    scanStatus = ScanAndConnStatus.scannererror;
                    break;
            }
            onScanAndConnListener.onScanStatus(scanStatus);
        }
        if (BleScanStatus.disscan.equals(status)) {
            if (getBleDevice() == null) {
                LogUtil.v("No found device");
                if (onScanAndConnListener != null) {
                    onScanAndConnListener.onScanStatus(ScanAndConnStatus.nofound);
                    return;
                }
            }
            if (onScanAndConnListener != null) {
                onScanAndConnListener.onScanStatus(ScanAndConnStatus.conning);
            }
            onConnect(getBleDevice());
        }
    }

    @Override
    public List<BleScanFilter> getScanFilter() {
        List<BleScanFilter> bleScanFilters = new ArrayList<>();
        bleScanFilters.add(new BleScanFilter.Builder().setDeviceMac(mac).build());
        return bleScanFilters;
    }

    /**
     * to connect
     *
     * @param bleDevice BleDevice bean
     */
    protected abstract void onConnect(BleDevice bleDevice);

    public enum ScanAndConnStatus {
        scaning, disscan, timeout, stopscan, scannererror, nofound, conning
    }

    public interface OnScanAndConnListener {

        void onScanStatus(ScanAndConnStatus status);

        void onScanProcess(float process);
    }

}
