package com.abstrack.hanasu.core.chatroom;

import com.abstrack.hanasu.core.chatroom.data.ChatType;
import com.abstrack.hanasu.core.chatroom.chat.message.Message;
import com.abstrack.hanasu.core.user.UserManager;

import java.util.ArrayList;
import java.util.List;

public class ChatRoom {

    private String chatRoomUUID, chatImgKey;
    private List<Message> messagesList;
    private List<String> usersList;
    private ChatType chatType;

    public ChatRoom() {
    }

    public ChatRoom(String chatRoomUUID, ChatType chatType, String contactIdentifier) {
        this.chatRoomUUID = chatRoomUUID;
        this.chatType = chatType;

        usersList = new ArrayList<String>();
        usersList.add(contactIdentifier);
        usersList.add(UserManager.currentPublicUser.getIdentifier());

        messagesList = new ArrayList<>();
        messagesList.add(new Message());
    }

    public ChatRoom(String chatRoomUUID, ChatType chatType, String chatImgKey, String contactIdentifier) {
        this.chatRoomUUID = chatRoomUUID;
        this.chatType = chatType;
        this.chatImgKey = chatImgKey;

        usersList = new ArrayList<String>();
        usersList.add(contactIdentifier);
        usersList.add(UserManager.currentPublicUser.getIdentifier());

        messagesList = new ArrayList<>();
        messagesList.add(new Message());
    }

    public ChatRoom(String chatRoomUUID, ChatType chatType, String chatImgKey, List<String> usersList, List<Message> messagesList) {
        this.chatRoomUUID = chatRoomUUID;
        this.chatType = chatType;
        this.chatImgKey = chatImgKey;
        this.usersList = usersList;
        this.messagesList = messagesList;
    }

    public String getChatRoomUUID() {
        return chatRoomUUID;
    }

    public void setChatRoomUUID(String chatRoomUUID) {
        this.chatRoomUUID = chatRoomUUID;
    }

    public String getChatImgKey(){
        return chatImgKey;
    }

    public void setChatImgKey(){
        this.chatImgKey = chatImgKey;
    }

    public List<Message> getMessagesList() {
        return messagesList;
    }

    public void setMessagesList(List<Message> messagesList) {
        this.messagesList = messagesList;
    }

    public List<String> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<String> usersList) {
        this.usersList = usersList;
    }

    public ChatType getChatType() {
        return chatType;
    }

    public void setChatType(ChatType chatType) {
        this.chatType = chatType;
    }
}
