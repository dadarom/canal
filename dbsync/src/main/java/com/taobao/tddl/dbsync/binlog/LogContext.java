package com.taobao.tddl.dbsync.binlog;

import java.util.HashMap;
import java.util.Map;

import com.taobao.tddl.dbsync.binlog.event.FormatDescriptionLogEvent;
import com.taobao.tddl.dbsync.binlog.event.GtidLogEvent;
import com.taobao.tddl.dbsync.binlog.event.PreviousGtidsLogEvent;
import com.taobao.tddl.dbsync.binlog.event.TableMapLogEvent;

/**
 * TODO: Document Me!! NOTE: Log context will NOT write multi-threaded.
 *
 * @author <a href="mailto:changyuan.lh@taobao.com">Changyuan.lh</a>
 * @version 1.0
 */
public final class LogContext {

    private final Map<Long, TableMapLogEvent> mapOfTable = new HashMap<Long, TableMapLogEvent>();

    private FormatDescriptionLogEvent formatDescription;

    private LogPosition logPosition;

    protected GtidLogEvent          lastGTID;
    //保留信息，以便restart时后组织起点gtid信息
    protected PreviousGtidsLogEvent lastPreviousGtids;

    public LogContext() {
        this.formatDescription = FormatDescriptionLogEvent.FORMAT_DESCRIPTION_EVENT_5_x;
    }

    public LogContext(FormatDescriptionLogEvent descriptionEvent) {
        this.formatDescription = descriptionEvent;
    }

    public final LogPosition getLogPosition() {
        return logPosition;
    }

    public final void setLogPosition(LogPosition logPosition) {
        this.logPosition = logPosition;
    }

    public final FormatDescriptionLogEvent getFormatDescription() {
        return formatDescription;
    }

    public final void setFormatDescription(FormatDescriptionLogEvent formatDescription) {
        this.formatDescription = formatDescription;
    }

    public final void putTable(TableMapLogEvent mapEvent) {
        mapOfTable.put(Long.valueOf(mapEvent.getTableId()), mapEvent);
    }

    public final TableMapLogEvent getTable(final long tableId) {
        return mapOfTable.get(Long.valueOf(tableId));
    }

    public final void clearAllTables() {
        mapOfTable.clear();
    }

    public void reset() {
        formatDescription = FormatDescriptionLogEvent.FORMAT_DESCRIPTION_EVENT_5_x;

        mapOfTable.clear();
    }

    /** ========== GTID相关 ========== **/
    public GtidLogEvent getLastGTID() {
        return lastGTID;
    }

    public void clearLastGTID() {
        this.lastGTID = null;
    }

    public PreviousGtidsLogEvent getLastPreviousGtids() {
        return lastPreviousGtids;
    }

    public void setLastPreviousGtids(PreviousGtidsLogEvent lastPreviousGtids) {
        this.lastPreviousGtids = lastPreviousGtids;
    }
}
