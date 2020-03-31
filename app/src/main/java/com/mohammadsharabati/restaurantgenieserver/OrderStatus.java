package com.mohammadsharabati.restaurantgenieserver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.mohammadsharabati.restaurantgenieserver.Common.Common;
import com.mohammadsharabati.restaurantgenieserver.Interface.ItemClickListener;
import com.mohammadsharabati.restaurantgenieserver.Model.Request;
import com.mohammadsharabati.restaurantgenieserver.ViewHolder.OrderViewHolder;

public class OrderStatus extends AppCompatActivity {


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    private FirebaseDatabase database;
    private DatabaseReference requests;
    private MaterialSpinner spinner, shipperSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        //Init Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        //Init
        recyclerView = (RecyclerView) findViewById(R.id.recyclerOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders(Common.currentUser.getPhone());
    }

    /**
     * Loading order status
     */
    private void loadOrders(String phone) {

        Query getOrderByUser = requests.orderByChild("phone").equalTo(phone);

        FirebaseRecyclerOptions<Request> orderOptions = new FirebaseRecyclerOptions.Builder<Request>()
                .setQuery(getOrderByUser, Request.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(orderOptions) {
            @Override
            public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.order_layout, parent, false);

                return new OrderViewHolder(itemView);
            }

            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder viewHolder, int position, final Request model) {
                viewHolder.txtOrderId.setText(adapter.getRef(position).getKey());
                viewHolder.txtOrderStatus.setText(Common.convertCodeToStatus(model.getStatus()));
                viewHolder.txtOrderAddress.setText(model.getAddress());
                viewHolder.txtOrderPhone.setText(model.getPhone());

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(OrderStatus.this, "your order status is: " + Common.convertCodeToStatus(model.getStatus()), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Fix click back on FoodDetail and get no item in FoodList
        if (adapter != null)
            adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null)
            adapter.stopListening();
    }

    // Update / Delete
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(Common.UPDATE)) {
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));
            Log.d("ORDER_UPDATE", "" + adapter.getRef(item.getOrder()).getKey() + " " + adapter.getItem(item.getOrder()));
        } else if (item.getTitle().equals(Common.DELETE)) {
            deleteOrder(adapter.getRef(item.getOrder()).getKey());
            Log.d("ORDER_DELETE", "" + adapter.getRef(item.getOrder()).getKey());
        }

        return super.onContextItemSelected(item);
    }

    private void showUpdateDialog(String key, final Request item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderStatus.this);
        alertDialog.setTitle("Update Order");
        alertDialog.setMessage("Please Choose Status");

        LayoutInflater inflater = this.getLayoutInflater();
        View update_order_layout = inflater.inflate(R.layout.update_order_layout, null);

        spinner = (MaterialSpinner) update_order_layout.findViewById(R.id.statusSpinner);
        spinner.setItems("Placed","Processing","Ready, On the way to you");

//        shipperSpinner = (MaterialSpinner) update_order_layout.findViewById(R.id.shipperSpinner);
//
//
//        final List<String> shipperLists = new ArrayList<>();
//        FirebaseDatabase.getInstance().getReference(Common.SHIPPER_TABLE)
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        for (DataSnapshot shipperSnapshot : dataSnapshot.getChildren()) {
//                            shipperLists.add(shipperSnapshot.getKey());
//                            shipperSpinner.setItems(shipperLists);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });


        alertDialog.setView(update_order_layout);

        final String localKey = key;
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                item.setStatus(String.valueOf(spinner.getSelectedIndex()));
//                requests.child(localKey).setValue(item);

//                if (item.getStatus().equals("2")) {
//
//                    FirebaseDatabase.getInstance().getReference(Common.ORDER_NEDD_SHIP_TABLE)
//                            .child(shipperSpinner.getItems().get(shipperSpinner.getSelectedIndex()).toString())
//                            .child(localKey)
//                            .setValue(item);
//
//                    requests.child(localKey).setValue(item);
//                    adapter.notifyDataSetChanged(); //Add to update item all
//
//                    sendOrderStatusToUser(localKey, item);
//                    sendOrderShipperToRequest(shipperSpinner.getItems().get(shipperSpinner.getSelectedIndex()).toString(), item);
//                }

                requests.child(localKey).setValue(item);
//                adapter.notifyDataSetChanged(); //Add to update item all
//
//                sendOrderStatusToUser(localKey, item);
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void deleteOrder(String key) {
        requests.child(key).removeValue();
        adapter.notifyDataSetChanged();

    }

}
