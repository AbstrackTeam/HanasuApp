package com.abstrack.hanasu.core.chatroom.message;

import com.abstrack.hanasu.core.chatroom.message.data.MessageStatus;
import com.abstrack.hanasu.core.chatroom.message.data.MessageType;

public class Message {
    public String content, time, sentBy;
    public MessageStatus messageStatus;
    public MessageType messageType;

    public Message(){
        this.content = "";
        this.time = "";
        this.sentBy = "";
        this.messageStatus = MessageStatus.ARRIVED_NOT_SEEN;
        this.messageType = MessageType.TEXT;
    }

    public Message(String content, String time, String sentBy, MessageStatus messageStatus, MessageType messageType) {
        this.content = content;
        this.time = time;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
}
