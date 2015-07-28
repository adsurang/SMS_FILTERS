package com.example.adsurang.smsfilter;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
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

    @android.webkit.JavascriptInterface
    public String showAllSMS() {
        //Intent intent = new Intent(Intent.ACTION_VIEW);

        List<Sms> lstSms = new ArrayList<Sms>();
        Sms objSms = new Sms();
        Uri message = Uri.parse("content://sms/");

        ContentResolver cr = context.getContentResolver();

        Cursor c = cr.query(message, null, null, null, null);
        //context.getActivity().startManagingCursor(c);
        int totalSMS = c.getCount();

        if (c.moveToFirst()) {
            for (int i = 0; i < 10; i++) {

                objSms = new Sms();
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
                c.moveToNext();
            }
        }
        // else {
        // throw new RuntimeException("You have no SMS");
        // }
        c.close();
        //activity.getApplication()
        //intent.setDataAndType(Uri.parse("file:///android_asset/CreateOrUpdateRule.html"), "");
        //activity.startActivity(intent);
        Gson gson = new Gson();

        String Json = gson.toJson(lstSms);

        return  Json;
    }

    @android.webkit.JavascriptInterface
    public String getALLFolders() {

        DatabaseHandler db = new DatabaseHandler(this.activity);

        /*Sample Rules */
        db.addRule(new Rule("Rule1", "FromTag1", "MessageBody1", true, "Folder1"));
        db.addRule(new Rule("Rule2", "FromTag2","MessageBody2",true, "Folder2"));
        db.addRule(new Rule("Rule3", null,"MessageBody3",false, "Folder3"));
        db.addRule(new Rule("Rule4", "FromTag3", null, false, "Folder1"));
        db.addRule(new Rule("Rule5", null,"MessageBody3",false, "Folder4"));
        db.addRule(new Rule("Rule6", null,"MessageBody3",false, "Folder5"));
        db.addRule(new Rule("Rule7", null,"MessageBody3",false, "Folder6"));
        db.addRule(new Rule("Rule8", null,"MessageBody3",false, "Folder7"));
        db.addRule(new Rule("Rule9", null,"MessageBody3",false, "Folder8"));




        List<Rule> rules = db.getAllRules();

        List<String> folders = new ArrayList<String>();

        Integer i =0;

        for(i=0;i<rules.size();i++){
            folders.add(rules.get(i).destinationFolder);
        }

        HashSet<String> uniqueFolders = new HashSet<String>(folders);

        Gson gson = new Gson();

        String Json = gson.toJson(uniqueFolders);

        return  Json;
    }

    @android.webkit.JavascriptInterface
    public void onFolderClick(String folderName) {

        DatabaseHandler db = new DatabaseHandler(this.activity);

        db.addMessage(new MessageHash(733, "Folder1,Folder2, Folder3"));
        db.addMessage(new MessageHash(734, "Folder2, Folder4"));
        db.addMessage(new MessageHash(735, "Folder2"));

        List<MessageHash> msgs2 = db.QueryMessges(folderName);
    }
}
