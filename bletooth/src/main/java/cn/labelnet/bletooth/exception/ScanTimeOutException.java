package cn.labelnet.bletooth.exception;

/**
 * @Package cn.labelnet.bletooth.exception
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 2:11 PM 2/6/2017
 * @Desc Desc
 * <p>
 * Scan time out : Error code 101
 */

public class ScanTimeOutException extends BleToothException {

    public ScanTimeOutException(String desc) {
        super(101, desc);
    }
}
