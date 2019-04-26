package arubhana.codepoet.org.arubhana;

import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Msignup extends AppCompatActivity {
    private EditText mSignupUsername;
    private EditText mSignupPassword;
    private Button mSignUpButton;
    private ProgressBar mprogressbar;
    private String TAG="#Signup";
    private ImageView msignuplogo;
    JsonParsers jsonParser=new JsonParsers();
    String sSignupUsername;
    String sSignupPassword;
    int FLAG,FETCH_USER_FLAG;
    ArrayList<String> listofuser=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msignup);

        mSignupUsername=(EditText) findViewById(R.id.signup_username);
        mSignupPassword=(EditText) findViewById(R.id.signup_password);
        mprogressbar=(ProgressBar) findViewById(R.id.signup_progressbar);
        msignuplogo=(ImageView) findViewById(R.id.signup_imageView);

       // new ListOfAllUser().execute();


    }

    public void signup(View view){
        Log.d(TAG, "signup: "+mSignupUsername.getText());
        Log.d(TAG, "signup: "+mSignupPassword.getText());
         sSignupUsername=mSignupUsername.getText().toString();
         sSignupPassword=mSignupPassword.getText().toString();
        if(sSignupUsername.length()<1){
            Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show();
        }else if(sSignupPassword.length()<1){
            Toast.makeText(this, "please enter password", Toast.LENGTH_SHORT).show();
        }else if(sSignupUsername.length()<6){
            Toast.makeText(this,"username must be longer than 6 characters",Toast.LENGTH_SHORT).show();
        }else{
           new signUpUser().execute();
        }

    }

    public class signUpUser extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mprogressbar.setVisibility(View.VISIBLE);
            msignuplogo.setVisibility(View.INVISIBLE);


        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> signuphasmap = new HashMap<>();
            signuphasmap.put("signup","yup");
            signuphasmap.put("username", sSignupUsername.toUpperCase());
            signuphasmap.put("password", sSignupPassword);
            JSONObject jobject = jsonParser.registerUser("http://codepoet.6te.net/arubhana/api/register/", signuphasmap);
            try {
                if (jobject == null) {
                    FLAG = 1;
                } else if (jobject.getString("status").equals("success")) {
                    FLAG = 2;

                }else if(jobject.getString("status").equals("user_present")){
                    FLAG=3;
                }else{
                    FLAG=4;
                }
            } catch (JSONException el) {

            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mprogressbar.setVisibility(View.INVISIBLE);
            msignuplogo.setVisibility(View.VISIBLE);
            if(FLAG==1){
                Toast.makeText(Msignup.this, "Connection error", Toast.LENGTH_SHORT).show();
            }else if(FLAG==2){
                Toast.makeText(Msignup.this, "successfully register", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(Msignup.this,Login.class);
                startActivity(intent);
            }else if(FLAG==3){
                Toast.makeText(Msignup.this, "Username already exist", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Msignup.this, "try again", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class ListOfAllUser extends AsyncTask<String,String,String>{
            JSONObject jsonObject;
        @Override
        protected String doInBackground(String... strings) {
           HashMap<String,String> tokenoflove=new HashMap<>();
           tokenoflove.put("fetch","user");
           jsonObject=jsonParser.registerUser("http://codepoet.6te.net/arubhana/api/fetch/",tokenoflove);
           try {
               if (jsonObject == null) {
                   FETCH_USER_FLAG = 1;
               }else if(jsonObject.getString("status").equals("success")){
                   FETCH_USER_FLAG = 2;
               }else{
                   FETCH_USER_FLAG = 3;
               }
           }catch(JSONException e){

           }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(FETCH_USER_FLAG==1){
                Toast.makeText(Msignup.this, "Please connect to internet", Toast.LENGTH_SHORT).show();
            }else if(FETCH_USER_FLAG==2){
                try{
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++){
                        listofuser.add(jsonArray.getJSONObject(i).getString("name"));
                        System.out.println(jsonArray.getJSONObject(i).get("name"));
                    }
                }catch(JSONException e){

                }

            }
        }
    }
}
