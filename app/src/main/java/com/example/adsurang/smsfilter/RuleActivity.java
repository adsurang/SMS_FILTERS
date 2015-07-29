package com.example.adsurang.smsfilter;

import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RuleActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseHandler db = new DatabaseHandler(this);
        JavaScriptInterface jsInterface = new JavaScriptInterface(this);

        WebView webview = new WebView(this);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(jsInterface, "android");

        addListenerOnButton();

        /**
         * CRUD Operations
         * */
        // Inserting RULES

    }

    private void addListenerOnButton() {

        //Select a specific button to bundle it with the action you want
        Button submitbutton = (Button) findViewById(R.id.SMS_Submit);

        final RuleActivity ra = this;

        submitbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                EditText smsFrom = (EditText) findViewById(R.id.SMS_INFrom);
                EditText smsTargetFolder = (EditText) findViewById(R.id.SMS_TargetFolder);
                //EditText smsContent   = (EditText)findViewById(R.id.SMS_IPCont);
                //CheckBox smsAndCheck = (CheckBox)findViewById(R.id.SMS_AndChk);
                String smsFromString = smsFrom.getText().toString();
                String smsTargetFolderString = smsTargetFolder.getText().toString();
                //  String smsContentFltString = smsContent.getText().toString();
                // boolean smsAndCheckStr = smsAndCheck.isChecked();

                JavaScriptInterface jsInterface = new JavaScriptInterface(ra);

                jsInterface.createApplyFromRule(smsTargetFolderString, smsFromString);

                Intent intent = new Intent(ra, MainActivity.class);
                ra.startActivity(intent);

            }

        });

        Button cancelbutton = (Button) findViewById(R.id.SMS_Cancel);

        cancelbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ra, MainActivity.class);
                ra.startActivity(intent);
            }

        });

    }
}