package cn.labelnet.bletooth.core.simple;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import cn.labelnet.bletooth.core.BaseBleToothService;

/**
 * @Package cn.labelnet.bluetoothdemo
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 9:06 PM 2/10/2017
 * @Desc Desc
 * init BleService
 * example :
 * @see SimpleBleToothService
 * @see BaseBleToothService
 */

public class SimpleServiceBleApplication extends Application {

    private BaseBleToothService.LocalBinder localBinder;
    protected static BaseBleToothService mBleToothService;
    private SimpleSerivceConnection simpleSerivceConnection = new SimpleSerivceConnection();
    private boolean isBind = false;

    private class SimpleSerivceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            localBinder = (BaseBleToothService.LocalBinder) service;
            mBleToothService = localBinder.getBleService();
            isBind = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBind = false;
        }
    }


    /**
     * 开启蓝牙服务
     */
    private void bindService() {
        Intent intent = new Intent(this, SimpleBleToothService.class);
        bindService(intent, simpleSerivceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * 关闭蓝牙服务
     */
    private void unBindService() {
        if (isBind) {
            unbindService(simpleSerivceConnection);
            isBind = false;
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        bindService();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        unBindService();
    }
}
