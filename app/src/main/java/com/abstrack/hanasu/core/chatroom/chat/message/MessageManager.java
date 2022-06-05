package com.abstrack.hanasu.core.chatroom.chat.message;

import com.abstrack.hanasu.core.chatroom.chat.Chat;

import java.util.ArrayList;
import java.util.List;

public class MessageManager {

    private static List<Message> messageList = new ArrayList<Message>();

    public static void addMessageToMessageList(Message message){
        if(!messageList.contains(message)){
            messageList.add(message);
        }
    }

    public static void removeMessageFromlist(Message message){
        if(messageList.contains(message)){
            messageList.remove(message);
        }
    }

    public static List<Message> getMessageList() {
        return messageList;
    }
}
