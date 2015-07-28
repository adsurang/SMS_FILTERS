package com.example.adsurang.smsfilter;

/**
 * Created by adsurang on 7/27/2015.
 */
public class Rule {
    int id;
    public String name;
    public String fromRule;
    public String contentRule;
    public boolean doAndRule;
    public String destinationFolder;

    public Rule(){

    }

    public Rule( String name, String fromrule, String contentRule, boolean isAndRule, String destination ){

        this.name = name;
        this.fromRule = fromrule;
        this.contentRule = contentRule;
        this.doAndRule = isAndRule;
        this.destinationFolder = destination;
    }

}
