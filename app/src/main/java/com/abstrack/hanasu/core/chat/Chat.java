package com.abstrack.hanasu.core.chat;

public class Chat {
    private boolean seen;
    private int sentMessageState;
    // Disabled for testing purposes
    // private Bitmap icon;
    private int messagesCount;
    private String messages;
    private String time;
    private String name;


    public Chat(boolean seen, String name, int sentMessageState, int messagesCount, String messages, String time) {
        this.seen = seen;
        this.name = name;
        this.sentMessageState = sentMessageState;
        this.messagesCount = messagesCount;
        this.messages = messages;
        this.time = time;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSentMessageState() {
        return sentMessageState;
    }

    public void setSentMessageState(int sentMessageState) {
        this.sentMessageState = sentMessageState;
    }

    public int getMessagesCount() {
        return messagesCount;
    }

    public void setMessagesCount(int messagesCount) {
        this.messagesCount = messagesCount;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
