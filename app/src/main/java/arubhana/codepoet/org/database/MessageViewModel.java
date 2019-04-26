package arubhana.codepoet.org.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class MessageViewModel extends AndroidViewModel {
    private MessageRepository messageRepository;
    private LiveData<List<Messages>> listofmessage;
    public MessageViewModel(@NonNull Application application) {
        super(application);
        this.messageRepository=new MessageRepository(application);
    }

    public LiveData<List<Messages>> getListofmessage(String friendName) {
        return messageRepository.mMessageList(friendName);
    }

    public void Insert(Messages message){
        messageRepository.insert(message);

    }


}
