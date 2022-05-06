package com.abstrack.hanasu.lib.chat;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abstrack.hanasu.R;


import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ChatsViewHolder> {
    // Store all the chats
    private List<Chat> chats;
    private Context context;

    public ChatsAdapter(List<Chat> chats, Context context){
        this.chats = chats;
        this.context = context;
    }
    @NonNull
    @Override
    public ChatsAdapter.ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.chat_card_item, parent, false);
        ChatsAdapter.ChatsViewHolder viewHolder = new ChatsAdapter.ChatsViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatsAdapter.ChatsViewHolder holder, int position) {
        holder.name.setText(chats.get(position).getName().toString());
        holder.previewMessage.setText(chats.get(position).getMessages().toString());
        holder.messageQuantity.setText(String.valueOf(chats.get(position).getMessagesCount()));

    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public static class ChatsViewHolder extends RecyclerView.ViewHolder{
        // Components
        private TextView name, previewMessage, messageQuantity;

        public ChatsViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.chatName);
            previewMessage = itemView.findViewById(R.id.previewMessage);
            messageQuantity = itemView.findViewById(R.id.messagesCount);

        }
    }
}
