package com.abstrack.hanasu.core.user.model;

import com.abstrack.hanasu.auth.AuthManager;

import java.util.HashMap;

public class UserModel {

    private String name, tag, imgKey, imgExtension, about, identifier, uid, displayName;
    private HashMap<String, String> contacts;

    public UserModel(String name, String tag) {
        this.name = name;
        this.tag = tag;
        imgKey = "";
        imgExtension = "";
        about = "";
        uid = AuthManager.getFireAuth().getUid();
        identifier = name + tag;
        displayName = "";
        contacts = new HashMap<String, String>();
        contacts.put("identifier", "");
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
