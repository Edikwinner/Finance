package com.example.finance.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.finance.R;
import com.example.finance.database.HistoryDatabase;
import com.example.finance.data.HistoryItem;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.view.LineChartView;

//TODO Добавить элементов на главный экран
public class HomeScreenFragment extends Fragment {
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
        View RootView = inflater.inflate(R.layout.fragment_home_screen, container, false);
        MaterialTextView totalIncomeTextView = RootView.findViewById(R.id.total_income);
        MaterialTextView totalExpenseTextView = RootView.findViewById(R.id.total_expense);
        Double totalIncome = 0.0;
        Double totalExpense = 0.0;
        StringBuilder balanceText = new StringBuilder().append(getResources().getString(R.string.balance)).append(": \n");
        StringBuilder incomeBalanceText = new StringBuilder().append(getResources().getString(R.string.total_income)).append(": \n");
        StringBuilder expenseBalanceText = new StringBuilder().append(getResources().getString(R.string.total_expense)).append(": \n");
        List<HistoryItem> items =  db.historyItemDAO().getAll();
        double rubleBalance = 0;
        double dollarBalance = 0;
        double euroBalance = 0;

        double rubleBalanceIncome = 0;
        double dollarBalanceIncome = 0;
        double euroBalanceIncome = 0;

        double rubleBalanceExpense = 0;
        double dollarBalanceExpense = 0;
        double euroBalanceExpense = 0;
        for (int i = 0;i < items.size();i++){
            if(items.get(i).getIncome()){
                totalIncome += Double.parseDouble(items.get(i).getOperationCost());
            }
            else{
                totalExpense += Double.parseDouble(items.get(i).getOperationCost());
            }
            switch (items.get(i).getOperationCurrency()){
                case "$":
                    if(items.get(i).getIncome()) {
                        dollarBalance += Double.parseDouble(items.get(i).getOperationCost());
                        dollarBalanceIncome += Double.parseDouble(items.get(i).getOperationCost());
                    }
                    else{
                        dollarBalance -= Double.parseDouble(items.get(i).getOperationCost());
                        dollarBalanceExpense += Double.parseDouble(items.get(i).getOperationCost());
                    }
                    break;
                case "₽":
                    if(items.get(i).getIncome()) {
                        rubleBalance += Double.parseDouble(items.get(i).getOperationCost());
                        rubleBalanceIncome += Double.parseDouble(items.get(i).getOperationCost());
                    }
                    else{
                        rubleBalance -= Double.parseDouble(items.get(i).getOperationCost());
                        rubleBalanceExpense += Double.parseDouble(items.get(i).getOperationCost());
                    }
                    break;
                case "€":
                    if(items.get(i).getIncome()) {
                         euroBalance += Double.parseDouble(items.get(i).getOperationCost());
                         euroBalanceIncome += Double.parseDouble(items.get(i).getOperationCost());
                    }
                    else{
                        euroBalance -= Double.parseDouble(items.get(i).getOperationCost());
                        euroBalanceExpense += Double.parseDouble(items.get(i).getOperationCost());
                    }
                    break;
            }
        }

        if(rubleBalance != 0.0) {
            balanceText.append(Math.ceil(rubleBalance * 100) / 100).append(" ₽").append('\n');
        }
        if(dollarBalance != 0.0) {
            balanceText.append(Math.ceil(dollarBalance * 100) / 100).append(" $").append('\n');
        }
        if(euroBalance != 0.0) {
            balanceText.append(Math.ceil(euroBalance * 100) / 100).append(" €");
        }

        if(rubleBalanceIncome != 0.0) {
           incomeBalanceText.append(Math.ceil(rubleBalanceIncome * 100) / 100).append(" ₽").append('\n');
        }
        if(dollarBalanceIncome != 0.0) {
            incomeBalanceText.append(Math.ceil(dollarBalanceIncome * 100) / 100).append(" $").append('\n');
        }
        if(euroBalanceIncome != 0.0) {
            incomeBalanceText.append(Math.ceil(euroBalanceIncome * 100) / 100).append(" €").append('\n');
        }

        if(rubleBalanceExpense != 0.0) {
            expenseBalanceText.append(Math.ceil(rubleBalanceExpense * 100) / 100).append(" ₽").append('\n');
        }
        if(dollarBalanceExpense != 0.0) {
            expenseBalanceText.append(Math.ceil(dollarBalanceExpense * 100) / 100).append(" $").append('\n');
        }
        if(euroBalanceExpense!= 0.0) {
            expenseBalanceText.append(Math.ceil(euroBalanceExpense * 100) / 100).append(" €").append('\n');
        }

        totalIncomeTextView.setText(incomeBalanceText.toString());
        totalExpenseTextView.setText(expenseBalanceText.toString());
        TextView balance = RootView.findViewById(R.id.balance);
        balance.setText(balanceText.toString());

        return RootView;
    }

}