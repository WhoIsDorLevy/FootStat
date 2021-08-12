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


import com.example.statsapp.databinding.FragmentShowStatsBinding;



public class ShowStatsFragment extends Fragment {

    private FragmentShowStatsBinding binding;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentShowStatsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonShowChosen.setOnClickListener(view1 -> {
            CheckBox[] checkBoxes = new CheckBox[]{binding.GoalsCheckBox, binding.SortByFootCheckBox,
                                                    binding.assistsCheckBox, binding.PenaltiesCheckBox,
                                                    binding.SortBySideCheckBox, binding.matchDifCheckBox};
            Boolean[] boxedArr = Helpers.map(checkBoxes, CompoundButton::isChecked, () -> new Boolean[0]);
            boolean[] checksPrimitive = Helpers.toPrimitiveBoolean(boxedArr);

            ShowStatsFragmentDirections.ActionShowStatsFragmentToShowStatsContFragment action =
                    ShowStatsFragmentDirections.actionShowStatsFragmentToShowStatsContFragment(checksPrimitive);
            NavHostFragment.findNavController(ShowStatsFragment.this)
                    .navigate(action);
        });

        binding.GoalsCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            binding.SortByFootCheckBox.setEnabled(b);
            if (!b) {
                binding.SortByFootCheckBox.setChecked(false);
            }
            updateMatchDif(b);
            updateShowChosen(b);
        });

        binding.assistsCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            updateMatchDif(b);
            updateShowChosen(b);
        });

        binding.PenaltiesCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            binding.SortBySideCheckBox.setEnabled(b);
            if (!b) {
                binding.SortBySideCheckBox.setChecked(false);
            }
            updateShowChosen(b);
        });
    }

    //update buttons that require at least one category to be enabled
    private void updateMatchDif(boolean b){
        if (b != binding.matchDifCheckBox.isEnabled()) {

            if (!binding.GoalsCheckBox.isChecked() &&
                    !binding.assistsCheckBox.isChecked()) {

                binding.matchDifCheckBox.setEnabled(false);
                binding.matchDifCheckBox.setChecked(false);
            }

            else if (!binding.matchDifCheckBox.isEnabled()) {
                binding.matchDifCheckBox.setEnabled(true);
            }

        }
    }

    private void updateShowChosen(boolean b){
        if (b != binding.buttonShowChosen.isEnabled()) {

            if (!binding.GoalsCheckBox.isChecked() &&
                    !binding.assistsCheckBox.isChecked() &&
                    !binding.PenaltiesCheckBox.isChecked()) {

                binding.buttonShowChosen.setEnabled(false);
            }

            else if (!binding.buttonShowChosen.isEnabled()) {
                binding.buttonShowChosen.setEnabled(true);
            }

        }
    }
}