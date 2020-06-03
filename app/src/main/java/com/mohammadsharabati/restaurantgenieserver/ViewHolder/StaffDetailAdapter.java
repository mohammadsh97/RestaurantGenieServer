package com.mohammadsharabati.restaurantgenieserver.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mohammadsharabati.restaurantgenieserver.Common.Common;
import com.mohammadsharabati.restaurantgenieserver.Model.Table;
import com.mohammadsharabati.restaurantgenieserver.R;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

class StaffDetailViewHolder extends RecyclerView.ViewHolder {
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
    Context mContext;
    FirebaseDatabase database;
    DatabaseReference AddTables;

    public StaffDetailAdapter(List<Table> listAddTable, List<String> listKey , Context mContext) {
        this.listAddTable = listAddTable;
        this.mContext = mContext;
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
        //Init firebase
        database = FirebaseDatabase.getInstance();
        AddTables = database.getReference().child("RestaurantGenie").child(Common.currentUser.getBusinessNumber()).child("AddTable");

        Table user = listAddTable.get(position);
        holder.table_name.setText(String.format(user.getName()));

        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTables.child(listKey.get(position)).removeValue();
            }
        });

    }
    @Override
    public int getItemCount() {
        return listAddTable.size();
    }

}
