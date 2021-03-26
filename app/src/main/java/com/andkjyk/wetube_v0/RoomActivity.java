package com.andkjyk.wetube_v0;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

public class RoomActivity extends AppCompatActivity {

    private ImageView left_icon;
    Fragment frag_playlist, frag_users, frag_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        left_icon = findViewById(R.id.left_icon);
        left_icon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(RoomActivity.this, MainActivity.class);
                startActivity(intent);
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
                if(position == 0){
                    selected = frag_chat;
                }else if (position == 1){
                    selected = frag_users;
                }else if (position == 2){
                    selected = frag_playlist;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.room_frame, selected).commit();
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
}