package com.example.finance.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finance.R;
import com.example.finance.database.HistoryDatabase;
import com.example.finance.recyclerview.HistoryItem;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;


//TODO Нормальный интерфейс
public class AddRecordFragment extends Fragment {

    private HistoryDatabase db;


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
        View RootView = inflater.inflate(R.layout.fragment_add_record, container, false);

        EditText sum = RootView.findViewById(R.id.sum);

        Spinner category = RootView.findViewById(R.id.category);
        ArrayAdapter<?> categoryAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.income, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(categoryAdapter);

        Spinner currency = RootView.findViewById(R.id.currency);
        ArrayAdapter<?> currencyAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.currency, android.R.layout.simple_spinner_item);
        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currency.setAdapter(currencyAdapter);

        HistoryItem item = new HistoryItem();
        item.setIncome(true);

        MaterialButton income = RootView.findViewById(R.id.income);
        income.setChecked(true);
        MaterialButton expense = RootView.findViewById(R.id.expense);
        income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setIncome(true);
                ArrayAdapter<?> categoryAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.income, android.R.layout.simple_spinner_item);
                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                category.setAdapter(categoryAdapter);
            }
        });
        expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setIncome(false);
                ArrayAdapter<?> categoryAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.expense, android.R.layout.simple_spinner_item);
                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                category.setAdapter(categoryAdapter);
            }
        });

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] choose;
                if(item.getIncome()){
                    choose = getResources().getStringArray(R.array.income);
                }
                else{
                    choose = getResources().getStringArray(R.array.expense);
                }
                item.setOperationName(choose[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        currency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] choose = getResources().getStringArray(R.array.currency);
                item.setOperationCurrency(choose[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        RootView.findViewById(R.id.to_date_picker).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                MaterialDatePicker<Long> datePicker = MaterialDatePicker
                        .Builder
                        .datePicker()
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build();
                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object o) {
                        TextView textView = RootView.findViewById(R.id.to_date_picker);
                        String selectedDate = datePicker.getHeaderText();
                        item.setOperationDate(selectedDate);
                        textView.setText(selectedDate);
                    }
                });
                datePicker.show(getFragmentManager(), "");
            }
        });
        RootView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeScreenFragment homeScreenFragment = new HomeScreenFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("Database", db);
                homeScreenFragment.setArguments(bundle);
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,homeScreenFragment)
                        .commit();
            }
        });
        RootView.findViewById(R.id.add_record).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setOperationCost(sum.getText().toString());
                if(item.getOperationDate() == null){
                    Toast.makeText(getContext(), getResources().getText(R.string.input_error), Toast.LENGTH_SHORT).show();
                }
                else {
                    if(!isCorrectItem(item)) {
                        Toast.makeText(getContext(), getResources().getText(R.string.incorrect_input), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        db.historyItemDAO().insert(item);
                        Fragment homeScreen = new HomeScreenFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Database", db);
                        homeScreen.setArguments(bundle);
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, homeScreen)
                                .commit();
                    }
                }
            }
        });

        return RootView;
    }
    public boolean isCorrectItem(HistoryItem item){
        if(item.getOperationCost().isEmpty() || item.getOperationCost() == null){
            return false;
        }
        if(item.getOperationCost().startsWith("0")){
            if(!item.getOperationCost().substring(1).startsWith(".")){
                item.setOperationCost(item.getOperationCost().substring(1));
            }
        }
        if(item.getOperationCost().contains(".")) {
            if (item.getOperationCost().matches("^\\d{1,}.\\d{1,2}$")) {
                if (item.getOperationCost().matches("^\\d{1,}.\\d{1}$")) {
                    item.setOperationCost(item.getOperationCost() + "0");
                }
            }
            else{
                return false;
            }
        }
        return true;
    }
}