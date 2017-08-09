package com.taobao.tddl.dbsync.binlog;

import static java.lang.String.format;

/**
 * Created by dadarom on 17-7-21.
 * @since mysql 5.6
 */
public class LogGtidUtil {

    public static String formatUUID(byte[] bytes) {
        return format("%s-%s-%s-%s-%s",
                byteArrayToHex(bytes, 0, 4),
                byteArrayToHex(bytes, 4, 2),
                byteArrayToHex(bytes, 6, 2),
                byteArrayToHex(bytes, 8, 2),
                byteArrayToHex(bytes, 10, 6));
    }

    private static String byteArrayToHex(byte[] a, int offset, int len) {
        StringBuilder sb = new StringBuilder();
        for (int idx = offset; idx < (offset + len) && idx < a.length; idx++) {
            sb.append(format("%02x", a[idx] & 0xff));
        }
        return sb.toString();
    }

}
