package com.rafaelfloressouza.whatsup.Objects;

public class Chat {

    private String chatId;
    private String name;

    public Chat(String chatId, String name) {
        this.chatId = chatId;
        this.name = name;
    }

    public String getChatId() {
        return chatId;
    }

    public String getName() {
        return name;
    }
}
