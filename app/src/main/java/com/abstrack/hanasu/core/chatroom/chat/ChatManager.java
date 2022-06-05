package com.abstrack.hanasu.core.chatroom.chat;

import java.util.ArrayList;
import java.util.List;

public class ChatManager {

    private static List<Chat> chatsList = new ArrayList<Chat>();

    public static void addChatToChatList(Chat chat){
        if(!chatsList.contains(chat)){
            chatsList.add(chat);
        }
    }

    public static List<Chat> getChatsList(){
        return chatsList;
    }
}
