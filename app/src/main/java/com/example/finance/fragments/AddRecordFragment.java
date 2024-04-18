package com.example.finance.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finance.R;
import com.example.finance.database.HistoryDatabase;
import com.example.finance.recyclerview.HistoryItem;

import java.text.DateFormat;
import java.util.Calendar;


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
        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(getContext(), R.array.income, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(adapter);
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
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        TextView textView = RootView.findViewById(R.id.to_date_picker);
                        Calendar mCalendar = Calendar.getInstance();
                        mCalendar.set(Calendar.YEAR, year);
                        mCalendar.set(Calendar.MONTH, month);
                        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String selectedDate = DateFormat.getDateInstance(DateFormat.DEFAULT).format(mCalendar.getTime());
                        item.setOperationDate(selectedDate);
                        textView.setText(selectedDate);
                    }
                });
                datePickerDialog.show();
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
                if(item.getOperationCost() == null | item.getOperationDate() == null){
                    Toast.makeText(getContext(), getResources().getText(R.string.input_error), Toast.LENGTH_SHORT).show();
                }
                else {
                    db.historyItemDAO().insert(item);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new HomeScreenFragment())
                            .commit();
                }
            }
        });

        return RootView;
    }
}