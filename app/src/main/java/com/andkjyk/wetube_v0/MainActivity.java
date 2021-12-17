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
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import android.net.Uri;

public class MainActivity extends AppCompatActivity {   //방 목록 액티비티

    RequestQueue requestQueue;
    private MainAdapter adapter;
    private ArrayList<RoomItem> roomItemList = new ArrayList<>();
    String room_title, host_name, room_code;
    int room_size;

    ArrayList<String> listTitle = new ArrayList<>();
    ArrayList<String> listHeadcount = new ArrayList<>();
    ArrayList<String> listVideoName = new ArrayList<>();
    //ArrayList<String> listPublisher = new ArrayList<>();
    ArrayList<String> listVideoId = new ArrayList<>();
    ArrayList<String> listThumbnail = new ArrayList<>();
    ArrayList<String> listRoomCode = new ArrayList<>();
    ArrayList<String> m_listRoomCode = new ArrayList<>();
    ArrayList<String> listHostName = new ArrayList<>();
    String email = "";

    // oauth login
    GoogleSignInClient mGoogleSignInClient;
    private static int RC_SIGN_IN = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // oauth login
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             signIn();
            };
        });
        // oauth login end


        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new FABClickListener());

        SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {   // 아래로 당기는 모션 취했을 때 방 목록 업데이트
                // 리사이클러뷰에 LinearLayoutManager 객체 지정.
                RecyclerView recyclerView = findViewById(R.id.room_recycler);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                // 리사이클러뷰에 MainAdapter 객체 지정.
                adapter = new MainAdapter(MainActivity.this, roomItemList);
                recyclerView.setAdapter(adapter);
                requestQueue = Volley.newRequestQueue(MainActivity.this);

                getData();
                refreshLayout.setRefreshing(false);
            }
        });

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView recyclerView = findViewById(R.id.room_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 리사이클러뷰에 MainAdapter 객체 지정.
        adapter = new MainAdapter(this, roomItemList);
        recyclerView.setAdapter(adapter);

        requestQueue = Volley.newRequestQueue(this);
        getData();
        //getRoomData();
    }

    private void getMediaData(){    // 재생목록 데이터를 서버에서 가져옴
        String media_url = "http://15.164.226.229:3000/media";

        //requestQueue.start();


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, media_url, null,
                response -> {
                    try {
                        System.out.println("두번째 시작함");
                        int roominfo_size = response.getInt("roominfoSize");
                        JSONArray roominfoarr = response.getJSONArray("roominfo");

                        ArrayList<String> mm_listRoomCode = new ArrayList<>();
                        for(int i = 0; i < roominfo_size; i++){
                            JSONObject jsonObject = roominfoarr.getJSONObject(i);
                            String roomCode = jsonObject.getString("roomCode");
                            mm_listRoomCode.add(roomCode);
                        }

                        for(int i = 0; i < room_size; i++){
                            for(int j = 0; j < roominfo_size; j++){
                                JSONObject jsonObject = roominfoarr.getJSONObject(j);
                                String roomCode = jsonObject.getString("roomCode");
                                if(listRoomCode.get(i).equals(roomCode)){
                                    if(!m_listRoomCode.contains(roomCode) || j == 0){
                                        //m_listRoomCode.add(roomCode);

                                        String title = jsonObject.getString("title");
                                        //System.out.println("제목:" + title);
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                            title = String.valueOf(Html.fromHtml(title, Html.FROM_HTML_MODE_COMPACT));
                                        } else {
                                            title = String.valueOf(Html.fromHtml(title));
                                        }

                                        listVideoName.set(i, title);
                                        listThumbnail.set(i, jsonObject.getString("thumbnailUrl"));
                                        listVideoId.set(i, jsonObject.getString("videoId"));

                                        System.out.println("뒷부분 roomCode: "+roomCode + "/ "+i);
                                    } else{
                                        System.out.println("roomCode 중복: "+j);
                                    }
                                }
                            }
                        }
                        //System.out.println("listVideoId 크기: "+listVideoId.size());

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
                            data.setVideoId(listVideoId.get(i));

                            System.out.println("확인확인: "+listTitle.get(i)+"roomCode"+listRoomCode.get(i));

                            // 각 값이 들어간 data를 adapter에 추가합니다.
                            roomItemList.add(data);
                        }

                        adapter.addItems(roomItemList);
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //  Toast.makeText(getActivity().getApplicationContext(), "msg from server => title: " , Toast.LENGTH_LONG).show();
                }, error -> {
            //Toast.makeText(this, "fail : msg from server", Toast.LENGTH_LONG).show();
        });

        requestQueue.add(jsonObjReq);

        //여기까지
    }

    private void getData(){     // 방 목록 데이터를 서버에서 가져옴
        String url = "http://15.164.226.229:3000/room";
        
        JsonObjectRequest jsonObjReq2 = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        System.out.println("첫번째 시작함");
                        room_size = response.getInt("roomSize");
                        JSONArray roomarr = response.getJSONArray("room");

                        //서버에서 받은 데이터를 list에 담는 부분
                        System.out.println("room_size: "+room_size);
                        listRoomCode.clear();
                        listTitle.clear();
                        listHeadcount.clear();
                        listHostName.clear();
                        listThumbnail.clear();
                        listRoomCode.clear();
                        listVideoName.clear();
                        listVideoId.clear();
                        m_listRoomCode.clear();
                        for(int i = 0; i < room_size; i++){
                            JSONObject jsonObject = roomarr.getJSONObject(i);

                            RoomItem room = new RoomItem(
                                    jsonObject.getString("roomTitle"),
                                    jsonObject.getString("hostName"),
                                    jsonObject.getString("roomCode")
                            );

                            listTitle.add(room.getRoomTitle());
                            listHeadcount.add(1+"");

                            System.out.println("중간점검");

                            //디폴트 설정 (재생목록에 아무것도 추가 안했을 때 이 값 유지, 아니면 getMediaData에서 변경됨)
                            String title = "[놀면 뭐하니?] 유야호가 쏘아 올린 왕의 귀환\uD83E\uDD34 한 클립에 모아보기ㅣ#SG워너비\u200B #유야호\u200B #엠뚜루마뚜루\u200B MBC210417방송";
                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                title = String.valueOf(Html.fromHtml(title, Html.FROM_HTML_MODE_COMPACT));
                            } else {
                                title = String.valueOf(Html.fromHtml(title));
                            }
                            listVideoName.add(title);
                            listThumbnail.add("https://i.ytimg.com/vi/wV81QXfN5O8/hqdefault.jpg");
                            listRoomCode.add(room.getRoomCode());
                            listVideoId.add("wV81QXfN5O8");
                            listHostName.add(room.getHostName());
                        }

                        System.out.println("listTitle 크기: "+listTitle.size());

                        getMediaData();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //Toast.makeText(getApplicationContext(), "msg from server => title: " + room_title + ", host : " + host_name, Toast.LENGTH_LONG).show();
                }, error -> {
            //Toast.makeText(getApplicationContext(), "fail : msg from server", Toast.LENGTH_LONG).show();
        });

        requestQueue.add(jsonObjReq2);
    }

    private class FABClickListener implements View.OnClickListener {    // 우측 하단의 + 버튼을 누르면 AddRoomActivity로 이동해서 방 개설 가능
        @Override
        public void onClick(View view) {
            if (email != "") {
                Intent intent = new Intent(MainActivity.this, AddRoomActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("로그인").setMessage("로그인이 필요한 서비스입니다 로그인 하시겠습니까?");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        signIn();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Toast.makeText(getApplicationContext(), "Cancel Click", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog alertDialog = builder.create();

                alertDialog.show();
            }
        }
    }

    // oauth login methods
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
              String personName = acct.getDisplayName();
              String personGivenName = acct.getGivenName();
              String personFamilyName = acct.getFamilyName();
              String personEmail = acct.getEmail();
              //email = personEmail;
              String personId = acct.getId();
              Uri personPhoto = acct.getPhotoUrl();

              System.out.println("===로그인===");
              System.out.println(personName);
              System.out.println(personGivenName);
              System.out.println(personFamilyName);
              System.out.println(personEmail);
              System.out.println(personEmail.indexOf('@'));
              int endOfEmailIndex = personEmail.indexOf('@');
              email = personEmail.substring(0, endOfEmailIndex);
              System.out.println(email);
              System.out.println(personId);
              System.out.println(personPhoto);
              postUser(email);
            }
        } catch (ApiException e) {
            System.out.println("오류");
            System.out.println(e.toString());
        }
    }

    private void postUser(String email) {   // 개설된 방의 정보를 서버에 보냄
        System.out.println("postUser!!!");

        String url = "http://15.164.226.229:3000/login";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.start();

        JSONObject params = new JSONObject();

        try {
            params.put("email", email);
        } catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, params,
                        response -> {
                            //Toast.makeText(getApplicationContext(), "email msg from server : " + response, Toast.LENGTH_LONG).show();
                        }, error -> {
                            //Toast.makeText(getApplicationContext(), "fail : msg from server", Toast.LENGTH_LONG).show();
        });

        requestQueue.add(jsonObjReq);
    }
    // oauth login methods end
}
