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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
//TODO Добавить элементов на главный экран
public class HomeScreenFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private HistoryDatabase db;

    public HomeScreenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeScreenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeScreenFragment newInstance(String param1, String param2) {
        HomeScreenFragment fragment = new HomeScreenFragment();
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

        balanceText.append(Math.ceil(rubleBalance * 100) / 100).append(" ₽").append('\n');
        balanceText.append(Math.ceil(dollarBalance * 100) / 100).append(" $").append('\n');
        balanceText.append(Math.ceil(euroBalance * 100) / 100).append(" €");

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