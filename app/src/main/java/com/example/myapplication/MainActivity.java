package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import okhttp3.Request;


public class MainActivity extends AppCompatActivity {

    private LoginButton loginButton;

    private TextView txtName, txtEmail;

    private Button Button;

    private CallbackManager callbackManager;
    MyTimer myTimer;

    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.realmain);

        loginButton = findViewById(R.id.login_button);
        Button= findViewById(R.id.button) ;
        txtName=findViewById(R.id.t1) ;
        txtEmail=findViewById(R.id.t2);
        txtName.setText("deds");
        txtEmail.setText("dede");


        final TextView textView1 = (TextView) findViewById(R.id.textView1);

        if(AccessToken.getCurrentAccessToken() !=null){
            Button.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MainActivity.this, MainActivity2.class);
                    i.putExtra("user_name",name);
                    startActivity(i);
                }
            });
        }
        else{
            Log.d("Tag","로그인해라.");
        }
        myTimer = new MyTimer(6000, 1000);

        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("email","public_profile"));
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));



        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i("eee",loginResult.getAccessToken().ACCESS_TOKEN_KEY);


            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    AccessTokenTracker tokenTracker = new AccessTokenTracker(){

        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken){

            if(currentAccessToken==null){
                txtName.setText("커런트 널");
                txtEmail.setText("커런트널");
                Log.i("erer","nullis");
            }
            else{
                loadUserProfile(currentAccessToken);
                Log.i("erer","not null but not work");

            }

        }
    };

    private void loadUserProfile (AccessToken newAccessToken){
        GraphRequest request=GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {


            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    Log.i("erergrdgrd",object.getString("name"));

                    name= object.getString("name");
                    txtName.setText(name);




                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,email");
        request.setParameters(parameters);
        request.executeAsync();

    }








    private class MyTimer extends CountDownTimer {
        public MyTimer(long millisInFuture, long countDownInterval)
        {
            super(millisInFuture, countDownInterval);
            this.start();
        }
        @Override
        public void onTick(long millisUntilFinished) {
            if(AccessToken.getCurrentAccessToken() !=null){
                Button.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(MainActivity.this, MainActivity2.class);
                        i.putExtra("user_name",name);
                        startActivity(i);
                    }
                });
            }
        }
        @Override
        public void onFinish() {
            this.start();
        }
    }
}

