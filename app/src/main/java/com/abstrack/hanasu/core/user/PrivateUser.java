package com.abstrack.hanasu.core.user;

import com.abstrack.hanasu.core.chatroom.ChatRoom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PrivateUser {

    private HashMap<String, String> contacts;
    private HashMap<String, Integer> chatRoomList;

    public PrivateUser() {
        contacts = new HashMap<String, String>();
        contacts.put("friendIdentifier", "chatRoomUid");

        chatRoomList = new HashMap<String, Integer>();
        chatRoomList.put("chatRoomUUID", chatRoomList.size());
    }

    public PrivateUser(HashMap<String, String> contacts, HashMap<String, Integer> chatRoomList){
        this.contacts = contacts;
        this.chatRoomList = chatRoomList;
    }

    public HashMap<String, String> getContacts() {
        return contacts;
    }

    public void setContacts(HashMap<String, String> contacts) {
        this.contacts = contacts;
    }

    public HashMap<String, Integer> getChatRoomList() {
        return chatRoomList;
    }

    public void setChatRoomList(HashMap<String, Integer> chatRoomList) {
        this.chatRoomList = chatRoomList;
    }
}
