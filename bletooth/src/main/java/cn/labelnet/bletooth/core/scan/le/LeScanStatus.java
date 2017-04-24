package cn.labelnet.bletooth.core.scan.le;

/**
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 4:18 PM 2/7/2017
 * @Desc Desc
 * 注意：这里的值应该与 ScanCallback 中的状态值区分开
 * 使用 ScanCallBack 下的 onScanFailed(int) 返回状态
 */

public class LeScanStatus {

    /**
     * 开始扫描
     */
    public static final int SCAN_START = 100;

    /**
     * 有数据，正常扫完
     */
    public static final int SCAN_END = 101;

    /**
     * 手动停止扫描
     */
    public static final int SCAN_STOP = 102;

    /**
     * 正常扫完，无数据
     */
    public static final int SCAN_TIMEOUT = 103;

}
