package com.example.finance.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.finance.recyclerview.HistoryItem;

import java.io.Serializable;

@Database(entities = {HistoryItem.class}, version = 1, exportSchema = false)
public abstract class HistoryDatabase extends RoomDatabase  implements Serializable {
    public abstract HistoryItemDAO historyItemDAO();
}
