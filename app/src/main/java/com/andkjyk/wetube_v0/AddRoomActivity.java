package com.andkjyk.wetube_v0;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.graphics.Color;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.andkjyk.wetube_v0.Adapter.AddPlaylistAdapter;
import com.andkjyk.wetube_v0.Adapter.AddRoomAdapter;
import com.andkjyk.wetube_v0.Model.SearchedVideoItem;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class AddRoomActivity extends AppCompatActivity {    // 방 개설 액티비티


    private SearchView searchView;
    private String roomCode;

    private ArrayList<SearchedVideoItem> searchedItemList = new ArrayList<>();
    private Intent intent;
    private static final int ADDROOM_REQUEST_CODE = 322;
    private ImageView left_icon;
    private Button random_btn;
    private TextView complete_btn;
    private EditText code, room_title_input, host_name_input;
    boolean isCodeEntered, isTitleEntered, isHostNameEntered;
    String room_code, room_title, host_name;
    String videoId, publisher, thumbnailUrl, title;
    private void postRoom() {   // 개설된 방의 정보를 서버에 보냄
        String url = "http://15.164.226.229:3000/room";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.start();

        JSONObject params = new JSONObject();

        try {
            params.put("roomTitle", room_title);
            params.put("hostName", host_name);
            params.put("roomCode", room_code);
        } catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, params,
                        response -> {
                            //Toast.makeText(getApplicationContext(), "msg from server : " + response, Toast.LENGTH_LONG).show();
                        }, error -> {
                            //Toast.makeText(getApplicationContext(), "fail : msg from server", Toast.LENGTH_LONG).show();
        });

        requestQueue.add(jsonObjReq);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        left_icon = findViewById(R.id.left_icon);
        complete_btn = findViewById(R.id.complete_btn);
        random_btn = findViewById(R.id.random_btn);
        code = findViewById(R.id.random_code);
        room_title_input = findViewById(R.id.room_title_input);
        host_name_input = findViewById(R.id.host_name_input);
        searchView = findViewById(R.id.searchView);
        intent = getIntent();
        videoId = intent.getStringExtra("s_videoId");
        publisher = intent.getStringExtra("s_publisher");
        thumbnailUrl = intent.getStringExtra("s_thumbnailUrl");
        title = intent.getStringExtra("s_title");

        left_icon.setOnClickListener(new View.OnClickListener() {   // 좌측 상단 뒤로가기 아이콘을 누르면 방 목록 액티비티로 이동
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddRoomActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        complete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {    // 완료 버튼을 누르면 기입한 정보를 서버로 보냄
                room_title = room_title_input.getText().toString();
                host_name = host_name_input.getText().toString();
                room_code = code.getText().toString();
                postMedia(room_code, title, publisher,thumbnailUrl, videoId);
                Intent intent = new Intent(AddRoomActivity.this, RoomActivity.class);
                intent.putExtra("roomTitle", room_title);
                intent.putExtra("roomCode", room_code);
                intent.putExtra("hostName", host_name);
                intent.putExtra("videoId",videoId);
                intent.putExtra("ActivityName", "AddRoom");

                postRoom();

                //여기서 postmedia로 보내기
                startActivityForResult(intent, ADDROOM_REQUEST_CODE);
            }
        });

        random_btn.setOnClickListener(new View.OnClickListener(){   // 버튼을 누르면 랜덤으로 roomcode를 생성
            @Override
            public void onClick(View view) {
                room_code = randomCodeMaker();
                code.setText(room_code);
                Snackbar.make(view, "ROOM 코드가 '"+room_code+"'로 설정되었습니다.", Snackbar.LENGTH_SHORT).show();
            }
        });

        room_title_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable edit) {   // 방 제목 칸이 비어있는지 감지
                String s = edit.toString();
                isTitleEntered = true;
                System.out.println("변경된 text: "+ s);
                enableCompleteBtn(s, 0);
            }
        });

        code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable edit) {       // 방 코드 칸이 비어있는지 감지
                String s = edit.toString();
                isCodeEntered = true;
                System.out.println("변경된 text: "+ s);
                enableCompleteBtn(s, 1);
            }
        });

        host_name_input.addTextChangedListener(new TextWatcher() {      // 호스트 이름 칸이 비어있는지 감지
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable edit) {
                String s = edit.toString();
                isHostNameEntered = true;
                System.out.println("변경된 text: "+ s);
                enableCompleteBtn(s, 2);
            }
        });
    }

    private void enableCompleteBtn(String editable, int pos) {  // afterTextChanged로 각 칸이 비어있는지 감지한 후에, 모든 칸이 비어있지 않으면 완료버튼이 활성화되고 그렇지 않은 경우 비활성화됨
        if(isTitleEntered == true && isCodeEntered == true &&  isHostNameEntered == true && editable.trim().isEmpty() == false){
            complete_btn.setTextColor(Color.parseColor("#FF7473"));
            complete_btn.setClickable(true);
            complete_btn.setEnabled(true);
        }else{
            if(pos == 0 && editable.trim().isEmpty()){
                isTitleEntered = false;
            }else if(pos == 1 && editable.trim().isEmpty()){
                isCodeEntered = false;
            }else if(pos == 2 && editable.trim().isEmpty()){
                isHostNameEntered = false;
            }
            complete_btn.setTextColor(Color.parseColor("#7E7E7E"));
            complete_btn.setClickable(false);
            complete_btn.setEnabled(false);
        }
    }

    private String randomCodeMaker(){   // 랜덤으로 방 코드를 생성
        Random rnd =new Random();
        StringBuffer buf =new StringBuffer();

        for(int i = 0; i < 6; i++){
            // rnd.nextBoolean()는 랜덤으로 true 또는 false를 반환
            // true면 소문자를 랜덤으로, false면 숫자를 랜덤으로 생성하여 StringBuffer 에 append 한다.
            if(rnd.nextBoolean()){
                buf.append((char)((int)(rnd.nextInt(26))+97));
            }else{
                buf.append((rnd.nextInt(10)));
            }
        }
        return buf.toString();
    }
    private void postMedia(String roomCode, String videoTitle, String publisher, String thumbnailUrl, String videoId) { // 재생목록에 추가된 영상에 대한 데이터를 서버에 보냄
        String url = "http://15.164.226.229:3000/media";
        RequestQueue requestQueue = Volley.newRequestQueue(this.getApplicationContext());
        requestQueue.start();

        JSONObject params = new JSONObject();

        /*
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            videoTitle = String.valueOf(Html.fromHtml(videoTitle, Html.FROM_HTML_MODE_COMPACT));
        } else {
            videoTitle = String.valueOf(Html.fromHtml(videoTitle));
        }
         */

        try {
            params.put("roomCode", roomCode);
            params.put("videoTitle", videoTitle);
            params.put("publisher", publisher);
            params.put("thumbnailUrl", thumbnailUrl);
            params.put("videoId", videoId);
        } catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, params,
                response -> {
                    //Toast.makeText(getActivity().getApplicationContext(), "msg from server : " + response, Toast.LENGTH_LONG).show();
                }, error -> {
            //Toast.makeText(getActivity().getApplicationContext(), "fail : msg from server", Toast.LENGTH_LONG).show();
        });

        requestQueue.add(jsonObjReq);
    }


}