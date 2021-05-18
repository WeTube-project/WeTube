package com.andkjyk.wetube_v0;

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

import org.json.JSONException;

import java.util.ArrayList;

public class UsersFragment extends Fragment {

    private RecyclerView usersRecyclerView;
    private UsersAdapter usersAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private TextView tv_my_name;
    private ImageView host_icon;

    private ArrayList<UserItem> userItems = new ArrayList<>();
    private String host_name, user_name;
    private ArrayList<String> user = new ArrayList<>();
    private boolean isHost;

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

        getUser();
        getData();
        Bundle bundle = getArguments();


        isHost = bundle.getBoolean("isHost");
        tv_my_name = view.findViewById(R.id.tv_my_name);

        if(isHost == true){
            host_name = bundle.getString("host_name");
            tv_my_name.setText(host_name);
            host_icon = view.findViewById(R.id.host_icon);
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

    private void getData(){
        userItems.clear();
        ArrayList<String> list = new ArrayList<>();
        int len = user.size();

        for(int i = 0; i < len; i++){
            list.add(user.get(i));
        }

        for(int i = 0; i < len; i++){
            UserItem data = new UserItem();
            data.setUserName(list.get(i));
            userItems.add(data);
        }
    }

    private void getUser() {
        String url = "http://3.37.36.38:3000/user";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.start();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        user = (ArrayList<String>) response.get("user");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getContext(), "msg from server => user: " + user , Toast.LENGTH_LONG).show();
                }, error -> {
            Toast.makeText(getContext(), "fail : msg from server", Toast.LENGTH_LONG).show();
        });

        requestQueue.add(jsonObjReq);
    }
}
