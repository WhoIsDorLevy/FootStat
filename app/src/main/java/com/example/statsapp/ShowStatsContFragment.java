package com.example.statsapp;

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
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.statsapp.databinding.FragmentShowStatsContBinding;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class ShowStatsContFragment extends Fragment {

    private FragmentShowStatsContBinding binding;
    private boolean[] checks;
    private SQLiteDatabase db;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        MainActivity activity = (MainActivity) getActivity();
        db = activity.getDb().getReadableDatabase();
        binding = FragmentShowStatsContBinding.inflate(inflater, container, false);
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.show_stats_fragment_label);
        return binding.getRoot();
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checks = ShowStatsContFragmentArgs.fromBundle(getArguments()).getChecks();
        filterTable();
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
            deleteView(binding.TabTotal);
            deleteView(binding.TabRGT);
            deleteView(binding.TabLGT);
            deleteView(binding.TabHGT);
            deleteView(binding.TabAsT);
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
}