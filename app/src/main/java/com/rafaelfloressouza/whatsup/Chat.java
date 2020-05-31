package com.rafaelfloressouza.whatsup;

public class Chat {

    private String chatId;
    private String name = null;

    public Chat(String chatId){
        this.chatId = chatId;
    }

    public String getChatId() {
        return chatId;
    }
}
