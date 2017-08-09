package com.taobao.tddl.dbsync.binlog.event;

import com.taobao.tddl.dbsync.binlog.LogBuffer;
import com.taobao.tddl.dbsync.binlog.LogEvent;
import com.taobao.tddl.dbsync.binlog.LogGtidUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

/**
 * @author jianghang 2013-4-8 上午12:36:29
 * @version 1.0.3
 * @since mysql 5.6
 *
 * @updated dadarom 2017-07-19
 */
public class PreviousGtidsLogEvent extends LogEvent {

    private String gtidsStr;
    //<server_uuid,gtid list>
    private Map<String,List<String>> previousGtids = new HashMap<String,List<String>>();

    public PreviousGtidsLogEvent(LogHeader header){
        super(header);
        // just for gtid binlog dump
    }

    public PreviousGtidsLogEvent(LogHeader header, LogBuffer buffer, FormatDescriptionLogEvent descriptionEvent){
        super(header);


        int loop = buffer.getUlong64().intValue();

        String[] gtidsStr = new String[loop];

        for (int i = 0; i < loop; i++) {
            String uuid = LogGtidUtil.formatUUID(buffer.getData(16));

            int intervals =  buffer.getUlong64().intValue();
            String[] intervalStrs = new String[intervals];
            for (int j = 0; j < intervals; j++) {
                long start = buffer.getUlong64().longValue();
                long end   = buffer.getUlong64().longValue();
                intervalStrs[j] = start + "-" + (end - 1);

                cachePreviousGtidStr(uuid, intervalStrs[j]);
            }
            gtidsStr[i] = format("%s:%s", uuid, join(intervalStrs, ":"));
        }

//        is.skip(is.available());
    }

    private String join(String[] values, String separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                sb.append(separator);
            }
            sb.append(values[i]);
        }
        return sb.toString();
    }

    public void cachePreviousGtidStr(String serverUUID,String interval){
        List<String> list = previousGtids.get(serverUUID);
        if(list == null){
            list = new ArrayList<String>();
            previousGtids.put(serverUUID, list);
        }
        list.add(interval);
    }

    public Map<String, List<String>> getPreviousGtids() {
        return previousGtids;
    }

    public String getLastGtidInterval(String serverUUID){
//        String serverUUID = header.getServerUUID();
//        if(serverUUID == null){
//            return null;
//        }
        List<String> list = previousGtids.get(serverUUID);
        if(list == null || list.size() == 0){
            return null;
        }
        return list.get(list.size() - 1);
    }
}
