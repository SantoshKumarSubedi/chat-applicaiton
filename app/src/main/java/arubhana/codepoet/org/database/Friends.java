package arubhana.codepoet.org.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity(tableName = "friends")
public class Friends {
    @PrimaryKey @NonNull
    private String friendName;

    @ColumnInfo
    private String status;

    @ColumnInfo
    private String roomName;

    @ColumnInfo
    private String lastMessage;

    public Friends( String friendName, String status,String roomName,String lastMessage) {

        this.friendName = friendName;

        this.status = status;

        this.roomName = roomName;

        this.lastMessage = lastMessage;
    }




    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getLastMessage() { return lastMessage; }

    public void setLastMessage(String lastMessage) { this.lastMessage = lastMessage; }
}
