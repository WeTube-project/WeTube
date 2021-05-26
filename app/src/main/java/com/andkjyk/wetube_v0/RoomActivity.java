package com.andkjyk.wetube_v0;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andkjyk.wetube_v0.Adapter.ChatAdapter;
import com.andkjyk.wetube_v0.Model.ChatItem;
import com.andkjyk.wetube_v0.Model.ChatType;
import com.andkjyk.wetube_v0.Model.MessageData;
import com.andkjyk.wetube_v0.Model.RoomData;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.socket.client.IO;
import io.socket.client.Socket;

public class RoomActivity extends AppCompatActivity {

    public Socket mSocket;
    private Gson gson = new Gson();

    private ImageView left_icon;
    Fragment frag_playlist, frag_users, frag_chat;
    String room_title, room_code, host_name, user_name;
    boolean isHost;

    private RecyclerView chatRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("onCreate 시작");
        setContentView(R.layout.activity_room);

        left_icon = findViewById(R.id.left_icon);

        Intent intent = getIntent();
        String SenderActivity = intent.getStringExtra("ActivityName");
        if(SenderActivity.equals("AddRoom")){
            // 재생목록에 추가하는 팝업 띄우고, 확인 누르면 AddPlaylistActivity로 인텐트 보냄
            System.out.println("AddRoomActivity로부터 옴");
            room_title = intent.getStringExtra("roomTitle");
            room_code = intent.getStringExtra("roomCode");
            host_name = intent.getStringExtra("hostName");
            user_name = host_name;
            isHost = true;
        } else if(SenderActivity.equals("Main")){
            user_name = intent.getStringExtra("userName");
            host_name = intent.getStringExtra("hostName");
            room_code = intent.getStringExtra("roomCode");
            postUser();
        } else if(SenderActivity.equals("AddPlaylist")) {   // AddPlaylist에서 뒤로가기 했을 때
            // 백엔드 작업 후 수정
        } else {
            System.out.println("RoomActivity가 intent를 제대로 받아오지 못함");
        }

        left_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RoomActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // socket.io 서버 연결
        try {
            mSocket = IO.socket("http://3.37.36.38:3000/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        mSocket.connect();

        // 입장메세지 띄우기 위해 방 정보 서버에 보냄
        mSocket.on(Socket.EVENT_CONNECT, args -> {
            //System.out.println("아아아아아아ㅏ아아아 enter");
            mSocket.emit("enter", gson.toJson(new RoomData(user_name, room_code)));
        });

        // YouTube Video 띄우는 부분
        YouTubePlayerView youTubePlayerView = findViewById(R.id.video);
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = "wV81QXfN5O8";
                youTubePlayer.loadVideo(videoId, 0);    // YouTubePlayer.loadVideo(String videoId, float startTime)
            }

        });

        frag_chat = new ChatFragment();
        frag_users = new UsersFragment();
        frag_playlist = new PlaylistFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.room_frame, frag_chat).commit();

        TabLayout tabs = (TabLayout) findViewById(R.id.tab_layout);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                Fragment selected = null;

                if (position == 0) {
                    selected = frag_chat;
                } else if (position == 1) {
                    selected = frag_users;
                } else if (position == 2) {
                    selected = frag_playlist;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.room_frame, selected).commit();

                Bundle bundle = new Bundle();
                bundle.putBoolean("isHost", isHost);
                bundle.putString("roomCode", room_code);
                if(isHost == true){
                    bundle.putString("host_name", host_name);
                }else{
                    bundle.putString("host_name", host_name);
                    bundle.putString("user_name", user_name);
                }

                selected.setArguments(bundle);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                    //
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    // 리사이클러뷰에 채팅 추가
    void addChat(MessageData data) {
        this.runOnUiThread(() -> {
            if (data.getType().equals("ENTER") || data.getType().equals("EXIT")) {
                chatAdapter.addItem(new ChatItem(data.getFrom(), data.getContent(), toDate(data.getSendTime()), ChatType.CENTER_MESSAGE));
                chatRecyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
            } else {
                if (user_name.equals(data.getFrom())) {
                    chatAdapter.addItem(new ChatItem(data.getFrom(), data.getContent(), toDate(data.getSendTime()), ChatType.RIGHT_MESSAGE));
                    chatRecyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
                } else {
                    chatAdapter.addItem(new ChatItem(data.getFrom(), data.getContent(), toDate(data.getSendTime()), ChatType.LEFT_MESSAGE));
                    chatRecyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
                }
            }
        });
    }

    // System.currentTimeMillis를 몇시:몇분 am/pm 형태의 문자열로 반환
    private String toDate(long currentMiliis) {
        return new SimpleDateFormat("a hh:mm").format(new Date(currentMiliis));
    }

    private void postUser() {
        String url = "http://3.37.36.38:3000/user";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.start();

        JSONObject params = new JSONObject();

        try {
            params.put("userName", user_name);
            params.put("roomCode", room_code);
        } catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, params,
                response -> {
                    Toast.makeText(getApplicationContext(), "msg from server : " + response, Toast.LENGTH_LONG).show();
                }, error -> {
            Toast.makeText(getApplicationContext(), "fail : msg from server", Toast.LENGTH_LONG).show();
        });

        requestQueue.add(jsonObjReq);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //퇴장(뒤로가기 혹은 앱 종료) 시 퇴장메세지 띄움
        mSocket.emit("exit", gson.toJson(new RoomData(user_name, room_code)));
        mSocket.disconnect();

        Intent intent = new Intent(RoomActivity.this, MainActivity.class);
        startActivity(intent);
    }
}