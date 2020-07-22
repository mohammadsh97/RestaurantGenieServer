package com.mohammadsharabati.restaurantgenieserver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mohammadsharabati.restaurantgenieserver.Common.Common;
import com.mohammadsharabati.restaurantgenieserver.Interface.ItemClickListener;
import com.mohammadsharabati.restaurantgenieserver.Model.Table;
import com.mohammadsharabati.restaurantgenieserver.ViewHolder.StaffDetailAdapter;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by Mohammad Sharabati.
 */
public class StaffDetail extends AppCompatActivity implements ItemClickListener {

    private TextView name_of_staff;

    private FirebaseDatabase database;
    private DatabaseReference AddTables;
    private StaffDetailAdapter adapter;
    private List<Table> listTable = new ArrayList<>();
    private List<String> listKey = new ArrayList<>();

    private String staffId = "", staffName = "";

    private RecyclerView table_list;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_detail);

        //Init firebase
        database = FirebaseDatabase.getInstance();
        AddTables = database.getReference().child("RestaurantGenie").child(Common.currentUser.getBusinessNumber()).child("AddTable");

        name_of_staff = (TextView) findViewById(R.id.name_of_staff);

        //Init View
        table_list = (RecyclerView) findViewById(R.id.table_list);
        table_list.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        table_list.setLayoutManager(layoutManager);

        if (getIntent() != null) {
            staffId = getIntent().getStringExtra("staffId");
            staffName = getIntent().getStringExtra("staffName");
        }
        if (!staffId.isEmpty()) {
            name_of_staff.setText(staffName);
            adapter = new StaffDetailAdapter(listTable, listKey);
            adapter.setItemClickListener(this);
            table_list.setAdapter(adapter);
            loadListTable(staffId);
        }
    }

    private void loadListTable(String staffId) {

        Query listTableByStaffId = AddTables.orderByChild("staffId").equalTo(staffId);

        listTableByStaffId.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listTable.clear();
                listKey.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    listKey.add(snapshot.getKey());
                    listTable.add(snapshot.getValue(Table.class));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view, int position, boolean isLongClick) {

    }

    @Override
    public void onRemove(View view, int position, boolean isLongClick) {
        AddTables.child(listKey.get(position)).removeValue();
    }

    @Override
    public void onDetail(int position) {

    }
}