package com.abstrack.hanasu.core.user;

import com.abstrack.hanasu.core.story.Story;

import java.util.ArrayList;

public class User {

    private String uid, name, imgKey, about;
    private ConnectionStatus connectionStatus;
    private ArrayList<String> contactsUid;
    private ArrayList<Story> stories;

    public User(){}

    public User(String uid) {
        this.uid = uid;
        name = "TestName";
        imgKey = "TestImgUri";
        about = "That's me, kaserola";
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getImgKey() {
        return imgKey;
    }

    public void setImgKey(String imgKey) {
        this.imgKey = imgKey;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public ConnectionStatus getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(ConnectionStatus connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public ArrayList<String> getContactsUid() {
        return contactsUid;
    }

    public void setContactsUid(ArrayList<String> contactsUid) {
        this.contactsUid = contactsUid;
    }

    public ArrayList<Story> getStories() {
        return stories;
    }

    public void setStories(ArrayList<Story> stories) {
        this.stories = stories;
    }
}
