package com.abstrack.hanasu.core.user;

import com.abstrack.hanasu.auth.AuthManager;
import com.abstrack.hanasu.core.user.data.ConnectionStatus;

import java.net.ConnectException;
import java.util.HashMap;

public class User {

    private String name, tag, imgKey, imgExtension, about, identifier, uid, displayName;
    private ConnectionStatus connectionStatus;
    private HashMap<String, String> contacts;

    public User(String name, String tag) {
        this.name = name;
        this.tag = tag;
        imgKey = "";
        imgExtension = "";
        about = "";
        uid = AuthManager.getFireAuth().getUid();
        identifier = name + tag;
        displayName = "";
        contacts = new HashMap<String, String>();
        contacts.put("identifier", "chatRoom");
        connectionStatus = ConnectionStatus.OFFLINE;
    }

    public User(String name, String tag, String imgKey, String imgExtension, String about, String identifier, String uid, String displayName, HashMap<String, String> contacts, ConnectionStatus connectionStatus) {
        this.name = name;
        this.tag = tag;
        this.imgKey = imgKey;
        this.imgExtension = imgExtension;
        this.about = about;
        this.identifier = identifier;
        this.uid = uid;
        this.displayName = displayName;
        this.contacts = contacts;
        this.connectionStatus = connectionStatus;
    }

    public ConnectionStatus getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(ConnectionStatus connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public HashMap<String, String> getContacts() {
        return contacts;
    }

    public void setContacts(HashMap<String, String> contacts) {
        this.contacts = contacts;
    }
}
