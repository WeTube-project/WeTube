package com.andkjyk.wetube_v0;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import com.andkjyk.wetube_v0.Adapter.AddPlaylistAdapter;
import com.andkjyk.wetube_v0.Model.PlaylistItem;
import com.andkjyk.wetube_v0.Model.SearchedVideoItem;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AddPlaylistActivity extends AppCompatActivity {

    public static final String KEY = "AIzaSyCE9vus0pcVAAdXOiMhdlmQnOBqgqONcHQ";

    private AddPlaylistAdapter adapter;
    private ImageView left_icon;
    private SearchView searchView;
    private String roomCode;

    private ArrayList<SearchedVideoItem> searchedItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_playlist);

        left_icon = findViewById(R.id.left_icon);
        searchView = findViewById(R.id.searchView);

        Intent intent = getIntent();
        roomCode = intent.getExtras().getString("roomCode");
        System.out.println("룸코드: " + roomCode);

        left_icon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AddPlaylistActivity.this, RoomActivity.class);
                intent.putExtra("ActivityName", "AddPlaylist"); // RoomActivity에서 ActivityName으로 분기처리 했기 때문에 필요.. 없으면 에러
                startActivity(intent);
            }
        });

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView recyclerView = findViewById(R.id.rv_search_video);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        adapter = new AddPlaylistAdapter(this, searchedItemList);
        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                // 입력받은 문자열 처리
                final String query = s;
                //Toast.makeText(LocalSearchActivity.this, "[검색버튼클릭] 검색어 = "+s, Toast.LENGTH_LONG).show();

                // Thread로 웹서버에 접속
                new Thread()
                {
                    public void run( )
                    {
                        getYoutubeSearch(query);
                        adapter.addItems(searchedItemList);
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run( )
                            {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                }.start();

                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });


        //getData();
    }

    private void getYoutubeSearch(String query) {
        try {
            HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
            final JsonFactory JSON_FACTORY = new GsonFactory();
            final long NUMBER_OF_VIDEOS_RETURNED = 3;

            YouTube youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {
                }
            }).setApplicationName("wetube_v0").build();

            YouTube.Search.List search = youtube.search().list("id,snippet");

            search.setKey(KEY);

            search.setQ(query);
            search.setOrder("relevance"); //date relevance

            search.setType("video");

            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/high/url,snippet/channelTitle,snippet/thumbnails/high/width,snippet/thumbnails/high/height)");
            search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
            SearchListResponse searchResponse = search.execute();

            List<SearchResult> searchResultList = searchResponse.getItems();

            if (searchResultList != null) {
                prettyPrint(searchResultList.iterator(), query);
            }
        } catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
            System.err.println("There was a service error 2: " + e.getLocalizedMessage() + " , " + e.toString());
        } catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void prettyPrint(Iterator<SearchResult> iteratorSearchResults, String query) {
        if (!iteratorSearchResults.hasNext()) {
            System.out.println(" There aren't any results for your query.");
        }

        //StringBuilder sb = new StringBuilder();

        searchedItemList.clear();
        ArrayList<String> listTitle = new ArrayList<>();
        ArrayList<String> listPublisher = new ArrayList<>();
        ArrayList<String> listThumbnail = new ArrayList<>();
        ArrayList<String> listId = new ArrayList<>();

        while (iteratorSearchResults.hasNext()) {
            SearchResult singleVideo = iteratorSearchResults.next();
            ResourceId rId = singleVideo.getId();

            // Double checks the kind is video.
            if (rId.getKind().equals("youtube#video")) {
                Thumbnail thumbnail = (Thumbnail) singleVideo.getSnippet().getThumbnails().get("high");

                String title = singleVideo.getSnippet().getTitle();
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    title = String.valueOf(Html.fromHtml(title, Html.FROM_HTML_MODE_COMPACT));
                } else {
                    title = String.valueOf(Html.fromHtml(title));
                }
                listTitle.add(title);
                //System.out.println("영상 너비: "+thumbnail.getWidth()+" 높이: "+thumbnail.getHeight());
                //System.out.println("영상 게시자: "+singleVideo.getSnippet().getChannelTitle());
                listPublisher.add(singleVideo.getSnippet().getChannelTitle());  // ChannelTitle = Publisher = 게시자
                listThumbnail.add(thumbnail.getUrl());
                listId.add(rId.getVideoId());
            }
        }

        for(int i = 0; i < 3; i++){
            SearchedVideoItem data = new SearchedVideoItem();
            System.out.println("영상 제목: "+listTitle.get(i));
            System.out.println("영상 게시자: "+listPublisher.get(i));
            System.out.println("영상 thumbnail url: "+listThumbnail.get(i));
            System.out.println("영상 id: "+listId.get(i));
            data.setTitle(listTitle.get(i));
            data.setPublisher(listPublisher.get(i));
            data.setThumbnailURL(listThumbnail.get(i));
            data.setId(listId.get(i));
            data.setRoomCode(roomCode);

            searchedItemList.add(data);
        }
    }
/*
    private void getData(){

        searchedItemList.clear();
        ArrayList<String> listTitle = new ArrayList<>();
        ArrayList<String> listPublisher = new ArrayList<>();

        for(int i = 0; i < 12; i++){
            listTitle.add(i+"번째 영상 가나다라마바사아자차카타파하가나다라마바사아자차");
            listPublisher.add(i+"번째 게시자 아야어여오요우유으이");
        }

        for(int i = 0; i < 12; i++){
            SearchedVideoItem data = new SearchedVideoItem();
            System.out.println("정보: "+listTitle.get(i)+" "+listPublisher.get(i));
            data.setTitle(listTitle.get(i));
            data.setPublisher(listPublisher.get(i));

            searchedItemList.add(data);
        }
    }

 */
}