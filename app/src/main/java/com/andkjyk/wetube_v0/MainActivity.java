package com.andkjyk.wetube_v0;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.andkjyk.wetube_v0.Adapter.MainAdapter;
import com.andkjyk.wetube_v0.Model.MainItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MainAdapter adapter;
    private ArrayList<MainItem> mainItemList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new FABClickListener());

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView recyclerView = findViewById(R.id.room_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        adapter = new MainAdapter(this, mainItemList);
        recyclerView.setAdapter(adapter);

        getData();
    }

    private void getData(){
        ArrayList<String> listTitle = new ArrayList<>();
        ArrayList<String> listHeadcount = new ArrayList<>();
        ArrayList<String> listVideoName = new ArrayList<>();

        for(int i = 0; i < 10; i++){
            listTitle.add("방 제목 "+i);
            listHeadcount.add(15+"");
            listVideoName.add("동영상"+i+" 제목");
        }

        for(int i = 0; i < 10; i++){
            // 각 List의 값들을 data 객체에 set 해줍니다.
            MainItem data = new MainItem();
            data.setTitle(listTitle.get(i));
            data.setHeadcount(listHeadcount.get(i));
            data.setVideoName(listVideoName.get(i));

            // 각 값이 들어간 data를 adapter에 추가합니다.
            mainItemList.add(data);
        }
        adapter.addItems(mainItemList);
        adapter.notifyDataSetChanged();
    }

    private class FABClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, AddRoomActivity.class);
            startActivity(intent);
        }
    }
}
