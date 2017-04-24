package cn.labelnet.bletooth.core.scan.le;

import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.os.CountDownTimer;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import cn.labelnet.bletooth.exception.ScanTimeOutException;

/**
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 6:00 PM 2/5/2017
 * @Desc Desc
 * （1）status
 * (2) filter
 * (3) setting
 */
public abstract class BleToothLeScanCallBack extends ScanCallback{

    //timeout millis
    private long timeOutMillis = 5000;
    private long timeInterval = 200;

    //timer
    private CountDownTimer countDownTimer;
    private AtomicBoolean isTimerFinish = new AtomicBoolean(false);

    public BleToothLeScanCallBack(long timeOutMill) {
        if (timeOutMill >= 3000) {
            this.timeOutMillis = timeOutMill;
        } else {
            try {
                throw new ScanTimeOutException("Timeout set at least 3S !");
            } catch (ScanTimeOutException e) {
                e.printStackTrace();
            }
        }

        countDownTimer = new CountDownTimer(timeOutMillis, timeInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                float process = (float) ((timeOutMillis - millisUntilFinished) / (timeOutMillis * 1.0));
                if (process > 0.9) {
                    process = 1.0f;
                }
                bleToothScanProcess(process);
            }

            @Override
            public void onFinish() {
                isTimerFinish.set(true);
                onScanCompleteListener.onScanFinish();
            }
        };

    }


    /**
     * scan listener
     */
    private OnScanCompleteListener onScanCompleteListener;

    public void setOnScanCompleteListener(OnScanCompleteListener onScanCompleteListener) {
        this.onScanCompleteListener = onScanCompleteListener;
    }


    /**
     * start scan timer
     */
    public void onStartTimmer() {
        countDownTimer.start();
        onScanFailed(LeScanStatus.SCAN_START);
        isTimerFinish.set(false);
    }


    /**
     * stop scan timer
     */
    public void onStopTimmer() {
        countDownTimer.cancel();
        if (isTimerFinish.get()) {
            onCheckStatus();
        } else {
            onScanFailed(LeScanStatus.SCAN_STOP);
        }
    }

    /**
     * check scan status
     * if scan result no data : time out
     * else scan end
     */
    protected void onCheckStatus() {

    }

    @Override
    public void onScanFailed(int errorCode) {
        onScanStatus(errorCode);
    }

    /**
     * Scan All Status
     *
     * @param errorCode LeScanStatus and ScanCallBack code
     */
    protected abstract void onScanStatus(int errorCode);

    /**
     * scan process
     */
    protected abstract void bleToothScanProcess(float process);

    /**
     * setting
     *
     * @param builder scan setting
     * @return setting
     */
    public ScanSettings getScanSettings(ScanSettings.Builder builder) {
        return builder.build();
    }

    /**
     * filters
     * can DeviceName , Mac , Service and so on !
     *
     * @return filters
     */
    public List<ScanFilter> getScanFilters() {
        return null;
    }

    public interface OnScanCompleteListener {
        void onScanFinish();
    }

}
