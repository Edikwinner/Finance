package com.example.finance.fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.finance.R;
import com.example.finance.database.HistoryDatabase;
import com.example.finance.recyclerview.HistoryAdapter;
import com.example.finance.recyclerview.HistoryItem;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryScreenFragment extends Fragment {

    HistoryDatabase db;
    RecyclerView history;
    View RootView;


    public HistoryScreenFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryScreenFragment.
     */
    public static HistoryScreenFragment newInstance(String param1, String param2) {
        return new HistoryScreenFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            db = (HistoryDatabase) getArguments().get("Database");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RootView = inflater
                .inflate(R.layout.fragment_history_screen, container, false);

        ImageButton deleteAll = RootView.findViewById(R.id.delete_all);
        history = RootView.findViewById(R.id.history);

        List<HistoryItem> historyItems = db.historyItemDAO().getAll();
        List<HistoryItem> incomeItems = new ArrayList<>();
        List<HistoryItem> expenseItems = new ArrayList<>();

        for(int i = 0;i < historyItems.size();i++){
            if(historyItems.get(i).getIncome()){
                incomeItems.add(historyItems.get(i));
            }
            else{
                expenseItems.add(historyItems.get(i));
            }
        }

        history.setLayoutManager(new LinearLayoutManager(getContext()));

        HistoryAdapter incomeAdapter = new HistoryAdapter(incomeItems);
        HistoryAdapter expenseAdapter = new HistoryAdapter(expenseItems);
        HistoryAdapter historyAdapter = new HistoryAdapter(historyItems);

        ItemTouchHelper historyItemsTouchHelper = getItemTouchHelper(historyItems,
                incomeItems, expenseItems, historyAdapter, incomeAdapter, expenseAdapter);
        historyItemsTouchHelper.attachToRecyclerView(history);
        history.setAdapter(historyAdapter);

        RootView.findViewById(R.id.filter_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                history.setAdapter(historyAdapter);
                historyItemsTouchHelper.attachToRecyclerView(history);
            }
        });
        RootView.findViewById(R.id.filter_income).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                history.setAdapter(incomeAdapter);
                historyItemsTouchHelper.attachToRecyclerView(null);
            }
        });
        RootView.findViewById(R.id.filter_expense).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                history.setAdapter(expenseAdapter);
                historyItemsTouchHelper.attachToRecyclerView(null);
            }
        });

        if (historyItems.isEmpty()) {
            history.setVisibility(View.INVISIBLE);
        } else {
            history.setVisibility(View.VISIBLE);
        }


        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
                builder.setTitle(getResources().getString(R.string.alert_dialog_title))
                        .setMessage(getResources().getString(R.string.alert_dialog_message))
                        .setIcon(getResources().getDrawable(R.drawable.warning))
                        .setPositiveButton(getResources().getText(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.historyItemDAO().deleteAll();
                                history.setVisibility(View.INVISIBLE);
                            }
                        })
                        .setNegativeButton(getResources().getText(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        return RootView;
    }

    @NonNull
    private ItemTouchHelper getItemTouchHelper(List<HistoryItem> historyItems,
                                               List<HistoryItem> incomeItems,
                                               List<HistoryItem> expenseItems,
                                               HistoryAdapter historyAdapter,
                                               HistoryAdapter incomeAdapter,
                                               HistoryAdapter expenseAdapter) {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback
                = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();
                HistoryItem item = historyItems.get(position);
                int incomePosition = incomeItems.indexOf(item);
                int expansePosition = expenseItems.indexOf(item);
                historyItems.remove(item);
                if(item.getIncome()){
                    incomeItems.remove(item);
                    incomeAdapter.notifyItemRemoved(incomePosition);
                }
                else{
                    expenseItems.remove(item);
                    expenseAdapter.notifyItemRemoved(expansePosition);
                }
                historyAdapter.notifyItemRemoved(position);


                db.historyItemDAO().delete(item);

                Snackbar snackbar = Snackbar.make(RootView, getResources()
                        .getText(R.string.snackbar_title), Snackbar.LENGTH_LONG);
                snackbar.setAction(getResources().getText(R.string.undo), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.historyItemDAO().insert(item);
                        historyAdapter.restoreItem(item, position);
                        if(item.getIncome()){
                            incomeAdapter.restoreItem(item, incomePosition);
                        }
                        else{
                            expenseAdapter.restoreItem(item, expansePosition);
                        }
                    }
                });
                snackbar.show();
            }
        };

        return new ItemTouchHelper(simpleItemTouchCallback);
    }
}