package com.example.statsapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    public static final int NUM_OF_COLS = 9;
    public static final String DATABASE_NAME = "sample_database";
    public static String TABLE_NAME = "matchesTab";
    public static String GOALS_TABLE_NAME = "goalsTab";
    public static String PENALTIES_TABLE_NAME = "penaltiesTab";
    public static final String COLUMN_ID = "id";
    public static final String DATE = "dayOfMatch";
    public static final String MATCH_DIFFICULTY = "matchDifficulty";
    public static final String R_GOALS = "rGoals";
    public static final String L_GOALS = "lGoals";
    public static final String H_GOALS = "hGoals";
    public static final String T_GOALS = "tGoals";//total goals
    public static final String ASSISTS = "assists";
    public static final String R_PENALTIES_MADE = "rPenaltiesMade";
    public static final String L_PENALTIES_MADE = "lPenaltiesMade";
    public static final String T_PENALTIES_MADE = "tPenaltiesMade";//total penalties made
    public static final String R_PENALTIES_MISSED = "rPenaltiesMissed";
    public static final String L_PENALTIES_MISSED = "lPenaltiesMissed";
    public static final String T_PENALTIES_MISSED = "tPenaltiesMissed";

    private static boolean createdTables = false;
    private static AppDB instance = null;



    private AppDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        if (!createdTables) {
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (\n" +
                    COLUMN_ID + " integer PRIMARY KEY AUTOINCREMENT,\n" +
                    DATE + " date NOT NULL,\n" +
                    MATCH_DIFFICULTY + " integer NOT NULL,\n" +
                    R_GOALS + " integer NOT NULL,\n" +
                    L_GOALS + " integer NOT NULL,\n" +
                    H_GOALS + " integer NOT NULL,\n" +
                    ASSISTS + " integer NOT NULL,\n" +
                    R_PENALTIES_MADE + " integer NOT NULL,\n" +
                    L_PENALTIES_MADE + " integer NOT NULL,\n" +
                    R_PENALTIES_MISSED + " integer NOT NULL,\n" +
                    L_PENALTIES_MISSED + " integer NOT NULL" +
                    ");");

            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + GOALS_TABLE_NAME + " (\n" +
                    COLUMN_ID + " integer PRIMARY KEY REFERENCES "+ TABLE_NAME + "(" +COLUMN_ID+ ")" + ",\n" +
                    DATE + " date REFERENCES "+ TABLE_NAME + "(" +DATE+ ")" + ",\n" +
                    MATCH_DIFFICULTY + " integer NOT NULL,\n" +
                    R_GOALS + " integer NOT NULL,\n" +
                    L_GOALS + " integer NOT NULL,\n" +
                    H_GOALS + " integer NOT NULL,\n" +
                    ASSISTS + " integer NOT NULL,\n" +
                    R_PENALTIES_MADE + " integer NOT NULL,\n" +
                    L_PENALTIES_MADE + " integer NOT NULL,\n" +
                    R_PENALTIES_MISSED + " integer NOT NULL,\n" +
                    L_PENALTIES_MISSED + " integer NOT NULL" +
                    ");");
            createdTables = true;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public static AppDB getInstance(Context context){
        if (instance == null){
            instance = new AppDB(context);
        }
        return instance;
    }


    public SQLiteDatabase getDatabase(boolean writable){//false for readable
        SQLiteDatabase output = (writable) ? super.getWritableDatabase() :
                super.getReadableDatabase();
        onCreate(output);
        return output;
    }
}
