package com.chj.veot.chatbot;

public class ChatMessage {
    private boolean Sender;
    private boolean user;
    private boolean bot;
    private String Message;

    public ChatMessage(String message, boolean sender) {
        this.Message = message;
        this.user = sender;
        this.bot = !sender;
    }

    public String getMessage() {
        return this.Message;
    }

    public void setMessage(String message) {
        this.Message = message;
    }

    public boolean isUser() {
        return this.user;
    }

    public void setSender(boolean sender) {
        this.user = sender;
        this.bot = !sender;
    }

    public boolean isBot() {return this.bot;}
}