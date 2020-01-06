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

    private ArrayList<ImageDTO> gallery_list;
    private JSONArray json_gallery_list;
    private GridView gallery_view;

    public static Gallery newInstance() {
        Gallery fragmentFirst = new Gallery();
        return fragmentFirst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getGalleryList();
        json_gallery_list = new JSONArray();

        for (int i = 0; i < gallery_list.size(); i++) {
            JSONObject img = new JSONObject();
            ImageDTO raw_img = gallery_list.get(i);
            try {
                img.accumulate("id", raw_img.getId());
                img.accumulate("name", raw_img.getDisplayname());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            json_gallery_list.put(img);
        }
        passData(json_gallery_list.toString());


    }
    public interface OnDataPass_gallery {
        public void OnDataPass_gallery(String data);
    }

    OnDataPass_gallery dataPasser;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (OnDataPass_gallery) context;
    }

    public void passData(String data) {
        dataPasser.OnDataPass_gallery(data);
    }


    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_gallery, container, false);

        gallery_view = view.findViewById(R.id.gallery_gridview);
        gallery_view.setAdapter(new Image_GridView_Adapter(view, gallery_list));

        gallery_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), Picture_detail.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("imglist", (ArrayList<? extends Parcelable>) gallery_list);
                bundle.putSerializable("position", position);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        return view;
    }



    private void getGalleryList() {
        gallery_list = new ArrayList<>();
        String[] projection = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media.SIZE
        };

        Cursor cursor = getActivity().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection, null, null, null);

        while (cursor.moveToNext()) {
            ImageDTO image_dto = new ImageDTO();
            image_dto.setId(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media._ID)));
            image_dto.setDisplayname(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)));
            image_dto.setDate(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)));
            image_dto.setSize(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.SIZE)));
            gallery_list.add(image_dto);
        }
        cursor.close();
    }

    public static class ImageDTO implements Parcelable {
        private String id;
        private String displayname;
        private String date;
        private String size;


        public ImageDTO() {
        }

        public ImageDTO(Parcel in) {
            id = in.readString();
            displayname = in.readString();
            date = in.readString();
            size = in.readString();

        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDisplayname() {
            return displayname;
        }

        public void setDisplayname(String displayname) {
            this.displayname = displayname;
        }

        public String getDate() {
            return date;
        }

        @Override
        public String toString() {
            return "ImageDto{" +
                    "id='" + id + '\'' +
                    ", displayname='" + displayname + '\'' +
                    ", date='" + date + '\'' +
                    ", size='" + size + '\'' +
                    '}';
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public static final Creator<ImageDTO> CREATOR = new Creator<ImageDTO>() {
            @Override
            public ImageDTO createFromParcel(Parcel source) {
                return new ImageDTO(source);
            }

            @Override
            public ImageDTO[] newArray(int size) {
                return new ImageDTO[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(displayname);
            dest.writeString(date);
            dest.writeString(size);
        }
    }
}
