package com.example.finance.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.finance.R;
import com.example.finance.database.HistoryDatabase;
import com.example.finance.recyclerview.HistoryItem;

import org.w3c.dom.Text;

import java.util.List;
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
        StringBuilder balanceText = new StringBuilder().append(getResources().getString(R.string.balance)).append(": \n");
        List<HistoryItem> items =  db.historyItemDAO().getAll();
        double rubleBalance = 0;
        double dollarBalance = 0;
        double euroBalance = 0;
        for (int i = 0;i < items.size();i++){
            switch (items.get(i).getOperationCurrency()){
                case "$":
                    if(items.get(i).getIncome()) {
                        dollarBalance += Double.parseDouble(items.get(i).getOperationCost());
                    }
                    else{
                        dollarBalance -= Double.parseDouble(items.get(i).getOperationCost());
                    }
                    break;
                case "₽":
                    if(items.get(i).getIncome()) {
                        rubleBalance += Double.parseDouble(items.get(i).getOperationCost());
                    }
                    else{
                        rubleBalance -= Double.parseDouble(items.get(i).getOperationCost());
                    }
                    break;
                case "€":
                    if(items.get(i).getIncome()) {
                         euroBalance += Double.parseDouble(items.get(i).getOperationCost());
                    }
                    else{
                        euroBalance -= Double.parseDouble(items.get(i).getOperationCost());
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

        RootView.findViewById(R.id.to_add_fragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment addRecord = new AddRecordFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("Database", db);
                addRecord.setArguments(bundle);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, addRecord)
                        .commit();
            }
        });

        TextView balance = RootView.findViewById(R.id.balance);
        balance.setText(balanceText.toString());

        return RootView;
    }
}