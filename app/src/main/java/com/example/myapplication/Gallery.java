package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

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
import java.util.ArrayList;

public class Gallery extends Fragment {


    private GridView gallery_view;

    public static Gallery newInstance() {
        Gallery fragmentFirst = new Gallery();
        return fragmentFirst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_gallery, container, false);

        gallery_view = view.findViewById(R.id.gallery_gridview);

        MainActivity2 mainActivity = (MainActivity2) getActivity();
        final JSONArray gallery_list_response = mainActivity.getData_gallery();
        gallery_view.setAdapter(new Image_GridView_Adapter(view, gallery_list_response));

        gallery_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), Picture_detail.class);
                Bundle bundle = new Bundle();
                bundle.putString("imglist", gallery_list_response.toString());
                bundle.putSerializable("position", position);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        return view;
    }
}
