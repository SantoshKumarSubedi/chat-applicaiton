package arubhana.codepoet.org.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;




@Dao
public interface MessageDao {

    @Query("SELECT * FROM messages WHERE (messageTo LIKE :friendName OR messageFrom LIKE :friendName)")
    LiveData<List<Messages>> getMessagesForFriend(String friendName);

    @Insert(onConflict = OnConflictStrategy.FAIL)
    void insertMessage(Messages message);

    @Query("SELECT COUNT(*) FROM messages")
    int getNoOfRows();
}
