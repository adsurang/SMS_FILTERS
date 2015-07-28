package com.example.adsurang.smsfilter;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.provider.Telephony;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


/**
 * Created by adsurang on 7/27/2015.
 */
public class MySMSReceiver extends BroadcastReceiver {

    public static final String SMS_CONTENT_INBOX = "content://sms/inbox";

    @Override
    public void onReceive(Context context, Intent intent){
        //do something with the message received
        Object[] pdus=(Object[])intent.getExtras().get("pdus");
        SmsMessage shortMessage=SmsMessage.createFromPdu((byte[]) pdus[0]);

        String SmsSender = shortMessage.getOriginatingAddress();
        String DisplayMessage = shortMessage.getDisplayMessageBody();
        Long msg_id = getId(shortMessage, context);

        Log.d("SMSReceiver", "SMS message sender: " + SmsSender);
        Log.d("SMSReceiver","SMS message text: "+ DisplayMessage);

        List<String> smses = getSMS(context);
        List<Sms> sms = getAllSms(context);

        Log.d("SMSReceiver", "SMS message sender: " + SmsSender);
        Log.d("SMSReceiver","SMS message text: "+ DisplayMessage);
    }

    public static long getId(SmsMessage msg, Context context){

        String idType = "id";
        //Logger.log(CLASS_TAG, "getId(): Locate message id or thread id: idType:" + idType);
        Uri uriSms=Uri.parse(SMS_CONTENT_INBOX);
        StringBuilder sb=new StringBuilder();
        sb.append("address='" + msg.getOriginatingAddress() + "' AND ");
        sb.append("body=" + DatabaseUtils.sqlEscapeString(msg.getMessageBody()));
        Cursor c=context.getContentResolver().query(uriSms, null, sb.toString(), null, null);
        if (c.getCount() > 0 && c != null) {
            c.moveToFirst();
            if (idType.equals("id")) {
                return c.getLong(c.getColumnIndex("_id"));
            }
            else     if (idType.equals("thread")) {
                return c.getLong(c.getColumnIndex("thread_id"));
            }
            c.close();
        }
        return 0;
    }

    public List<Sms> getAllSms(Context context) {
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

        return lstSms;
    }

    public List<String> getSMS(Context context){
        List<String> sms = new ArrayList<String>();
        Uri uriSMSURI = Uri.parse("content://sms");
        Cursor cur = context.getContentResolver().query(uriSMSURI, null, null, null, null);

        while (cur.moveToNext()) {
            String address = cur.getString(cur.getColumnIndex("address"));
            String body = cur.getString(cur.getColumnIndexOrThrow("body"));
            sms.add("Number: " + address + " .Message: " + body);

        }
        return sms;
    }
}
