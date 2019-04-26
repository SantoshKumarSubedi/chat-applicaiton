package arubhana.codepoet.org.arubhana;

import android.app.Application;
import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import arubhana.codepoet.org.database.Friends;
import arubhana.codepoet.org.database.FriendsRepository;
import arubhana.codepoet.org.database.FriendsViewModel;
import arubhana.codepoet.org.models.Friend;

public class SearchResultsActivity extends AppCompatActivity {

    JsonParsers parsers;
    LinearLayout linearLayoutWithUser;
    LinearLayout linearLayoutWithoutUser;
    TextView usernameAfterSearch;
    ProgressBar progressBar;
    FriendsViewModel friendsViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        parsers=new JsonParsers();
        friendsViewModel= ViewModelProviders.of(this).get(FriendsViewModel.class);
        linearLayoutWithUser=findViewById(R.id.linear_layout_user_and_add);
        linearLayoutWithoutUser=findViewById(R.id.linear_layout_without_username);
        usernameAfterSearch=findViewById(R.id.username_after_search);
        progressBar=findViewById(R.id.progressBar);
        handleIntent(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent){
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY).toString().toUpperCase();
            new searchUser(query).execute();

        }
    }

    public void addFriend(View view){
        final String name=usernameAfterSearch.getText().toString();

        if(name.equals(UserGlobal.username)){
            Toast.makeText(this, "You cannot add yourself", Toast.LENGTH_SHORT).show();
        }else{
        new InsertFriend(friendsViewModel).execute(name,UserGlobal.username);
        }
    }

    private class searchUser extends AsyncTask<Void,Void,Void>{
        JSONObject jsonObject;
        private int FLAG;
        String query;
        public searchUser(String query) {
            this.query=query;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            linearLayoutWithoutUser.setVisibility(View.INVISIBLE);
            linearLayoutWithUser.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HashMap<String,String> searchUser = new HashMap<>();
            searchUser.put("search","sdfda");
            searchUser.put("username",query);
            jsonObject = parsers.registerUser("http://codepoet.6te.net/arubhana/api/search/",searchUser);
            try{
                if(jsonObject==null){
                    FLAG=1;
                }else if(jsonObject.getString("status").equals("present")){
                    FLAG=2;
                }else if(jsonObject.getString("status").equals("not present")){
                    FLAG=3;
                }else{
                    FLAG=4;
                }

            }catch(JSONException e){
                FLAG=0;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.INVISIBLE);
            System.out.println("FLAG "+FLAG);
            if(FLAG==1){
                Toast.makeText(SearchResultsActivity.this, "Connect to internet and try again", Toast.LENGTH_SHORT).show();
            }else if(FLAG==2){
                linearLayoutWithUser.setVisibility(View.VISIBLE);
                usernameAfterSearch.setText(query);
            }else if(FLAG==3){
                linearLayoutWithoutUser.setVisibility(View.VISIBLE);
            }else if(FLAG==0){
                Toast.makeText(SearchResultsActivity.this, "Server error", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(SearchResultsActivity.this, "Try again", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class InsertFriend extends AsyncTask<String,String,Void>{

        FriendsViewModel friendsViewModel;
        JSONObject jsonObject;
        int FLAG;

        public InsertFriend(FriendsViewModel friendsViewModel) {
            this.friendsViewModel=friendsViewModel;
        }

        @Override
        protected Void doInBackground(String... strings) {
            Friends friend=friendsViewModel.findbyName(strings[0]);
            System.out.println(friend==null);
            if(friend==null){
                HashMap<String,String> request = new HashMap<>();
                request.put("requestFrom",strings[1]);
                request.put("requestTo",strings[0]);
                request.put("sendRequest","dfasd");
                jsonObject = parsers.registerUser("http://codepoet.6te.net/arubhana/api/search/",request);
                 try {
                if(jsonObject==null){
                    FLAG=1;
                }else if(jsonObject.getString("status").equals("success")){
                    FLAG=2;
                }else{
                    FLAG=3;
                }
                }catch (JSONException e){
                    System.out.println("error");
                    FLAG=0;
                }
            }else{
                FLAG=4;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            System.out.println(FLAG);
            if(FLAG==1){
                Toast.makeText(SearchResultsActivity.this, "connection error", Toast.LENGTH_SHORT).show();
            }else if(FLAG==2){
                Toast.makeText(SearchResultsActivity.this, "Request sent", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(SearchResultsActivity.this,Home.class);
                startActivity(intent);
            }else if(FLAG==3){
                Toast.makeText(SearchResultsActivity.this, "try again", Toast.LENGTH_SHORT).show();
            }else if(FLAG==4){
                Toast.makeText(SearchResultsActivity.this, "Friend is already in your friend list", Toast.LENGTH_SHORT).show();
            }else if(FLAG==0){
                Toast.makeText(SearchResultsActivity.this, "Server error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
