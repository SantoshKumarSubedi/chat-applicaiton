package arubhana.codepoet.org;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonObject;
import com.scaledrone.lib.Listener;
import com.scaledrone.lib.Member;
import com.scaledrone.lib.Room;
import com.scaledrone.lib.RoomListener;
import com.scaledrone.lib.Scaledrone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import arubhana.codepoet.org.arubhana.UserGlobal;
import arubhana.codepoet.org.database.FriendsRepository;
import arubhana.codepoet.org.database.MessageRepository;
import arubhana.codepoet.org.database.Messages;

public class ListenerService extends Service implements Listener,RoomListener,ValueEventListener{
Scaledrone scaledrone;
FriendsRepository friendsRepository;
MessageRepository messageRepository;
FirebaseDatabase firebaseDatabase;
DatabaseReference databaseReference;
DatabaseReference onlineReference;
public ListenerService(){

}


    @javax.annotation.Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("service is created");
        scaledrone=new Scaledrone("3PCAPeDbMhHzk27W");
        messageRepository=new MessageRepository(getApplication());
        friendsRepository=new FriendsRepository(getApplication());
        scaledrone.connect(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");
        onlineReference = firebaseDatabase.getReference(".info/connected");
        databaseReference.addValueEventListener(this);
        onlineReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean status=dataSnapshot.getValue(Boolean.class);
                if(status){
                    Map<String,Object> map=new HashMap<>();
                    map.put("username",UserGlobal.username);
                    map.put("presence",false);
                    databaseReference.child(UserGlobal.username).onDisconnect().setValue(map);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        System.out.println("service is starting");
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("service is destroyed");
        scaledrone.close();
    }

    @Override
    public void onOpen() {
        scaledrone.subscribe("observable-"+UserGlobal.username,this);
        System.out.println("connected to room");
        Map<String,Object> map = new HashMap<>();
        map.put("username",UserGlobal.username);
        map.put("presence",true);
        databaseReference.child(UserGlobal.username).setValue(map);
    }

    @Override
    public void onOpenFailure(Exception ex) {
        System.out.println("fail to open connection");
        System.out.println(ex);
    }

    @Override
    public void onFailure(Exception ex) {
        System.out.println("connection fail");
        System.out.println(ex);
    }

    @Override
    public void onClosed(String reason) {

    }

    @Override
    public void onOpen(Room room) {
        System.out.println("connected to room");
    }

    @Override
    public void onOpenFailure(Room room, Exception ex) {
        System.out.println("failed connecting to room");
        System.out.println(ex);
    }

    @Override
    public void onMessage(Room room, JsonNode message, Member member) {
        String msg=message.asText();
        try {
            JSONObject jsonObject=new JSONObject(msg);
            String khabar=jsonObject.getString("message");
            String sender=jsonObject.getString("sender");
            String receiver=jsonObject.getString("receiver");
            if(receiver.equals(UserGlobal.username)){
                if (!khabar.equals("R_E_S_P_O_N_S_E")){
                    messageRepository.insert(new Messages(khabar,sender,receiver));
                    friendsRepository.updateLastMessage(khabar,sender);
                    ObjectMapper mapper = new ObjectMapper();
                    ObjectNode objectNode = mapper.createObjectNode();
                    objectNode.put("message","R_E_S_P_O_N_S_E");
                    objectNode.put("sender",receiver);
                    objectNode.put("receiver",sender);
                    scaledrone.publish("observable-"+sender,objectNode.toString());
                }
            }
        }catch(JSONException e){
            System.out.println(e);
        }



    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        System.out.println("actual database change");
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
