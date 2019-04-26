package arubhana.codepoet.org.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import arubhana.codepoet.org.arubhana.UserGlobal;
import io.reactivex.Flowable;

@Dao
public interface CurrentUserDao {
    @Query("SELECT * FROM CURRENT_USER LIMIT 1")
    public Flowable<CurrentUser> getUser();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(CurrentUser currentUser);

    @Delete
    public void delete(CurrentUser currentUser);
}
