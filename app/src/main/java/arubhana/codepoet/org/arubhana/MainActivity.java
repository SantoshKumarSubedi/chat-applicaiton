package arubhana.codepoet.org.arubhana;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.scaledrone.lib.Listener;
import com.scaledrone.lib.Member;
import com.scaledrone.lib.Room;
import com.scaledrone.lib.RoomListener;
import com.scaledrone.lib.Scaledrone;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import arubhana.codepoet.org.adapters.MessageAdapter;
import arubhana.codepoet.org.database.Friends;
import arubhana.codepoet.org.database.FriendsRepository;
import arubhana.codepoet.org.database.FriendsViewModel;
import arubhana.codepoet.org.database.MessageRepository;
import arubhana.codepoet.org.database.MessageViewModel;
import arubhana.codepoet.org.database.Messages;

public class MainActivity extends AppCompatActivity implements Listener {

    String TAG="#message";
    Scaledrone drone;
    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    String[] intentDetail;
    EditText messagetosend;
    MessageViewModel messageViewModel;
    FriendsViewModel friendsViewModel;
    String MESSAGE_FEEDBACK_From ="non";
    JsonParsers parsers=new JsonParsers();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       final LinearLayoutManager layoutManager=new LinearLayoutManager(this);

        recyclerView=findViewById(R.id.activity_main_recycle_bin);
        recyclerView.setLayoutManager(layoutManager);

        Intent recived=getIntent();
        messagetosend=findViewById(R.id.message);
        if(recived !=null){
            if(recived.hasExtra(Intent.EXTRA_TEXT)){
                String friendname= recived.getStringExtra(Intent.EXTRA_TEXT);
                intentDetail=friendname.split(";");



            }
        }
        getSupportActionBar().setTitle("santosh");

        adapter=new MessageAdapter(intentDetail[1]);
        recyclerView.setAdapter(adapter);

        messageViewModel= ViewModelProviders.of(this).get(MessageViewModel.class);
        messageViewModel.getListofmessage(intentDetail[0]).observe(this, new Observer<List<Messages>>() {
            @Override
            public void onChanged(@Nullable List<Messages> messages) {
                adapter.setMessageList(messages);
                recyclerView.scrollToPosition(adapter.getItemCount()-1);
            }
        });
        friendsViewModel = ViewModelProviders.of(this).get(FriendsViewModel.class);

        drone=new Scaledrone(UserGlobal.channelID);
        drone.connect(this);

    }

    public void sendMessage(View view){

        String message=messagetosend.getText().toString();
        messagetosend.setText("");
        messageViewModel.Insert(new Messages(message,UserGlobal.username,intentDetail[0]));
        friendsViewModel.updateLastMessage(message,intentDetail[0]);
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode arrayNode = mapper.createArrayNode();
        ObjectNode objectNode=mapper.createObjectNode();
        objectNode.put("sender",UserGlobal.username);
        objectNode.put("message", message);
        objectNode.put("receiver",intentDetail[0]);
        arrayNode.add(objectNode);
        drone.publish("observable-"+intentDetail[0],objectNode.toString());
        System.out.println(objectNode.toString());

    }


    @Override
    public void onOpen() {
        System.out.println("connected to channel");
    }

    @Override
    public void onOpenFailure(Exception ex) {
        System.out.println(ex);
    }

    @Override
    public void onFailure(Exception ex) {
        System.out.println(ex);
    }

    @Override
    public void onClosed(String reason) {
    }

    private class SendMessageToServer extends AsyncTask<String,Void,Void>{
        JSONObject jsonObject;
        int FLAG;

        @Override
        protected Void doInBackground(String... strings) {
            String[] extract_message=strings[0].split(";;--;;");
            HashMap<String,String> sendMessageToServer=new HashMap<>();
            sendMessageToServer.put("message",extract_message[0]);
            sendMessageToServer.put("messageTo",extract_message[1]);
            sendMessageToServer.put("messageFrom",extract_message[2]);
            sendMessageToServer.put("insertMessage","jfslkdfj");
            jsonObject = parsers.registerUser("http://codepoet.6te.net/arubhana/api/messages/", sendMessageToServer);
           try {
               if (jsonObject == null) {
                  FLAG=1;
               } else if (jsonObject.getString("status").equals("success")) {
                   FLAG=2;
               }else{
                   FLAG=3;
               }
           }catch (JSONException e){

           }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (FLAG==1){
                Toast.makeText(MainActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }else if(FLAG==2){
                Toast.makeText(MainActivity.this, "Message sent to server", Toast.LENGTH_SHORT).show();
            }else if(FLAG==3){
                Toast.makeText(MainActivity.this, "Try again", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        drone.connect(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        drone.close();
    }
}

