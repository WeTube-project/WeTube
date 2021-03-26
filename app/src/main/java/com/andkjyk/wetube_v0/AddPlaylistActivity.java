package com.andkjyk.wetube_v0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AddPlaylistActivity extends AppCompatActivity {

    private ImageView left_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_playlist);

        left_icon = findViewById(R.id.left_icon);
        left_icon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AddPlaylistActivity.this, RoomActivity.class);
                startActivity(intent);
            }
        });
    }
}