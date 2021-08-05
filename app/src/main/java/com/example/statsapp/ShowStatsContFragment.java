package com.example.statsapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.statsapp.databinding.FragmentShowStatsBinding;
import com.example.statsapp.databinding.FragmentShowStatsContBinding;


public class ShowStatsContFragment extends Fragment {

    private FragmentShowStatsContBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentShowStatsContBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ShowStatsContFragment.this)
                        .navigate(R.id.action_showStatsContFragment_to_FirstFragment);
            }
        });
    }
}