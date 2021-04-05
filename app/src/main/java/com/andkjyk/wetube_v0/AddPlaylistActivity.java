package com.andkjyk.wetube_v0;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.andkjyk.wetube_v0.Adapter.AddPlaylistAdapter;
import com.andkjyk.wetube_v0.Model.PlaylistItem;

import java.util.ArrayList;

public class AddPlaylistActivity extends AppCompatActivity {

    private AddPlaylistAdapter adapter;
    private ImageView left_icon;

    private ArrayList<PlaylistItem> searchedItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_playlist);

        left_icon = findViewById(R.id.left_icon);
        left_icon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AddPlaylistActivity.this, RoomActivity.class);
                startActivity(intent);
            }
        });

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView recyclerView = findViewById(R.id.rv_search_video);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        adapter = new AddPlaylistAdapter(this, searchedItemList);
        recyclerView.setAdapter(adapter);

        getData();
    }

    private void getData(){

        searchedItemList.clear();
        ArrayList<String> listPlVideoName = new ArrayList<>();
        ArrayList<String> listPlPublisher = new ArrayList<>();

        for(int i = 0; i < 12; i++){
            listPlVideoName.add(i+"번째 영상 가나다라마바사아자차카타파하가나다라마바사아자차");
            listPlPublisher.add(i+"번째 게시자 아야어여오요우유으이");
        }

        for(int i = 0; i < 12; i++){
            PlaylistItem data = new PlaylistItem();
            System.out.println("정보: "+listPlVideoName.get(i)+" "+listPlPublisher.get(i));
            data.setPlVideoName(listPlVideoName.get(i));
            data.setPlPublisher(listPlPublisher.get(i));

            searchedItemList.add(data);
        }
    }
}