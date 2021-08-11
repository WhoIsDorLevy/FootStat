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
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.statsapp.databinding.FragmentShowStatsContBinding;

import static com.example.statsapp.AppDB.*;


public class ShowStatsContFragment extends Fragment {

    private FragmentShowStatsContBinding binding;
    private boolean[] checks;
    private SQLiteDatabase db;
    private int[][] statsTotSorted;
    private double[][] statsAvgSorted;
    private int[] statsTotUnsorted;
    private double[] statsAvgUnsorted;
    private int[] totalGoals;
    private int totalMadePens;
    private int totalMissedPens;
    private int totalNumOfMatches;
    private final int numOfMatchDifs = 5;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        MainActivity activity = (MainActivity) getActivity();
        setDB(activity);
        binding = FragmentShowStatsContBinding.inflate(inflater, container, false);
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.show_stats_fragment_label);
        return binding.getRoot();
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checks = ShowStatsContFragmentArgs.fromBundle(getArguments()).getChecks();
        statsTotSorted = new int[numOfMatchDifs][AppDB.NUM_OF_COLS ];//each table sorted by matchDif
        statsAvgSorted = new double[numOfMatchDifs][AppDB.NUM_OF_COLS];
        statsTotUnsorted = new int[AppDB.NUM_OF_COLS];
        statsAvgUnsorted = new double[AppDB.NUM_OF_COLS];
        setStats();
        fillStatsToTable(false);
        filterTable();
        binding.switch2.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                binding.switch2.setText(R.string.show_averages);
            }
            else {
                binding.switch2.setText(R.string.show_totals);
            }
            fillStatsToTable(b);
        });

        binding.buttonHome.setOnClickListener(view1 ->
                NavHostFragment.findNavController(ShowStatsContFragment.this)
                .navigate(R.id.action_showStatsContFragment_to_FirstFragment));
    }

