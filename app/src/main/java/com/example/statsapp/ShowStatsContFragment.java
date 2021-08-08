package com.example.statsapp;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.appcompat.widget.Toolbar;

import com.example.statsapp.databinding.FragmentShowStatsBinding;
import com.example.statsapp.databinding.FragmentShowStatsContBinding;


public class ShowStatsContFragment extends Fragment {

    private FragmentShowStatsContBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentShowStatsContBinding.inflate(inflater, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.show_stats_fragment_label);
        return binding.getRoot();
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        boolean[] checks = ShowStatsContFragmentArgs.fromBundle(getArguments()).getChecks();
        binding.switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    binding.switch2.setText(R.string.show_averages);
                }
                else {
                    binding.switch2.setText(R.string.show_totals);
                }
            }
        });
        binding.buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ShowStatsContFragment.this)
                        .navigate(R.id.action_showStatsContFragment_to_FirstFragment);
            }
        });
    }
}