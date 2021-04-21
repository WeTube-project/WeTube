package com.andkjyk.wetube_v0.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.andkjyk.wetube_v0.MainActivity;
import com.andkjyk.wetube_v0.Model.MainItem;
import com.andkjyk.wetube_v0.R;
import com.andkjyk.wetube_v0.RoomActivity;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private ArrayList<MainItem> mainList = new ArrayList<>();
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView room_thumbnail;
        TextView tv_room_title, tv_headcount, tv_video_name_playing;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            room_thumbnail = itemView.findViewById(R.id.room_thumbnail);
            tv_room_title = itemView.findViewById(R.id.tv_room_title);
            tv_headcount = itemView.findViewById(R.id.tv_headcount);
            tv_video_name_playing = itemView.findViewById(R.id.tv_video_name_playing);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        //Snackbar.make(v, pos+"번째 방 선택", Snackbar.LENGTH_SHORT).show();

                        final EditText et = new EditText(v.getContext());


                        et.setSingleLine(true); //EditText를 한 줄로 제한
                        et.setTypeface(Typeface.DEFAULT); //글꼴 적용
                        et.setHint("닉네임은 2~8글자만 등록할 수 있습니다.");

                        FrameLayout container = new FrameLayout(v.getContext());
                        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.leftMargin = v.getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                        params.rightMargin = v.getResources().getDimensionPixelSize(R.dimen.dialog_margin);

                        et.setBackgroundTintList(ContextCompat.getColorStateList(v.getContext(), R.color.colorPrimary));
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                            et.setTextCursorDrawable(R.drawable.dialog_cursur);
                        }
                        et.setLayoutParams(params);

                        container.addView(et);

                        final AlertDialog.Builder alt_bld = new AlertDialog.Builder(v.getContext(),R.style.AlertDialogStyle);

                        alt_bld.setMessage("사용하실 닉네임을 입력하세요.")
                                .setCancelable(false).setView(container).setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        String userName = et.getText().toString();
                                        Intent intent = new Intent(v.getContext(), RoomActivity.class);
                                        intent.putExtra("userName", userName);
                                        intent.putExtra("roomPos", pos);    // 몇번째 방인지.. 필요할지는 모르겠음 roomCode를 알아야할것같은데..
                                        // 애초에 roomPos가 있다면 roomCode가 필요한지 의문
                                        intent.putExtra("ActivityName", "Main");
                                        ((MainActivity) context).startActivity(intent);
                                    }

                                }).setNegativeButton("취소", null);

                        final AlertDialog alert = alt_bld.create();

                        et.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                Button button = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                                //System.out.println("닉네임 길이: "+et.getText().length());
                                if(et.getText().length() >= 2 && et.getText().length() <= 8){
                                    //입력값이 2글자~8글자 일 때만 확인 버튼 활성화
                                    button.setEnabled(true);
                                }else{
                                    //그 외의 경우는 확인 버튼 비활성화
                                    button.setEnabled(false);
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                            }
                        });

                        alert.show();
                        alert.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    }
                }
            });
        }
    }

    public MainAdapter(Context context, ArrayList<MainItem> list){
        this.mainList = list;
        this.context = context;
    }

    public void addItems(ArrayList<MainItem> items){
        this.mainList = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Context context = parent.getContext();
        //LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //View view = inflater.inflate(R.layout.room_item, parent, false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_item, parent, false);
        MainAdapter.ViewHolder vh = new MainAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MainItem mainItem = mainList.get(position);

        holder.tv_room_title.setText(mainItem.getTitle());
        holder.tv_headcount.setText(mainItem.getHeadcount());
        holder.tv_video_name_playing.setText(mainItem.getVideoName());
        String url = mainItem.getThumbnail();
        Glide.with(holder.itemView.getContext()).load(url).into(holder.room_thumbnail);
    }

    @Override
    public int getItemCount() {
        return (null != mainList ? mainList.size():0);
    }
}
