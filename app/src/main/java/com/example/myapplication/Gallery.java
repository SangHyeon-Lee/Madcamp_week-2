package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.GridView;

import org.json.JSONArray;

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

        MainActivity2 mainActivity2 = (MainActivity2) getActivity();
        final JSONArray gallery_list_response = mainActivity2.getData_gallery();
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
