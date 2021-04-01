package com.andkjyk.wetube_v0.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andkjyk.wetube_v0.Model.ChatItem;
import com.andkjyk.wetube_v0.Model.ChatType;
import com.andkjyk.wetube_v0.R;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ChatItem> items = new ArrayList<ChatItem>();
    private Context context;

    public ChatAdapter(Context context) { this.context = context; }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (viewType == ChatType.CENTER_MESSAGE) {
            view = inflater.inflate(R.layout.enter_message, parent, false);
            return new CenterViewHolder(view);
        } else if (viewType == ChatType.LEFT_MESSAGE) {
            view = inflater.inflate(R.layout.received_message, parent, false);
            return new LeftViewHolder(view);
        } else { // if (viewType == ChatItem.RIGHT_MESSAGE){
            view = inflater.inflate(R.layout.my_message, parent, false);
            return new RightViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof CenterViewHolder) {
            ChatItem item = items.get(position);
            ((CenterViewHolder) viewHolder).setItem(item);
        } else if (viewHolder instanceof LeftViewHolder) {
            ChatItem item = items.get(position);
            ((LeftViewHolder) viewHolder).setItem(item);
        } else { // if (viewHolder instanceof RightViewHolder) {
            ChatItem item = items.get(position);
            ((RightViewHolder) viewHolder).setItem(item);
        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(ChatItem item){
        items.add(item);
        notifyDataSetChanged();
    }

    public void setItems(ArrayList<ChatItem> items){ this.items = items; }

    public ChatItem getItem(int position){
        return items.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getViewType();
    }

    public class CenterViewHolder extends RecyclerView.ViewHolder{
        TextView contentText;

        public CenterViewHolder(View itemView) {
            super(itemView);

            contentText = itemView.findViewById(R.id.enter_tv);
        }

        public void setItem(ChatItem item){
            contentText.setText(item.getContent());
        }
    }

    public class LeftViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        TextView contentText;
        TextView sendTimeText;

        public LeftViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.rcvName);
            contentText = itemView.findViewById(R.id.rcvContent);
            sendTimeText = itemView.findViewById(R.id.rcvTime);
        }

        public void setItem(ChatItem item){
            nameText.setText(item.getName());
            contentText.setText(item.getContent());
            sendTimeText.setText(item.getSendTime());
        }
    }

    public class RightViewHolder extends RecyclerView.ViewHolder{
        TextView contentText;
        TextView sendTimeText;

        public RightViewHolder(View itemView) {
            super(itemView);
            contentText = itemView.findViewById(R.id.sentContent);
            sendTimeText = itemView.findViewById(R.id.sendTime);
        }

        public void setItem(ChatItem item){
            contentText.setText(item.getContent());
            sendTimeText.setText(item.getSendTime());
        }
    }
}
