package arubhana.codepoet.org.arubhana;

import android.app.ActionBar;
import android.app.Activity;

import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;

import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.Adapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SearchView;

import com.fasterxml.jackson.databind.JsonNode;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.scaledrone.lib.Listener;
import com.scaledrone.lib.Member;
import com.scaledrone.lib.Room;
import com.scaledrone.lib.RoomListener;
import com.scaledrone.lib.Scaledrone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import arubhana.codepoet.org.ListenerService;
import arubhana.codepoet.org.adapters.HomepageAdapter;
import arubhana.codepoet.org.adapters.RequestAdapter;
import arubhana.codepoet.org.database.Friends;
import arubhana.codepoet.org.database.FriendsViewModel;
import arubhana.codepoet.org.database.MessageViewModel;
import arubhana.codepoet.org.database.Messages;
import arubhana.codepoet.org.models.Friend;

import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

public class Home extends AppCompatActivity implements HomepageAdapter.HomePageAdapterOnClickHandler,
        RequestAdapter.RequestAdapterClickListener{
    RecyclerView recyclerView;
    HomepageAdapter adapter;
    ArrayList<Friend> friends;
    JSONObject jsonObject;
    JsonParsers parsers = new JsonParsers();
    int flag;
    String checkactive;
    boolean a=false;
    Scaledrone drone;
    private FriendsViewModel friendsViewModel;
    MessageViewModel messageViewModel;
    private PopupWindow mPopupWindow;
    LinearLayout mLinearLayout;
    RequestAdapter requestAdapter;

    Room[] room;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = (RecyclerView) findViewById(R.id.home_recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        friends = new ArrayList<>();
        adapter = new HomepageAdapter(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        new ListFriends().execute();
        friendsViewModel= ViewModelProviders.of(this).get(FriendsViewModel.class);
        messageViewModel= ViewModelProviders.of(this).get(MessageViewModel.class);
        startService(new Intent(this,ListenerService.class));
        friendsViewModel.getAll().observe(this,new Observer<List<Friends>>(){

            @Override
            public void onChanged(@Nullable List<Friends> friends) {
                adapter.setMFriends(friends);
            }

        });
        mLinearLayout = findViewById(R.id.home_liner_layout);

        final SenderDetail mem=new SenderDetail(UserGlobal.username);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.homepage_menu,menu);

        //Associate searchable configuration with the searchview
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search_id).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.notification){
            showRequestPopup();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(String friendname,String roomName) {
        Intent intent=new Intent(Home.this,MainActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT,friendname+";"+roomName);
        startActivity(intent);
    }

    @Override
    public void onClickRequest(String name, String action) {
        System.out.println(name+":"+action);
        new PerformAction().execute(action,name,UserGlobal.username);
    }




    public class ListFriends extends AsyncTask<String, String, String> {
    JSONObject json;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> pullfriends = new HashMap<>();
            pullfriends.put("table", UserGlobal.username.toLowerCase());
            jsonObject = parsers.registerUser("http://codepoet.6te.net/arubhana/api/fetch/", pullfriends);
            try {
                if (jsonObject == null) {
                    flag = 1;
                } else if (jsonObject.getString("status").equals("success")) {
                    flag = 2;
                } else {
                    flag = 3;
                }
            } catch (JSONException e) {

            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("flag", "onPostExecute: " + flag);
            if (flag == 2) {
                try {
                    JSONArray array = jsonObject.getJSONArray("data");

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jobj = array.getJSONObject(i);
                       final String name = jobj.getString("name");
                        String status=jobj.getString("active");

                        String[] room={name,UserGlobal.username};

                        Arrays.sort(room);
                        final Friends friends=new Friends(name,status,"observable-"+room[0]+room[1],"Click to text");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                    friendsViewModel.Insert(friends);

                            }
                        });

                    }

                } catch (JSONException e) {

                }
            }
            new FetchMessage().execute();

        }
    }

    public class FetchMessage extends AsyncTask<Void,Void,Void>{
        JSONObject jsonObject;
        int FLAG;
        @Override
        protected Void doInBackground(Void... voids) {
            HashMap<String,String> fetchuser = new HashMap<>();
            fetchuser.put("messageTo",UserGlobal.username);
            fetchuser.put("fetchMessage","sdfsd");
            jsonObject = parsers.registerUser("http://codepoet.6te.net/arubhana/api/messages/",fetchuser);

            try {
                if (jsonObject == null) {
                    FLAG = 1;
                } else if (jsonObject.getString("status").equals("success")){
                    FLAG=2;
                }else{
                    FLAG=3;
                }

            }catch(JSONException e){

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(FLAG==1){

            }else if(FLAG==2){
                try{
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        Messages message = new Messages(object.getString("message"),object.getString("messageFrom"),object.getString("messageTo"));
                        messageViewModel.Insert(message);
                        friendsViewModel.updateLastMessage(object.getString("message"),object.getString("messageFrom"));
                    }
                }catch(JSONException e){

                }


            }else if(FLAG==3){

            }
        }
    }

    private void showRequestPopup(){
       LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);

       View view = layoutInflater.inflate(R.layout.request_popup,null);

       RecyclerView recyclerView = view.findViewById(R.id.popup_recyclerbin);
       RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
       recyclerView.setLayoutManager(layoutManager);
       requestAdapter=new RequestAdapter(this);
       recyclerView.setAdapter(requestAdapter);

        mPopupWindow = new PopupWindow(
                view,
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );

        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        if(Build.VERSION.SDK_INT>=21){
            mPopupWindow.setElevation(5.0f);
        }

        mPopupWindow.showAtLocation(mLinearLayout,Gravity.CENTER,0,0);
        new FetchRequest().execute(UserGlobal.username);
    }

    public class FetchRequest extends AsyncTask<String,Void,Void>{
        JSONObject jsonObject;
        int FLAG;
        @Override
        protected Void doInBackground(String... strings) {
            HashMap<String,String> fetchRequest = new HashMap<>();
            fetchRequest.put("username",strings[0]);
            fetchRequest.put("fetchRequest","dfsd");
            jsonObject=parsers.registerUser("http://codepoet.6te.net/arubhana/api/search/",fetchRequest);
         try {
             if (jsonObject == null) {
                 FLAG = 1;
             }else if(jsonObject.getString("status").equals("success")){
                 FLAG = 2;
             }else{
                 FLAG = 3;
             }

         }catch(JSONException e){

         }
                return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(FLAG==1){
                Toast.makeText(Home.this, "Connection Error", Toast.LENGTH_SHORT).show();
            }else if(FLAG==2){
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("friends");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject obj=jsonArray.getJSONObject(i);
                        String name=obj.getString("name");
                        requestAdapter.addName(name);
                    }
                }catch(JSONException e){

                }
                }else{
                Toast.makeText(Home.this, "TRY again", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class PerformAction extends AsyncTask<String,String,String>{
        JSONObject object;
        int FLAG;

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> action=new HashMap<>();
            action.put("performAction","sdfadsf");
            action.put("action",strings[0]);
            action.put("requestFrom",strings[1].toLowerCase());
            action.put("requestTo",strings[2].toLowerCase());
            System.out.println("request from:"+strings[1]);
            System.out.println("request to:"+strings[2]);
            object = parsers.registerUser("http://codepoet.6te.net/arubhana/api/search/",action);
            try{
                if(object==null){
                 FLAG=1;
                }else if(object.getString("status").equals("success")){
                    FLAG=2;
                }else{
                    FLAG=3;
                }
            }catch (Exception e){
                    FLAG=4;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(FLAG==1){
                Toast.makeText(Home.this, "Network error", Toast.LENGTH_SHORT).show();
            }else if(FLAG==2){
                Toast.makeText(Home.this, "Success", Toast.LENGTH_SHORT).show();
            }else if(FLAG==3){
                Toast.makeText(Home.this, "try again", Toast.LENGTH_SHORT).show();
            }else if(FLAG==4){
                Toast.makeText(Home.this, "Server error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        new ListFriends().execute();
    }
}
