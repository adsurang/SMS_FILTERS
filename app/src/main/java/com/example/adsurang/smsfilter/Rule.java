package com.example.adsurang.smsfilter;

/**
 * Created by adsurang on 7/27/2015.
 */
public class Rule {
    int id;
    public String name;
    public String rule;
    public String destinationFolder;

    public Rule(){

    }

    public Rule( String name, String rule, String Destination){

        this.name = name;
        this.rule = rule;
        this.destinationFolder = Destination;
    }

}
