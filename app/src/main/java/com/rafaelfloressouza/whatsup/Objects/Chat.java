package com.rafaelfloressouza.whatsup.Objects;

import android.media.Image;

public class Chat {

    private String chatId;
    private String name;
    private Image chatImage = null;

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
