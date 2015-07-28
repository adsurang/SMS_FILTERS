package com.example.adsurang.smsfilter;

import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class RuleActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseHandler db = new DatabaseHandler(this);

        /**
         * CRUD Operations
         * */
        // Inserting RULES
        Log.d("Insert: ", "Inserting ..");
        db.addRule(new Rule("Rule1", "FromTag1", "MessageBody1", true, "Folder1"));
        db.addRule(new Rule("Rule1", "FromTag2","MessageBody2",true, "Folder1"));
        db.addRule(new Rule("Rule1", null,"MessageBody3",false, "Folder1"));
        db.addRule(new Rule("Rule1", "FromTag3",null,false, "Folder1"));


        //InsertMessageHash

        db.addMessage(new MessageHash(733, "ICICI,FLIPKART, ADHAR"));
        db.addMessage(new MessageHash(734, "SBI, PANKAJ"));
        db.addMessage(new MessageHash(735, "ABHINAV ,SBI"));

        // Reading all Rules
        Log.d("Reading: ", "Reading all Rules..");
        List<Rule> rules = db.getAllRules();

        List<MessageHash> msgs1 = db.QueryMessges("ABHINAV");
        List<MessageHash> msgs2 = db.QueryMessges("SBI");

        for (Rule cn : rules) {
            String log = "Id: "+cn.id+" ,Name: " + cn.name + " ,FromExpression: " + cn.fromRule + ", MessageBodyExp: "+ cn.contentRule + ", DoAndExpression: " + cn.doAndRule + ", Destination:" + cn.destinationFolder;
            // Writing Rules to log
            Log.d("Name: ", log);
        }
    }
}