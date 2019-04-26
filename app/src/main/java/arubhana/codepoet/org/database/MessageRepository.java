package arubhana.codepoet.org.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class MessageRepository {
    private MessageDao messageDao;

    public MessageRepository(Application application){
        AppDatabase appDatabase=AppDatabase.getDatabase(application);
        messageDao=appDatabase.messageDao();
    }

    LiveData<List<Messages>> mMessageList(String friendName){
        return messageDao.getMessagesForFriend(friendName);
    }

    public void insert(Messages message){
        new AsyncInsert(messageDao).execute(message);
    }


    private static class AsyncInsert extends AsyncTask<Messages,Void,Void>{

        private MessageDao messageDao;
        public AsyncInsert(MessageDao messageDao){
            this.messageDao=messageDao;
        }

        @Override
        protected Void doInBackground(Messages... messages) {
            Messages message=messages[0];
            messageDao.insertMessage(message);
            return null;
        }
    }
}
