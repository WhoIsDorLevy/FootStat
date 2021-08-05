package com.example.statsapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "sample_database";
    public static final String MATCHES_TABLE_NAME = "matches";
    public static final String COLUMN_ID = "id";
    public static final String MATCH_DIFFICULTY_TABLE_NAME = "matchDifficulty";
    public static final String R_GOALS_TABLE_NAME = "rGoals";
    public static final String L_GOALS_TABLE_NAME = "lGoals";
    public static final String H_GOALS_TABLE_NAME = "hGoals";
    public static final String ASSISTS_TABLE_NAME = "assists";
    public static final String R_PENALTIES_MADE_TABLE_NAME = "rPenaltiesMade";
    public static final String L_PENALTIES_MADE_TABLE_NAME = "lPenaltiesMade";
    public static final String R_PENALTIES_MISSED_TABLE_NAME = "rPenaltiesMissed";
    public static final String L_PENALTIES_MISSED_TABLE_NAME = "lPenaltiesMissed";




    public AppDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + MATCHES_TABLE_NAME + "(\n" +
                COLUMN_ID + " integer PRIMARY KEY AUTOINCREMENT,\n" +
                MATCH_DIFFICULTY_TABLE_NAME +" INTEGER NOT NULL,\n" +
                R_GOALS_TABLE_NAME + " integer NOT NULL,\n" +
                L_GOALS_TABLE_NAME + " integer NOT NULL,\n" +
                H_GOALS_TABLE_NAME + " integer NOT NULL,\n" +
                ASSISTS_TABLE_NAME + " integer NOT NULL,\n" +
                R_PENALTIES_MADE_TABLE_NAME + " integer NOT NULL,\n" +
                L_PENALTIES_MADE_TABLE_NAME + " integer NOT NULL,\n" +
                R_PENALTIES_MISSED_TABLE_NAME + " integer NOT NULL,\n" +
                L_PENALTIES_MISSED_TABLE_NAME + " integer NOT NULL" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MATCHES_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
