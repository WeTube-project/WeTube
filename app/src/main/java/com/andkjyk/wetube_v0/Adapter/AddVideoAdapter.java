package com.andkjyk.wetube_v0.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andkjyk.wetube_v0.AddRoomActivity;
import com.andkjyk.wetube_v0.AddVideoActivity;
import com.andkjyk.wetube_v0.MainActivity;
import com.andkjyk.wetube_v0.Model.SearchedVideoItem;
import com.andkjyk.wetube_v0.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.ArrayList;

public class AddVideoAdapter extends RecyclerView.Adapter<AddVideoAdapter.ViewHolder>{

    private ArrayList<SearchedVideoItem> searchedList = null;
    Context context;
    String userName;
    Uri profileImage;

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView pl_thumbnail;
        TextView tv_pl_video_name, tv_pl_publisher;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pl_thumbnail = itemView.findViewById(R.id.pl_thumbnail);
            tv_pl_video_name = itemView.findViewById(R.id.tv_pl_video_name);
            tv_pl_publisher = itemView.findViewById(R.id.tv_pl_publisher);

            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {    // 검색해서 나온 영상 목록 중에 원하는 영상을 선택하면 ~를 플레이리스트에 추가할것인지 확인하는 팝업을 띄움. 확인 누르면 RoomActivity로 이동
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){

                        SearchedVideoItem s_videoItem = searchedList.get(pos);
                        String videoId = s_videoItem.getId();
                        String publisher = s_videoItem.getPublisher();
                        String thumbnailUrl = s_videoItem.getThumbnailURL();
                        String title = s_videoItem.getTitle();

                        System.out.println("결과: "+thumbnailUrl);

                        String sTitle = null;
                        AlertDialog.Builder alt_bld = new AlertDialog.Builder(view.getContext(), R.style.AlertDialogStyle);
                        if(title.length() > 35){
                            sTitle = title.substring(0, 35)+"...";
                        } else{
                            sTitle = title;
                        }
                        String finalSTitle = sTitle;
                        System.out.println("짤렸는지 확인: "+finalSTitle);
                        alt_bld.setMessage(sTitle+"을(를) 플레이리스트에 추가하시겠습니까?").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Intent intent = new Intent(view.getContext(), AddRoomActivity.class);
                                intent.putExtra("s_videoId", videoId);
                                intent.putExtra("s_publisher", publisher);
                                intent.putExtra("s_thumbnailUrl", thumbnailUrl);
                                intent.putExtra("s_title", finalSTitle);
                                intent.putExtra("userName", userName);
                                intent.putExtra("profileImage", profileImage);
                                //((AddRoomActivity) context).setResult(Activity.RESULT_OK, intent);
                                ((AddVideoActivity) context).startActivity(intent);
                            }
                        }).setNegativeButton("취소", null);
                        AlertDialog alert = alt_bld.create();
                        alert.show();
                    }
                }
            });
        }
    }

    public AddVideoAdapter(Context context, ArrayList<SearchedVideoItem> searchedList) {
        this.searchedList = searchedList;
        this.context = context;
    }


    public void addItems(ArrayList<SearchedVideoItem> items){
        this.searchedList = items;
    }

    @NonNull
    @Override
    public AddVideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_item, parent, false);
        AddVideoAdapter.ViewHolder vh = new AddVideoAdapter.ViewHolder(view);
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
    public void onBindViewHolder(@NonNull AddVideoAdapter.ViewHolder holder, int position) {
        SearchedVideoItem plItem = searchedList.get(position);

        holder.tv_pl_video_name.setText(plItem.getTitle());
        holder.tv_pl_publisher.setText(plItem.getPublisher());

        String url = plItem.getThumbnailURL();

        userName = ((AddVideoActivity) AddVideoActivity.mContext).userName;
        profileImage = ((AddVideoActivity) AddVideoActivity.mContext).profileImage;

        int radius = convertDpToPixel(5, context);
        Glide.with(holder.itemView.getContext()).load(url).transform(new CenterCrop(), new RoundedCorners(radius)).into(holder.pl_thumbnail);
    }

    @Override
    public int getItemCount() {
        return (null != searchedList ? searchedList.size():0);
    }
}
