package com.example.adsurang.smsfilter;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
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
            for (int i = 0; i < totalSMS; i++) {

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

        return  lstSms.toString();
    }
}
