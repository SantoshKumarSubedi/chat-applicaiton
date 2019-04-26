package arubhana.codepoet.org.arubhana;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import arubhana.codepoet.org.database.CurrentRepository;
import arubhana.codepoet.org.database.CurrentUser;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class Login extends AppCompatActivity{
    EditText mloginUsername;
    EditText mLoginPassword;
    ProgressBar mLoginProgressbar;
    ImageView mLoginImageview;
    String sLoginUsername,sLoginPassword;
    JsonParsers json;
    int flag;
    CurrentRepository currentRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mloginUsername=(EditText)findViewById(R.id.login_username);
        mLoginPassword=(EditText)findViewById(R.id.login_password);
        mLoginProgressbar=(ProgressBar)findViewById(R.id.signup_progressbar);
        mLoginImageview=(ImageView)findViewById(R.id.signup_imageView);
        currentRepository=new CurrentRepository(getApplication());

    }

    public void login(View view){
        sLoginPassword=mLoginPassword.getText().toString();
        sLoginUsername=mloginUsername.getText().toString().toUpperCase();
        if(sLoginUsername.length()<1){
            Toast.makeText(this, "Enter Username", Toast.LENGTH_SHORT).show();
        }else if(sLoginPassword.length()<1){
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
        }else if(sLoginUsername.length()<6){
            Toast.makeText(this, "Password cannot be that short", Toast.LENGTH_SHORT).show();
        }else{
            new LoginThead().execute(sLoginUsername,sLoginPassword);
        }

    }

    public void openSignup(View view){
        Intent intent=new Intent(Login.this,Msignup.class);
        startActivity(intent);
    }


    public class LoginThead extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoginImageview.setVisibility(View.INVISIBLE);
            mLoginProgressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            json=new JsonParsers();

            HashMap<String,String> loginhasmap=new HashMap<>();
            loginhasmap.put("login","yup");
            loginhasmap.put("username",strings[0]);
            loginhasmap.put("password",strings[1]);
            JSONObject jsonObject=json.registerUser("http://codepoet.6te.net/arubhana/api/register/",loginhasmap);
            try {
                if (jsonObject == null) {
                    flag=1;
                } else if (jsonObject.getString("status").equals("success")) {
                    flag=2;
                } else if(jsonObject.getString("status").equals("failed")){
                    flag=3;
                } else{
                    flag=4;
                }
            }catch(JSONException e){

            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mLoginImageview.setVisibility(View.VISIBLE);
            mLoginProgressbar.setVisibility(View.INVISIBLE);
            if(flag==1){
                Toast.makeText(Login.this, "Network error", Toast.LENGTH_SHORT).show();
            }else if(flag==2){
                Toast.makeText(Login.this, "You are now loged in", Toast.LENGTH_SHORT).show();
                UserGlobal.login=true;
                UserGlobal.username=sLoginUsername;
                currentRepository.insert(new CurrentUser(sLoginUsername,sLoginPassword));
                Intent intent=new Intent(Login.this,Home.class);
                startActivity(intent);
            }else if(flag==3){
                Toast.makeText(Login.this, "username and password don't match", Toast.LENGTH_SHORT).show();
            }else if(flag==4){
                Toast.makeText(Login.this, "Try again", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
