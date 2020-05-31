package com.rafaelfloressouza.whatsup;

public class MessageObject {

    private String messageId;
    private String senderId;
    private String message;


    public MessageObject(String messageId, String senderId, String message) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.message = message;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getMessage() {
        return message;
    }
}
