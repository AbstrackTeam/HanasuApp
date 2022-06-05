package com.abstrack.hanasu.core.chatroom.chat;

import com.abstrack.hanasu.activity.landing.LandingActivity;
import com.abstrack.hanasu.callback.OnContactDataReceiveCallback;
import com.abstrack.hanasu.core.chatroom.ChatRoom;
import com.abstrack.hanasu.core.chatroom.chat.message.Message;
import com.abstrack.hanasu.core.chatroom.chat.message.data.MessageStatus;
import com.abstrack.hanasu.core.chatroom.data.ChatType;
import com.abstrack.hanasu.core.user.PublicUser;
import com.abstrack.hanasu.core.user.UserManager;

public class Chat {

    private String chatName, lastMessageContent, lastMessageTimeStamp, imgKey;
    private MessageStatus lastMessageStatus;
    private ChatRoom chatRoom;

    public void retrieveChatData(ChatRoom chatRoom, LandingActivity activity) {
        this.chatRoom = chatRoom;

        Message lastMessage = chatRoom.getMessagesList().get(chatRoom.getMessagesList().size() - 1);
        lastMessageContent = lastMessage.getContent();
        lastMessageStatus = lastMessage.getMessageStatus();
        lastMessageTimeStamp = lastMessage.getTimeStamp();

        if (chatRoom.getChatType() == ChatType.GROUP) {
            chatName = "Grouapal Chat";
            imgKey = chatRoom.getChatImgKey();
            return;
        }

        for (String userIdentifier : chatRoom.getUsersList()) {
            if (userIdentifier.equals(UserManager.currentPublicUser.getIdentifier())) {
                continue;
            }

            UserManager.fetchContactPublicInformation(userIdentifier, new OnContactDataReceiveCallback() {
                @Override
                public void onDataReceive(PublicUser contactPublicUser) {
                    chatName = contactPublicUser.getDisplayName();
                    imgKey = contactPublicUser.getImgKey();

                    ChatManager.addChatToChatList(Chat.this);
                    activity.addChatsToView();
                }
            });
        }
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
