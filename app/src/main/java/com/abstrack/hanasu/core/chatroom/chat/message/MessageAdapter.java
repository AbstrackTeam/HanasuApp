package com.abstrack.hanasu.core.chatroom.chat.message;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.abstrack.hanasu.R;
import com.abstrack.hanasu.core.Flame;
import com.abstrack.hanasu.core.chatroom.chat.message.data.MessageType;
import com.bumptech.glide.Glide;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> messageList;
    private Context context;

    public MessageAdapter(List<Message> messageList, Context context) {
        this.messageList = messageList;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        if(viewType != 0){
            view = inflater.inflate(R.layout.chat_image_item, parent, false);
            return new MessageViewHolder(view);
        }

        view = inflater.inflate(R.layout.chat_message_item, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MessageViewHolder holder, int position) {
        Message messageModel = messageList.get(position);
        boolean messageSendByOther = !messageModel.getSentBy().equals(Flame.getFireAuth().getCurrentUser().getUid());

        holder.txtTime.setText(messageModel.getTimeStamp());

        if (messageModel.getMessageType() == MessageType.IMAGE) {
            decorateMessageBox(holder.messageImgLinearLayout, holder.messageImgConstraintLayout, messageSendByOther);
            Glide.with(context).asBitmap().load(messageModel.getContent()).into(holder.imgMsgPicture);
            return;
        }

        decorateMessageBox(holder.messageLinearLayout, holder.messageConstraintLayout, messageSendByOther);
        holder.txtMsg.setText(messageModel.getContent());
    }

    public void decorateMessageBox(LinearLayout linearLayout, ConstraintLayout constraintLayout, boolean messageSendByOther){
        if(messageSendByOther){
            constraintLayout.setBackgroundResource(R.drawable.rounded_message_contrast);
            linearLayout.setGravity(Gravity.LEFT);
        } else {
            constraintLayout.setBackgroundResource(R.drawable.rounded_message);
            linearLayout.setGravity(Gravity.END);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message model = messageList.get(position);
        if(model.getMessageType() == MessageType.IMAGE){
            return 1;
        }

        return 0;
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView txtMsg, txtTime;
        LinearLayout messageLinearLayout, messageImgLinearLayout;
        ConstraintLayout messageConstraintLayout, messageImgConstraintLayout;
        ImageView imgMsgPicture;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTime = itemView.findViewById(R.id.txtTime);
            imgMsgPicture = itemView.findViewById(R.id.imgMsgPicture);
            txtMsg = itemView.findViewById(R.id.txtMsg);
            messageLinearLayout = itemView.findViewById(R.id.messageLinearLayout);
            messageImgLinearLayout = itemView.findViewById(R.id.messageImgLinearLayout);
            messageConstraintLayout = itemView.findViewById(R.id.messageConstraintLayout);
            messageImgConstraintLayout = itemView.findViewById(R.id.messageImgConstraintLayout);
        }
    }
}
