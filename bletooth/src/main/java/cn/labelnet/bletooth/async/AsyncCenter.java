package cn.labelnet.bletooth.async;

import android.os.Handler;
import android.os.HandlerThread;

/**
 * @Package cn.labelnet.bletooth.async
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 10:38 AM 2/10/2017
 * @Desc Desc
 * 处理耗时任务
 */

public class AsyncCenter {

    private static final String ASYNCCENTER_HANDER_THREAD_NAME = "ble_bluetooth_hander_thread";
    private HandlerThread asyncHanderThread;
    private Handler asyncHandler;

    private static AsyncCenter mInstance;

    public static AsyncCenter getInstance() {
        if (mInstance == null) {
            synchronized (AsyncCenter.class) {
                if (mInstance == null) {
                    mInstance = new AsyncCenter();
                }
            }
        }
        return mInstance;
    }

    public AsyncCenter() {
        //async thread
        asyncHanderThread = new HandlerThread(ASYNCCENTER_HANDER_THREAD_NAME);
        asyncHanderThread.start();
        asyncHandler = new Handler(asyncHanderThread.getLooper());
    }

    public void postRunnable(Runnable runnable) {
        asyncHandler.post(runnable);
    }

    public void postRunnable(Runnable runnable, long delayMills) {
        asyncHandler.postDelayed(runnable, delayMills);
    }
}
