package com.abstrack.hanasu.core.chatroom;

import com.abstrack.hanasu.core.Flame;
import com.abstrack.hanasu.core.chatroom.message.Message;
import com.abstrack.hanasu.core.user.UserManager;

import java.util.ArrayList;
import java.util.List;

public class ChatRoom {

    private String chatRoomUUID;
    private List<Message> messagesList;
    private List<String> usersList;

    public ChatRoom() {
    }

    public ChatRoom(String chatRoomUUID, String contactIdentifier) {
        this.chatRoomUUID = chatRoomUUID;

        usersList = new ArrayList<String>();
        usersList.add(contactIdentifier);
        usersList.add(UserManager.getCurrentPublicUser().getIdentifier());

        messagesList = new ArrayList<>();
        messagesList.add(new Message());
    }

    public ChatRoom(String chatRoomUUID, List<String> usersList, List<Message> messagesList) {
        this.chatRoomUUID = chatRoomUUID;
        this.usersList = usersList;
        this.messagesList = messagesList;
    }

    public String getChatRoomUUID() {
        return chatRoomUUID;
    }

    public void setChatRoomUUID(String chatRoomUUID) {
        this.chatRoomUUID = chatRoomUUID;
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
}
