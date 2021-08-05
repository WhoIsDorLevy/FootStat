package com.example.statsapp;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BackgroundQuery extends AsyncTask<String, Void, Integer> {
    private Connection conn;

    protected BackgroundQuery(Connection conn){
        super();
        this.conn = conn;
    }
    @Override
    protected Integer doInBackground(String... strings) {
        Integer result = null;
        try {

            Statement statement = conn.createStatement();
            ResultSet res = statement.executeQuery(strings[0]);
            if (res.next()) {
                result = res.getInt(strings[1]);
            }
        }catch (SQLException fail){
            System.out.println("failed to create statement");
        }
        return result;
    }
    protected void destroy(){
        try {
            conn.close();
        } catch (Exception ignored){}
    }
}
