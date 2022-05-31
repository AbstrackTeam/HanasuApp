package com.abstrack.hanasu.core.chat;

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
import com.abstrack.hanasu.R;
import com.abstrack.hanasu.activity.chat.ChatActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ChatsViewHolder> {
    // Store all the chats
    private List<ChatModel> chats;
    private Context context;

    public ChatsAdapter(List<ChatModel> chats, Context context){
        this.chats = chats;
        this.context = context;
    }
    @NonNull
    @Override
    public ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.chat_card_item, parent, false);
        return new ChatsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatsViewHolder holder, int position) {
        /*
            TODO: Add a ValueEventListener to sync the last message, time, and notification icon
            TODO: It wont take into account the user icon and the display name, those sync after entering landing (although it can be done).
         */
        // Set chat name
        holder.name.setText(chats.get(position).getName());
        // Set preview message
        holder.previewMessage.setText(chats.get(position).getMessages());
        // Set placeholder icon
        holder.userIcon.setImageResource(R.drawable.ic_profile_pic);

        // If the messages are zero or is seen, hide the notification view.
        if(chats.get(position).getMessagesCount() == 0 || chats.get(position).isSeen()) {
            holder.chatNotification.setVisibility(View.INVISIBLE);
        } else{
            holder.chatNotification.setVisibility(View.VISIBLE);
        }

        // Set the notification icon quantity
        holder.messageQuantity.setText(String.valueOf(chats.get(position).getMessagesCount()));
        // Set the chat room, this is the unique identifier of each chat
        holder.setChatRoom(chats.get(position).getChatRoom());

        // This is called when the chat captures a click
        // You can put this listener down in the ChatsViewHolder class, but it's better to set it up
        // only when you have all the data (when this method is called).
        holder.chatCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Being able to open the chat taking into account the chatRoom
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("chatRoom", holder.getChatRoom());
                context.startActivity(intent);
            }
        });

        // Try to get the image from the firebase storage and if found, update the user icon
        String imagePath = "image/" + chats.get(position).getImgKey() + chats.get(position).getImgExtension();

        StorageReference imgRef = FirebaseStorage.getInstance().getReference();

        imgRef.child(imagePath).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (!task.isSuccessful()){
                    return;
                }

                if(holder.getAdapterPosition() < 0){
                    return;
                }

                if(chats.get(holder.getAdapterPosition()).getImgExtension().equals(".gif")){
                    Glide.with(context).asGif().load(task.getResult()).into(holder.userIcon);
                }
                else{
                    Glide.with(context).asBitmap().load(task.getResult()).into(holder.userIcon);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public static class ChatsViewHolder extends RecyclerView.ViewHolder{
        // Components
        private TextView name, previewMessage, messageQuantity;
        private CardView chatCard, chatNotification;
        private ImageView userIcon;

        // ChatRoom
        private String chatRoom;

        public ChatsViewHolder(@NonNull View itemView) {
            super(itemView);
            chatCard = itemView.findViewById(R.id.chatCard);
            name = itemView.findViewById(R.id.chatName);
            previewMessage = itemView.findViewById(R.id.previewMessage);
            chatNotification = itemView.findViewById(R.id.chatNotification);
            messageQuantity = itemView.findViewById(R.id.messagesCount);
            userIcon = itemView.findViewById(R.id.userIcon);
        }

        public String getChatRoom(){
            return chatRoom;
        }

        public void setChatRoom(String chatRoom){
            this.chatRoom= chatRoom;
        }
    }
}
