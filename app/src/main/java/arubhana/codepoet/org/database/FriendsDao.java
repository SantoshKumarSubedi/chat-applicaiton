package arubhana.codepoet.org.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;
@Dao
public interface FriendsDao {
    @Query("Select * FROM friends ORDER BY FriendName ASC")
    LiveData<List<Friends>> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Friends friends);

    @Delete
    void delete(Friends friends);

    @Query("SELECT * FROM friends WHERE friendName LIKE :name LIMIT 1")
    Friends findByName(String name);

    @Query("UPDATE friends SET lastMessage=:message WHERE friendName=:friendName")
    void updateLastMessage(String message,String friendName);




}
