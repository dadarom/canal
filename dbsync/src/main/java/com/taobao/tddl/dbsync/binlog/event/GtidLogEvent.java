package com.taobao.tddl.dbsync.binlog.event;

import com.taobao.tddl.dbsync.binlog.LogBuffer;
import com.taobao.tddl.dbsync.binlog.LogEvent;
import com.taobao.tddl.dbsync.binlog.LogGtidUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @author jianghang 2013-4-8 上午12:36:29
 * @version 1.0.3
 * @since mysql 5.6 / mariadb10
 */
public class GtidLogEvent extends LogEvent {

    // / Length of the commit_flag in event encoding
    public static final int ENCODED_FLAG_LENGTH = 1;
    // / Length of SID in event encoding
    public static final int ENCODED_SID_LENGTH  = 16;

    private boolean         commitFlag;
    private String          serverUUID;
    private long            transcationId;

    public GtidLogEvent(LogHeader header, LogBuffer buffer, FormatDescriptionLogEvent descriptionEvent){
        super(header);

        final int commonHeaderLen = descriptionEvent.commonHeaderLen;
        // final int postHeaderLen = descriptionEvent.postHeaderLen[header.type
        // - 1];

        buffer.position(commonHeaderLen);
        commitFlag = (buffer.getUint8() != 0); // ENCODED_FLAG_LENGTH

        // ignore gtid info read
        // sid.copy_from((uchar *)ptr_buffer);
        // ptr_buffer+= ENCODED_SID_LENGTH;
        //
        // // SIDNO is only generated if needed, in get_sidno().
        // spec.gtid.sidno= -1;
        //
        // spec.gtid.gno= uint8korr(ptr_buffer);
        // ptr_buffer+= ENCODED_GNO_LENGTH;

//        this.serverUUID    = buffer.getFixString(16);
        this.serverUUID    = LogGtidUtil.formatUUID(buffer.getData(16));
        this.transcationId = buffer.getLong64();

        //crc
    }

    public boolean isCommitFlag() {
        return commitFlag;
    }

    public long getTranscationId() {
        return transcationId;
    }

    public String getServerUUID() {
        return serverUUID;
    }
}
