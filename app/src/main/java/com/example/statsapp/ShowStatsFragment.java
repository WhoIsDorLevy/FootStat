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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import androidx.appcompat.widget.Toolbar;

import com.example.statsapp.databinding.FragmentFirstBinding;
import com.example.statsapp.databinding.FragmentShowStatsBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ShowStatsFragment extends Fragment {

    private FragmentShowStatsBinding binding;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentShowStatsBinding.inflate(inflater, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.show_stats_fragment_label);
        return binding.getRoot();
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonShowChosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox[] checkBoxes = new CheckBox[]{binding.GoalsCheckBox, binding.SortByFootCheckBox,
                                                        binding.assistsCheckBox, binding.PenaltiesCheckBox,
                                                        binding.SortBySideCheckBox, binding.matchDifCheckBox};
                Boolean[] boxedArr = Helpers.map(checkBoxes, CompoundButton::isChecked, () -> new Boolean[0]);
                boolean[] checksPrimitive = Helpers.toPrimitiveBoolean(boxedArr);

                ShowStatsFragmentDirections.ActionShowStatsFragmentToShowStatsContFragment action =
                        ShowStatsFragmentDirections.actionShowStatsFragmentToShowStatsContFragment(checksPrimitive);
                NavHostFragment.findNavController(ShowStatsFragment.this)
                        .navigate(action);
            }
        });
        binding.GoalsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                binding.SortByFootCheckBox.setEnabled(b);
                if (!b) {
                    binding.SortByFootCheckBox.setChecked(false);
                }
                updateMinButtons(b);
            }
        });
        binding.assistsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateMinButtons(b);
            }
        });
        binding.PenaltiesCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                binding.SortBySideCheckBox.setEnabled(b);
                if (!b) {
                    binding.SortBySideCheckBox.setChecked(false);
                }
                updateMinButtons(b);
            }
        });
    }

    //update buttons that require at least one category to be enabled
    private void updateMinButtons(boolean b){
        if (b != binding.buttonShowChosen.isEnabled()) {

            if (!binding.GoalsCheckBox.isChecked() &&
                    !binding.assistsCheckBox.isChecked() &&
                    !binding.PenaltiesCheckBox.isChecked()) {

                binding.buttonShowChosen.setEnabled(false);
                binding.matchDifCheckBox.setEnabled(false);
                binding.matchDifCheckBox.setChecked(false);
            }

            else if (!binding.buttonShowChosen.isEnabled()) {
                binding.buttonShowChosen.setEnabled(true);
                binding.matchDifCheckBox.setEnabled(true);
            }

        }
    }
}