package com.example.statsapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.example.statsapp.databinding.FragmentInsertMatchBinding;

import java.util.Calendar;


public class insertMatchFragment extends Fragment {

    private FragmentInsertMatchBinding binding;
    private SQLiteDatabase db;
    private String matchDifVal, rGoalsVal, lGoalsVal, hGoalsVal, assistsVal,
            rPenaltiesMadeVal, lPenaltiesMadeVal, rPenaltiesMissedVal, lPenaltiesMissedVal;
    private EditText[] editTexts;
    private boolean[] isValid;
    private String date;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        db = AppDB.getInstance(getActivity()).getDatabase(true);
        date = null;
        binding = FragmentInsertMatchBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Activity activity = getActivity();
        binding.linearLay.setOnTouchListener((view1, motionEvent) -> hideKeyboard(activity) );
        binding.SelectDateTextView.setOnClickListener(this::showDatePickerDialog);
        binding.buttonEnter.setOnClickListener(view12 -> {
            if (date == null){
                Toast.makeText(activity, R.string.match_added, Toast.LENGTH_LONG).show();
            }
            else {
                getInputs();
                uploadStats();
                Toast.makeText(activity, R.string.match_added, Toast.LENGTH_LONG).show();
                NavHostFragment.findNavController(insertMatchFragment.this)
                        .navigate(R.id.action_insertMatchFragment_to_FirstFragment);
            }
        });

        setEditTexts();



    }

    private void setEditTexts(){
        isValid = new boolean[9];

        editTexts = new EditText[]{binding.matchDifEditText, binding.RGoalsEditText,
                binding.LGoalsEditText, binding.HGoalsEditText, binding.AssistsEditText,
                binding.RPenaltiesMadeEditText, binding.LPenaltiesMadeEditText,
                binding.RPenaltiesMissedEditText, binding.LPenaltiesMissedEditText};

        binding.matchDifEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                String input = binding.matchDifEditText.getText().toString();
                if (checkValidNumber(input) && checkNumberInRange(input)){
                    isValid[0] = true;
                }
                else {
                    binding.matchDifEditText.setError("Must be a valid number between 1-5");
                    isValid[0] = false;
                }
                checkValidity();
            }
        });

        for (int ind = 1; ind < editTexts.length; ind++){
            int finalInd = ind;
            isValid[ind] = true;
            editTexts[ind].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                @Override
                public void afterTextChanged(Editable editable) {
                    String input = editTexts[finalInd].getText().toString();
                    if (!checkValidNumber(input)){
                        editTexts[finalInd].setError("Must be a valid and positive number, or empty");
                        isValid[finalInd] = false;
                    }
                    else {
                        isValid[finalInd] = true;
                    }
                    checkValidity();
                }
            });
        }
    }

    private void checkValidity(){
        boolean toEnable = date != null;
        for (boolean valid : isValid){
            toEnable &= valid;
        }
        binding.buttonEnter.setEnabled(toEnable);
    }

    private boolean checkNumberInRange(String str){
        int num = (str.isEmpty()) ? 0 : Integer.parseInt(str);
        return 1 <= num & num <= 5;
    }

    private boolean checkValidNumber(String str){
        if (str.isEmpty()){
            return true;
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
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



    }

    private void uploadStats(){
        ContentValues values = new ContentValues();
        values.put(AppDB.DATE, date);
        values.put(AppDB.MATCH_DIFFICULTY, matchDifVal);
        values.put(AppDB.R_GOALS, rGoalsVal);
        values.put(AppDB.L_GOALS, lGoalsVal);
        values.put(AppDB.H_GOALS, hGoalsVal);
        values.put(AppDB.ASSISTS, assistsVal);
        values.put(AppDB.R_PENALTIES_MADE, rPenaltiesMadeVal);
        values.put(AppDB.L_PENALTIES_MADE, lPenaltiesMadeVal);
        values.put(AppDB.R_PENALTIES_MISSED, rPenaltiesMissedVal);
        values.put(AppDB.L_PENALTIES_MISSED, lPenaltiesMissedVal);

        db.insert(AppDB.MATCHES_TABLE_NAME, null, values);



    }

    private void showDatePickerDialog(View view){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                this::onDateSet,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        month++;//Jenuary is 0
        date = getString(R.string.date_holder,dayOfMonth,month,year);
        binding.SelectDateTextView.setText(date);
    }

    private boolean hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        return false;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        db.close();
    }



}