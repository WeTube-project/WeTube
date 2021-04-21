package com.andkjyk.wetube_v0;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data != null){
            String videoId = data.getStringExtra("s_videoId");
            String publisher = data.getStringExtra("s_publisher");
            String thumbnailUrl = data.getStringExtra("s_thumbnailUrl");
            String title = data.getStringExtra("s_title");
            plAdapter.addItem(new PlaylistItem(title, publisher, videoId, thumbnailUrl));
            plAdapter.notifyDataSetChanged();
        }
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
        ArrayList<String> listPlVideoId = new ArrayList<>();
        ArrayList<String> listPlThumbnailURL = new ArrayList<>();

        for(int i = 0; i < 1; i++){
            String title = "[놀면 뭐하니?] 유야호가 쏘아 올린 왕의 귀환\uD83E\uDD34 한 클립에 모아보기ㅣ#SG워너비\u200B #유야호\u200B #엠뚜루마뚜루\u200B MBC210417방송";
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                title = String.valueOf(Html.fromHtml(title, Html.FROM_HTML_MODE_COMPACT));
            } else {
                title = String.valueOf(Html.fromHtml(title));
            }
            listPlVideoName.add(title);
            listPlPublisher.add("엠뚜루마뚜루 : MBC 공식 종합 채널");
            listPlVideoId.add("wV81QXfN5O8");
            listPlThumbnailURL.add("https://i.ytimg.com/vi/wV81QXfN5O8/hqdefault.jpg");
        }

        for(int i = 0; i < listPlVideoName.size(); i++){
            PlaylistItem data = new PlaylistItem();
            //System.out.println("정보: "+listPlVideoName.get(i)+" "+listPlPublisher.get(i));
            data.setPlVideoName(listPlVideoName.get(i));
            data.setPlPublisher(listPlPublisher.get(i));
            data.setPlVideoId(listPlVideoId.get(i));
            data.setPlThumbnailURL(listPlThumbnailURL.get(i));

            plItemList.add(data);
        }
    }
}