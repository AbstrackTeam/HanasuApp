package com.abstrack.hanasu.core.chatroom.chat;

import android.util.Log;

import com.abstrack.hanasu.core.chatroom.ChatRoom;
import com.abstrack.hanasu.core.chatroom.chat.message.Message;
import com.abstrack.hanasu.core.chatroom.chat.message.data.MessageStatus;
import com.abstrack.hanasu.core.chatroom.data.ChatType;
import com.abstrack.hanasu.core.contact.ContactManager;
import com.abstrack.hanasu.core.user.PublicUser;
import com.abstrack.hanasu.core.user.UserManager;

public class Chat  {

    private String chatName, lastMessageContent, lastMessageTimeStamp, imgKey;
    private MessageStatus lastMessageStatus;
    private ChatRoom chatRoom;

    public void createChat(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;

        Message lastMessage = chatRoom.getMessagesList().get(chatRoom.getMessagesList().size() - 1);
        this.lastMessageContent = lastMessage.getContent();
        this.lastMessageStatus = lastMessage.getMessageStatus();
        this.lastMessageTimeStamp = lastMessage.getTimeStamp();

        if(chatRoom.getChatType() == ChatType.GROUP) {
            this.chatName = "Groupal Chat";
            this.imgKey = chatRoom.getChatImgKey();
            ChatManager.addChatToChatList(this);
            return;
        }

        for(String userIdentifier : chatRoom.getUsersList()) {
            if(userIdentifier.equals(UserManager.currentPublicUser.getIdentifier())){
                continue;
            }

            PublicUser contactPublicUser = ContactManager.getContactPublicUserList().get(userIdentifier);

            if(contactPublicUser != null) {
                this.chatName = contactPublicUser.getDisplayName();
                this.imgKey = contactPublicUser.getImgKey();
                break;
            }
        }

        ChatManager.addChatToChatList(this);
    }

    public String getChatName() {
        return chatName;
    }

    public String getLastMessageContent() {
        return lastMessageContent;
    }

    public String getLastMessageTimeStamp() {
        return lastMessageTimeStamp;
    }

    public String getImgKey() {
        return imgKey;
    }

    public MessageStatus getLastMessageStatus() {
        return lastMessageStatus;
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }
}
