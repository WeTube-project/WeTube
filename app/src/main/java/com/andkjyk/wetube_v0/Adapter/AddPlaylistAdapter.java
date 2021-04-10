package com.andkjyk.wetube_v0.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andkjyk.wetube_v0.Model.PlaylistItem;
import com.andkjyk.wetube_v0.Model.SearchedVideoItem;
import com.andkjyk.wetube_v0.R;
import com.bumptech.glide.Glide;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

public class AddPlaylistAdapter extends RecyclerView.Adapter<AddPlaylistAdapter.ViewHolder>{

    private ArrayList<SearchedVideoItem> searchedList = null;
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

    public AddPlaylistAdapter(Context context, ArrayList<SearchedVideoItem> searchedList) {
        this.searchedList = searchedList;
        this.context = context;
    }

    public void addItems(ArrayList<SearchedVideoItem> items){
        this.searchedList = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_item, parent, false);
        AddPlaylistAdapter.ViewHolder vh = new AddPlaylistAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SearchedVideoItem plItem = searchedList.get(position);

        holder.tv_pl_video_name.setText(plItem.getTitle());
        holder.tv_pl_publisher.setText(plItem.getPublisher());

        String url = plItem.getThumbnailURL();
        Glide.with(holder.itemView.getContext()).load(url).into(holder.pl_thumbnail);
    }

    @Override
    public int getItemCount() {
        return (null != searchedList ? searchedList.size():0);
    }
}
