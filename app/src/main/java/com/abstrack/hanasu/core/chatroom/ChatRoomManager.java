package com.abstrack.hanasu.core.chatroom;

import com.abstrack.hanasu.core.Flame;
import com.abstrack.hanasu.core.user.UserManager;

import java.util.List;
import java.util.UUID;

public class ChatRoomManager {

    public static void writeNewChatRoom(String chatRoomUUID, String contactIdentifier){
        ChatRoom chatRoom = new ChatRoom(chatRoomUUID, contactIdentifier);
        Flame.getDataBaseReferenceWithPath("private").child("chatRooms").child(chatRoomUUID.toString()).setValue(chatRoom);
    }
}
