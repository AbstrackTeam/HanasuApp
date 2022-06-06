package com.abstrack.hanasu.core.chatroom.chat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.abstrack.hanasu.core.chatroom.data.ChatType;
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
        //
        holder.userIcon.setImageResource(R.drawable.ic_profile_pic);

        fetchChatPicture(holder);
        buildChatListeners(holder);
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

    /** TODO: Waiting for file rules **/
    public void fetchChatPicture(ChatViewHolder holder){
        // Get the user chatImgKey
        String chatImgKey = chatsList.get(holder.getAdapterPosition()).getImgKey();
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

                String userUid = task.getResult().getKey();
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
