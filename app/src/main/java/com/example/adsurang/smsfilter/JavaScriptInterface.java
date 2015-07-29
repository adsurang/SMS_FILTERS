package com.example.adsurang.smsfilter;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Created by abhinasi on 7/28/2015.
 */
public class JavaScriptInterface {
    private final Context context;
    private Activity activity;

    public JavaScriptInterface(Activity activiy) {
        this.activity = activiy;
        this.context = activity.getApplicationContext();
    }

    public JavaScriptInterface(Context context){
        this.context = context;
    }

    @android.webkit.JavascriptInterface
    public String showAllSMS(String ruleName) {
        List<Sms> lstSms = getSmses(ruleName);
        Gson gson = new Gson();
        String Json = gson.toJson(lstSms);
        return  Json;
    }

    private List<Sms> getSmses(String ruleName) {
        List<Sms> lstSms = new ArrayList<Sms>();
        Sms objSms = new Sms();
        Uri message = Uri.parse("content://sms/");
        boolean getAllMessages = (ruleName == null || ruleName.isEmpty());
        ContentResolver cr = context.getContentResolver();
        Cursor c = cr.query(message, null, null, null, null);
        DatabaseHandler db = new DatabaseHandler(this.context);

        List<MessageHash> messageHashList = db.QueryMessges(ruleName);
        List<Integer> validMessageIds = new ArrayList<>();
        if(!getAllMessages) {
            for (int i = 0; i < messageHashList.size(); i++) {
                validMessageIds.add(messageHashList.get(i).msgId);
            }
            Collections.sort(validMessageIds);
        }

        if (c.moveToFirst()) {
            int limit = c.getCount() < 200 ? c.getCount() : 200;
            for (int i = 0; i < limit; i++) {
                objSms = new Sms();
                int messageId = Integer.parseInt(c.getString(c.getColumnIndexOrThrow("_id")));
                if(getAllMessages || validMessageIds.contains(messageId)){
                    objSms.setId(c.getString(c.getColumnIndexOrThrow("_id")));
                    objSms.setAddress(c.getString(c
                            .getColumnIndexOrThrow("address")));
                    objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
                    objSms.setReadState(c.getString(c.getColumnIndex("read")));
                    objSms.setTime(c.getString(c.getColumnIndexOrThrow("date")));
                    if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
                        objSms.setFolderName("inbox");
                    } else {
                        objSms.setFolderName("sent");
                    }
                    lstSms.add(objSms);
                }
                c.moveToNext();
            }
        }

        c.close();
        db.close();

        return lstSms;
    }

    @android.webkit.JavascriptInterface
    public void createApplyFromRule(String ruleName, String fromRule) {
        String address;
        int messageId;

        // Add rule
        DatabaseHandler db = new DatabaseHandler(this.context);
        Rule newRule = new Rule(ruleName, fromRule);
        newRule.messageCount = 0;

        // Apply Rule
        List<MessageHash> messageHashList = new ArrayList<MessageHash>();
        Uri message = Uri.parse("content://sms/");

        ContentResolver cr = context.getContentResolver();
        Cursor c = cr.query(message, null, null, null, null);

        if (c.moveToFirst()) {
            int limit = c.getCount() < 200 ? c.getCount() : 200;
            for (int i = 0; i < limit; i++) {
                messageId = Integer.parseInt(c.getString(c.getColumnIndexOrThrow("_id")));
                address = (c.getString(c
                        .getColumnIndexOrThrow("address")));
                if(isContains(address, fromRule)){
                    messageHashList.add(new MessageHash(messageId, ruleName));
                    newRule.messageCount++;
                    newRule.newMessageAvailable=true;
                }
                c.moveToNext();
            }
        }
        c.close();

        db.addRule(newRule);
        db.addMessages(messageHashList);
        db.close();
    }

    public void applyRuleOnSms(long smsId, String smsAddress, String smsBody){
        DatabaseHandler db = new DatabaseHandler(this.context);
        List<Rule> rules = db.getAllRules();
        List<MessageHash> messageHashList = new ArrayList<MessageHash>();
        Rule rule;

        Iterator<Rule> rulesIterator = rules.iterator();
        while (rulesIterator.hasNext()){
            rule = rulesIterator.next();
            if(isContains(smsAddress, rule.fromRule)){
                messageHashList.add(new MessageHash((int)smsId, rule.name));
                rule.newMessageAvailable = true;
                rule.messageCount++;
            }
        }

        db.addRules(rules);
        db.addMessages(messageHashList);
        db.close();
    }

    private boolean isContains(String address, String fromRule) {
        String[] fromRules = fromRule.split(";");
        for (int i=0; i<fromRules.length; i++){
            if(address.contains(fromRules[i])){
                return true;
            }
        }
        return false;
    }

    @android.webkit.JavascriptInterface
    public String getALLFolders() {
        DatabaseHandler db = new DatabaseHandler(this.activity);
        List<Rule> rules = db.getAllRules();
        List<String> folders = new ArrayList<String>();

        for(int i=0;i<rules.size();i++){
            folders.add(rules.get(i).destinationFolder);
        }

        HashSet<String> uniqueFolders = new HashSet<String>(folders);
        Gson gson = new Gson();
        String Json = gson.toJson(rules);
        return  Json;
    }

    @android.webkit.JavascriptInterface
    public void onFolderClick(String folderName) {

        DatabaseHandler db = new DatabaseHandler(this.activity);

        List<Sms> msgs2 = getSmses(folderName);
    }

    @android.webkit.JavascriptInterface
    public void onCreateRuleClicked() {
        Intent intent = new Intent(this.activity, RuleActivity.class);
        this.activity.startActivity(intent);
    }
}
