package com.abstrack.hanasu.core.user.model;

import com.abstrack.hanasu.core.user.chat.MessageState;

public class MessageModel {
    public String text, time, sentBy;
    public MessageState messageStatus;

    public MessageModel(){
        this.text = "";
        this.time = "";
        this.sentBy = "";
        this.messageStatus = MessageState.ARRIVED_NOT_SEEN;
    }

    public MessageModel(String text, String time, String sentBy, MessageState messageStatus) {
        this.text = text;
        this.time = time;
        this.sentBy = sentBy;
        this.messageStatus = messageStatus;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public MessageState getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(MessageState messageStatus) {
        this.messageStatus = messageStatus;
    }
}
