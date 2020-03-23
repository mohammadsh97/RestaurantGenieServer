package com.mohammadsharabati.restaurantgenieserver.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;
import com.mohammadsharabati.restaurantgenieserver.Common.Common;
import com.mohammadsharabati.restaurantgenieserver.Interface.ItemClickListener;
import com.mohammadsharabati.restaurantgenieserver.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TimeWorkerViewHolder extends RecyclerView.ViewHolder implements
        View.OnClickListener, View.OnCreateContextMenuListener {

    public TextView name;
    public TextView startTime;
    public TextView endTime;
    private ItemClickListener itemClickListener;

    public TimeWorkerViewHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.nameWorker);
        startTime =  itemView.findViewById(R.id.startTime);
        endTime =  itemView.findViewById(R.id.endTime);

        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
    }


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select the action");

        menu.add(0, 0, getAdapterPosition(), Common.UPDATE);
        menu.add(0, 1, getAdapterPosition(), Common.DELETE);

    }
    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}
