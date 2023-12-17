package com.example.chatu;

public class ChatModel {
    String msgID;
    String senderID;
    String msg;
    private long timestamp;
    public ChatModel(String msgID, String senderID, String msg,long timestamp) {
        this.msg = msg;
        this.msgID = msgID;
        this.senderID = senderID;
        this.timestamp = timestamp;
    }

    public ChatModel() {
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMsgID() {
        return msgID;
    }

    public void setMsgID(String msgID) {
        this.msgID = msgID;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
