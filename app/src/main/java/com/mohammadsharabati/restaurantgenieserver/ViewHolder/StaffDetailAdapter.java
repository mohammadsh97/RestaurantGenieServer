package com.mohammadsharabati.restaurantgenieserver.ViewHolder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.mohammadsharabati.restaurantgenieserver.Interface.ItemClickListener;
import com.mohammadsharabati.restaurantgenieserver.Model.Table;
import com.mohammadsharabati.restaurantgenieserver.R;
import java.util.List;
import androidx.recyclerview.widget.RecyclerView;

class StaffDetailViewHolder extends RecyclerView.ViewHolder{
    TextView table_name;
    Button btnRemove;

    StaffDetailViewHolder(View itemView) {
        super(itemView);
        table_name = itemView.findViewById(R.id.table_name);
        btnRemove = itemView.findViewById(R.id.btnRemove);
    }
}

public class StaffDetailAdapter extends RecyclerView.Adapter<StaffDetailViewHolder> {

    List<Table> listAddTable;
    List<String> listKey;
    ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public StaffDetailAdapter(List<Table> listAddTable, List<String> listKey) {
        this.listAddTable = listAddTable;
        this.listKey = listKey;
    }

    @Override
    public StaffDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.staff_detail_layout, parent, false);
        return new StaffDetailViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StaffDetailViewHolder holder, int position) {

        Table user = listAddTable.get(position);
        holder.table_name.setText(String.format(user.getName()));
        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onRemove(v,position,false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listAddTable.size();
    }

}