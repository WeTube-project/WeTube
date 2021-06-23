package com.andkjyk.wetube_v0;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.andkjyk.wetube_v0.Adapter.MainAdapter;
import com.andkjyk.wetube_v0.Model.PlaylistItem;
import com.andkjyk.wetube_v0.Model.RoomItem;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Html;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    private MainAdapter adapter;
    private ArrayList<RoomItem> roomItemList = new ArrayList<>();
    String room_title, host_name, room_code;

    ArrayList<String> listTitle = new ArrayList<>();
    ArrayList<String> listHeadcount = new ArrayList<>();
    ArrayList<String> listVideoName = new ArrayList<>();
    ArrayList<String> listPublisher = new ArrayList<>();
    ArrayList<String> listVideoId = new ArrayList<>();
    ArrayList<String> listThumbnail = new ArrayList<>();
    ArrayList<String> listRoomCode = new ArrayList<>();
    ArrayList<String> listHostName = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new FABClickListener());

        SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 리사이클러뷰에 LinearLayoutManager 객체 지정.
                RecyclerView recyclerView = findViewById(R.id.room_recycler);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
                adapter = new MainAdapter(MainActivity.this, roomItemList);
                recyclerView.setAdapter(adapter);
                requestQueue = Volley.newRequestQueue(MainActivity.this);

                // adpater.clear();
                getData();
                refreshLayout.setRefreshing(false);
            }
        });

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView recyclerView = findViewById(R.id.room_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        adapter = new MainAdapter(this, roomItemList);
        recyclerView.setAdapter(adapter);

        requestQueue = Volley.newRequestQueue(this);
        getData();
        //getRoomData();
    }

    private void getData(){
        String media_url = "http://3.37.36.38:3000/media";

        //requestQueue.start();


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, media_url, null,
                response -> {
                    try {
                        int roominfo_size = response.getInt("roominfoSize");
                        JSONArray roominfoarr = response.getJSONArray("roominfo");

                        for(int i = 0; i < roominfo_size; i++){
                            JSONObject jsonObject = roominfoarr.getJSONObject(i);
                            String roomCode = jsonObject.getString("roomCode");
                            if(!listRoomCode.contains(roomCode) || i == 0){
                                String title = jsonObject.getString("title");
                                //System.out.println("제목:" + title);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    title = String.valueOf(Html.fromHtml(title, Html.FROM_HTML_MODE_COMPACT));
                                } else {
                                    title = String.valueOf(Html.fromHtml(title));
                                }

                                listVideoName.add(title);
                                listPublisher.add(jsonObject.getString("publisher"));
                                listVideoId.add(jsonObject.getString("videoId"));
                                listThumbnail.add(jsonObject.getString("thumbnailUrl"));
                                listRoomCode.add(roomCode);
                                System.out.println("앞부분 roomCode: "+roomCode + "/ "+i);
                            }else{
                                System.out.println("roomCode 중복: "+i);
                            }
                        }
                        System.out.println("listVideoId 크기: "+listVideoId.size());
                        getRoomData();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //  Toast.makeText(getActivity().getApplicationContext(), "msg from server => title: " , Toast.LENGTH_LONG).show();
                }, error -> {
            Toast.makeText(this, "fail : msg from server", Toast.LENGTH_LONG).show();
        });

        requestQueue.add(jsonObjReq);

        //여기까지
    }

    private void getRoomData(){
        String url = "http://3.37.36.38:3000/room";
        
        JsonObjectRequest jsonObjReq2 = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        System.out.println("두번째 시작함");
                        int room_size = response.getInt("roomSize");
                        JSONArray roomarr = response.getJSONArray("room");

                        //서버에서 받은 데이터를 list에 담는 부분
                        System.out.println("room_size: "+room_size);
                        for(int i = 0; i < room_size; i++){
                            JSONObject jsonObject = roomarr.getJSONObject(i);

                            RoomItem room = new RoomItem(
                                    jsonObject.getString("roomTitle"),
                                    jsonObject.getString("hostName"),
                                    jsonObject.getString("roomCode")
                            );

                            listTitle.add(room.getRoomTitle());
                            listHeadcount.add(15+"");

                            System.out.println("중간점검");
                            if(listVideoName.size()==0){
                                String title = "[놀면 뭐하니?] 유야호가 쏘아 올린 왕의 귀환\uD83E\uDD34 한 클립에 모아보기ㅣ#SG워너비\u200B #유야호\u200B #엠뚜루마뚜루\u200B MBC210417방송";
                                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    title = String.valueOf(Html.fromHtml(title, Html.FROM_HTML_MODE_COMPACT));
                                } else {
                                    title = String.valueOf(Html.fromHtml(title));
                                }
                                listVideoName.add(title);
                                listThumbnail.add("https://i.ytimg.com/vi/wV81QXfN5O8/hqdefault.jpg");
                                listRoomCode.add(room.getRoomCode());
                            }

                            listHostName.add(room.getHostName());
                        }

                        System.out.println("listTitle 크기: "+listTitle.size());

                        roomItemList.clear();

                        for(int i = 0; i < listVideoName.size(); i++){
                            // 각 List의 값들을 data 객체에 set 해줍니다.
                            RoomItem data = new RoomItem();
                            data.setRoomTitle(listTitle.get(i));
                            data.setHeadcount(listHeadcount.get(i));
                            data.setVideoName(listVideoName.get(i));
                            data.setThumbnail(listThumbnail.get(i));
                            data.setRoomCode(listRoomCode.get(i));
                            data.setHostName(listHostName.get(i));

                            System.out.println("확인확인: "+listTitle.get(i)+"roomCode"+listRoomCode.get(i));

                            // 각 값이 들어간 data를 adapter에 추가합니다.
                            roomItemList.add(data);
                        }

                        adapter.addItems(roomItemList);
                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(), "msg from server => title: " + room_title + ", host : " + host_name, Toast.LENGTH_LONG).show();
                }, error -> {
            Toast.makeText(getApplicationContext(), "fail : msg from server", Toast.LENGTH_LONG).show();
        });

        requestQueue.add(jsonObjReq2);
    }

    private class FABClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, AddRoomActivity.class);
            startActivity(intent);
        }
    }
}
