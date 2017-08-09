package com.alibaba.otter.canal.protocol.position;

/**
 * 数据库对象的唯一标示
 *
 * @author jianghang 2012-6-14 下午09:20:07
 * @version 1.0.0
 */
public class EntryPosition extends TimePosition {

    private static final long serialVersionUID      = 81432665066427482L;
    public static final int   EVENTIDENTITY_SEGMENT = 3;
    public static final char  EVENTIDENTITY_SPLIT   = (char) 5;

    private boolean           included              = false;
    private String            journalName;
    private Long              position;
    // add by agapple at 2016-06-28
    private Long              serverId              = null;              // 记录一下位点对应的serverId

    /* GTID info **/
    private String 			  serverUUID;
    private Long  		      transcationId;
    private String 			  lastGtidInterval;

    public EntryPosition(){
        super(null);
    }

    public EntryPosition(Long timestamp){
        this(null, null, timestamp);
    }

    public EntryPosition(String journalName, Long position){
        this(journalName, position, null);
    }

    public EntryPosition(String journalName, Long position, Long timestamp){
        super(timestamp);
        this.journalName = journalName;
        this.position = position;
    }

    public EntryPosition(String journalName, Long position, Long timestamp, Long serverId){
        this(journalName, position, timestamp);
        this.serverId = serverId;
    }

    public String getJournalName() {
        return journalName;
    }

    public void setJournalName(String journalName) {
        this.journalName = journalName;
    }

    public Long getPosition() {
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }

    public boolean isIncluded() {
        return included;
    }

    public void setIncluded(boolean included) {
        this.included = included;
    }

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    public String getServerUUID() {
        return serverUUID;
    }

    public void setServerUUID(String serverUUID) {
        this.serverUUID = serverUUID;
    }

    public Long getTranscationId() {
        return transcationId;
    }

    public void setTranscationId(Long transcationId) {
        this.transcationId = transcationId;
    }

    public String getLastGtidInterval() {
        return lastGtidInterval;
    }

    public void setLastGtidInterval(String lastGtidInterval) {
        this.lastGtidInterval = lastGtidInterval;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((journalName == null) ? 0 : journalName.hashCode());
        result = prime * result + ((position == null) ? 0 : position.hashCode());
        // 手写equals，自动生成时需注意
        result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
        // GTID
        // 手写equals，自动生成时需注意
        result = prime * result + ((serverUUID == null) ? 0 : serverUUID.hashCode());
        result = prime * result + ((transcationId == null) ? 0 : transcationId.hashCode());
        result = prime * result + ((lastGtidInterval == null) ? 0 : lastGtidInterval.hashCode());

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof EntryPosition)) {
            return false;
        }
        EntryPosition other = (EntryPosition) obj;
        if (journalName == null) {
            if (other.journalName != null) {
                return false;
            }
        } else if (!journalName.equals(other.journalName)) {
            return false;
        }
        if (position == null) {
            if (other.position != null) {
                return false;
            }
        } else if (!position.equals(other.position)) {
            return false;
        }
        // 手写equals，自动生成时需注意
        if (timestamp == null) {
            if (other.timestamp != null) {
                return false;
            }
        } else if (!timestamp.equals(other.timestamp)) {
            return false;
        }

        // GTID
        if (serverUUID == null) {
            if (other.serverUUID != null) {
                return false;
            }
        } else if (!serverUUID.equals(other.serverUUID)) {
            return false;
        }

        if (transcationId == null) {
            if (other.transcationId != null) {
                return false;
            }
        } else if (!transcationId.equals(other.transcationId)) {
            return false;
        }

        if (lastGtidInterval == null) {
            if (other.lastGtidInterval != null) {
                return false;
            }
        } else if (!lastGtidInterval.equals(other.lastGtidInterval)) {
            return false;
        }

        return true;
    }
}
