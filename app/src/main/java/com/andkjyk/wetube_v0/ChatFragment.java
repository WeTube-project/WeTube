package com.andkjyk.wetube_v0;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.andkjyk.wetube_v0.Adapter.ChatAdapter;
import com.andkjyk.wetube_v0.Model.ChatItem;
import com.andkjyk.wetube_v0.Model.ChatType;
import com.andkjyk.wetube_v0.Model.MessageData;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ChatFragment extends Fragment {

    private RecyclerView chatRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ChatAdapter chatAdapter;
    String room_code, host_name, user_name;
    int room_pos;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        init(view);
        return view;
    }

    private void init(View view){


        chatRecyclerView = (RecyclerView) view.findViewById(R.id.rv_chat);
        chatRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        chatRecyclerView.setLayoutManager(layoutManager);
        chatRecyclerView.setItemAnimator(new DefaultItemAnimator());
        chatAdapter = new ChatAdapter(getActivity());
        chatRecyclerView.setAdapter(chatAdapter);

        ImageButton sendBtn = view.findViewById(R.id.sendBtn);
        EditText editText = view.findViewById(R.id.editText);
        Intent intent = getActivity().getIntent();
        String SenderActivity = intent.getStringExtra("ActivityName");
        if(SenderActivity.equals("AddRoom")){
            // 재생목록에 추가하는 팝업 띄우고, 확인 누르면 AddPlaylistActivity로 인텐트 보냄
            System.out.println("AddRoomActivity로부터 옴");
            //room_title = intent.getStringExtra("roomTitle");
            room_code = intent.getStringExtra("roomCode");
            host_name = intent.getStringExtra("hostName");
            MessageData data = new MessageData("ENTER", host_name, room_code,host_name+"님이 입장하셨습니다.", System.currentTimeMillis());
            addChat(data);
            chatRecyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
        } else if(SenderActivity.equals("Main")){
            user_name = intent.getStringExtra("userName");
            room_pos = intent.getIntExtra("roomPos", -1);
            MessageData data = new MessageData("ENTER", user_name, room_pos+"",user_name+"님이 입장하셨습니다.", System.currentTimeMillis());
            addChat(data);
            chatRecyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
        } else{
            System.out.println("RoomActivity가 intent를 제대로 받아오지 못함");
        }

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(view);
            }
        });
    }

    // 리사이클러뷰에 채팅 추가
    private void addChat(MessageData data) {
        getActivity().runOnUiThread(() -> {
            if (data.getType().equals("ENTER") || data.getType().equals("LEFT")) {
                chatAdapter.addItem(new ChatItem(data.getFrom(), data.getContent(), toDate(data.getSendTime()), ChatType.CENTER_MESSAGE));
                chatRecyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
            } else {
                chatAdapter.addItem(new ChatItem(data.getFrom(), data.getContent(), toDate(data.getSendTime()), ChatType.LEFT_MESSAGE));
                chatRecyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
            }
        });
    }

    private void sendMessage(View view) {
        EditText msg = view.findViewById(R.id.editText);
        Log.d("MESSAGE", new MessageData("MESSAGE",
                user_name,
                room_pos+"",
                msg.getText().toString(),
                System.currentTimeMillis()).toString());
        chatAdapter.addItem(new ChatItem(user_name, msg.getText().toString(), toDate(System.currentTimeMillis()), ChatType.RIGHT_MESSAGE));
        chatRecyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
        msg.setText("");
    }

    // System.currentTimeMillis를 몇시:몇분 am/pm 형태의 문자열로 반환
    private String toDate(long currentMiliis) {
        return new SimpleDateFormat("a hh:mm").format(new Date(currentMiliis));
    }
}