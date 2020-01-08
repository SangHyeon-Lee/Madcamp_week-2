package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ContactDetailView extends AppCompatActivity {

    TextView name, phone_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail_view);
        name = findViewById(R.id.contact_detail_name);
        phone_number = findViewById(R.id.contact_detail_phone_number);


        String raw_contact = getIntent().getStringExtra("data");
        JSONObject contact = null;
        try {
            contact = new JSONObject(raw_contact);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        name.setText(name.getText() + contact.optString("name"));
        phone_number.setText(phone_number.getText() + contact.optString("number"));

    }
}
