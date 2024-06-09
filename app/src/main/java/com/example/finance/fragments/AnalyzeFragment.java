package com.example.finance.fragments;

import android.os.Bundle;

import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.finance.R;
import com.example.finance.data.Category;
import com.example.finance.data.HistoryItem;
import com.example.finance.database.HistoryDatabase;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

public class AnalyzeFragment extends Fragment {
    HistoryDatabase db;
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
        View RootView = inflater.inflate(R.layout.fragment_analyze, container, false);
        MaterialTextView total = RootView.findViewById(R.id.total_sum);

        String[] income = getResources().getStringArray(R.array.income);
        String[] expense = getResources().getStringArray(R.array.expense);


        List<HistoryItem> historyItems = db.historyItemDAO().getAll();

        List<Category> incomeArray = new ArrayList<Category>();
        for(int i = 0;i < income.length;i++){
            Category category = new Category();
            category.category = income[i];
            for(HistoryItem item : historyItems){
                if(item.getOperationName().equals(category.category)) {
                    switch (item.getOperationCurrency()) {
                        case "$":
                            if (item.getIncome()) {
                                category.incomeDollar += Double.parseDouble(item.getOperationCost());
                            } else {
                                category.expenseDollar += Double.parseDouble(item.getOperationCost());
                            }
                            break;
                        case "₽":
                            if (item.getIncome()) {
                                category.incomeRuble += Double.parseDouble(item.getOperationCost());
                            } else {
                                category.expenseRuble += Double.parseDouble(item.getOperationCost());
                            }
                            break;
                        case "€":
                            if (item.getIncome()) {
                                category.incomeEuro += Double.parseDouble(item.getOperationCost());
                            } else {
                                category.expenseEuro += Double.parseDouble(item.getOperationCost());
                            }
                            break;
                    }
                }
            }
            incomeArray.add(category);
        }

        for(int i = 0;i < income.length;i++){
            for(Category category : incomeArray){
                if(income[i].equals(category.category)){
                    StringBuilder builder = new StringBuilder();
                    builder.append(category.category);
                    if(category.incomeRuble != 0.0){
                        builder.append("\n").append(category.incomeRuble).append(" ₽");
                    }
                    if(category.incomeDollar != 0.0){
                        builder.append("\n").append(category.incomeDollar).append(" $");
                    }
                    if(category.incomeEuro != 0.0){
                        builder.append("\n").append(category.incomeEuro).append(" €");
                    }
                    income[i] = builder.toString();
                }
            }
        }

        List<Category> expenseArray = new ArrayList<Category>();
        for(int i = 0;i < expense.length;i++){
            Category category = new Category();
            category.category = expense[i];
            for(HistoryItem item : historyItems){
                if(item.getOperationName().equals(category.category)) {
                    switch (item.getOperationCurrency()) {
                        case "$":
                            if (item.getIncome()) {
                                category.incomeDollar += Double.parseDouble(item.getOperationCost());
                            } else {
                                category.expenseDollar += Double.parseDouble(item.getOperationCost());
                            }
                            break;
                        case "₽":
                            if (item.getIncome()) {
                                category.incomeRuble += Double.parseDouble(item.getOperationCost());
                            } else {
                                category.expenseRuble += Double.parseDouble(item.getOperationCost());
                            }
                            break;
                        case "€":
                            if (item.getIncome()) {
                                category.incomeEuro += Double.parseDouble(item.getOperationCost());
                            } else {
                                category.expenseEuro += Double.parseDouble(item.getOperationCost());
                            }
                            break;
                    }
                }
            }
            expenseArray.add(category);
        }

        for(int i = 0;i < expense.length;i++){
            for(Category category : expenseArray){
                if(expense[i].equals(category.category)){
                    StringBuilder builder = new StringBuilder();
                    builder.append(category.category);
                    if(category.expenseRuble != 0.0){
                        builder.append("\n").append(category.expenseRuble).append(" ₽");
                    }
                    if(category.expenseDollar != 0.0){
                        builder.append("\n").append(category.expenseDollar).append(" $");
                    }
                    if(category.expenseEuro != 0.0){
                        builder.append("\n").append(category.expenseEuro).append(" €");
                    }
                    expense[i] = builder.toString();
                }
            }
        }

        StringBuilder incomeTotalBuilder = new StringBuilder(getResources().getString(R.string.total_income));
        Double incomeRuble = 0.0;
        Double incomeDollar = 0.0;
        Double incomeEuro= 0.0;
        for(Category category : incomeArray){
            if(category.incomeRuble != 0.0){
                incomeRuble += category.incomeRuble;
            }
            if(category.incomeDollar != 0.0){
                incomeDollar += category.incomeDollar;
            }
            if(category.incomeEuro != 0.0){
                incomeEuro += category.incomeEuro;
            }
        }
        if(incomeRuble != 0.0){
            incomeTotalBuilder.append("\n").append(incomeRuble).append(" ₽");
        }
        if(incomeDollar != 0.0){
            incomeTotalBuilder.append("\n").append(incomeDollar).append(" $");
        }
        if(incomeEuro != 0.0){
            incomeTotalBuilder.append("\n").append(incomeEuro).append(" €");
        }



        StringBuilder expenseTotalBuilder = new StringBuilder(getResources().getString(R.string.total_expense));
        Double expenseRuble = 0.0;
        Double expenseDollar = 0.0;
        Double expenseEuro= 0.0;
        for(Category category : expenseArray){
            if(category.expenseRuble != 0.0){
                expenseRuble += category.expenseRuble;
            }
            if(category.expenseDollar != 0.0){
                expenseDollar += category.expenseDollar;
            }
            if(category.expenseEuro != 0.0){
                expenseEuro += category.expenseEuro;
            }
        }
        if(expenseRuble != 0.0){
            expenseTotalBuilder.append("\n").append(expenseRuble).append(" ₽");
        }
        if(expenseDollar != 0.0){
            expenseTotalBuilder.append("\n").append(expenseDollar).append(" $");
        }
        if(expenseEuro != 0.0){
            expenseTotalBuilder.append("\n").append(expenseEuro).append(" €");
        }


        ListView listView = RootView.findViewById(R.id.list_view);

        ArrayAdapter<String> incomeAdapter= new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, income);
        ArrayAdapter<String> expenseAdapter= new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, expense);
        TabLayout tabLayout = RootView.findViewById(R.id.analyze_tab_layout);
        listView.setAdapter(incomeAdapter);
        total.setText(incomeTotalBuilder.toString());
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0){
                    listView.setAdapter(incomeAdapter);
                    total.setText(incomeTotalBuilder.toString());

                } else if (tab.getPosition() == 1) {
                    listView.setAdapter(expenseAdapter);
                    total.setText(expenseTotalBuilder.toString());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return RootView;
    }
}