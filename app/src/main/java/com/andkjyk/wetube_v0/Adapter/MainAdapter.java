package com.andkjyk.wetube_v0.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.andkjyk.wetube_v0.MainActivity;
import com.andkjyk.wetube_v0.Model.RoomItem;
import com.andkjyk.wetube_v0.R;
import com.andkjyk.wetube_v0.RoomActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private ArrayList<RoomItem> mainList = new ArrayList<>();
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
                public void onClick(View v) {       // 방 목록에서 원하는 방을 선택하면, 닉네임을 입력하라는 팝업이 뜨도록 함. 확인을 누르면 RoomActivity로 이동해서 방에 입장.
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        //Snackbar.make(v, pos+"번째 방 선택", Snackbar.LENGTH_SHORT).show();

//                        final EditText et = new EditText(v.getContext());
//
//
//                        et.setSingleLine(true); //EditText를 한 줄로 제한
//                        et.setTypeface(Typeface.DEFAULT); //글꼴 적용
//                        et.setHint("닉네임은 2~8글자만 등록할 수 있습니다.");
//
//                        FrameLayout container = new FrameLayout(v.getContext());
//                        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                        params.leftMargin = v.getResources().getDimensionPixelSize(R.dimen.dialog_margin);
//                        params.rightMargin = v.getResources().getDimensionPixelSize(R.dimen.dialog_margin);
//
//                        et.setBackgroundTintList(ContextCompat.getColorStateList(v.getContext(), R.color.colorPrimary));
//                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
//                            et.setTextCursorDrawable(R.drawable.dialog_cursur);
//                        }
//                        et.setLayoutParams(params);
//
//                        container.addView(et);
//
//                        final AlertDialog.Builder alt_bld = new AlertDialog.Builder(v.getContext(),R.style.AlertDialogStyle);
//
//                        alt_bld.setMessage("사용하실 닉네임을 입력하세요.")
//                                .setCancelable(false).setView(container).setPositiveButton("확인",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        String userName = et.getText().toString();
//                                        Intent intent = new Intent(v.getContext(), RoomActivity.class);
//                                        System.out.println("MainAdapter - 호스트이름: "+mainList.get(pos).getHostName());
//                                        intent.putExtra("hostName", mainList.get(pos).getHostName());
//                                        intent.putExtra("userName", userName);
//                                        intent.putExtra("videoId", mainList.get(pos).getVideoId());
//                                        System.out.println("방코드: "+mainList.get(pos).getRoomCode());
//                                        intent.putExtra("roomCode", mainList.get(pos).getRoomCode());    // 몇번째 방인지.. 필요할지는 모르겠음 roomCode를 알아야할것같은데..
//                                        intent.putExtra("ActivityName", "Main");
//                                        ((MainActivity) context).startActivity(intent);
//                                    }
//
//                                }).setNegativeButton("취소", null);
//
//                        final AlertDialog alert = alt_bld.create();
//
//                        et.addTextChangedListener(new TextWatcher() {
//                            @Override
//                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                            }
//
//                            @Override
//                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {   // 텍스트 입력을 감지해서 닉네임 길이에 따라 버튼 활성화/비활성화
//                                Button button = alert.getButton(AlertDialog.BUTTON_POSITIVE);
//                                //System.out.println("닉네임 길이: "+et.getText().length());
//                                if(et.getText().length() >= 2 && et.getText().length() <= 8){
//                                    //입력값이 2글자~8글자 일 때만 확인 버튼 활성화
//                                    button.setEnabled(true);
//                                }else{
//                                    //그 외의 경우는 확인 버튼 비활성화
//                                    button.setEnabled(false);
//                                }
//                            }
//
//                            @Override
//                            public void afterTextChanged(Editable editable) {
//
//                            }
//                        });
//
//                        alert.show();
//                        alert.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                        //String userName = et.getText().toString();
                        String userName = ((MainActivity) MainActivity.mContext).email;
                        Intent intent = new Intent(v.getContext(), RoomActivity.class);
                        System.out.println("MainAdapter - 호스트이름: "+mainList.get(pos).getHostName());
                        intent.putExtra("email", userName);
                        intent.putExtra("hostName", mainList.get(pos).getHostName());
                        //intent.putExtra("userName", userName);
                        intent.putExtra("videoId", mainList.get(pos).getVideoId());
                        System.out.println("방코드: "+mainList.get(pos).getRoomCode());
                        intent.putExtra("roomCode", mainList.get(pos).getRoomCode());    // 몇번째 방인지.. 필요할지는 모르겠음 roomCode를 알아야할것같은데..
                        intent.putExtra("ActivityName", "Main");
                        ((MainActivity) context).startActivity(intent);
                    }
                }
            });
        }
    }

    public MainAdapter(Context context, ArrayList<RoomItem> list){
        this.mainList = list;
        this.context = context;
    }

    public void addItems(ArrayList<RoomItem> items){
        this.mainList = items;
    }

    public void clear(){
        mainList.clear();
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

    //dp를 px로 변환 (dp를 입력받아 px을 리턴)
    public static int convertDpToPixel(int dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = dp * (metrics.densityDpi / 160);
        return px;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RoomItem roomItem = mainList.get(position);

        holder.tv_room_title.setText(roomItem.getRoomTitle());
        holder.tv_headcount.setText(roomItem.getHeadcount());
        holder.tv_video_name_playing.setText(roomItem.getVideoName());
        String url = roomItem.getThumbnail();

        int radius = convertDpToPixel(5, context);

        Glide.with(holder.itemView.getContext()).load(url).transform(new CenterCrop(), new RoundedCorners(radius)).into(holder.room_thumbnail);
    }

    @Override
    public int getItemCount() {
        return (null != mainList ? mainList.size():0);
    }
}
