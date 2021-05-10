package com.andkjyk.wetube_v0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.graphics.Color;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class AddRoomActivity extends AppCompatActivity {

    private static final int ADDROOM_REQUEST_CODE = 322;
    private ImageView left_icon;
    private Button random_btn;
    private TextView complete_btn;
    private EditText code, room_title_input, host_name_input;
    boolean isCodeEntered, isTitleEntered, isHostNameEntered;
    String room_code, room_title, host_name;
    JSONObject testjson = new JSONObject();
    String url = "http://192.168.0.12:4000";

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

        left_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddRoomActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        complete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                room_title = room_title_input.getText().toString();
                host_name = host_name_input.getText().toString();
                room_code= code.getText().toString();

                Intent intent = new Intent(AddRoomActivity.this, RoomActivity.class);
                intent.putExtra("roomTitle", room_title);
                intent.putExtra("roomCode", room_code);
                intent.putExtra("hostName", host_name);
                intent.putExtra("ActivityName", "AddRoom");
                startActivityForResult(intent, ADDROOM_REQUEST_CODE);
                try {
                    testjson.put("roomtitle", room_title);
                    testjson.put("roomcode", room_code);
                    testjson.put("hostname", host_name);

                    String jsonString = testjson.toString();

                    final RequestQueue requestQueue = Volley.newRequestQueue(AddRoomActivity.this);
                    final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, testjson, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }

                        public void ErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });
                    jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(jsonObjectRequest);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        random_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                room_code = randomCodeMaker();
                code.setText(room_code);
                Snackbar.make(view, "ROOM 코드가 '" + room_code + "'로 설정되었습니다.", Snackbar.LENGTH_SHORT).show();
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
            public void afterTextChanged(Editable edit) {
                String s = edit.toString();
                isTitleEntered = true;
                System.out.println("변경된 text: " + s);
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
            public void afterTextChanged(Editable edit) {
                String s = edit.toString();
                isCodeEntered = true;
                System.out.println("변경된 text: " + s);
                enableCompleteBtn(s, 1);
            }
        });

        host_name_input.addTextChangedListener(new TextWatcher() {
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
                System.out.println("변경된 text: " + s);
                enableCompleteBtn(s, 2);
            }
        });

}
        private void enableCompleteBtn (String editable,int pos){
            if (isTitleEntered == true && isCodeEntered == true && isHostNameEntered == true && editable.trim().isEmpty() == false) {
                complete_btn.setTextColor(Color.parseColor("#FF7473"));
                complete_btn.setClickable(true);
                complete_btn.setEnabled(true);
            } else {
                if (pos == 0 && editable.trim().isEmpty()) {
                    isTitleEntered = false;
                } else if (pos == 1 && editable.trim().isEmpty()) {
                    isCodeEntered = false;
                } else if (pos == 2 && editable.trim().isEmpty()) {
                    isHostNameEntered = false;
                }
                complete_btn.setTextColor(Color.parseColor("#7E7E7E"));
                complete_btn.setClickable(false);
                complete_btn.setEnabled(false);
            }
        }
        
    private String randomCodeMaker() {
        Random rnd = new Random();
        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < 6; i++) {
            // rnd.nextBoolean()는 랜덤으로 true 또는 false를 반환
            // true면 소문자를 랜덤으로, false면 숫자를 랜덤으로 생성하여 StringBuffer 에 append 한다.
            if (rnd.nextBoolean()) {
                buf.append((char) ((int) (rnd.nextInt(26)) + 97));
            } else {
                buf.append((rnd.nextInt(10)));
            }
        }
        return buf.toString();
    }
}









