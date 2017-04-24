package cn.labelnet.bletooth.exception;

import java.io.Serializable;

/**
 * @Package cn.labelnet.bletooth.exception
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 2:09 PM 2/6/2017
 * @Desc Desc
 */

public abstract class BleToothException extends Exception implements Serializable {

    private int code;
    private String desc;

    public BleToothException(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "BleToothException{" +
                "code=" + code +
                ", desc='" + desc + '\'' +
                '}';
    }
}
