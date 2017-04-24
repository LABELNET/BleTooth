package cn.labelnet.bletooth.exception;

/**
 * @Package cn.labelnet.bletooth.exception
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 4:36 PM 2/6/2017
 * @Desc Desc
 */

public class ConnException extends BleToothException {
    public ConnException(String desc) {
        super(102, desc);
    }
}
