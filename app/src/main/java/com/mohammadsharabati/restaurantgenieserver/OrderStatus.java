package com.mohammadsharabati.restaurantgenieserver;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.paperdb.Paper;
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
import android.widget.Button;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.mohammadsharabati.restaurantgenieserver.Adapter.AdapterOrderStatus;
import com.mohammadsharabati.restaurantgenieserver.Common.Common;
import com.mohammadsharabati.restaurantgenieserver.Model.DataMessage;
import com.mohammadsharabati.restaurantgenieserver.Model.MyResponse;
import com.mohammadsharabati.restaurantgenieserver.Model.Request;
import com.mohammadsharabati.restaurantgenieserver.Model.RequestWithKey;
import com.mohammadsharabati.restaurantgenieserver.Model.Table;
import com.mohammadsharabati.restaurantgenieserver.Model.Token;
import com.mohammadsharabati.restaurantgenieserver.Remote.APIService;
import com.mohammadsharabati.restaurantgenieserver.ViewHolder.OrderViewHolder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderStatus extends AppCompatActivity {


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button btnSignOut;
    private FirebaseDatabase database;
    private DatabaseReference requests, addTables , tables;
    private MaterialSpinner spinner;
    private APIService mService;
    private List<RequestWithKey> listOfSpecialRequest = new ArrayList<>();
    RecyclerView.Adapter<OrderViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        mService = Common.getFCMService();

        //Init Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference().child("RestaurantGenie").child(Common.currentUser.getBusinessNumber()).child("Requests");
        addTables = database.getReference().child("RestaurantGenie").child(Common.currentUser.getBusinessNumber()).child("AddTable");
        tables = database.getReference().child("RestaurantGenie").child(Common.currentUser.getBusinessNumber()).child("Worker").child("Table");

        btnSignOut = findViewById(R.id.btnSignOut);
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Delete Remember user
                Paper.book().destroy();

                //Logout
                Intent signIn = new Intent(OrderStatus.this, MainActivity.class);
                signIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(signIn);
            }
        });

        //Init
        recyclerView = (RecyclerView) findViewById(R.id.recyclerOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders();
    }

    /**
     * Loading order status
     */
    private void loadOrders() {

        List<String> listPhoneNumber = new ArrayList<>();
        if (Common.userManger == true) {

            tables.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        listPhoneNumber.add(snapshot.getValue(Table.class).getPhone());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } else {
            Query phoneNumberOfTables = addTables.orderByChild("staffId").equalTo(Common.keyUser);

            phoneNumberOfTables.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        listPhoneNumber.add(snapshot.getValue(Table.class).getPhone());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        requests.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (int i = 0; i < listPhoneNumber.size(); i++) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Request model = snapshot.getValue(Request.class);

                        RequestWithKey modelWithKey = new RequestWithKey(model.getPhone(), model.getName(), model.getNote(), model.getTotal(),
                                model.getStatus(), model.getFoods(), snapshot.getRef().getKey());

                        if (model.getPhone().equals(listPhoneNumber.get(i))) {
                            listOfSpecialRequest.add(modelWithKey);
                        }
                    }
                }
                adapter = new AdapterOrderStatus(listOfSpecialRequest, getBaseContext());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    // Update / Delete
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(Common.UPDATE)) {
            showUpdateDialog(listOfSpecialRequest.get(item.getItemId()).getKey(), listOfSpecialRequest.get(item.getItemId()));
        }
        return super.onContextItemSelected(item);
    }

    private void showUpdateDialog(String key, final RequestWithKey item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderStatus.this);
        alertDialog.setTitle("Update Order");
        alertDialog.setMessage("Please Choose Status");

        LayoutInflater inflater = this.getLayoutInflater();
        View update_order_layout = inflater.inflate(R.layout.update_order_layout, null);

        spinner = (MaterialSpinner) update_order_layout.findViewById(R.id.statusSpinner);
        spinner.setItems("Placed", "Processing", "Ready, On the way to you");

        alertDialog.setView(update_order_layout);

        final String localKey = key;
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                item.setStatus(String.valueOf(spinner.getSelectedIndex()));
                requests.child(localKey).setValue(item);
                adapter.notifyDataSetChanged(); //Add to update item all

                sendOrderStatusToUser(item);
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

    private void sendOrderStatusToUser(final RequestWithKey item) {

        DatabaseReference tokens = database.getReference().child("RestaurantGenie").child(Common.currentUser.getBusinessNumber()).child("Tokens");

        tokens.child(item.getPhone())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Token token = dataSnapshot.getValue(Token.class);

                            Map<String, String> dataSend = new HashMap<>();
                            if (item.getStatus().equals("0")) {
                                dataSend.put("title", "Restaurant Genie");
                                dataSend.put("message", "Status of order is: Placed");
                            } else if (item.getStatus().equals("1")) {
                                dataSend.put("title", "Restaurant Genie");
                                dataSend.put("message", "Status of order is: Processing");
                            } else if (item.getStatus().equals("2")) {
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
}
