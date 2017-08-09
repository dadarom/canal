package com.alibaba.otter.canal.parse.driver.mysql.packets.client;

import com.alibaba.otter.canal.parse.driver.mysql.packets.CommandPacket;
import com.alibaba.otter.canal.parse.driver.mysql.utils.ByteHelper;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * COM_BINLOG_DUMP_GTID
 * 
 * @author dadarom
 * @since 1.0
 * wiki: http://imysql.com/mysql-internal-manual/com-binlog-dump-gtid.html
 */
public class BinlogDumpGtidCommandPacket extends CommandPacket {

    /** BINLOG_DUMP options */
    public static final int BINLOG_DUMP_NON_BLOCK           = 1;
    public static final int BINLOG_THROUGH_POSITION         = 2;
    public static final int BINLOG_THROUGH_GTID             = 4;
    public long             binlogPosition;
    public long             slaveServerId;

    //GTID相关
    public String           binlogFileName,masterServerUUID;
    public long             lastTransactionId;                  // GTID = masterServerUUID:lastTransactionId

//    public Map<String,List<PreviousGtidPos>> pgtidsMap      = Maps.newHashMap();
    public String           gtidInterval;

    public BinlogDumpGtidCommandPacket(){
        setCommand((byte) 0x1e);
    }

    public void fromBytes(byte[] data) {
        // bypass
    }

    /**
     * <pre>
     * Bytes                        Name
     *  -----                        ----
     *  1                            command
     *  2                            flags
     *  4                            server id of this slave
     *  4                            binlog-filename-len
     *  string[len]                  binlog filename of the binlog on the master
     *  8                            binlog-pos (in little-endian format)
     *  --------------------------------------------------------
     *  if flags & BINLOG_THROUGH_GTID {
     *      4               data-size
     *      string[len]     data(SID block)
     *  }
     *  ----------------------data------------------------------
     *    8                   n_sids
     *    for n_sids {
     *       string[16]       SID
     *       8                n_intervals
     *           for n_intervals {
     *               8                start (signed)
     *               8                end (signed)
     *           }
     *    }
     * </pre>
     */
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // 0. write command number
        out.write(getCommand());

        // 1. write 2 bytes bin-log flags
        int binlog_flags = 0;
        binlog_flags |= BINLOG_THROUGH_GTID;
        out.write(binlog_flags);
        out.write(0x00);

        // 2. write 4 bytes server id of the slave
        ByteHelper.writeUnsignedIntLittleEndian(this.slaveServerId, out);

        // 3. write bin-log file name if necessary
        if (StringUtils.isNotEmpty(this.binlogFileName)) {
            byte[] bfBytes = this.binlogFileName.getBytes();
            ByteHelper.writeUnsignedIntLittleEndian(bfBytes.length, out);
            out.write(bfBytes);

        }else{
            ByteHelper.writeUnsignedIntLittleEndian(0, out);
        }

        // 4. write 8 bytes bin-log position to start at
        ByteHelper.writeUnsignedLittleEndian(binlogPosition, 8, out);

        // 5. data
        int nSides = 1, nIntervals=1;

        String uuid = masterServerUUID.replaceAll("-", "");
        int dataLen = 8 + nSides * (24 + 16 * nIntervals);

        //---------- data size ----------
        ByteHelper.writeUnsignedLittleEndian(dataLen, 4, out);
        //---------- sid block & compute data size----------
        // n_sids
        ByteHelper.writeUnsignedLittleEndian(nSides, 8, out);
        // string[16] SID: master server UUID
        out.write(hexToByteArray(uuid));

        // n_intervals
        ByteHelper.writeUnsignedLittleEndian(nIntervals, 8, out);
        // start + end
        PreviousGtidPos gtid = parseGtidInterval();
        if(gtid != null){
            ByteHelper.writeUnsignedLittleEndian(gtid.start, 8, out);
            ByteHelper.writeUnsignedLittleEndian(lastTransactionId + 1, 8, out);
        }

        return out.toByteArray();
    }

    private static byte[] hexToByteArray(String uuid) {
        byte[] b = new byte[uuid.length() / 2];
        for (int i = 0, j = 0; j < uuid.length(); j += 2) {
            b[i++] = (byte) Integer.parseInt(uuid.charAt(j) + "" + uuid.charAt(j + 1), 16);
        }
        return b;
    }

    protected PreviousGtidPos parseGtidInterval(){
        String[] strs = StringUtils.split(gtidInterval, "-");

        PreviousGtidPos pos = null;
        if(strs != null && strs.length == 2){
            pos = new PreviousGtidPos();
            pos.start = Long.parseLong(strs[0]);
            pos.end   = Long.parseLong(strs[1]);
        }
        return pos;
    }

    public static class PreviousGtidPos {
        public long start,end;
    }

    public static void main(String[] args) {
        int binlog_flags = 0;
        binlog_flags |= BINLOG_THROUGH_GTID;
        System.out.println(binlog_flags);
        System.out.println(binlog_flags & 0);

        String uuid = "e0c13881-f697-11e6-9ad9-8038bc155e51".replaceAll("-","");
        byte[] b = hexToByteArray(uuid);
        System.out.println(uuid.length());
        System.out.println(b.length);
        System.out.println(b);
    }
}
