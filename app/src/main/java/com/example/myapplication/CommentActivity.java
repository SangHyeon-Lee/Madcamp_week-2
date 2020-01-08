package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CommentActivity extends AppCompatActivity {
    Intent i;
    TextView name;
    TextView post;
    ImageView imageView;
    Button button;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        i = getIntent();
        name = (TextView)findViewById(R.id.name);
        post = (TextView)findViewById(R.id.post22);
        imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.basic);
        String user_name = i.getStringExtra("user_name");
        String posted = i.getStringExtra("post");
        Log.i("comment_activity",user_name);
        Log.i("comment_activity",posted);

        name.setText(user_name);
       post.setText(posted);
    }

    public void onClick(View v){
        
        finish();
    }
}
