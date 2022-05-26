package com.abstrack.hanasu.core.user;

import com.abstrack.hanasu.auth.AuthManager;

public class User {

    public String name, tag, imgKey, imgExtension, about, identifier, uid, displayName, connectionStatus;

    public User(String name, String tag) {
        this.name = name;
        this.tag = tag;
        imgKey = "";
        imgExtension = "";
        about = "";
        identifier = name + tag;
        uid = AuthManager.getFireAuth().getUid();
        displayName = "";
        connectionStatus = "";
    }

    public User(String name, String tag, String imgKey, String imgExtension, String about, String identifier, String uid, String displayName) {
        this.name = name;
        this.tag = tag;
        this.imgKey = imgKey;
        this.imgExtension = imgExtension;
        this.about = about;
        this.identifier = identifier;
        this.uid = uid;
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }

    public String getImgKey() {
        return imgKey;
    }

    public String getImgExtension() {
        return imgExtension;
    }

    public String getAbout() {
        return about;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getUid() {
        return uid;
    }

    public String getDisplayName() {
        return displayName;
    }
}