//    private void insertNewLine(TextView view){
//        String text = view.getText().toString();
//        view.setText(text.replace("\\\n"));
//    }

    private void setDB(MainActivity activity){
        db = activity.getDb().getReadableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS " + MATCHES_TABLE_NAME + " (\n" +
                COLUMN_ID + " integer PRIMARY KEY AUTOINCREMENT,\n" +
                MATCH_DIFFICULTY_TABLE_NAME +" INTEGER NOT NULL,\n" +
                R_GOALS_TABLE_NAME + " integer NOT NULL,\n" +
                L_GOALS_TABLE_NAME + " integer NOT NULL,\n" +
                H_GOALS_TABLE_NAME + " integer NOT NULL,\n" +
                ASSISTS_TABLE_NAME + " integer NOT NULL,\n" +
                R_PENALTIES_MADE_TABLE_NAME + " integer NOT NULL,\n" +
                L_PENALTIES_MADE_TABLE_NAME + " integer NOT NULL,\n" +
                R_PENALTIES_MISSED_TABLE_NAME + " integer NOT NULL,\n" +
                L_PENALTIES_MISSED_TABLE_NAME + " integer NOT NULL" +
                ");");
    }

    private void filterTable(){
        if (filterFirstTable(checks[0], checks[2])) {
            filterMatchDif(checks[5]);
            filterGoals(checks[0], checks[1]);
            filterAssists(checks[2]);
        }
        filterPenalties(checks[3], checks[4]);
    }

    private boolean filterFirstTable(boolean goals, boolean assists){
        boolean result = goals | assists;
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
            deleteView(binding.TabRG1);
            deleteView(binding.TabLG1);
            deleteView(binding.TabHG1);
            deleteView(binding.TabAs1);
            deleteView(binding.Tab2);
            deleteView(binding.TabRG2);
            deleteView(binding.TabLG2);
            deleteView(binding.TabHG2);
            deleteView(binding.tabAs2);
            deleteView(binding.Tab3);
            deleteView(binding.TabRG3);
            deleteView(binding.TabLG3);
            deleteView(binding.TabHG3);
            deleteView(binding.tabAs3);
            deleteView(binding.Tab4);
            deleteView(binding.TabRG4);
            deleteView(binding.TabLG4);
            deleteView(binding.TabHG4);
            deleteView(binding.tabAs4);
            deleteView(binding.Tab5);
            deleteView(binding.TabRG5);
            deleteView(binding.TabLG5);
            deleteView(binding.TabHG5);
            deleteView(binding.tabAs5);
            binding.Tab1.setText(R.string.total);
            binding.TabMatchDif.setText("");
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

    private void setStats(){
        String[] projection = {AppDB.MATCH_DIFFICULTY_TABLE_NAME, AppDB.R_GOALS_TABLE_NAME,
                AppDB.L_GOALS_TABLE_NAME,AppDB.H_GOALS_TABLE_NAME,AppDB.ASSISTS_TABLE_NAME,
                AppDB.R_PENALTIES_MADE_TABLE_NAME,AppDB.L_PENALTIES_MADE_TABLE_NAME,
                AppDB.R_PENALTIES_MISSED_TABLE_NAME,AppDB.L_PENALTIES_MISSED_TABLE_NAME};

        Cursor cursor = db.query(
                MATCHES_TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        //get totals sorted
        while (cursor.moveToNext()){
            int matchDifInd = cursor.getColumnIndex(AppDB.MATCH_DIFFICULTY_TABLE_NAME);
            int matchDif = cursor.getInt(matchDifInd);
            statsTotSorted[matchDif - 1][0]++;
            for (int i = 1; i < AppDB.NUM_OF_COLS; i++){
                statsTotSorted[matchDif - 1][i] += cursor.getInt(i);
            }
        }

        //calculate totals unsorted
        for (int i = 0; i < numOfMatchDifs; i++){
            for (int j = 0; j < AppDB.NUM_OF_COLS; j++) {
                statsTotUnsorted[j] += statsTotSorted[i][j];
            }
        }

        //calculate averages sorted
        for (int i = 0; i < statsAvgSorted.length; i++){
            statsAvgSorted[i][0] = performDivision(statsTotSorted[i][0], statsTotUnsorted[0]);
            for (int j = 1; j < AppDB.NUM_OF_COLS; j++){
                statsAvgSorted[i][j] = performDivision(statsTotSorted[i][j], statsTotSorted[i][0]);
            }
        }

        //calculate averages unsorted
        totalNumOfMatches = cursor.getCount();
        for (int i = 0; i < AppDB.NUM_OF_COLS; i++){
            statsAvgUnsorted[i] = performDivision(statsTotUnsorted[i], totalNumOfMatches);
        }

        //get total number of goals without sorting by foot
        totalGoals = new int[numOfMatchDifs + 1];//one for total's total
        for (int i = 0; i < numOfMatchDifs; i++){
            totalGoals[i] = statsTotSorted[i][1] + statsTotSorted[i][2] + statsTotSorted[i][3];
            totalGoals[5] += totalGoals[i];
        }

        //get total number of penalties without sorting by side
        totalMadePens = statsTotUnsorted[5] + statsTotUnsorted[6];
        totalMissedPens = statsTotUnsorted[7] + statsTotUnsorted[8];

        cursor.close();
    }
    
    private void fillStatsToTable(boolean averages){

        if (checks[0]) {
            fillGoals(averages);
        }

        if (checks[2]){
            fillAssists(averages);
        }

        if (checks[3]){
            fillPenalties(averages);
        }
    }

    private void fillGoals(boolean averages){
        boolean sortFoot = checks[1];
        boolean matchDif = checks[5];

        if (sortFoot) {
            //right goals row
            if (matchDif) {
                setSortedStatInPlace(averages, binding.TabRG1, 1, 1);
                setSortedStatInPlace(averages, binding.TabRG2, 2, 1);
                setSortedStatInPlace(averages, binding.TabRG3, 3, 1);
                setSortedStatInPlace(averages, binding.TabRG4, 4, 1);
                setSortedStatInPlace(averages, binding.TabRG5, 5, 1);
            }
            //total
            setUnsortedStatInPlace(averages, binding.TabRGT, 1);

            //left goals row
            if (matchDif) {
                setSortedStatInPlace(averages, binding.TabLG1, 1, 2);
                setSortedStatInPlace(averages, binding.TabLG2, 2, 2);
                setSortedStatInPlace(averages, binding.TabLG3, 3, 2);
                setSortedStatInPlace(averages, binding.TabLG4, 4, 2);
                setSortedStatInPlace(averages, binding.TabLG5, 5, 2);
            }
            //total
            setUnsortedStatInPlace(averages, binding.TabLGT, 2);

            //head goals row
            if (matchDif) {
                setSortedStatInPlace(averages, binding.TabHG1, 1, 3);
                setSortedStatInPlace(averages, binding.TabHG2, 2, 3);
                setSortedStatInPlace(averages, binding.TabHG3, 3, 3);
                setSortedStatInPlace(averages, binding.TabHG4, 4, 3);
                setSortedStatInPlace(averages, binding.TabHG5, 5, 3);
            }
            //total
            setUnsortedStatInPlace(averages,binding.TabHGT,3);
        }
        else {
            if (matchDif) {
                setTotalGoalsInPlace(averages, binding.TabLG1, 0);
                setTotalGoalsInPlace(averages, binding.TabLG2, 1);
                setTotalGoalsInPlace(averages, binding.TabLG3, 2);
                setTotalGoalsInPlace(averages, binding.TabLG4, 3);
                setTotalGoalsInPlace(averages, binding.TabLG5, 4);
            }
            //total
            setTotalGoalsInPlace(averages, binding.TabLGT, 5);
        }
    }

    private void fillAssists(boolean averages){
        boolean matchDif = checks[5];
        if (matchDif) {
            setSortedStatInPlace(averages, binding.TabAs1, 1, 4);
            setSortedStatInPlace(averages, binding.tabAs2, 2, 4);
            setSortedStatInPlace(averages, binding.tabAs3, 3, 4);
            setSortedStatInPlace(averages, binding.tabAs4, 4, 4);
            setSortedStatInPlace(averages, binding.tabAs5, 5, 4);
        }
        //total
        setUnsortedStatInPlace(averages,binding.TabAsT,4);
    }

    private void fillPenalties(boolean averages){
        if (checks[4]){//sort by side
            setUnsortedStatInPlace(averages,binding.TabRPMd,5);
            setUnsortedStatInPlace(averages,binding.TabLPMd,6);
            setUnsortedStatInPlace(averages,binding.TabRPMs,7);
            setUnsortedStatInPlace(averages,binding.TabLPMs,8);
        }
        else {
            setTotalPensInPlace(averages);
        }

    }

    private void setSortedStatInPlace(boolean averages, TextView view, int matchDif, int col){
        String text = (averages) ? getString(R.string.float_holder, statsAvgSorted[matchDif-1][col]) :
                getString(R.string.int_holder, statsTotSorted[matchDif-1][col]);
        view.setText(text);
    }

    private void setTotalGoalsInPlace(boolean averages, TextView view, int ind){
        String text = (averages) ? getString(R.string.float_holder, performDivision(totalGoals[ind],totalNumOfMatches)) :
                getString(R.string.int_holder, totalGoals[ind]);
        view.setText(text);
    }

    private void setTotalPensInPlace(boolean averages){
        String textMade = (averages) ? getString(R.string.float_holder, performDivision(totalMadePens, totalNumOfMatches)) :
                getString(R.string.int_holder, totalMadePens);
        String textMissed = (averages) ? getString(R.string.float_holder, performDivision(totalMissedPens, totalNumOfMatches)) :
                getString(R.string.int_holder, totalMissedPens);
        binding.TabRPMd.setText(textMade);
        binding.TabRPMs.setText(textMissed);
    }

    private void setUnsortedStatInPlace(boolean averages, TextView view, int col){
        String text = (averages) ? getString(R.string.float_holder, statsAvgUnsorted[col]) :
                getString(R.string.int_holder, statsTotUnsorted[col]);

        view.setText(text);
    }

    private double performDivision(int dividend, int divisor){
        return (divisor == 0) ? 0.0 : ((double) dividend) / ((double) divisor);
    }
}