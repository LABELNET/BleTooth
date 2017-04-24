package cn.labelnet.bletooth.peripheral.uuid;

import java.util.UUID;

import static java.util.UUID.fromString;

/**
 * @Package cn.labelnet.bletooth.peripheral.uuid
 * <p>
 * @Author yuan
 * @Blog http://blog.csdn.net/lablenet
 * <p>
 * @Date Created in 6:12 PM 2/8/2017
 * @Desc Desc
 */

public class TestUUID {

    public static final UUID  UUID_MOV_SERV = fromString("f000aa80-0451-4000-b000-000000000000"),
            UUID_MOV_DATA = fromString("f000aa81-0451-4000-b000-000000000000"),
               UUID_MOV__DESCRIPTOR=fromString("00002902-0000-1000-8000-00805f9b34fb"),
            UUID_MOV_CONF = fromString("f000aa82-0451-4000-b000-000000000000"), // 0: disable, bit 0: enable x, bit 1: enable y, bit 2: enable z
            UUID_MOV_PERI = fromString("f000aa83-0451-4000-b000-000000000000");// Period in tens of milliseconds

}
