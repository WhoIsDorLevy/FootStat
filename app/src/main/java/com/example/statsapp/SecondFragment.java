package com.example.statsapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.statsapp.databinding.FragmentSecondBinding;

import java.util.HashMap;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private SQLiteDatabase db;
    private String matchDifVal, rGoalsVal, lGoalsVal, hGoalsVal, assistsVal,
            rPenaltiesMadeVal, lPenaltiesMadeVal, rPenaltiesMissedVal, lPenaltiesMissedVal;
    private HashMap<EditText, String> inputs;// {title, <text input, value>}
    private HashMap<String, EditText> tableNameToEditText;//

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        MainActivity activity = (MainActivity) getActivity();
        db = activity.getDb().getWritableDatabase();
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }
    private void initMaps(){
        inputs = new HashMap<>();
        //inputs
        //default value is 0
        inputs.put(binding.matchDifEditText, "0");
        inputs.put(binding.RGoalsEditText, "0");
        inputs.put(binding.LGoalsEditText, "0");
        inputs.put(binding.HGoalsEditText, "0");
        inputs.put(binding.AssistsEditText, "0");
        inputs.put(binding.RPenaltiesMadeEditText, "0");
        inputs.put(binding.LPenaltiesMadeEditText, "0");
        inputs.put(binding.RPenaltiesMissedEditText, "0");
        inputs.put(binding.LPenaltiesMissedEditText, "0");

        //tableNameToEditText
        tableNameToEditText.put(AppDB.MATCH_DIFFICULTY_TABLE_NAME, binding.matchDifEditText);
        tableNameToEditText.put(AppDB.R_GOALS_TABLE_NAME, binding.RGoalsEditText);
        tableNameToEditText.put(AppDB.L_GOALS_TABLE_NAME, binding.LGoalsEditText);
        tableNameToEditText.put(AppDB.H_GOALS_TABLE_NAME, binding.HGoalsEditText);
        tableNameToEditText.put(AppDB.ASSISTS_TABLE_NAME, binding.AssistsEditText);
        tableNameToEditText.put(AppDB.R_PENALTIES_MADE_TABLE_NAME, binding.RPenaltiesMadeEditText);
        tableNameToEditText.put(AppDB.L_PENALTIES_MADE_TABLE_NAME, binding.LPenaltiesMadeEditText);
        tableNameToEditText.put(AppDB.R_PENALTIES_MISSED_TABLE_NAME, binding.RPenaltiesMissedEditText);
        tableNameToEditText.put(AppDB.L_PENALTIES_MISSED_TABLE_NAME, binding.LPenaltiesMissedEditText);
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.buttonEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getInputs();
                uploadStats();
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
    }

    private boolean isEmptyEditText(EditText ed){
        return TextUtils.isEmpty(ed.getText());
    }

    private void getInputs(){
        matchDifVal = (isEmptyEditText(binding.matchDifEditText)) ? "0" : binding.matchDifEditText.getText().toString();
        rGoalsVal =  (isEmptyEditText(binding.RGoalsEditText)) ? "0" : binding.RGoalsEditText.getText().toString();
        lGoalsVal =  (isEmptyEditText(binding.LGoalsEditText)) ? "0" : binding.LGoalsEditText.getText().toString();
        hGoalsVal =  (isEmptyEditText(binding.HGoalsEditText)) ? "0" : binding.HGoalsEditText.getText().toString();
        assistsVal = (isEmptyEditText(binding.AssistsEditText)) ? "0" : binding.AssistsEditText.getText().toString();
        rPenaltiesMadeVal = (isEmptyEditText(binding.RPenaltiesMadeEditText)) ? "0" : binding.RPenaltiesMadeEditText.getText().toString();
        lPenaltiesMadeVal = (isEmptyEditText(binding.LPenaltiesMadeEditText)) ? "0" : binding.LPenaltiesMadeEditText.getText().toString();
        rPenaltiesMissedVal = (isEmptyEditText(binding.RPenaltiesMissedEditText)) ? "0" : binding.RPenaltiesMissedEditText.getText().toString();
        lPenaltiesMissedVal = (isEmptyEditText(binding.LPenaltiesMissedEditText)) ? "0" : binding.LPenaltiesMissedEditText.getText().toString();


//        for (EditText curr :inputs.keySet()){
//            if (!isEmptyEditText(curr)){
//                inputs.put(curr, curr.getText().toString());
//            }
//        }


    }

    private void uploadStats(){
        ContentValues values = new ContentValues();

        values.put(AppDB.MATCH_DIFFICULTY_TABLE_NAME, matchDifVal);
        values.put(AppDB.R_GOALS_TABLE_NAME, rGoalsVal);
        values.put(AppDB.L_GOALS_TABLE_NAME, lGoalsVal);
        values.put(AppDB.H_GOALS_TABLE_NAME, hGoalsVal);
        values.put(AppDB.ASSISTS_TABLE_NAME, assistsVal);
        values.put(AppDB.R_PENALTIES_MADE_TABLE_NAME, rPenaltiesMadeVal);
        values.put(AppDB.L_PENALTIES_MADE_TABLE_NAME, lPenaltiesMadeVal);
        values.put(AppDB.R_PENALTIES_MISSED_TABLE_NAME, rPenaltiesMissedVal);
        values.put(AppDB.L_PENALTIES_MISSED_TABLE_NAME, lPenaltiesMissedVal);

        db.insert(AppDB.MATCHES_TABLE_NAME, null, values);


//        for (String currTableName : tableNameToEditText.keySet()){
//            values.put(currTableName, inputs.get(tableNameToEditText.get(currTableName)));
//        }
//        db.insert(AppDB.MATCHES_TABLE_NAME, null, values);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        db.close();
    }
