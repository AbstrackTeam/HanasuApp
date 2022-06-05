package com.abstrack.hanasu.core.chatroom.chat.message;

import android.os.Parcel;
import android.os.Parcelable;

import com.abstrack.hanasu.core.chatroom.ChatRoom;
import com.abstrack.hanasu.core.chatroom.chat.message.data.MessageStatus;
import com.abstrack.hanasu.core.chatroom.chat.message.data.MessageType;
import com.abstrack.hanasu.core.chatroom.data.ChatType;

import java.util.List;

public class Message implements Parcelable {
    
    public String content, timeStamp, sentBy;
    public MessageStatus messageStatus;
    public MessageType messageType;

    public Message(){
        this.content = "";
        this.timeStamp = "";
        this.sentBy = "";
        this.messageStatus = MessageStatus.ARRIVED_NOT_SEEN;
        this.messageType = MessageType.TEXT;
    }

    public Message(String content, String time, String sentBy, MessageStatus messageStatus, MessageType messageType) {
        this.content = content;
        this.timeStamp = time;
        this.sentBy = sentBy;
        this.messageStatus = messageStatus;
        this.messageType = messageType;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String time) {
        this.timeStamp = time;
    }

    public String getSentBy() {
        return sentBy;
    }

    public void setSentBy(String sentBy) {
        this.sentBy = sentBy;
    }

    public MessageStatus getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(MessageStatus messageStatus) {
        this.messageStatus = messageStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(content);
        parcel.writeString(timeStamp);
        parcel.writeString(sentBy);
        parcel.writeString(messageStatus.toString());
        parcel.writeString(messageType.toString());
    }

    public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator<Message>() {
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    private Message(Parcel in) {
        content = in.readString();
        timeStamp = in.readString();
        sentBy = in.readString();
        messageStatus = MessageStatus.valueOf(in.readString());
        messageType = MessageType.valueOf(in.readString());
    }
}
