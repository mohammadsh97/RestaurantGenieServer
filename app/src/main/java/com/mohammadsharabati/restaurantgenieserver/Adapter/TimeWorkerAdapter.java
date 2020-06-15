package com.mohammadsharabati.restaurantgenieserver.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mohammadsharabati.restaurantgenieserver.Model.TimeWorker;
import com.mohammadsharabati.restaurantgenieserver.R;
import com.mohammadsharabati.restaurantgenieserver.ViewHolder.StaffTimeWorkerItemViewHolder;

import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TimeWorkerAdapter extends RecyclerView.Adapter<StaffTimeWorkerItemViewHolder> {

    private List<TimeWorker> timeWorkerList;

    public TimeWorkerAdapter(List<TimeWorker> timeWorkerList) {
        this.timeWorkerList = timeWorkerList;
    }

    @NonNull
    @Override
    public StaffTimeWorkerItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.staff_time_table_item, parent, false);

        return new StaffTimeWorkerItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StaffTimeWorkerItemViewHolder holder, int position) {
        holder.name.setText(timeWorkerList.get(position).getName());
        holder.startTime.setText(timeWorkerList.get(position).getStartTime());
        holder.endTime.setText(timeWorkerList.get(position).getEndTime());
    }

    @Override
    public int getItemCount() {
        return timeWorkerList.size();
    }
}
