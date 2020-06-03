package com.mohammadsharabati.restaurantgenieserver.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mohammadsharabati.restaurantgenieserver.Common.Common;
import com.mohammadsharabati.restaurantgenieserver.Interface.ItemClickListener;
import com.mohammadsharabati.restaurantgenieserver.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StaffViewHolder  extends RecyclerView.ViewHolder implements
        View.OnClickListener, View.OnCreateContextMenuListener  {

    public TextView user_name_staff ,password_staff , phone_number_staff,email_staff ;
    public Button btnEdit, btnRemove , btnAddTable,btnDetail;

    private ItemClickListener itemClickListener;


    public StaffViewHolder(@NonNull View itemView) {
        super(itemView);

        user_name_staff = itemView.findViewById(R.id.user_name_staff);
        password_staff =  itemView.findViewById(R.id.password_staff);
        phone_number_staff =  itemView.findViewById(R.id.phone_number_staff);
        email_staff =  itemView.findViewById(R.id.email_staff);

        btnEdit = (Button) itemView.findViewById(R.id.btnEdit);
        btnRemove = (Button) itemView.findViewById(R.id.btnRemove);
        btnAddTable = itemView.findViewById(R.id.btnAddTable);
        btnDetail = itemView.findViewById(R.id.btnDetail);

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
