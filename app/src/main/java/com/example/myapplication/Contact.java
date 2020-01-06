package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Contact extends Fragment {

    private JSONArray contact_list;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;


    public static Contact newInstance() {
        Contact fragmentFirst = new Contact();
        return fragmentFirst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showContacts();
        passData(contact_list.toString());
    }


    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.activity_contact, container, false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recycler_layout);
        LinearLayoutManager manager = new LinearLayoutManager(
                view.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        recyclerView.setLayoutManager(manager); // LayoutManager 등록
        MainActivity mainActivity = (MainActivity) getActivity();
        JSONArray contact_list_response = mainActivity.getData();
        recyclerView.setAdapter(new RecyclerViewAdapter(contact_list_response));  // Adapter 등록
        return view;
    }


    private void showContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            getContactList();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                showContacts();
            } else {
                Toast.makeText(getActivity(), "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT);
            }
        }
    }



    public interface OnDataPass {
        public void onDataPass(String data);
    }

    OnDataPass dataPasser;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (OnDataPass) context;
    }

    public void passData(String data) {
        dataPasser.onDataPass(data);
    }



    private void getContactList() {
        contact_list = new JSONArray();

        Cursor cursor = getActivity().getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                null, null, null);

        while(cursor.moveToNext()){
            Contact_obj contact = new Contact_obj();
            contact.setName(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
            contact.setNumber(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
            JSONObject put_object = new JSONObject();
            try {
                put_object.accumulate("name", contact.getName());
                put_object.accumulate("number", contact.getNumber());
                contact_list.put(put_object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        cursor.close();
    }

    public static class Contact_obj implements Parcelable {
        private String name;
        private String number;

        public Contact_obj(){}

        public Contact_obj(Parcel in){
            name = in.readString();
            number = in.readString();
        }

        public String getName(){
            return name;
        }

        public void setName(String name){
            this.name = name;
        }

        public String getNumber(){
            return number;
        }

        public void setNumber(String number){
            this.number = number;
        }
        public static final Creator<Contact_obj> CREATOR = new Creator<Contact_obj>(){
            @Override
            public Contact_obj createFromParcel(Parcel source) {
                return new Contact_obj(source);
            }

            @Override
            public Contact_obj[] newArray(int size) {
                return new Contact_obj[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(name);
            dest.writeString(number);
        }
    }
}

