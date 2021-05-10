package com.andkjyk.wetube_v0.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.andkjyk.wetube_v0.Model.PlaylistItem;
import com.andkjyk.wetube_v0.Model.SearchedVideoItem;
import com.andkjyk.wetube_v0.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {

    private ArrayList<PlaylistItem> plList = null;
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView pl_thumbnail;
        TextView tv_pl_video_name, tv_pl_publisher;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pl_thumbnail = itemView.findViewById(R.id.pl_thumbnail);
            tv_pl_video_name = itemView.findViewById(R.id.tv_pl_video_name);
            tv_pl_publisher = itemView.findViewById(R.id.tv_pl_publisher);
        }
    }

    public PlaylistAdapter(Context context, ArrayList<PlaylistItem> plList){
        this.plList = plList;
        this.context = context;
    }

    //public void addItems(ArrayList<PlaylistItem> items){ this.plList = items; }
    public void addItem(PlaylistItem item){ plList.add(item); }

    @NonNull
    @Override
    public PlaylistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_item, parent, false);
        PlaylistAdapter.ViewHolder vh = new PlaylistAdapter.ViewHolder(view);
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
    public void onBindViewHolder(@NonNull PlaylistAdapter.ViewHolder holder, int position) {
        PlaylistItem plItem = plList.get(position);

        holder.tv_pl_video_name.setText(plItem.getPlVideoName());
        holder.tv_pl_publisher.setText(plItem.getPlPublisher());

        String url = plItem.getPlThumbnailURL();

        int radius = convertDpToPixel(5, context);

        Glide.with(holder.itemView.getContext()).load(url).transform(new CenterCrop(), new RoundedCorners(radius)).into(holder.pl_thumbnail);
    }

    @Override
    public int getItemCount() {
        return (null != plList ? plList.size():0);
    }
}
