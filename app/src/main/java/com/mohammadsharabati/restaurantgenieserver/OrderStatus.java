package com.mohammadsharabati.restaurantgenieserver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.mohammadsharabati.restaurantgenieserver.Common.Common;
import com.mohammadsharabati.restaurantgenieserver.Interface.ItemClickListener;
import com.mohammadsharabati.restaurantgenieserver.Model.DataMessage;
import com.mohammadsharabati.restaurantgenieserver.Model.MyResponse;
import com.mohammadsharabati.restaurantgenieserver.Model.Request;
import com.mohammadsharabati.restaurantgenieserver.Model.Token;
import com.mohammadsharabati.restaurantgenieserver.Remote.APIService;
import com.mohammadsharabati.restaurantgenieserver.ViewHolder.OrderViewHolder;
import java.util.HashMap;
import java.util.Map;

public class OrderStatus extends AppCompatActivity {


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    private FirebaseDatabase database;
    private DatabaseReference requests;
    private MaterialSpinner spinner;
    private APIService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        mService = Common.getFCMService();

        //Init Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference().child("RestaurantGenie").child(Common.currentUser.getBusinessNumber()).child("Requests");

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
                viewHolder.txtOrderDate.setText(Common.getDate(Long.parseLong(adapter.getRef(position).getKey())));
                viewHolder.txtOrderId.setText(adapter.getRef(position).getKey());
                viewHolder.txtOrderStatus.setText(Common.convertCodeToStatus(model.getStatus()));
                viewHolder.txtOrderNote.setText(model.getNote());
                viewHolder.txtOrderPhone.setText(model.getPhone());

                //New event Button
                viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showUpdateDialog(adapter.getRef(position).getKey(), adapter.getItem(position));
                    }
                });

                viewHolder.btnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteOrder(adapter.getRef(position).getKey());
                    }
                });

                viewHolder.btnDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent orderDetailIntent = new Intent(OrderStatus.this, OrderDetail.class);
                        Common.currentRequest = model;
                        orderDetailIntent.putExtra("OrderId", adapter.getRef(position).getKey());
                        startActivity(orderDetailIntent);
                    }
                });

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

        alertDialog.setView(update_order_layout);

        final String localKey = key;
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                item.setStatus(String.valueOf(spinner.getSelectedIndex()));
                requests.child(localKey).setValue(item);
                requests.child(localKey).setValue(item);
                adapter.notifyDataSetChanged(); //Add to update item all

                sendOrderStatusToUser(localKey, item);
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

    private void sendOrderStatusToUser(final String key, final Request item) {

        DatabaseReference tokens = database.getReference().child("RestaurantGenie").child(Common.currentUser.getBusinessNumber()).child("Tokens");

        tokens.child(item.getPhone())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Token token = dataSnapshot.getValue(Token.class);

                            Map<String, String> dataSend = new HashMap<>();
                            if (item.getStatus().equals("0")){
                                dataSend.put("title", "Restaurant Genie");
                                dataSend.put("message", "Status of order is: Placed");
                            }else if (item.getStatus().equals( "1")){
                                dataSend.put("title", "Restaurant Genie");
                                dataSend.put("message", "Status of order is: Processing");
                            }else if (item.getStatus().equals(  "2")){
                                dataSend.put("title", "Restaurant Genie");
                                dataSend.put("message", "Status of order is: Ready, On the way to you");
                            }

                            DataMessage dataMessage = new DataMessage(token != null ? token.getToken() : null, dataSend);

                            String test = new Gson().toJson(dataMessage);
                            Log.d("Content", test);

                            mService.sendNotification(dataMessage)
                                    .enqueue(new Callback<MyResponse>() {
                                        @Override
                                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                            if (response.body().success == 1) {
                                                Toast.makeText(OrderStatus.this, "Order was updated", Toast.LENGTH_SHORT).show();

                                            } else {
                                                Toast.makeText(OrderStatus.this, "Order was updated but failed to send notification !", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<MyResponse> call, Throwable t) {
                                            Log.e("ERROR", t.getMessage());
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void deleteOrder(String key) {
        requests.child(key).removeValue();
        adapter.notifyDataSetChanged();

    }

}
