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
import com.andkjyk.wetube_v0.R;

import java.util.ArrayList;

public class AddPlaylistAdapter extends RecyclerView.Adapter<AddPlaylistAdapter.ViewHolder>{

    private ArrayList<PlaylistItem> searchedList = null;
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

    public AddPlaylistAdapter(Context context, ArrayList<PlaylistItem> searchedList) {
        this.searchedList = searchedList;
        this.context = context;
    }

    public void addItems(ArrayList<PlaylistItem> items){
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
        PlaylistItem plItem = searchedList.get(position);

        holder.tv_pl_video_name.setText(plItem.getPlVideoName());
        holder.tv_pl_publisher.setText(plItem.getPlPublisher());
    }

    @Override
    public int getItemCount() {
        return (null != searchedList ? searchedList.size():0);
    }
}
