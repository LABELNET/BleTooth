package cn.labelnet.bletooth_peripheral.callback;

import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseSettings;

/**
 * @Package cn.labelnet.bletooth_peripheral.callback
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 6:01 PM 2/15/2017
 * @Desc Desc
 * default start call back
 */

public class BleAdvertiseCallback extends AdvertiseCallback {


    private OnBleAdListener onBleAdListener;

    public void setOnBleAdListener(OnBleAdListener onBleAdListener) {
        this.onBleAdListener = onBleAdListener;
    }

    @Override
    public void onStartSuccess(AdvertiseSettings settingsInEffect) {
        super.onStartSuccess(settingsInEffect);
        onBleAdListener.onStartSuccess();
    }

    @Override
    public void onStartFailure(int errorCode) {
        super.onStartFailure(errorCode);
        String errorMsg = "No know error.";
        switch (errorCode) {
            case 0:
                onBleAdListener.onStartSuccess();
                break;
            case 1:
                errorMsg = "Failed to start advertising as the advertise data to be broadcasted is larger than 31 bytes.";
                break;
            case 2:
                errorMsg = "Failed to start advertising because no advertising instance is available.";
                break;
            case 3:
                errorMsg = "Failed to start advertising as the advertising is already started.";
                break;
            case 4:
                errorMsg = "Operation failed due to an internal error.";
                break;
            case 5:
                errorMsg = "This feature is not supported on this platform.";
                break;
            default:
                errorMsg = "No know error.";
                break;
        }
        onBleAdListener.onStartFailure(errorMsg);
    }

    public interface OnBleAdListener {

        void onStartSuccess();

        void onStartFailure(String errorMsg);
    }
}
