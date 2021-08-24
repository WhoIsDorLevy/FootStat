package com.example.statsapp;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
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



import com.example.statsapp.databinding.FragmentShowStatsBinding;

import java.util.Calendar;


public class ShowStatsFragment extends Fragment {

    private FragmentShowStatsBinding binding;
    private String dateFrom, dateTo; //yyyyMMdd
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dateFrom = "19000101";
        dateTo = "21001231";
        binding = FragmentShowStatsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.checkBoxFrom.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                showDatePickerDialog(binding.checkBoxFrom, (datePicker, year, month, day) -> {
                    dateFrom = getString(R.string.date_holder, year,month+1,day);
                    binding.textViewDateFrom.setText(getString(R.string.date_hint_holder, day, month+1, year));

                });
            }
            else {
                dateFrom = "19000101";
                binding.textViewDateFrom.setText(R.string.date_hint);
            }
        });
        binding.checkBoxTo.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                showDatePickerDialog(binding.checkBoxTo, (datePicker, year, month, day) -> {
                    dateTo = getString(R.string.date_holder, year,month+1,day);
                    binding.textViewDateTo.setText(getString(R.string.date_hint_holder, day, month+1, year));
                });
            }
            else {
                dateTo = "21001231";
                binding.textViewDateTo.setText(R.string.date_hint);
            }
        });

        binding.buttonShowChosen.setOnClickListener(view1 -> {
            CheckBox[] checkBoxes = new CheckBox[]{binding.GoalsCheckBox, binding.SortByFootCheckBox,
                                                    binding.assistsCheckBox, binding.PenaltiesCheckBox,
                                                    binding.SortBySideCheckBox, binding.matchDifCheckBox,
                                                    binding.NumOfMatchesCheckBox};
            Boolean[] boxedArr = Helpers.map(checkBoxes, CompoundButton::isChecked, () -> new Boolean[0]);
            boolean[] checksPrimitive = Helpers.toPrimitiveBoolean(boxedArr);

            ShowStatsFragmentDirections.ActionShowStatsFragmentToShowStatsContFragment action =
                    ShowStatsFragmentDirections.actionShowStatsFragmentToShowStatsContFragment(checksPrimitive, dateFrom, dateTo);
            NavHostFragment.findNavController(ShowStatsFragment.this)
                    .navigate(action);
            resetChecksDefault();
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

    private void resetChecksDefault(){
        binding.GoalsCheckBox.setChecked(false);
        binding.SortByFootCheckBox.setChecked(false);
        binding.assistsCheckBox.setChecked(false);
        binding.PenaltiesCheckBox.setChecked(false);
        binding.SortBySideCheckBox.setChecked(false);
        binding.checkBoxFrom.setChecked(false);
        binding.checkBoxTo.setChecked(false);
        binding.NumOfMatchesCheckBox.setChecked(false);
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

    private void showDatePickerDialog(CheckBox box ,DatePickerDialog.OnDateSetListener listener){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                listener,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel),
                (dialogInterface, i) -> box.setChecked(false));

        datePickerDialog.show();
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