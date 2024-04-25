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
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;


//TODO Нормальный интерфейс
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddRecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddRecordFragment extends Fragment {

    private HistoryDatabase db;

    public AddRecordFragment() {
    }

    public static AddRecordFragment newInstance() {
        AddRecordFragment fragment = new AddRecordFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
        View RootView = inflater.inflate(R.layout.fragment_add_record, container, false);

        SwitchCompat switchCompat = RootView.findViewById(R.id.switch_compat);
        TextView label = RootView.findViewById(R.id.label);
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


        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    label.setText(getResources().getText(R.string.income));
                    ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(getContext(), R.array.income, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    category.setAdapter(adapter);
                    item.setIncome(true);
                }
                else{
                    label.setText(getResources().getText(R.string.expense));
                    ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(getContext(), R.array.expense, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    category.setAdapter(adapter);
                    item.setIncome(false);
                }
            }
        });

        RootView.findViewById(R.id.to_date_picker).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker().build();
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
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new HomeScreenFragment())
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
                        Toast.makeText(getContext(), getResources().getText(R.string.input_error), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Log.i("TAG", item.getOperationName());
                        Log.i("TAG", item.getOperationCost());
                        Log.i("TAG", item.getOperationCurrency());
                        Log.i("TAG", item.getOperationDate());
                        Log.i("TAG", item.getIncome().toString());
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
        if(item.getOperationCost().equals("") || item.getOperationCost() == null){
            return false;
        }
        if(item.getOperationCost().startsWith("0")){
            if(!item.getOperationCost().substring(1).startsWith(",")){
                item.setOperationCost(item.getOperationCost().substring(1));
            }
        }
        if(item.getOperationCost().contains(",")) {
            if (item.getOperationCost().matches("^\\d{1,},\\d{1,2}$")) {
                if (item.getOperationCost().matches("^\\d{1,},\\d{1}$")) {
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