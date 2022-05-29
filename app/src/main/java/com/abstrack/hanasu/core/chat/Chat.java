package com.abstrack.hanasu.core.chat;

import com.abstrack.hanasu.core.user.chat.MessageState;

public class Chat {
    private boolean seen;
    private MessageState sentMessageState;
    // Disabled for testing purposes
    // private Bitmap icon;
    private int messagesCount;
    private String messages, time, name, chatRoom, userIdentifier, imgKey, imgExtension;

    public Chat(boolean seen, String name, MessageState sentMessageState, int messagesCount,
                String messages, String time, String chatRoom, String userIdentifier,
                String imgKey, String imgExtension) {
        this.seen = seen;
        this.name = name;
        this.sentMessageState = sentMessageState;
        this.messagesCount = messagesCount;
        this.messages = messages;
        this.time = time;
        this.chatRoom = chatRoom;
        this.userIdentifier = userIdentifier;
        this.imgKey = imgKey;
        this.imgExtension = imgExtension;

    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MessageState getSentMessageState() {
        return sentMessageState;
    }

    public void setSentMessageState(MessageState sentMessageState) {
        this.sentMessageState = sentMessageState;
    }

    public int getMessagesCount() {
        return messagesCount;
    }

    public void setMessagesCount(int messagesCount) {
        this.messagesCount = messagesCount;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(String chatRoom) {
        this.chatRoom = chatRoom;
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    public String getImgKey() {
        return imgKey;
    }

    public void setImgKey(String imgKey) {
        this.imgKey = imgKey;
    }

    public String getImgExtension() {
        return imgExtension;
    }

    public void setImgExtension(String imgExtension) {
        this.imgExtension = imgExtension;
    }
}
