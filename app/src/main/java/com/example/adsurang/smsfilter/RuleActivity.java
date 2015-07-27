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
        // Inserting Contacts
        Log.d("Insert: ", "Inserting ..");
        db.addRule(new Rule("Rule1", "Tag1", "Folder1"));
        db.addRule(new Rule("Rule2", "Tag2", "Folder2"));
        db.addRule(new Rule("Rule3", "Tag3", "Folder3"));
        db.addRule(new Rule("Rule4", "Tag4", "Folder4"));

        // Reading all contacts
        Log.d("Reading: ", "Reading all Rules..");
        List<Rule> rules = db.getAllRules();

        for (Rule cn : rules) {
            String log = "Id: "+cn.id+" ,Name: " + cn.name + " ,Expression: " + cn.rule + ", Destination:" + cn.destinationFolder;
            // Writing Contacts to log
            Log.d("Name: ", log);
        }
    }
}