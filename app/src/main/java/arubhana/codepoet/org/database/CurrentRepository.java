package arubhana.codepoet.org.database;

import android.app.Application;
import android.os.AsyncTask;

import java.security.PublicKey;

import io.reactivex.Flowable;

public class CurrentRepository {
    private CurrentUserDao currentUserDao;

    public CurrentRepository(Application application){
        AppDatabase appDatabase=AppDatabase.getDatabase(application);
        currentUserDao=appDatabase.currentUserDao();
    }

    public void insert(CurrentUser currentUser){
        new InsertData(currentUserDao).execute(currentUser);
    }

    public void delete(CurrentUser currentUser){
        new InsertData(currentUserDao).execute(currentUser);
    }

    public Flowable<CurrentUser> getUser(){
        return currentUserDao.getUser();
    }




    public class InsertData extends AsyncTask<CurrentUser,Void,Void>{
        private CurrentUserDao currentUserDao;
        public InsertData(CurrentUserDao currentUserDao){
            this.currentUserDao=currentUserDao;
        }

        @Override
        protected Void doInBackground(CurrentUser... currentUsers) {
            currentUserDao.insert(currentUsers[0]);
            return null;
        }
    }

    public class DeleteUser extends AsyncTask<CurrentUser,Void,Void>{
        private CurrentUserDao currentUserDao;
        public DeleteUser(CurrentUserDao currentUserDao){
            this.currentUserDao=currentUserDao;
        }

        @Override
        protected Void doInBackground(CurrentUser... currentUsers) {
            currentUserDao.delete(currentUsers[0]);
            return null;
        }
    }
}
