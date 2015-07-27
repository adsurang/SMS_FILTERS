package com.example.adsurang.smsfilter;

/**
 * Created by adsurang on 7/28/2015.
 */
public class MessageHash {

    public int id;
    public int msgId;
    public String SerializedTags;

    public MessageHash(){

    }

    public MessageHash( int msgId, String SerializedTags){

        this.msgId = msgId;
        this.SerializedTags = SerializedTags;
    }

}
