package com.andkjyk.wetube_v0;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.andkjyk.wetube_v0.Model.PauseData;
import com.andkjyk.wetube_v0.Model.RoomData;
import com.andkjyk.wetube_v0.Model.SyncData;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.socket.client.IO;
import io.socket.client.Socket;

import static android.media.MediaPlayer.MetricsConstants.PLAYING;

public class RoomActivity extends AppCompatActivity {

    public Socket mSocket;
    private Gson gson = new Gson();

    private ImageView left_icon;
    Fragment frag_playlist, frag_users, frag_chat;
    String room_title, room_code, host_name, user_name, isHost;
    float _guestTimestamp;

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
            isHost = "true";
        } else if(SenderActivity.equals("Main")){
            user_name = intent.getStringExtra("userName");
            host_name = intent.getStringExtra("hostName");
            room_code = intent.getStringExtra("roomCode");
            isHost = "false";
            postUser();
        } else if(SenderActivity.equals("AddPlaylist")) {   // AddPlaylist에서 뒤로가기 했을 때
            // 백엔드 작업 후 수정
        } else {
            System.out.println("RoomActivity가 intent를 제대로 받아오지 못함");
        }
// userFragment로 roomCode 보내는 인텐트
        Intent userIntent = new Intent(this, UsersFragment.class);
        userIntent.putExtra("userRoomCode", room_code);
        left_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isHost=="true"){
                    // 호스트가 퇴장할 때
                    //System.out.println("호스트다아아아");
                    AlertDialog.Builder alt_bld = new AlertDialog.Builder(RoomActivity.this, R.style.AlertDialogStyle);
                    alt_bld.setMessage("퇴장하시겠습니까? 호스트 권한이 다음 사용자에게 위임됩니다. 사용자가 없으면 방은 삭제됩니다.").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // 이 부분에서 서버랑 통신해서 hostName을 위임할 사용자 닉네임으로 바꾸고, 기존 호스트 정보는 삭제
                            // 위임할 사용자의 isHost를 true로 변경하기 위해서 UserFragment에서 서버와 통신하는 부분에서 isHost변수 또한 받아야할것같네요
                            // 현재 db_index.js에서는 userName, roomCode만 보내고 있어서요
                            postDelete();
                            Intent intent = new Intent(RoomActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }).setNegativeButton("취소", null);
                    AlertDialog alert = alt_bld.create();
                    alert.show();

                } else{
                    // 게스트가 퇴장할 때
                    //System.out.println("게스트다아아아");
                    AlertDialog.Builder alt_bld = new AlertDialog.Builder(RoomActivity.this, R.style.AlertDialogStyle);
                    alt_bld.setMessage("퇴장하시겠습니까?").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            postDelete();
                            Intent intent = new Intent(RoomActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }).setNegativeButton("취소", null);
                    AlertDialog alert = alt_bld.create();
                    alert.show();
                }
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
            mSocket.emit("enter", gson.toJson(new RoomData(user_name, room_code)));
        });

        // YouTube Video 띄우는 부분
        YouTubePlayerView youTubePlayerView = findViewById(R.id.video);
        getLifecycle().addObserver(youTubePlayerView);

        YouTubePlayerTracker tracker = new YouTubePlayerTracker();

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = "wV81QXfN5O8";
                youTubePlayer.loadVideo(videoId, 0);    // YouTubePlayer.loadVideo(String videoId, float startTime)
            }

            @Override
            public void onStateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlayerState state) {
                super.onStateChange(youTubePlayer, state);
                youTubePlayer.addListener(tracker);
                String videoId = tracker.getVideoId();
                if(isHost=="true"){
                    float hostTimestamp = tracker.getCurrentSecond();
                    if(state.equals(PlayerConstants.PlayerState.PAUSED)){
                        mSocket.emit("pauseData", gson.toJson((new PauseData(true, room_code))));
                    }
                    mSocket.emit("syncData", gson.toJson(new SyncData(true, hostTimestamp, videoId, room_code)));
                }
            }

            @Override
            public void onCurrentSecond(YouTubePlayer youTubePlayer, float second) {
                super.onCurrentSecond(youTubePlayer, second);
                youTubePlayer.addListener(tracker);
                String videoId = tracker.getVideoId();

                if(isHost=="true"){
                    //System.out.println("HostVideoId:"+videoId);
                    float hostTimestamp = Math.round(second*10)/10.0f;
                    if(hostTimestamp % 1 == 0){
                        mSocket.emit("syncData", gson.toJson(new SyncData(true, hostTimestamp, videoId, room_code)));
                    }
                } else{
                    //System.out.println("GuestVideoId:"+videoId);
                    _guestTimestamp = Math.round(second*10)/10.0f;
                    System.out.println(user_name+"- guestTimestamp:"+_guestTimestamp);
                    if(_guestTimestamp % 1 == 0){
                        mSocket.on("sync", args -> {
                            SyncData data = gson.fromJson(args[0].toString(), SyncData.class);
                            String hostVideoId = data.getVideoId();
                            float hostTimestamp = data.getHostTimestamp();
                            //System.out.println("videoId: "+videoId+"/ hostVideoId: "+hostVideoId);
                            if(false){   // if(!videoId.equals(hostVideoId))인데 둘다 null을 return하는 문제 있어서 해결 필요, 일단 false로 처리
                                //System.out.println("videoId: "+videoId+"/ hostVideoId: "+hostVideoId);
                                youTubePlayer.loadVideo(hostVideoId, hostTimestamp);
                            } else {
                                float gap = hostTimestamp - _guestTimestamp;
                                //System.out.println("게스트 synchronize");

                                if (Math.abs(gap) >= 1.0) {   // gap이 3초 이상일 때
                                    System.out.println("gap이 3초 이상:" +gap+"/ guestTimestamp: "+ _guestTimestamp+"/ seekTo " + hostTimestamp);
                                    PlayerConstants.PlayerState state = tracker.getState();
                                    if(state.equals(PlayerConstants.PlayerState.PAUSED)){
                                        youTubePlayer.play();
                                    }
                                    youTubePlayer.seekTo(hostTimestamp);
                                }
                            }
                        });
                        mSocket.on("pause", args -> {
                            PauseData data = gson.fromJson(args[0].toString(), PauseData.class);
                            boolean isPaused = data.getIsPaused();
                            if(isPaused){
                                youTubePlayer.pause();
                            }
                        });
                    }
                }
            }
        });

        frag_chat = new ChatFragment();
        frag_users = new UsersFragment();
        frag_playlist = new PlaylistFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.room_frame, frag_chat).commit();

        TabLayout tabs = findViewById(R.id.tab_layout);

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
                bundle.putString("isHost", isHost);
                bundle.putString("roomCode", room_code);
                if(isHost=="true"){
                    bundle.putString("host_name", host_name);
                }else{
                    bundle.putString("host_name", host_name);
                    bundle.putString("user_name", user_name);
                }

                selected.setArguments(bundle);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

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
            params.put("isHost", isHost);
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
    private void postDelete() {
        String url = "http://3.37.36.38:3000/delete";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.start();

        JSONObject params = new JSONObject();

        try {
            params.put("userName", user_name);
            params.put("roomCode", room_code);
            params.put("isHost", isHost);
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