package com.example.statsapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.statsapp.databinding.FragmentSecondBinding;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private Connection conn;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.buttonEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
    private void uploadStats(){
        int id = 0;//initialized with 0
        try {
            if (conn == null)        setConnection();

            Statement statement = FirstFragment.conn.createStatement();
            //get last id
            ResultSet res = statement.executeQuery("SELECT MAX(id) AS lastId\n" + "FROM matches");
            if (res.next()){
                id = res.getInt("lastId") + 1;
            }

            @SuppressLint("DefaultLocale") String query = "INSERT INTO matches\n" + String.format("VALUES (%d, %s, %s, %s, %s, %s, %s, %s, %s, %s);",
                    id, binding.matchDifEditText.getText(), binding.RGoalsEditText.getText(), binding.LGoalsEditText.getText(),
                    binding.HGoalsEditText.getText(), binding.AssistsEditText.getText(), binding.RPenaltiesMadeEditText.getText(),
                    binding.LPenaltiesMadeEditText.getText(), binding.RPenaltiesMissedEditText.getText(), binding.LPenaltiesMissedEditText.getText());

            statement.executeUpdate(query);
            statement.close();
            System.out.println("Match added successfully");
        } catch (SQLException fail){
            System.out.println(fail.getMessage());
        }
    }

    private void setConnection(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://204.2.195.209:425/TindeRegel", "admin","TdGyN3vs");
        } catch (SQLException connectFail){
//            Toast.makeText(getApplicationContext(),"Hello Javatpoint",Toast.LENGTH_SHORT).show();
            System.out.println("CUSOMOARS!!!!!!!!!!!!!!!!! "+connectFail.getMessage());
        } catch (ClassNotFoundException driverEx){
            System.out.println("\n\n\n\n\n\n\n\n\n\n\nfailed to find driver\n\n\n\n\n\n\n\n\n\n\n\n");
        }
    }
    private void closeConn(){
        try{
            conn.close();
        } catch (SQLException ignored){

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        closeConn();
    }

}