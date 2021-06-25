package com.andkjyk.wetube_v0;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {     // 스플래시 화면 (앱 실행 시 처음 화면)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run( )
            {
                //1초 뒤 MainActivity로 이동
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // 이제 더이상 사용하지 않을 액티비티이므로 현재 액티비티를 파괴
            }
        }, 1000);
    }
}