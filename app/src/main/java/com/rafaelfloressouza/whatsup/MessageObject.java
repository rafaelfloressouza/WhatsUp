package com.rafaelfloressouza.whatsup;

import java.util.Date;

public class MessageObject {

    private String messageId;
    private String senderId;
    private String message;
    private String sent_at;

    public MessageObject(String messageId, String senderId, String message, String sent_at) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.message = message;
        this.sent_at = sent_at;
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

    public String getSentAt() {
        return this.sent_at;
    }

}
