package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Picture_detail extends AppCompatActivity {

    ImageView detail_image;
    Uri uri_id;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_picture_detail);

        detail_image = (ImageView) findViewById(R.id.detail_image);
        TextView img_name = (TextView) findViewById(R.id.img_name);

        Intent intent = getIntent();

        JSONArray gallery_list = null;
        try {
            gallery_list = new JSONArray(intent.getExtras().getString("imglist"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final int position = intent.getExtras().getInt("position");

        uri_id = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, gallery_list.optJSONObject(position).optString("id"));
        String name = gallery_list.optJSONObject(position).optString("name");

        detail_image.setImageURI(uri_id);
        img_name.setText(name);

        Button back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


}
