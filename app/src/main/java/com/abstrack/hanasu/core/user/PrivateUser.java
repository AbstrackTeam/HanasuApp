package com.abstrack.hanasu.core.user;

import com.abstrack.hanasu.core.chatroom.ChatRoom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PrivateUser {

    private HashMap<String, String> contacts;
    private List<String> chatRoomList;

    public PrivateUser() {
        contacts = new HashMap<String, String>();
        contacts.put("friendIdentifier", "chatRoomUid");

        chatRoomList = new ArrayList<String>();
        chatRoomList.add("chatRoomUUID");
    }

    public PrivateUser(HashMap<String, String> contacts, List<String> chatRoomList){
        this.contacts = contacts;
        this.chatRoomList = chatRoomList;
    }

    public HashMap<String, String> getContacts() {
        return contacts;
    }

    public void setContacts(HashMap<String, String> contacts) {
        this.contacts = contacts;
    }

    public List<String> getChatRoomList() {
        return chatRoomList;
    }

    public void setChatRoomList(List<String> chatRoomList) {
        this.chatRoomList = chatRoomList;
    }
}
