package com.abstrack.hanasu.core.user;

import com.abstrack.hanasu.auth.AuthManager;
import com.abstrack.hanasu.core.story.Story;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Random;

public class User {

    private String name, tag, imgKey, imgExtension, about, identifier, uid, displayName;
    private ConnectionStatus connectionStatus;
    private ArrayList<String> contactsUid;
    private ArrayList<Story> stories;

    public User(String name, String tag) {
        this.name = name;
        this.tag = tag;
        imgKey = getDefaultImageUri();
        imgExtension = "";
        about = "Hey there, I'm using Hanasu!";
        uid = AuthManager.getFireAuth().getUid();
        identifier = name + tag;
        displayName = "";
    }

    public User(String displayName, String name, String tag, String imgKey, String imgExtension, String about, String identifier) {
        this.displayName = displayName;
        this.name = name;
        this.tag = tag;
        this.imgKey = imgKey;
        this.imgExtension = imgExtension;
        this.about = about;
        this.uid = AuthManager.getFireAuth().getUid();
        this.identifier = name + tag;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setImgExtension(String imgExtension) {
        this.imgExtension = imgExtension;
    }

    public String getImgExtension() {
        return imgExtension;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    private String getDefaultImageUri() {
        return "";
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

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
