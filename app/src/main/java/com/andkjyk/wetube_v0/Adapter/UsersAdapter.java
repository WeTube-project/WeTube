package com.andkjyk.wetube_v0.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andkjyk.wetube_v0.Model.UserItem;
import com.andkjyk.wetube_v0.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    private ArrayList<UserItem> userList = null;
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView user_item;
        ImageView host_icon;
        CircleImageView user_profile_img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            user_item = itemView.findViewById(R.id.user_name);
            user_profile_img = itemView.findViewById(R.id.profile_img);
            host_icon = itemView.findViewById(R.id.host_icon_notme);
        }
    }

    public UsersAdapter(Context context, ArrayList<UserItem> userList){
        this.userList = userList;
        this.context = context;
    }

    public void addItems(ArrayList<UserItem> items){ this.userList = items; }

    @NonNull
    @Override
    public UsersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_item, parent, false);
        UsersAdapter.ViewHolder vh = new UsersAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.ViewHolder holder, int position) {
        UserItem userItem = userList.get(position);
        holder.user_item.setText(userItem.getUserName());
        // holder.user_profile_img.setImageResource();
    }

    @Override
    public int getItemCount() {
        return (null != userList ? userList.size():0);
    }


}
