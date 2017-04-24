package cn.labelnet.bletooth.data;

/**
 * @Package cn.labelnet.bletooth.data
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 3:02 PM 2/10/2017
 * @Desc Desc
 * 操作监听
 */

public interface OnOperationListener {

    void onOperationStatus(OperationStatus status, String msg);


    /**
     * Operation Status
     */
    enum OperationStatus {
        writerSuccess, readSuccess, writerFail, readFail, error, notifySuccess,notifyFail
    }

}
