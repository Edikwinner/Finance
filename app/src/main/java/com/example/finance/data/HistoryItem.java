package com.example.finance.data;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class HistoryItem {
    @PrimaryKey(autoGenerate = true)
    public int id;
    private Boolean income;
    private String operationDescription;
    private String operationName;
    private String operationCost;
    private String operationDate;
    private String operationCurrency;
    public HistoryItem(String operationName, String operationCost, String operationDate, Boolean income, String operationCurrency){
        this.operationName = operationName;
        this.operationCost = operationCost;
        this.operationDate = operationDate;
        this.income = income;
        this.operationCurrency = operationCurrency;
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

    public void setOperationCurrency(String operationCurrency) {
        this.operationCurrency = operationCurrency;
    }

    public void setOperationDescription(String operationDescription) {
        this.operationDescription = operationDescription;
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

    public String getOperationCurrency() {
        return operationCurrency;
    }

    public String getOperationDescription() {
        return operationDescription;
    }
}
