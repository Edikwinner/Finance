package com.example.finance.database;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.DeleteTable;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.finance.recyclerview.HistoryItem;

import java.util.List;

@Dao
public interface HistoryItemDAO {
    @Query("SELECT * FROM HistoryItem")
    List<HistoryItem> getAll();
    @Query("SELECT * FROM HistoryItem WHERE id = :id")
    HistoryItem getById(long id);
    @Query("DELETE FROM HistoryItem")
    void deleteAll();
    @Insert
    void insert(HistoryItem item);
    @Update
    void update(HistoryItem item);
    @Delete
    void delete(HistoryItem item);

}
