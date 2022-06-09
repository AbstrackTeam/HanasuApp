package com.abstrack.hanasu.callback;

import com.abstrack.hanasu.core.chatroom.ChatRoom;

public interface OnChatRoomDataReceiveCallback {
    void onDataReceiver(ChatRoom chatRoom);
    void onDataReceived();
}
