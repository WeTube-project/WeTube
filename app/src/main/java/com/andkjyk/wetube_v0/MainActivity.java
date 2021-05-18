package com.andkjyk.wetube_v0;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.andkjyk.wetube_v0.Adapter.MainAdapter;
import com.andkjyk.wetube_v0.Model.MainItem;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MainAdapter adapter;
    private ArrayList<MainItem> mainItemList = new ArrayList<>();
    String room_title, host_name;

    private void getRoom() {
        String url = "http://3.37.36.38:3000/room";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.start();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        room_title = response.getString("title");
                        host_name = response.getString("host");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(), "msg from server => title: " + room_title + ", host : " + host_name, Toast.LENGTH_LONG).show();
                }, error -> {
            Toast.makeText(getApplicationContext(), "fail : msg from server", Toast.LENGTH_LONG).show();
        });

        requestQueue.add(jsonObjReq);
    }

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
        ArrayList<String> listThumbnail = new ArrayList<>();

        for(int i = 0; i < 1; i++){
            listTitle.add("같이봐요~~");
            listHeadcount.add(15+"");
            String title = "[놀면 뭐하니?] 유야호가 쏘아 올린 왕의 귀환\uD83E\uDD34 한 클립에 모아보기ㅣ#SG워너비\u200B #유야호\u200B #엠뚜루마뚜루\u200B MBC210417방송";
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                title = String.valueOf(Html.fromHtml(title, Html.FROM_HTML_MODE_COMPACT));
            } else {
                title = String.valueOf(Html.fromHtml(title));
            }
            listVideoName.add(title);
            listThumbnail.add("https://i.ytimg.com/vi/wV81QXfN5O8/hqdefault.jpg");
        }

        for(int i = 0; i < 1; i++){
            listTitle.add("펜트하우스 정주행");
            listHeadcount.add(7+"");
            String title = "[#펜트하우스\u200B] 시즌1 복습 11~21화 핵심만 요약! 정주행 하다보면 시즌2 온다";
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                title = String.valueOf(Html.fromHtml(title, Html.FROM_HTML_MODE_COMPACT));
            } else {
                title = String.valueOf(Html.fromHtml(title));
            }
            listVideoName.add(title);
            listThumbnail.add("https://i.ytimg.com/vi/CPRCglipOrs/hqdefault.jpg");
        }

        for(int i = 0; i < 1; i++){
            listTitle.add("무한도전 같이볼사람 모여라!");
            listHeadcount.add(23+"");
            String title = "[8月의 무도] 우리 재석이가 화가 (많이) 났어요\uD83D\uDC7F 흑화한 재석, 그리고 배신과 반전의 작대기 파티! “도둑들” 1편 infinite challenge";
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                title = String.valueOf(Html.fromHtml(title, Html.FROM_HTML_MODE_COMPACT));
            } else {
                title = String.valueOf(Html.fromHtml(title));
            }
            listVideoName.add(title);
            listThumbnail.add("https://i.ytimg.com/vi/XyPJq4ukAyE/hqdefault.jpg");
        }

        for(int i = 0; i < listVideoName.size(); i++){
            // 각 List의 값들을 data 객체에 set 해줍니다.
            MainItem data = new MainItem();
            data.setTitle(listTitle.get(i));
            data.setHeadcount(listHeadcount.get(i));
            data.setVideoName(listVideoName.get(i));
            data.setThumbnail(listThumbnail.get(i));

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
