package arubhana.codepoet.org.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import arubhana.codepoet.org.arubhana.UserGlobal;
import arubhana.codepoet.org.models.Friend;

public class FriendsViewModel extends AndroidViewModel {
    private FriendsRepository friendsRepository;
    private LiveData<List<Friends>> listOfFriends;

    public FriendsViewModel(@NonNull Application application) {
        super(application);
        this.friendsRepository=new FriendsRepository(application);
        listOfFriends=friendsRepository.getALL();
    }

    public LiveData<List<Friends>> getAll(){
        return listOfFriends;
    }

    public void Insert(Friends friend){
        friendsRepository.insert(friend);
    }

    public Friends findbyName(String name){
        return friendsRepository.getFriendByName(name);
    }

    public void updateLastMessage(String message,String friendName){
        friendsRepository.updateLastMessage(message,friendName);
    }
}
