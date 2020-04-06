package com.mohammadsharabati.restaurantgenieserver.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.mohammadsharabati.restaurantgenieserver.Interface.ItemClickListener;
import com.mohammadsharabati.restaurantgenieserver.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TableViewHolder  extends RecyclerView.ViewHolder implements
        View.OnClickListener  {

    public TextView user_name_table ,password_table, phone_number_table ;
    public Button btnEditTable, btnRemoveTable;

    private ItemClickListener itemClickListener;


    public TableViewHolder(@NonNull View itemView) {
        super(itemView);

        user_name_table = itemView.findViewById(R.id.user_name_table);
        password_table =  itemView.findViewById(R.id.password_table);
        phone_number_table =  itemView.findViewById(R.id.phone_number_table);

        btnEditTable = (Button) itemView.findViewById(R.id.btnEditTable);
        btnRemoveTable = (Button) itemView.findViewById(R.id.btnRemoveTable);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}
