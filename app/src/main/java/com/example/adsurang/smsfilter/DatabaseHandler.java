package com.example.adsurang.smsfilter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adsurang on 7/27/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version


    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "RuleSets9";

    // Rules and MessaheHash table name
    private static final String TABLE_RULES = "Rules";
    private static final String TABLE_TAGS = "Tags";

    //  Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EXPRESSION = "expression";
    private static final String KEY_DESTINATION = "destination";
    private static final String TAGS_ID = "id";
    private static final String TAGS_MSG_ID = "msg_id";
    private static final String TAGS_TAGS = "tags";


    public DatabaseHandler(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RULES_TABLE = "CREATE TABLE " + TABLE_RULES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EXPRESSION + " TEXT," + KEY_DESTINATION + " TEXT" + ")";

        String CREATE_TAGS_TABLE = "CREATE TABLE " + TABLE_TAGS + "("
                + TAGS_ID + " INTEGER PRIMARY KEY," + TAGS_MSG_ID + " INTEGER, "
                + TAGS_TAGS + " TEXT" + ")";
        db.execSQL(CREATE_RULES_TABLE);
        db.execSQL(CREATE_TAGS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RULES);

        // Create tables again
        onCreate(db);
    }

    // Adding new rule
    public void addRule(Rule newRule) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, newRule.name);
        values.put(KEY_EXPRESSION, newRule.rule);
        values.put(KEY_DESTINATION, newRule.destinationFolder);

        // Inserting Row
        db.insert(TABLE_RULES, null, values);
        db.close(); // Closing database connection
    }

    // Getting All Rules
    public List<Rule> getAllRules() {
        List<Rule> ruleList = new ArrayList<Rule>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_RULES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Rule rule = new Rule();
                rule.id = Integer.parseInt(cursor.getString(0));
                rule.name = cursor.getString(1);
                rule.rule = cursor.getString(2);
                rule.destinationFolder = cursor.getString(3);

                ruleList.add(rule);
            } while (cursor.moveToNext());
        }

        // return rule list
        return ruleList;
    }

    public void deleteRule(Rule rule) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RULES, KEY_ID + " = ?",
                new String[]{String.valueOf(rule.id)});
        db.close();
    }

    public void addMessage(MessageHash message){

        MessageHash get_message = getMessage(message.msgId);

        if(get_message == null) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(TAGS_MSG_ID, message.msgId);
            values.put(TAGS_TAGS, message.SerializedTags);

            // Inserting Row
            db.insert(TABLE_TAGS, null, values);
            db.close(); // Closing database connection
        }
        else{
            updateMessage(message);
        }
    }

    // Getting single message
    public MessageHash getMessage(int msg_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TAGS, new String[] { TAGS_ID,
                        TAGS_MSG_ID, TABLE_TAGS }, TAGS_MSG_ID + "=?",
                new String[] { String.valueOf(msg_id) }, null, null, null, null);
        if (!(!(cursor.moveToFirst()) || cursor.getCount() ==0) ) {
            //cursor.moveToFirst();

            MessageHash message = new MessageHash(Integer.parseInt(cursor.getString(1)),
                    cursor.getString(2));
            message.id = Integer.parseInt(cursor.getString(0));

            // return message
            return message;
        }
        else{
            return null;
        }
    }

    public int updateMessage( MessageHash message) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TAGS_MSG_ID, message.msgId);
        values.put(TAGS_TAGS, message.SerializedTags);

        // updating row
        return db.update(TABLE_TAGS, values, TAGS_ID + " = ?",
                new String[] { String.valueOf(message.id) });
    }

    public List<MessageHash> QueryMessges(String QueryString){

        List<MessageHash> messageList = new ArrayList<MessageHash>();
        // Select All Query
        String selectQuery = "";
        Cursor cursor;
        SQLiteDatabase db = this.getWritableDatabase();
        if(QueryString == null) {
            selectQuery = "SELECT  * FROM " + TABLE_TAGS;
            cursor = db.rawQuery(selectQuery, null);
        }
        else{
            //selectQuery = "SELECT  * FROM " + TABLE_TAGS
            //+ " WHERE " +  TAGS_TAGS + " CONTAINS " + "\"" + QueryString + "\" ";
            cursor = db.query(true, TABLE_TAGS, null, TAGS_TAGS + " LIKE ?",
                    new String[]{"%" + QueryString + "%"}, null, null, null,
                    null);
        }


        //Cursor cursor = db.rawQuery(selectQuery, null);

        //Cursor cursor = db.query(true, TABLE_TAGS, null, TAGS_TAGS + " LIKE ?",
          //      new String[]{"%" + QueryString + "%"}, null, null, null,
            //    null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MessageHash message = new MessageHash();
                message.id = Integer.parseInt(cursor.getString(0));
                message.msgId = Integer.parseInt(cursor.getString(1));
                message.SerializedTags = cursor.getString(2);

                messageList.add(message);
            } while (cursor.moveToNext());
        }

        // return message list
        return messageList;
    }

}