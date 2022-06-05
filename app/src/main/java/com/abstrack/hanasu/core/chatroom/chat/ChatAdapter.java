package com.abstrack.hanasu.core.chatroom.chat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.abstrack.hanasu.BaseAppActivity;
import com.abstrack.hanasu.R;
import com.abstrack.hanasu.activity.chat.ChatActivity;
import com.abstrack.hanasu.core.Flame;
import com.abstrack.hanasu.core.chatroom.data.ChatType;
import com.abstrack.hanasu.util.AndroidUtil;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<Chat> chatsList;
    private BaseAppActivity activity;

    public ChatAdapter(List<Chat> chatsList, BaseAppActivity activity) {
        this.chatsList = chatsList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.chat_card_item, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {

        Chat chat = chatsList.get(position);

        holder.name.setText(chat.getChatName());
        holder.previewMessage.setText(chat.getLastMessageContent());
        holder.chatTime.setText(chat.getLastMessageTimeStamp());

        fetchChatPicture(holder);
        buildChatListeners(holder);
    }

    public void buildChatListeners(ChatViewHolder holder){
        holder.chatCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AndroidUtil.startNewActivity(activity, ChatActivity.class);
            }
        });
    }

    /** TODO: Waiting for file rules **/
    public void fetchChatPicture(ChatViewHolder holder){
        holder.userIcon.setImageResource(R.drawable.ic_profile_pic);
    }

    @Override
    public int getItemCount() {
        return ChatManager.getChatsList().size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        // Components
        private TextView name, previewMessage, messageQuantity, chatTime;
        private CardView chatCard, chatNotification;
        private ImageView userIcon;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            chatCard = itemView.findViewById(R.id.chatCard);
            name = itemView.findViewById(R.id.chatName);
            previewMessage = itemView.findViewById(R.id.previewMessage);
            messageQuantity = itemView.findViewById(R.id.messagesCount);
            chatTime = itemView.findViewById(R.id.chatTime);
            chatNotification = itemView.findViewById(R.id.chatNotification);
            userIcon = itemView.findViewById(R.id.userIcon);
        }
    }
}
