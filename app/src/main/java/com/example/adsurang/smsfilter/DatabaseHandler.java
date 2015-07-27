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
    private static final String DATABASE_NAME = "RuleSets4";

    // Contacts table name
    private static final String TABLE_RULES = "Rules";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EXPRESSION = "expression";
    private static final String KEY_DESTINATION = "destination";

    public DatabaseHandler(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RULES_TABLE = "CREATE TABLE " + TABLE_RULES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EXPRESSION + " TEXT," + KEY_DESTINATION + " TEXT" + ")";
        db.execSQL(CREATE_RULES_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RULES);

        // Create tables again
        onCreate(db);
    }

    // Adding new contact
    public void addRule(Rule newRule) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, newRule.name); // Contact Name
        values.put(KEY_EXPRESSION, newRule.rule); // Contact Phone Number
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

        // return contact list
        return ruleList;
    }

    public void deleteRule(Rule rule) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RULES, KEY_ID + " = ?",
                new String[] { String.valueOf(rule.id) });
        db.close();
    }
}