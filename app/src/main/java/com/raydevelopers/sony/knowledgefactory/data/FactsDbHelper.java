package com.raydevelopers.sony.knowledgefactory.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SONY on 04-05-2017.
 */

public class FactsDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "facts.db";
    public static final int DATABASE_VERSION = 1;
    public FactsDbHelper(Context context) {
        super(context,DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
final String SQL_CREATE_FACTS_TABLE="CREATE TABLE "+ FactsContract.FactsEntry.TABLE_NAME+" ( "+
        FactsContract.FactsEntry.COLUMN_FACT_NUMBER+" TEXT NOT NULL, "+
        FactsContract.FactsEntry.COLUMN_FACT_TEXT+" TEXT NOT NULL"+" )";
        db.execSQL(SQL_CREATE_FACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FactsContract.FactsEntry.TABLE_NAME);
        onCreate(db);
    }
}
