package arubhana.codepoet.org.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import arubhana.codepoet.org.models.Friend;

public class FriendsRepository {
    private FriendsDao friendsDao;
    LiveData<List<Friends>> listOfFrineds;

    public FriendsRepository(Application application) {
        AppDatabase appDatabase=AppDatabase.getDatabase(application);
        friendsDao=appDatabase.friendsDao();
        listOfFrineds=friendsDao.getAll();
    }

    LiveData<List<Friends>> getALL(){
        return listOfFrineds;
    }

    public void updateLastMessage(String message,String friendname){
        new AsyncUpdate(friendsDao).execute(message,friendname);
    }

    Friends getFriendByName(String friendname){
        return friendsDao.findByName(friendname);
    }

    public void insert(Friends friends){
        new AsyncInsert(friendsDao).execute(friends);
    }

    private static class AsyncInsert extends AsyncTask<Friends,Void,Void>{

        private FriendsDao friendsDao;
        public AsyncInsert(FriendsDao friendsDao) {
        this.friendsDao=friendsDao;
        }

        @Override
        protected Void doInBackground(Friends... friends) {
            Friends friend=friends[0];
            friendsDao.insert(friend);
            return null;
        }
    }
    private static class AsyncUpdate extends AsyncTask<String,String,Void>{
       private FriendsDao friendsDao;

        public AsyncUpdate(FriendsDao friendsDao) {
            this.friendsDao=friendsDao;
        }


        @Override
        protected Void doInBackground(String... strings) {
            friendsDao.updateLastMessage(strings[0],strings[1]);
            return null;
        }
    }
}
