package com.abstrack.hanasu.core.chatroom.chat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
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
import com.abstrack.hanasu.activity.landing.LandingActivity;
import com.abstrack.hanasu.callback.OnContactDataReceiveCallback;
import com.abstrack.hanasu.core.Flame;
import com.abstrack.hanasu.core.chatroom.ChatRoom;
import com.abstrack.hanasu.core.chatroom.chat.message.Message;
import com.abstrack.hanasu.core.chatroom.data.ChatType;
import com.abstrack.hanasu.core.contact.ContactManager;
import com.abstrack.hanasu.core.user.PublicUser;
import com.abstrack.hanasu.core.user.UserManager;
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
    private LandingActivity activity;

    public ChatAdapter(List<Chat> chatsList, LandingActivity activity) {
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
        holder.userIcon.setImageResource(R.drawable.ic_profile_pic);

        fetchChatPicture(holder);
        buildChatListeners(holder);
        buildDataListeners(holder);
    }

    public void buildChatListeners(ChatViewHolder holder){
        holder.chatCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToChatActivity(holder);
            }
        });
    }

    public void goToChatActivity(ChatViewHolder holder){
        Intent intent = new Intent(activity, ChatActivity.class);
        intent.putExtra("cachedChatRoom", chatsList.get(holder.getAdapterPosition()).getChatRoom());
        activity.startActivity(intent);
    }

    public void buildDataListeners(ChatViewHolder holder){
        //Ya me harté así que ni modo
        Chat chat = chatsList.get(holder.getAdapterPosition());
        ChatRoom chatRoom = chat.getChatRoom();

        Flame.getDataBaseReferenceWithPath("private").child("chatRooms").child(chatRoom.getChatRoomUUID()).child("messagesList").limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot messageData : snapshot.getChildren()){
                    Message lastMessage = messageData.getValue(Message.class);
                    holder.previewMessage.setText(lastMessage.getContent());
                    holder.chatTime.setText(lastMessage.getTimeStamp());
                    fetchChatPicture(holder);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void fetchChatPicture(ChatViewHolder holder){
        // Get the user chatImgKey
        String chatImgKey = chatsList.get(holder.getAdapterPosition()).getImgKey();
        if(chatImgKey == null || chatImgKey.length() == 0){
            return;
        }

        String imgExtension = chatImgKey.substring(chatImgKey.indexOf('.'));

        // TODO: make contactIdentifier compatible with groups (chatType)

        List<String> chatUsersList = chatsList.get(holder.getAdapterPosition()).getChatRoom().getUsersList();

        String contactIdentifier = null;

        for(String identifier : chatUsersList){
            if(!identifier.equals(UserManager.currentPublicUser.getIdentifier())){
                contactIdentifier = identifier;
                break;
            }
        }

        // Find the user uid with the identifier
        DatabaseReference userRef = Flame.getDataBaseReferenceWithPath("public").child("users");

        userRef.orderByChild("identifier").equalTo(contactIdentifier).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()){
                    Log.e("Hanasu-ChatAdapter", "Error getting user");
                }

                String userUid = null;

                for(DataSnapshot dt : task.getResult().getChildren()){
                    userUid = dt.getKey();
                }

                locateAndSetChatPicture(holder, userUid, chatImgKey, imgExtension);
            }
        });
    }

    private void locateAndSetChatPicture(ChatViewHolder holder,String userUid, String chatImgKey, String imgExtension){
        StorageReference imgRef = FirebaseStorage.getInstance().getReference();

        imgRef.child("image").child("profilePic").child(userUid).child(chatImgKey).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (!task.isSuccessful()) {
                    Log.e("Hanasu-ChatAdapter", "Error getting user");
                    return;
                }

                if (imgExtension.equals(".gif")) {
                    Glide.with(holder.itemView).asGif().load(task.getResult()).into(holder.userIcon);
                } else {
                    Glide.with(holder.itemView).asBitmap().load(task.getResult()).into(holder.userIcon);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return ChatManager.getChatsList().size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        // Components
        private TextView name, previewMessage, chatTime;
        private CardView chatCard;
        private ImageView userIcon;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            chatCard = itemView.findViewById(R.id.chatCard);
            name = itemView.findViewById(R.id.chatName);
            previewMessage = itemView.findViewById(R.id.previewMessage);
            chatTime = itemView.findViewById(R.id.chatTime);
            userIcon = itemView.findViewById(R.id.userIcon);
        }
    }
}
