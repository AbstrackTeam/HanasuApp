package com.abstrack.hanasu.callback;

import com.abstrack.hanasu.core.user.PrivateUser;
import com.abstrack.hanasu.core.user.PublicUser;

public interface OnUserDataReceiveCallback {
    void onDataReceiver(PublicUser publicUser);
    void onDataReceiver(PrivateUser privateUser);
    void onDataReceived();
}
