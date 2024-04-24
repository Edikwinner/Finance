package com.example.finance;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.finance.database.HistoryDatabase;
import com.example.finance.fragments.DiagramScreenFragment;
import com.example.finance.fragments.HistoryScreenFragment;
import com.example.finance.fragments.HomeScreenFragment;
import com.example.finance.fragments.SettingsScreenFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
//TODO Добавить логи, Добавить Material 3?
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HistoryDatabase db = Room
                .databaseBuilder(getApplicationContext(), HistoryDatabase.class, "History")
                .allowMainThreadQueries()
                .build();


        Fragment homeScreen = new HomeScreenFragment();
        Bundle homeScreenBundle = new Bundle();
        homeScreenBundle.putSerializable("Database", db);
        homeScreen.setArguments(homeScreenBundle);

        Fragment historyScreen = new HistoryScreenFragment();
        Bundle historyScreenBundle = new Bundle();
        historyScreenBundle.putSerializable("Database", db);
        historyScreen.setArguments(historyScreenBundle);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, homeScreen)
                .commit();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.home_screen){
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, homeScreen)
                            .commit();
                    return true;
                }
                else if(menuItem.getItemId() == R.id.history_screen){
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, historyScreen)
                            .commit();
                    return true;
                }
                else if(menuItem.getItemId() == R.id.diagram_screen){
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new DiagramScreenFragment())
                            .commit();
                    return true;
                }
                else if(menuItem.getItemId() == R.id.settings_screen){
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new SettingsScreenFragment())
                            .commit();
                    return true;
                }
                return false;
            }
        });
    }
}