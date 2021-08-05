package com.example.statsapp;

import android.os.AsyncTask;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BackgroundConnect extends AsyncTask<String, Void, Connection> {

    @Override
    protected Connection doInBackground(String... strings) {
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://204.2.195.209:425/TindeRegel", "admin","TdGyN3vs");
        } catch (SQLException connectFail){
//            Toast.makeText(getApplicationContext(),"Hello Javatpoint",Toast.LENGTH_SHORT).show();
            System.out.println("CUSOMOARS!!!!!!!!!!!!!!!!! "+connectFail.getMessage());
        } catch (ClassNotFoundException driverEx){
            System.out.println("\n\n\n\n\n\n\n\n\n\n\nfailed to find driver\n\n\n\n\n\n\n\n\n\n\n\n");
        }
        return conn;
    }
}

//    @Override
//    protected String doInBackground(String... strings) {
//        String connStr = "204.2.195.209:425";
//        String output = "";
//        try {
//            URL url = new URL(connStr);
//            HttpURLConnection http = (HttpURLConnection) url.openConnection();
//            http.setRequestMethod("POST");
//            http.setDoInput(true);
//            http.setDoOutput(true);
//            OutputStream ops = http.getOutputStream();
//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops, "UTF-8"));
//            String data = URLEncoder.encode("user","UTF-8") + "=" + URLEncoder.encode("admin", "UTF-8")
//                    +"&&" + URLEncoder.encode("pass","UTF-8") + "=" + URLEncoder.encode("pass", "UTF-8");
//        } catch (MalformedURLException e) {
//            output = e.getMessage();
//        } catch (IOException e) {
//            output = e.getMessage();
//        }
//        return output;
//    }
//}
