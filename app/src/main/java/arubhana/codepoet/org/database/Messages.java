package arubhana.codepoet.org.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "messages")
public class Messages {

    @PrimaryKey(autoGenerate = true)
    private int message_id;

    @ColumnInfo
    private String message;

    @ColumnInfo
    private String messageFrom;

    @ColumnInfo
    private String messageTo;

    public Messages( String message, String messageFrom,String messageTo) {
        this.message = message;
        this.messageFrom = messageFrom;
        this.messageTo=messageTo;
    }

    public int getMessage_id() {
        return message_id;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageFrom() {
        return messageFrom;
    }

    public void setMessageFrom(String messageFrom) {
        this.messageFrom = messageFrom;
    }

    public String getMessageTo() {
        return messageTo;
    }

    public void setMessageTo(String messageTo) {
        this.messageTo = messageTo;
    }
}
