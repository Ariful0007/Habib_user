package com.habib.habib_user;

public class MessageModel {
    String username,email,uuid,message,time;

    public MessageModel() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }



    public MessageModel(String username, String email, String uuid, String message, String time) {
        this.username = username;
        this.email = email;
        this.uuid = uuid;
        this.message = message;
        this.time = time;

    }
}
