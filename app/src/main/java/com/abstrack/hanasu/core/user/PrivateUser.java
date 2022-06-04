package com.abstrack.hanasu.core.user;

import java.util.HashMap;

public class PrivateUser {

    private HashMap<String, String> contacts;

    public PrivateUser() {
        contacts = new HashMap<String, String>();
        contacts.put("friendIdentifier", "chatRoomUid");
    }

    public PrivateUser(HashMap<String, String> contacts){
        this.contacts = contacts;
    }

    public HashMap<String, String> getContacts() {
        return contacts;
    }

    public void setContacts(HashMap<String, String> contacts) {
        this.contacts = contacts;
    }
}
