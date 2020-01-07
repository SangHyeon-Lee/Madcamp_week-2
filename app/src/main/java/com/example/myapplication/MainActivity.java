package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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

import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity {

    private LoginButton loginButton;

    private TextView txtName, txtEmail;

    private CircleImageView circleImageView;

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
        txtName.setText("로그인되지 않았습니다.");
        circleImageView=findViewById(R.id.t3);



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
        //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));



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
                txtName.setText("로그인되지 않았습니다.");
                circleImageView.setImageResource(R.drawable.basic);
                Button.setOnClickListener(null);

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
                    txtName.setText(name+"님 환영합니다");
                    String id= object.getString("id");
                    Log.i("erergrdgrd",object.getString("id"));

                    String image_url = "https://graph.facebook.com/"+ id + "/picture?type=normal";

                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.dontAnimate();

                    Glide.with(MainActivity.this).load(image_url).into(circleImageView);







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

