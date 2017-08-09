package com.alibaba.otter.canal.parse;

/**
 * 常量值
 * 
 * @author dadarom 2017-7-20
 * @version 1.0.0
 */
public interface CanalParseConstants {

    // 支持在事务结尾内塞入gtid_interal，用于gtid_dump binlog
    public static final String GTID_INTERVAL       = "GTID_INTERVAL";
}
