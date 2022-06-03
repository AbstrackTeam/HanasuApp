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

import com.abstrack.hanasu.R;
import com.abstrack.hanasu.activity.chat.ChatActivity;
import com.abstrack.hanasu.core.chatroom.message.data.MessageStatus;
import com.abstrack.hanasu.core.user.UserManager;
import com.abstrack.hanasu.core.Flame;
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

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ChatsViewHolder> {
    // Store all the chats
    private List<Chat> chats;
    private Context context;

    public ChatsAdapter(List<Chat> chats, Context context) {
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

        // Set the chat room, this is the unique identifier of each chat
        holder.setChatRoom(chats.get(position).getChatRoom());
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

        // If the messages are zero
        if (chats.get(position).getMessagesCount() == 0) {
            MessageStatus messageStatus = chats.get(position).getMessageState();
            holder.clearNotifications();
            if (messageStatus == MessageStatus.SEEN) {
                holder.messageQuantity.setBackgroundResource(R.drawable.seen);
            }
            if (messageStatus == MessageStatus.NOT_SEEN) {
                holder.messageQuantity.setBackgroundResource(R.drawable.not_seen);
            }
            if (messageStatus == MessageStatus.SENDING) {
                holder.messageQuantity.setBackgroundResource(R.drawable.sended);
            }
        }
        else{
            // Set the notification icon quantity and value
            holder.messageQuantity.setText(String.valueOf(chats.get(position).getMessagesCount()));
            holder.messageQuantityValue = chats.get(position).getMessagesCount();
        }


        // Set the provided time
        holder.chatTime.setText(chats.get(position).getTime());
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

        // Start the sync
        holder.sync();

        // Try to get the image from the firebase storage and if found, update the user icon
        String imagePath = "image/" + chats.get(position).getImgKey() + chats.get(position).getImgExtension();

        StorageReference imgRef = FirebaseStorage.getInstance().getReference();

        imgRef.child(imagePath).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (!task.isSuccessful()) {
                    return;
                }

                if (holder.getAdapterPosition() < 0) {
                    return;
                }

                if (chats.get(holder.getAdapterPosition()).getImgExtension().equals(".gif")) {
                    Glide.with(context).asGif().load(task.getResult()).into(holder.userIcon);
                } else {
                    Glide.with(context).asBitmap().load(task.getResult()).into(holder.userIcon);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public static class ChatsViewHolder extends RecyclerView.ViewHolder {
        // Components
        private TextView name, previewMessage, messageQuantity, chatTime;
        private CardView chatCard, chatNotification;
        private ImageView userIcon;

        // Message quantity value
        int messageQuantityValue = 0;

        // ChatRoom
        private String chatRoom;

        public ChatsViewHolder(@NonNull View itemView) {
            super(itemView);
            chatCard = itemView.findViewById(R.id.chatCard);
            name = itemView.findViewById(R.id.chatName);
            previewMessage = itemView.findViewById(R.id.previewMessage);
            messageQuantity = itemView.findViewById(R.id.messagesCount);
            chatTime = itemView.findViewById(R.id.chatTime);
            chatNotification = itemView.findViewById(R.id.chatNotification);
            userIcon = itemView.findViewById(R.id.userIcon);
        }

        public String getChatRoom() {
            return chatRoom;
        }

        public void setChatRoom(String chatRoom) {
            this.chatRoom = chatRoom;
        }

        public void sync() {
            syncMessages();
            DatabaseReference chatRoomRef = Flame.getDataBaseReferenceWithPath("chat-rooms").child(chatRoom);
            chatRoomRef.child("users").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        return;
                    }
                    for (DataSnapshot user : task.getResult().getChildren()) {
                        if (!user.getValue().toString().equals(UserManager.getCurrentUser().getIdentifier())) {
                            syncUserInfo(user.getValue().toString());
                        }
                    }
                }
            });
        }

        public void syncMessages() {
            // Reference
            DatabaseReference chatRoomRef = Flame.getDataBaseReferenceWithPath("chat-rooms").child(chatRoom);
            // Creating the listener
            ValueEventListener syncMessages = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot data) {
                    // Get the list of messages
                    List<HashMap<String, String>> messagesList = (List<HashMap<String, String>>) data.child("messagesList").getValue();
                    // Get the lastMessage
                    HashMap<String, String> lastMessage = messagesList.get(messagesList.size() - 1);
                    // Assign all the properties
                    chatTime.setText(lastMessage.get("time"));
                    previewMessage.setText(lastMessage.get("content"));

                    // First check if you were the last message
                    if (lastMessage.get("sentBy").equals(UserManager.getCurrentUser().getIdentifier())) {
                        // If so, now validate the three types of states
                        MessageStatus messageStatus = MessageStatus.valueOf(lastMessage.get("messageStatus"));

                        clearNotifications();
                        if (messageStatus == MessageStatus.SEEN) {
                            System.out.println("SEEN");
                            messageQuantity.setBackgroundResource(R.drawable.seen);
                            return;
                        }
                        if (messageStatus == MessageStatus.NOT_SEEN) {
                            System.out.println("NOT_SEEN");

                            messageQuantity.setBackgroundResource(R.drawable.not_seen);
                            return;
                        }
                        if (messageStatus == MessageStatus.SENDING) {
                            System.out.println("SENDING");
                            messageQuantity.setBackgroundResource(R.drawable.sended);
                        }
                    }
                    else{
                        for (int i = 1; i < messagesList.size(); i++) {
                            if (messagesList.get(i).get("sentBy").equals(UserManager.getCurrentUser().getIdentifier())) {
                                continue;
                            }
                            if (MessageStatus.valueOf(messagesList.get(i).get("messageStatus")) != MessageStatus.SEEN) {
                                messageQuantityValue++;
                            }
                        }

                        messageQuantity.setText(String.valueOf(messageQuantityValue));
                        messageQuantity.setBackground(null);
                        chatNotification.setCardBackgroundColor(itemView.getResources().getColor(R.color.detail_color_secondary));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            };

            // Adding the listener
            chatRoomRef.addValueEventListener(syncMessages);
        }

        public void clearNotifications() {
            messageQuantityValue = 0;
            // Clear the message
            messageQuantity.setText("");
            // clear the background
            // Translucent
            chatNotification.setCardBackgroundColor(null);
            messageQuantity.setBackground(null);
        }

        public void syncUserInfo(String identifier) {
            // References
            DatabaseReference userImgKeyRef = Flame.getDataBaseReferenceWithPath("users").child(identifier).child("imgKey");
            DatabaseReference userImgExtensionRef = Flame.getDataBaseReferenceWithPath("users").child(identifier).child("imgExtension");
            DatabaseReference userDisplayNameRef = Flame.getDataBaseReferenceWithPath("users").child(identifier).child("displayName");

            // Create the valueEventListeners
            // Image listener
            ValueEventListener imgKeyListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot data) {
                    userImgExtensionRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                return;
                            }

                            fetchFriendProfilePicture(task.getResult().getValue().toString(), data.getValue().toString());
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            };

            // Display name listener
            ValueEventListener displayNameListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot data) {
                    name.setText(data.getValue().toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            };

            userImgKeyRef.addValueEventListener(imgKeyListener);
            userDisplayNameRef.addValueEventListener(displayNameListener);
        }

        public void fetchFriendProfilePicture(String imgExtension, String imgKey) {
            String imagePath = "image/" + imgKey + imgExtension;

            StorageReference imgRef = FirebaseStorage.getInstance().getReference();
            imgRef.child(imagePath).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (!task.isSuccessful()) {
                        return;
                    }

                    if (imgExtension.equals(".gif")) {
                        Glide.with(itemView).asGif().load(task.getResult()).into(userIcon);
                    } else {
                        Glide.with(itemView).asBitmap().load(task.getResult()).into(userIcon);
                    }
                }
            });
        }
    }
}
