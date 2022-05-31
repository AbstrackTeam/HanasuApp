package com.abstrack.hanasu.core.chatroom;

import com.abstrack.hanasu.core.chatroom.message.Message;

import java.util.ArrayList;
import java.util.List;

public class ChatRoom {
    private List<String> users;
    private List<Message> messagesList;
    private String lastMessageTime, chatRoomKey;

    public ChatRoom() {

    }

    public ChatRoom(List<String> users, String chatRoomKey) {
        this.users = users;
        messagesList = new ArrayList<>();
        messagesList.add(new Message());
        lastMessageTime = "";
        this.chatRoomKey = chatRoomKey;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public List<Message> getMessagesList() {
        return messagesList;
    }

    public void setMessagesList(List<Message> messagesList) {
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
