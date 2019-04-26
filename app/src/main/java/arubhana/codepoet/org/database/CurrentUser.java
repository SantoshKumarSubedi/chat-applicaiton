package arubhana.codepoet.org.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "current_user")
public class CurrentUser {

    @PrimaryKey @NonNull
    private String username;

    @ColumnInfo
    private String password;

    public CurrentUser(String username,String password){
        this.username=username;
        this.password=password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword(){
        return password;

    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
