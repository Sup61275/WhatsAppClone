package com.example.whatsappclone.Models;

public class User {
    private String profilePic;
    private String userName;
    private String mail;
    private String password;
    private String userid;
    private String lastMessage;
    private String status;
    private boolean readStatus;

    public User(String username, String email, boolean b, String userId) {
        // Default constructor required for Firebase Realtime Database
    }
    public User() {
        // Default constructor required for Firebase Realtime Database
    }

    public User(String profilePic, String userName, String mail, String password, String userid, String lastMessage, String status, boolean readStatus) {
        this.profilePic = profilePic;
        this.userName = userName;
        this.mail = mail;
        this.password = password;
        this.userid = userid;
        this.lastMessage = lastMessage;
        this.status = status;
        this.readStatus = readStatus;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isReadStatus() {
        return readStatus;
    }

    public void setReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }
}



