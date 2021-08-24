package com.example.statsapp;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.statsapp.databinding.FragmentFirstBinding;


public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(view1 ->
                NavHostFragment.findNavController(FirstFragment.this)
                .navigate(R.id.action_FirstFragment_to_insertMatchFragment));
        binding.buttonShowStats.setOnClickListener(view1 ->
                NavHostFragment.findNavController(FirstFragment.this)
                .navigate(R.id.action_FirstFragment_to_ShowStatsFragment));
        binding.buttonListOfMatches.setOnClickListener(view12 ->
                NavHostFragment.findNavController(FirstFragment.this)
                .navigate(R.id.action_FirstFragment_to_listOfMatchesFragment));
        binding.buttonExit.setOnClickListener(view1 -> {
            getActivity().finish();
            System.exit(0);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}