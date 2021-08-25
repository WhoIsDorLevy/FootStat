package com.example.statsapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;

import com.example.statsapp.databinding.FragmentListOfMatchesBinding;

import java.util.HashMap;
import java.util.Set;

import static com.example.statsapp.AppDB.*;

public class ListOfMatchesFragment extends Fragment {
    FragmentListOfMatchesBinding binding;
    Activity activity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentListOfMatchesBinding.inflate(inflater, container, false);
        activity = getActivity();
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        return binding.getRoot();

    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        View header = getLayoutInflater().inflate(R.layout.list_header, null);
        binding.listView.addHeaderView(header);
        updateList();
        binding.button.setOnClickListener(this::createAlertDialog);
    }

    private void updateList(){
        SQLiteDatabase readDB = AppDB.getInstance(activity).getDatabase(false);
        Cursor cursor = readDB.rawQuery("SELECT * FROM " + TABLE_NAME + ";", null);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(activity,
                R.layout.list_item,
                cursor,
                new String[]{COLUMN_ID, DATE, MATCH_DIFFICULTY,
                        R_GOALS,L_GOALS,H_GOALS,ASSISTS,
                        R_PENALTIES_MADE,L_PENALTIES_MADE,
                        R_PENALTIES_MISSED,L_PENALTIES_MISSED},
                new int[]{R.id.id_list, R.id.dateList, R.id.matchDifList,
                        R.id.r_goals_list,R.id.l_goals_list,R.id.h_goals_list,
                        R.id.assists_list,R.id.r_penalties_made_list,
                        R.id.l_penalties_made_list,R.id.r_penalties_missed_list,
                        R.id.l_penalties_missed_list},
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        );
        int dateColIndex = cursor.getColumnIndex(DATE);
        adapter.setViewBinder((view1, cursor1, colIndex) -> {
            if (colIndex == dateColIndex){
                String date = setFormatDate("" + cursor1.getInt(dateColIndex));
                ((TextView) view1).setText(date);
                return true;
            }
            return false;
        });

        binding.listView.setAdapter(adapter);
        binding.listView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                binding.listView.removeOnLayoutChangeListener(this);
                setCheckBoxes();
            }
        });
    }

    private String setFormatDate(String date){
        String year = date.substring(2,4);
        String month = date.substring(4,6);
        String day = date.substring(6);
        return day + '/' + month + '/' + year;
    }

    private void setDeleteButton(){
        HashMap<CheckBox, Integer> checkBoxesWithId = getCheckBoxes();
        CheckBox[] checkBoxes = checkBoxesWithId.keySet().toArray(new CheckBox[0]);
        Integer[] idList = Helpers.map(
                Helpers.filter(checkBoxes, CompoundButton::isChecked, ()->new CheckBox[0]),

                checkBoxesWithId::get,

                ()->new Integer[0]);

        performDelete(idList);
        updateList();
        Toast.makeText(activity, "Match(s) deleted successfully", Toast.LENGTH_LONG).show();
    }

    private void performDelete(Integer[] idList){
        SQLiteDatabase writeDB = AppDB.getInstance(activity).getDatabase(true);
        StringBuilder toDelete = new StringBuilder(COLUMN_ID + "=" + idList[0]);
        for (int i = 1; i < idList.length; i++){
            toDelete.append(" OR ").append(COLUMN_ID).append("=").append(idList[i]);
        }
        writeDB.delete(TABLE_NAME, toDelete.toString(), null);
        writeDB.close();
    }



    private HashMap<CheckBox, Integer> getCheckBoxes(){//{checkBox, id}
        HashMap<CheckBox, Integer> output = new HashMap<>();
        ListView list = binding.listView;
        int firstRow = list.getFirstVisiblePosition();
        int numOfRows = list.getChildCount();
        for (int i = firstRow+1; i < numOfRows; i++){
            View row = list.getChildAt(i);
            CheckBox checkBox = row.findViewById(R.id.checkbox_list);
            TextView idTextView = row.findViewById(R.id.id_list);
            Integer idVal = Integer.parseInt(idTextView.getText().toString());
            output.put(checkBox, idVal);
        }
        return output;
    }

    private void setCheckBoxes(){
        Set<CheckBox> checkBoxes = getCheckBoxes().keySet();
        for (CheckBox checkBox : checkBoxes){
            checkBox.setOnCheckedChangeListener((compoundButton,b) -> {
                if (b){
                    binding.button.setEnabled(true);
                }
                else {
                    checkButtonEnabled(checkBoxes);
                }
            });
        }

    }

    private void checkButtonEnabled(Set<CheckBox> checkBoxes){
        boolean enabled = false;
        for (CheckBox checkBox : checkBoxes){
            if (checkBox.isChecked()){
                enabled = true;
                break;
            }
        }
        binding.button.setEnabled(enabled);
    }

    private void createAlertDialog(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure?");
        builder.setPositiveButton("YES", (dialog, which) -> {
            dialog.dismiss();
            setDeleteButton();
            });
        builder.setNegativeButton("NO", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }
}