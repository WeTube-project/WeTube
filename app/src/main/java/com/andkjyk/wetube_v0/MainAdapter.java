package com.andkjyk.wetube_v0;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private ArrayList<MainItem> mainList = null;
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
                        Snackbar.make(v, pos+"번째 방 선택", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public MainAdapter(Context context, ArrayList<MainItem> list){
        this.mainList = list;
        this.context = context;
    }

    void addItems(ArrayList<MainItem> items){
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


    }

    @Override
    public int getItemCount() {
        return (null != mainList ? mainList.size():0);
    }
}
