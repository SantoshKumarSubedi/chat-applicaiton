package arubhana.codepoet.org.database;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Friends.class,Messages.class,CurrentUser.class},version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
public abstract FriendsDao friendsDao();
public abstract MessageDao messageDao();
public abstract CurrentUserDao currentUserDao();
private static AppDatabase INSTANCE;

static AppDatabase getDatabase(final Context context) {
    if (INSTANCE == null) {
        synchronized (AppDatabase.class){
            if(INSTANCE==null){
                INSTANCE= Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class,"friends").build();
            }
        }
    }
    return INSTANCE;
}

}
