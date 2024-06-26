package com.example.finance.recyclerview;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finance.R;
import com.example.finance.data.HistoryItem;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>{
    private List<HistoryItem> items;
    public HistoryAdapter(List<HistoryItem> items){
        this.items = items;
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HistoryItem item = items.get(position);
        holder.operation_name.setText(item.getOperationName());
        holder.operation_time.setText(item.getOperationDate());

        if(item.getIncome()) {
            holder.operation_cost.setTextColor(Color.parseColor("#00C853"));
            holder.operation_cost.setText("+" + item.getOperationCost() + " " + item.getOperationCurrency());
        }
        else {
            holder.operation_cost.setTextColor(Color.parseColor("#D50000"));
            holder.operation_cost.setText("-" + item.getOperationCost() + " " + item.getOperationCurrency());
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TAG", position + "");
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public void restoreItem(HistoryItem item, int position) {
        items.add(position, item);
        notifyItemInserted(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView operation_name;
        TextView operation_time;
        TextView operation_cost;
        CardView cardView;
        ViewHolder(View view){
            super(view);
            operation_name = view.findViewById(R.id.operation_name);
            operation_time = view.findViewById(R.id.operation_time);
            operation_cost = view.findViewById(R.id.operation_cost);
            cardView = view.findViewById(R.id.card_view);
        }
    }
}
