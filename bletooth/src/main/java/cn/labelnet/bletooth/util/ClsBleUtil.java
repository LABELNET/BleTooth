package cn.labelnet.bletooth.util;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;

import java.lang.reflect.Method;

/**
 * @Package cn.labelnet.bletooth.util
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 5:55 PM 2/6/2017
 * @Desc Desc
 * 反射相关类，方法操作
 */

public class ClsBleUtil {

    /**
     * Clears the device cache. After uploading new hello4 the DFU target will have other services than before.
     *
     * @return true on success, false on error
     * 清除设备缓存
     */
    public static boolean refreshDeviceCache(BluetoothGatt mBluetoothGatt) {
         /*
         * There is a refresh() method in BluetoothGatt class but for now it's hidden. We will call it using reflections.
		 */
        try {
            final Method refresh = BluetoothGatt.class.getMethod("refresh");
            if (refresh != null) {
                final boolean success = (Boolean) refresh.invoke(mBluetoothGatt);
                return success;
            }
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * Cancel an in-progress bonding request started
     *
     * @return true on success, false on error
     * <p>
     * 取消当前正在连接的操作
     */
    public static boolean cancelBondProcess(BluetoothDevice mBluetoothDevice) {
        try {
            final Method createBondMethod = BluetoothDevice.class.getMethod("cancelBondProcess");
            if (createBondMethod != null) {
                final boolean returnValue = (Boolean) createBondMethod.invoke(mBluetoothDevice);
                return returnValue;
            }
        } catch (Exception e) {
        }

        return false;
    }


}
