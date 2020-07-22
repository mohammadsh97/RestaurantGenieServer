package com.mohammadsharabati.restaurantgenieserver.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mohammadsharabati.restaurantgenieserver.Common.Common;
import com.mohammadsharabati.restaurantgenieserver.Interface.ItemClickListener;
import com.mohammadsharabati.restaurantgenieserver.Model.RequestWithKey;
import com.mohammadsharabati.restaurantgenieserver.R;
import com.mohammadsharabati.restaurantgenieserver.ViewHolder.OrderViewHolder;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class AdapterOrderStatus extends RecyclerView.Adapter<OrderViewHolder> {

    public List<RequestWithKey> list;
    ItemClickListener itemClickListener;


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public AdapterOrderStatus(List<RequestWithKey> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_layout, parent, false);
        return new OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder viewHolder, int position) {

        //show this item
        viewHolder.txtOrderDate.setText(Common.getDate(Long.parseLong(list.get(position).getKey())));
        viewHolder.txtOrderId.setText(list.get(position).getKey());
        viewHolder.txtOrderStatus.setText(Common.convertCodeToStatus(list.get(position).getStatus()));
        viewHolder.txtOrderNote.setText(list.get(position).getNote());
        viewHolder.txtOrderPhone.setText(list.get(position).getPhone());
        viewHolder.txtTableName.setText(list.get(position).getName());

        //New event Button
        viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onClick(v, position, false);
            }
        });

        if (Common.userManger == true) {
            viewHolder.btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onRemove(v,position,false);
                }
            });
        }

        viewHolder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onDetail(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
