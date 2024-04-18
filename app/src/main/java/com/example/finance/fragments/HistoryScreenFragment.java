package com.example.finance.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.finance.R;
import com.example.finance.database.HistoryDatabase;
import com.example.finance.recyclerview.HistoryAdapter;
import com.example.finance.recyclerview.HistoryItem;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryScreenFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    HistoryDatabase db;

    public HistoryScreenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryScreenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryScreenFragment newInstance(String param1, String param2) {
        HistoryScreenFragment fragment = new HistoryScreenFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            db = (HistoryDatabase) getArguments().get("Database");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View RootView = inflater.inflate(R.layout.fragment_history_screen, container, false);
        ImageButton deleteAll = RootView.findViewById(R.id.delete_all);
        RecyclerView history = RootView.findViewById(R.id.history);

        history.setLayoutManager(new LinearLayoutManager(getContext()));
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

        HistoryAdapter historyAdapter = new HistoryAdapter(historyItems);
        history.setAdapter(historyAdapter);
        RootView.findViewById(R.id.filter_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HistoryAdapter historyAdapter = new HistoryAdapter(historyItems);
                history.setAdapter(historyAdapter);
            }
        });
        RootView.findViewById(R.id.filter_income).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HistoryAdapter incomeAdapter = new HistoryAdapter(incomeItems);
                history.setAdapter(incomeAdapter);
            }
        });
        RootView.findViewById(R.id.filter_expense).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HistoryAdapter expenseAdapter = new HistoryAdapter(expenseItems);
                history.setAdapter(expenseAdapter);
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
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
                builder.setTitle(getResources().getString(R.string.alert_dialog_title))
                        .setMessage(getResources().getString(R.string.alert_dialog_message))
                        .setIcon(android.R.drawable.ic_dialog_alert)
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
}