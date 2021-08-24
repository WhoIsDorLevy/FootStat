package com.example.statsapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.statsapp.databinding.FragmentShowStatsContBinding;

import java.util.Locale;

import static com.example.statsapp.AppDB.*;


public class ShowStatsContFragment extends Fragment {

    private FragmentShowStatsContBinding binding;
    private boolean[] checks;
    private String dateFrom;
    private String dateTo;
    private SQLiteDatabase db;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        db = AppDB.getInstance(getActivity()).getDatabase(false);
        binding = FragmentShowStatsContBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        checks = ShowStatsContFragmentArgs.fromBundle(getArguments()).getChecks();
        dateFrom = ShowStatsContFragmentArgs.fromBundle(getArguments()).getDateFrom();
        dateTo = ShowStatsContFragmentArgs.fromBundle(getArguments()).getDateTo();
        getAndFillStatsToTable(false);
        filterTable();
        binding.switch2.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                binding.switch2.setText(R.string.show_averages);
            }
            else {
                binding.switch2.setText(R.string.show_totals);
            }
            getAndFillStatsToTable(b);
        });

        binding.buttonHome.setOnClickListener(view1 ->
                NavHostFragment.findNavController(ShowStatsContFragment.this)
                .navigate(R.id.action_showStatsContFragment_to_FirstFragment));
    }


    private void filterTable(){
        if (filterFirstTable(checks[0], checks[2], checks[6])) {
            filterMatchDif(checks[5]);
            filterNumOfMatches(checks[6]);
            filterGoals(checks[0], checks[1]);
            filterAssists(checks[2]);
        }
        filterPenalties(checks[3], checks[4]);
    }

    private boolean filterFirstTable(boolean goals, boolean assists, boolean numOfMatches){
        boolean result = goals | assists | numOfMatches;
        if (!result){
            deleteView(binding.tableLayout);
        }
        return result;
    }

    private void filterMatchDif(boolean check){
        if (!check){
            for (int i = 3; i < 8; i++){
                binding.tableLayout.setColumnStretchable(i, false);
            }
            deleteView(binding.Tab1);
            deleteView(binding.NumOfMatches1);
            deleteView(binding.TabRG1);
            deleteView(binding.TabLG1);
            deleteView(binding.TabHG1);
            deleteView(binding.TabAs1);
            deleteView(binding.Tab2);
            deleteView(binding.NumOfMatches2);
            deleteView(binding.TabRG2);
            deleteView(binding.TabLG2);
            deleteView(binding.TabHG2);
            deleteView(binding.TabAs2);
            deleteView(binding.Tab3);
            deleteView(binding.NumOfMatches3);
            deleteView(binding.TabRG3);
            deleteView(binding.TabLG3);
            deleteView(binding.TabHG3);
            deleteView(binding.TabAs3);
            deleteView(binding.Tab4);
            deleteView(binding.NumOfMatches4);
            deleteView(binding.TabRG4);
            deleteView(binding.TabLG4);
            deleteView(binding.TabHG4);
            deleteView(binding.TabAs4);
            deleteView(binding.Tab5);
            deleteView(binding.NumOfMatches5);
            deleteView(binding.TabRG5);
            deleteView(binding.TabLG5);
            deleteView(binding.TabHG5);
            deleteView(binding.TabAs5);
            binding.Tab1.setText(R.string.total);
            binding.TabMatchDif.setText("");
        }
    }

    private void filterNumOfMatches(boolean check){
        if (!check){
            deleteView(binding.numOfMatchesRow);
        }
    }

    private void filterGoals(boolean check, boolean sort){
        if (!check){
            deleteView(binding.GoalsRow1);
            deleteView(binding.GoalsRow2);
            deleteView(binding.GoalsRow3);
        }
        else if (!sort){
            binding.TabLG.setText(R.string.goals);
            deleteView(binding.GoalsRow1);
            deleteView(binding.GoalsRow3);
        }
    }

    private void filterAssists(boolean check){
        if (!check)
            deleteView(binding.AssistsRow);
    }

    private void filterPenalties(boolean check, boolean sort){
        if (!check){
            deleteView(binding.penaltiesTable);
        }
        else if (!sort){
            deleteView(binding.PenTabLeftRow);
            binding.TabPenRight.setText(R.string.penalties);
            binding.TabHeadPen.setText("");
        }
    }

    private void deleteView(View view){
        ((ViewGroup) view.getParent()).removeView(view);
    }

    private void getAndFillStatsToTable(boolean averages){
        if (checks[6]){//num of matches
            getAndFillNumOfMatches(averages);
        }
        if (checks[0]){//goals
            getAndFillGoals(averages);
        }
        if (checks[2]){//assists
            getAndFillAssists(averages);
        }
        if (checks[3]){//penalties
            getAndFillPenalties(averages);
        }
    }

    private void getAndFillNumOfMatches(boolean averages){
        int totalNumOfMatches = getNumOfMatches(6);
        fillNumOfMatches(averages, binding.NumOfMatchesT, totalNumOfMatches, totalNumOfMatches);
        if (checks[5]){
            fillNumOfMatches(averages, binding.NumOfMatches1, getNumOfMatches(1), totalNumOfMatches);
            fillNumOfMatches(averages, binding.NumOfMatches2, getNumOfMatches(2), totalNumOfMatches);
            fillNumOfMatches(averages, binding.NumOfMatches3, getNumOfMatches(3), totalNumOfMatches);
            fillNumOfMatches(averages, binding.NumOfMatches4, getNumOfMatches(4), totalNumOfMatches);
            fillNumOfMatches(averages, binding.NumOfMatches5, getNumOfMatches(5), totalNumOfMatches);
        }
    }

    private void fillNumOfMatches(boolean averages, TextView view, int value, int total){
        if (averages){
            String toFill = String.format(Locale.US, "%.1f", Helpers.calculatePercent(value,total)) + "%";
            view.setText(toFill);
        }
        else {
            view.setText(getString(R.string.int_holder, value));
        }
    }

    private int getNumOfMatches(int matchDif){
        String selection = (matchDif == 6) ? null : MATCH_DIFFICULTY + " = " + matchDif;
        Cursor cursor = db.query(
                TABLE_NAME,
                new String[]{"*"},
                selection,
                null,
                null,
                null,
                null
        );
        int output = cursor.getCount();
        cursor.close();
        return output;
    }


    private void getAndFillGoals(boolean averages){

        if (checks[1]) {//sort by foot
            getAndFillFirstTableRow(averages, binding.GoalsRow1, R_GOALS);
            getAndFillFirstTableRow(averages, binding.GoalsRow2, L_GOALS);
            getAndFillFirstTableRow(averages, binding.GoalsRow3, H_GOALS);

        }
        else {//don't sort by foot
            getAndFillFirstTableRow(averages, binding.GoalsRow2, T_GOALS);
        }

    }
    private void getAndFillAssists(boolean averages){
        getAndFillFirstTableRow(averages, binding.AssistsRow, ASSISTS);
    }

    private void getAndFillPenalties(boolean averages){
        if (checks[4]){//sort by side
            if (averages){
                getAndFillPenaltyAvg(binding.PenTabRightRow, R_PENALTIES_MADE, R_PENALTIES_MISSED);
                getAndFillPenaltyAvg(binding.PenTabLeftRow, L_PENALTIES_MADE, L_PENALTIES_MISSED);
            }
            else {
                getAndFillStat(false, R_PENALTIES_MADE, 6, binding.TabRPMd);
                getAndFillStat(false, R_PENALTIES_MISSED, 6, binding.TabRPMs);
                getAndFillStat(false, L_PENALTIES_MADE, 6, binding.TabLPMd);
                getAndFillStat(false, L_PENALTIES_MISSED, 6, binding.TabLPMs);
            }
        }
        else {//don't sort by side
            if (averages){
                getAndFillPenaltyAvg(binding.PenTabRightRow, T_PENALTIES_MADE, T_PENALTIES_MISSED);
            }
            else {
                getAndFillStat(false, T_PENALTIES_MADE, 6, binding.TabRPMd);
                getAndFillStat(false, T_PENALTIES_MISSED, 6, binding.TabRPMs);
            }
        }
    }

    private void getAndFillFirstTableRow(boolean averages, TableRow row, String stat){
        int numOfChildren = row.getChildCount();//num of cols
        if (checks[5]) {//sort by matchDif
            for (int i = 1; i < numOfChildren; i++) {//first col is headline, skip it
                TextView view = (TextView) row.getChildAt(i);
                getAndFillStat(averages, stat, i, view);
            }
        }
        else {//don't sort by matchDif
            TextView view = (TextView) row.getChildAt(numOfChildren - 1);
            getAndFillStat(averages, stat, 6, view);
        }
    }



    private void getAndFillStat(boolean averages, String stat, int matchDif, TextView view){
        if (averages){
            fillAvg(view, getAvgOf(stat, matchDif));
        }
        else {
            fillSum(view, getSumOf(stat, matchDif));
        }
    }

    private int getSumOf(String stat, int matchDif){
        boolean totalGoals = stat.equals(T_GOALS);
        boolean totalPenaltiesMade = stat.equals(T_PENALTIES_MADE);
        boolean totalPenaltiesMissed = stat.equals(T_PENALTIES_MISSED);

        String toWrap = totalGoals ? "SUM(" + R_GOALS + " + " + L_GOALS + " + " + H_GOALS + ")" :
                totalPenaltiesMade ? "SUM(" + R_PENALTIES_MADE + " + " + L_PENALTIES_MADE + ")" :
                        totalPenaltiesMissed ? "SUM(" + R_PENALTIES_MISSED + " + " + L_PENALTIES_MISSED + ")" :
                                "SUM(" + stat + ")";

        String[] projection = {toWrap};
        String selection = DATE + " >= " + dateFrom + " and " + DATE + " <= " + dateTo;
        if (matchDif < 6){
            selection += " and " + MATCH_DIFFICULTY + " = " + matchDif;
        }

        Cursor cursor = db.query(
                TABLE_NAME,
                projection,
                selection,
                null,
                null,
                null,
                null);

        cursor.moveToNext();
        int output = cursor.getInt(0);
        cursor.close();
        return output;
    }

    private double getAvgOf(String stat, int matchDif){
        boolean totalGoals = stat.equals(T_GOALS);
        boolean totalPenaltiesMade = stat.equals(T_PENALTIES_MADE);
        boolean totalPenaltiesMissed = stat.equals(T_PENALTIES_MISSED);

        String toWrap = totalGoals ? "AVG(" + R_GOALS + " + " + L_GOALS + " + " + H_GOALS + ")" :
                totalPenaltiesMade ? "AVG(" + R_PENALTIES_MADE + " + " + L_PENALTIES_MADE + ")" :
                        totalPenaltiesMissed ? "AVG(" + R_PENALTIES_MISSED + " + " + L_PENALTIES_MISSED + ")" :
                                "AVG(" + stat + ")";

        String[] projection = {toWrap};
        String selection = DATE + " >= " + dateFrom + " and " + DATE + " <= " + dateTo;
        if (matchDif < 6){
            selection += " and " + MATCH_DIFFICULTY + " = " + matchDif;
        }

        Cursor cursor = db.query(
                TABLE_NAME,
                projection,
                selection,
                null,
                null,
                null,
                null);

        cursor.moveToNext();
        double output = cursor.getDouble(0);
        cursor.close();
        return output;
    }

    private void fillSum(TextView view, int stat){
        view.setText(getString(R.string.int_holder, stat));
    }

    private void fillAvg(TextView view, double stat){
        view.setText(getString(R.string.float_holder, stat));
    }

    private void getAndFillPenaltyAvg(TableRow row, String made, String missed){
        fillPenaltyAvg(row, getPenaltyAvg(made, missed));
    }

    //returns value of made penalties only
    private double getPenaltyAvg(String made, String missed){
        int sumMade = getSumOf(made, 6);
        int sumMissed = getSumOf(missed, 6);
        return Helpers.calculatePercent(sumMade, sumMade+sumMissed);
    }
    private void fillPenaltyAvg(TableRow row, double value){
        String formatPercent = "%.1f";
        String madePercent = String.format(Locale.US,formatPercent, value) + "%";
        String missedPercent = String.format(Locale.US,formatPercent, 100.0 - value) + "%";
        TextView madeTextView = (TextView) row.getChildAt(1);
        TextView missedTextView = (TextView) row.getChildAt(2);
        madeTextView.setText(madePercent);
        missedTextView.setText(missedPercent);

    }


}