package com.abstrack.hanasu.core.user;

import com.abstrack.hanasu.core.Flame;
import com.abstrack.hanasu.core.user.data.ConnectionStatus;

import java.net.ConnectException;
import java.util.HashMap;

public class PublicUser {

    private String identifier, displayName, fcmToken, about, imgKey;
    private ConnectionStatus connectionStatus;

    public PublicUser(){}

    public PublicUser(String identifier) {
        this.identifier = identifier;
        displayName = "";
        fcmToken = "";
        about = "Un problema en una rama deja de ser un problema después de ser cortada.";
        imgKey = "";
        connectionStatus = ConnectionStatus.OFFLINE;
    }

    public PublicUser(String identifier, String displayName, String fcmToken, String about, String imgKey, ConnectionStatus connectionStatus) {
        this.identifier = identifier;
        this.displayName = displayName;
        this.fcmToken = fcmToken;
        this.about = about;
        this.imgKey = imgKey;
        this.connectionStatus = connectionStatus;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getImgKey() {
        return imgKey;
    }

    public void setImgKey(String imgKey) {
        this.imgKey = imgKey;
    }

    public ConnectionStatus getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(ConnectionStatus connectionStatus) {
        this.connectionStatus = connectionStatus;
    }
}
