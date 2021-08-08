package com.example.statsapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.statsapp.databinding.FragmentFirstBinding;

import java.sql.*;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    protected static Connection conn;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
//        setConnection();
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }
//    private static void setConnection(){
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            conn = DriverManager.getConnection("jdbc:mysql://204.2.195.209:425/TindeRegel", "WhoIsDor","humUuw8i!");
//        } catch (SQLException connectFail){
////            Toast.makeText(getApplicationContext(),"Hello Javatpoint",Toast.LENGTH_SHORT).show();
//            System.out.println("failed to create connection");
//        } catch (ClassNotFoundException driverEx){
//            System.out.println("\n\n\n\n\n\n\n\n\n\n\nfailed to find driver\n\n\n\n\n\n\n\n\n\n\n\n");
//        }
//    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
        binding.buttonShowStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_ShowStatsFragment);
            }
        });

        binding.buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
                System.exit(0);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}