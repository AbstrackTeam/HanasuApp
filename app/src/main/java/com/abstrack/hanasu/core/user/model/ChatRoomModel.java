package com.abstrack.hanasu.core.user.model;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomModel {
    private List<String> users;
    private List<MessageModel> messagesList;
    private String lastMessageTime, chatRoomKey;

    public ChatRoomModel() {

    }

    public ChatRoomModel(List<String> users, String chatRoomKey) {
        this.users = users;
        messagesList = new ArrayList<>();
        messagesList.add(new MessageModel());
        lastMessageTime = "";
        this.chatRoomKey = chatRoomKey;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public List<MessageModel> getMessagesList() {
        return messagesList;
    }

    public void setMessagesList(List<MessageModel> messagesList) {
        this.messagesList = messagesList;
    }

    public String getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(String lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public String getChatRoomKey() {
        return chatRoomKey;
    }

    public void setChatRoomKey(String chatRoomKey) {
        this.chatRoomKey = chatRoomKey;
    }
}