//    private void uploadStats(){
//        int id = 0;
//        if (conn == null){
//            try {
//                Class.forName("com.mysql.cj.jdbc.Driver");
//                conn = DriverManager.getConnection("jdbc:mysql://204.2.195.209:425/TindeRegel", "admin","TdGyN3vs");
//
//            } catch (SQLException connectFail){
////            Toast.makeText(getApplicationContext(),"Hello Javatpoint",Toast.LENGTH_SHORT).show();
//                System.out.println("CUSOMOARS!!!!!!!!!!!!!!!!! "+connectFail.getMessage());
//            } catch (ClassNotFoundException driverEx){
//                System.out.println("\n\n\n\n\n\n\n\n\n\n\nfailed to find driver\n\n\n\n\n\n\n\n\n\n\n\n");
//            }
//        }
//        try {
//            //get last id
//            Statement statement = conn.createStatement();
//            ResultSet res = statement.executeQuery("SELECT MAX(id) AS lastId\n" + "FROM matches");
//            if (res.next()) {
//                id = res.getInt("lastId");
//            }
//            //upload match
//@SuppressLint("DefaultLocale") String update = "INSERT INTO matches\n" + String.format("VALUES (%d, %s, %s, %s, %s, %s, %s, %s, %s, %s);",
//                    id, binding.matchDifEditText.getText(), binding.RGoalsEditText.getText(), binding.LGoalsEditText.getText(),
//                    binding.HGoalsEditText.getText(), binding.AssistsEditText.getText(), binding.RPenaltiesMadeEditText.getText(),
//                    binding.LPenaltiesMadeEditText.getText(), binding.RPenaltiesMissedEditText.getText(), binding.LPenaltiesMissedEditText.getText());
//
//            statement.executeUpdate(update);
//            statement.close();
//            System.out.println("Match added successfully");
//        }catch (SQLException fail){
//            System.out.println("failed to execute statement");
//        }
//
//
//    }
//}
//    private void uploadStats(){
//        int id = 0;//initialized with 0
//        try {
//            if (conn == null) {
//                BackgroundConnect bConnect = new BackgroundConnect();
//                conn = bConnect.doInBackground("");
//            }
//            if (bq == null){
//                bq = new BackgroundQuery(conn);
//            }
//            //get last id
//
//            Integer res = bq.execute("SELECT MAX(id) AS lastId\n" + "FROM matches", "lastId").get();
//            if (res != null){
//                id = res;
//            }
//
//            @SuppressLint("DefaultLocale") String query = "INSERT INTO matches\n" + String.format("VALUES (%d, %s, %s, %s, %s, %s, %s, %s, %s, %s);",
//                    id, binding.matchDifEditText.getText(), binding.RGoalsEditText.getText(), binding.LGoalsEditText.getText(),
//                    binding.HGoalsEditText.getText(), binding.AssistsEditText.getText(), binding.RPenaltiesMadeEditText.getText(),
//                    binding.LPenaltiesMadeEditText.getText(), binding.RPenaltiesMissedEditText.getText(), binding.LPenaltiesMissedEditText.getText());
//
//            statement.executeUpdate(query);
//            statement.close();
//            System.out.println("Match added successfully");
//        } catch (SQLException fail){
//            System.out.println(fail.getMessage());
//        }
//    }
//
//    private void setConnection(){
//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//            conn = DriverManager.getConnection("jdbc:mysql://204.2.195.209:425/TindeRegel", "admin","TdGyN3vs");
//        } catch (SQLException connectFail){
////            Toast.makeText(getApplicationContext(),"Hello Javatpoint",Toast.LENGTH_SHORT).show();
//            System.out.println("CUSOMOARS!!!!!!!!!!!!!!!!! "+connectFail.getMessage());
//        } catch (ClassNotFoundException driverEx){
//            System.out.println("\n\n\n\n\n\n\n\n\n\n\nfailed to find driver\n\n\n\n\n\n\n\n\n\n\n\n");
//        }
//    }
//    private void closeConn(){
//        try{
//            conn.close();
//        } catch (SQLException ignored){
//
//        }
//    }
//


}