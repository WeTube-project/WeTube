package com.andkjyk.wetube_v0;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andkjyk.wetube_v0.Adapter.PlaylistAdapter;
import com.andkjyk.wetube_v0.Model.PlaylistItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class PlaylistFragment extends Fragment {

    private RecyclerView plRecyclerView;
    private PlaylistAdapter plAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private int ADDPLAYLIST_REQUEST_CODE = 208;

    private ArrayList<PlaylistItem> plItemList = new ArrayList<>();

    public PlaylistFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.playlist_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddPlaylistActivity.class);
                startActivityForResult(intent, ADDPLAYLIST_REQUEST_CODE);
                //startActivity(intent);
            }
        });

        getData();
        plRecyclerView = (RecyclerView) view.findViewById(R.id.rv_playlist);
        plRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        plRecyclerView.setLayoutManager(layoutManager);
        plRecyclerView.setItemAnimator(new DefaultItemAnimator());
        plAdapter = new PlaylistAdapter(getActivity(), plItemList);
        plRecyclerView.setAdapter(plAdapter);

        return view;
    }

    private void getData(){

        plItemList.clear();
        ArrayList<String> listPlVideoName = new ArrayList<>();
        ArrayList<String> listPlPublisher = new ArrayList<>();

        for(int i = 0; i < 6; i++){
            listPlVideoName.add(i+"번째 영상 가나다라마바사아자차카타파하가나다라마바사아자차");
            listPlPublisher.add(i+"번째 게시자 아야어여오요우유으이");
        }

        for(int i = 0; i < 6; i++){
            PlaylistItem data = new PlaylistItem();
            System.out.println("정보: "+listPlVideoName.get(i)+" "+listPlPublisher.get(i));
            data.setPlVideoName(listPlVideoName.get(i));
            data.setPlPublisher(listPlPublisher.get(i));

            plItemList.add(data);
        }
    }
}