package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CommentActivity extends AppCompatActivity {
    Intent i;
    TextView name;
    TextView post;
    Button button;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        i = getIntent();
        name = (TextView)findViewById(R.id.name);
        post = (TextView)findViewById(R.id.post);

        name.setText(i.getStringExtra("user_name"));
        post.setText(i.getStringExtra("post"));
    }

    public void onClick(View v){
        
        finish();
    }
}
