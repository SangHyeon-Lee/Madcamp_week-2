package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;

public class Contact extends Fragment {

    public static Contact newInstance() {
        Contact fragmentFirst = new Contact();
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
        MainActivity2 mainActivity2 = (MainActivity2) getActivity();
        JSONArray contact_list_response = mainActivity2.getData();
        recyclerView.setAdapter(new RecyclerViewAdapter(contact_list_response));  // Adapter 등록
        return view;
    }

}

