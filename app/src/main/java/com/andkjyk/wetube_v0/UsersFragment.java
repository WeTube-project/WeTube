package com.andkjyk.wetube_v0;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andkjyk.wetube_v0.Adapter.UsersAdapter;
import com.andkjyk.wetube_v0.Model.UserItem;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UsersFragment extends Fragment {   // 사용자 목록 fragment

    private RecyclerView usersRecyclerView;
    private UsersAdapter usersAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private TextView tv_my_name;
    private ImageView host_icon;

    private ArrayList<UserItem> userItems = new ArrayList<>();
    private String host_name, user_name, room_code;
    private ArrayList<String> user = new ArrayList<>();
    private String isHost;
    private int user_size = 0;

    public UsersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_users, container, false);

        getData();
        Bundle bundle = getArguments();


        isHost = bundle.getString("isHost");
        room_code = bundle.getString("roomCode"); // 번들에서 roomCode도 받아옴
        tv_my_name = view.findViewById(R.id.tv_my_name);

        if(isHost == "true"){
            host_name = bundle.getString("host_name");
            tv_my_name.setText(host_name);
            host_icon = view.findViewById(R.id.host_icon);
            user_name = host_name;
        }else{
            host_name = bundle.getString("host_name");
            user_name = bundle.getString("user_name");
            tv_my_name.setText(user_name);
            host_icon = view.findViewById(R.id.host_icon_notme);

            TextView tv_host_name = view.findViewById(R.id.host_name);
            tv_host_name.setVisibility(View.VISIBLE);
            System.out.println("호스트 이름: "+host_name);
            tv_host_name.setText(host_name);
        }
        host_icon.setVisibility(View.VISIBLE);


        usersRecyclerView = (RecyclerView) view.findViewById(R.id.rv_users);
        usersRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        usersRecyclerView.setLayoutManager(layoutManager);
        usersRecyclerView.setItemAnimator(new DefaultItemAnimator());
        usersAdapter = new UsersAdapter(getActivity(), userItems);
        usersRecyclerView.setAdapter(usersAdapter);

        return view;
    }

    private void getData(){     // 사용자 목록 데이터를 서버에서 받아옴
        String url = "http://3.37.36.38:3000/user";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray users = response.getJSONArray("users");
                        //System.out.println("user_size = " + user_size + " userSize = " + response.getInt("userSize"));
                        user_size = response.getInt("userSize");

                        user.clear();
                        for(int i = 0; i < user_size; i++) { //roomCode 받아온거랑 서버에서 받은 user테이블이랑 비교해서 같은 방에있는 유저 판별
                            if(users.getJSONObject(i).getString("roomCode").equals(room_code)) {
                                user.add(users.getJSONObject(i).getString("userName"));
                            }
                        }
                        ArrayList<String> list = new ArrayList<>();
                        int len = user.size();
                        //Toast.makeText(getContext(), "roomCode: " +users.getJSONObject(0).getString("roomCode")  , Toast.LENGTH_LONG).show();
                       Toast.makeText(getContext(), "user: " + user+len , Toast.LENGTH_LONG).show();


                        //System.out.println("length: "+user.size());

                        userItems.clear();
                        list.clear();

                        for(int i = 0; i < len; i++){
                            list.add(user.get(i));
                            //System.out.println("user list ["+i+"] : "+user.get(i)+"/"+user.get(i));
                        }

                        for(int i = 0; i < len; i++){
                            //System.out.println("user list ["+i+"] : "+user.get(i));
                            if(!user_name.equals(list.get(i))){
                                UserItem data = new UserItem();
                                data.setUserName(list.get(i));
                                userItems.add(data);
                                //System.out.println("userItems: "+userItems.get(i).getUserName());
                            }
                        }
                        usersAdapter.addItems(userItems);
                        usersAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        //Toast.makeText(getContext(), "get user fail" , Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }, error -> {
            //Toast.makeText(getContext(), "fail : msg from server", Toast.LENGTH_LONG).show();
        });
        requestQueue.add(jsonObjReq);

    }
}
