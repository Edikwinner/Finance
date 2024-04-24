package com.example.finance.recyclerview;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class HistoryItem {
    @PrimaryKey(autoGenerate = true)
    public int id;
    private Boolean income;
    private String operationName;
    private String operationCost;
    private String operationDate;
    public HistoryItem(String operationName, String operationCost, String operationDate, Boolean income){
        this.operationName = operationName;
        this.operationCost = operationCost;
        this.operationDate = operationDate;
        this.income = income;
    }
    public HistoryItem(){

    }

    public void setOperationCost(String operationCost) {
        this.operationCost = operationCost;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public void setOperationDate(String operationDate) {
        this.operationDate = operationDate;
    }

    public void setIncome(Boolean income) {
        this.income = income;
    }

    public String getOperationName() {
        return operationName;
    }

    public String getOperationCost() {
        return operationCost;
    }

    public String getOperationDate() {
        return operationDate;
    }

    public Boolean getIncome() {
        return income;
    }

}
