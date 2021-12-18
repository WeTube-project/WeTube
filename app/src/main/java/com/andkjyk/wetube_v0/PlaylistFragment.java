package com.andkjyk.wetube_v0;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andkjyk.wetube_v0.Adapter.PlaylistAdapter;
import com.andkjyk.wetube_v0.Model.PlaylistItem;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlaylistFragment extends Fragment {    // 재생 목록 fragment
    RequestQueue requestQueue;
    private RecyclerView plRecyclerView;
    private PlaylistAdapter plAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private int ADDPLAYLIST_REQUEST_CODE = 208;
    String videoId, publisher, thumbnailUrl, title;
    private ArrayList<PlaylistItem> plItemList = new ArrayList<>();
    String roomCode, email;

    public PlaylistFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {  // AddPlaylistActivity에서 돌아왔을 때 호출됨
        super.onActivityResult(requestCode, resultCode, data);

        if(data != null){
            videoId = data.getStringExtra("s_videoId");
            publisher = data.getStringExtra("s_publisher");
            thumbnailUrl = data.getStringExtra("s_thumbnailUrl");
            title = data.getStringExtra("s_title");
            //System.out.println("영상 제목 짤렸는지 확인~: "+title);
            postMedia(roomCode, title, publisher,thumbnailUrl, videoId);
            plAdapter.addItem(new PlaylistItem(title, publisher, videoId, thumbnailUrl, roomCode));
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

        Bundle bundle = getArguments();
        roomCode = bundle.getString("roomCode");
        email = bundle.getString("email");

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.playlist_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = getArguments();
                roomCode = bundle.getString("roomCode");

                Intent intent = new Intent(getActivity(), AddPlaylistActivity.class);
                intent.putExtra("roomCode", roomCode);
                startActivityForResult(intent, ADDPLAYLIST_REQUEST_CODE);
                //startActivity(intent);
            }
        });

        plRecyclerView = (RecyclerView) view.findViewById(R.id.rv_playlist);
        plRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        plRecyclerView.setLayoutManager(layoutManager);
        plRecyclerView.setItemAnimator(new DefaultItemAnimator());
        plAdapter = new PlaylistAdapter(getActivity(), plItemList);
        plRecyclerView.setAdapter(plAdapter);

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        getData();

        return view;
    }

    private void getData(){     // 재생 목록 데이터를 서버에서 가져옴
        //System.out.println("getData() 호출됨");
        String media_url = "http://15.164.226.229:3000/media";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, media_url, null,
                response -> {
                    try {
                        int roominfo_size = response.getInt("roominfoSize");
                        JSONArray roominfoarr = response.getJSONArray("roominfo");

                        ArrayList<String> listPlVideoName = new ArrayList<>();
                        ArrayList<String> listPlPublisher = new ArrayList<>();
                        ArrayList<String> listPlVideoId = new ArrayList<>();
                        ArrayList<String> listPlThumbnailURL = new ArrayList<>();

                        //Toast.makeText(getActivity().getApplicationContext(), "arr"+roominfoarr , Toast.LENGTH_LONG).show();
                        //Toast.makeText(getActivity().getApplicationContext(), "length"+roominfo_size , Toast.LENGTH_LONG).show();

                        int numOfVideo = 0;
                        for(int i = 0; i < roominfo_size; i++){
                            String rcv_roomCode = roominfoarr.getJSONObject(i).getString("roomCode");
                            if(rcv_roomCode.equals(roomCode)) {
                                JSONObject jsonObject = roominfoarr.getJSONObject(i);

                                String title = jsonObject.getString("title");
                                //System.out.println("제목:" + title);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    title = String.valueOf(Html.fromHtml(title, Html.FROM_HTML_MODE_COMPACT));
                                } else {
                                    title = String.valueOf(Html.fromHtml(title));
                                }

                                PlaylistItem playlist = new PlaylistItem(
                                        title,
                                        jsonObject.getString("publisher"),
                                        jsonObject.getString("videoId"),
                                        jsonObject.getString("thumbnailUrl"),
                                        roomCode
                                );

                                listPlVideoName.add(playlist.getPlVideoName());
                                listPlPublisher.add(playlist.getPlPublisher());
                                listPlVideoId.add(playlist.getPlVideoId());
                                listPlThumbnailURL.add(playlist.getPlThumbnailURL());

                                numOfVideo++;
                            }
                        }

                        plItemList.clear();

                        for(int i = 0; i < numOfVideo; i++){
                            PlaylistItem data = new PlaylistItem();
                            //System.out.println("정보: " + listPlVideoName.get(i) + " " + listPlPublisher.get(i));
                            data.setPlVideoName(listPlVideoName.get(i));
                            data.setPlPublisher(listPlPublisher.get(i));
                            data.setPlVideoId(listPlVideoId.get(i));
                            data.setPlThumbnailURL(listPlThumbnailURL.get(i));
                            data.setPlRoomCode(roomCode);

                            //System.out.println("data 정보: " + data.getPlVideoName());

                            plItemList.add(data);
                        }

                        plAdapter.addItems(plItemList);
                        plAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //  Toast.makeText(getActivity().getApplicationContext(), "msg from server => title: " , Toast.LENGTH_LONG).show();
                }, error -> {
            Toast.makeText(getActivity().getApplicationContext(), "fail : msg from server", Toast.LENGTH_LONG).show();
        });

        requestQueue.add(jsonObjReq);
    }

    private void postMedia(String roomCode, String videoTitle, String publisher, String thumbnailUrl, String videoId) { // 재생목록에 추가된 영상에 대한 데이터를 서버에 보냄
        String url = "http://15.164.226.229:3000/media";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
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