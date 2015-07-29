package com.example.adsurang.smsfilter;

/**
 * Created by adsurang on 7/27/2015.
 */
public class Rule {
    int id;
    public String name;
    public String fromRule;
    public String contentRule;
    public String destinationFolder;
    public boolean newMessageAvailable;
    public int messageCount;

    public Rule(String name, String fromRule){
        this.name = name;
        this.fromRule = fromRule;
        this.destinationFolder = name;
        this.messageCount=0;
        this.newMessageAvailable=false;
    }

    public Rule( String name, String fromRule, String contentRule, boolean isAndRule, String destination ){

        this.name = name;
        this.fromRule = fromRule;
        this.contentRule = contentRule;
        this.destinationFolder = destination;
    }

}
