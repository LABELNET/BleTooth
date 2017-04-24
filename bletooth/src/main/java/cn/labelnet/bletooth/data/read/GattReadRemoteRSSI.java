package cn.labelnet.bletooth.data.read;

import cn.labelnet.bletooth.data.BaseOperation;
import cn.labelnet.bletooth.data.OnOperationListener;

/**
 * @Package cn.labelnet.bletooth.data.read
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 4:01 PM 2/10/2017
 * @Desc Desc
 */

public class GattReadRemoteRSSI extends BaseOperation {
    @Override
    public void run() {
        if (!isCheckGattNull()) {
            boolean isSuccess = mBluetoothGatt.readRemoteRssi();
            if (isSuccess) {
                onOperationStatus(OnOperationListener.OperationStatus.readSuccess, "Rssi read success!");
            } else {
                onOperationStatus(OnOperationListener.OperationStatus.readFail, "Rssi read fail!");
            }
        }
    }
}
