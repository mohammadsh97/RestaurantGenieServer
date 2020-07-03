package com.mohammadsharabati.restaurantgenieserver.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.mohammadsharabati.restaurantgenieserver.Common.Common;
import com.mohammadsharabati.restaurantgenieserver.Interface.ItemClickListener;
import com.mohammadsharabati.restaurantgenieserver.Model.DataMessage;
import com.mohammadsharabati.restaurantgenieserver.Model.MyResponse;
import com.mohammadsharabati.restaurantgenieserver.Model.RequestWithKey;
import com.mohammadsharabati.restaurantgenieserver.Model.Token;
import com.mohammadsharabati.restaurantgenieserver.OrderDetail;
import com.mohammadsharabati.restaurantgenieserver.R;
import com.mohammadsharabati.restaurantgenieserver.Remote.APIService;
import com.mohammadsharabati.restaurantgenieserver.ViewHolder.OrderViewHolder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterOrderStatus extends RecyclerView.Adapter<OrderViewHolder> {

    public List<RequestWithKey> list;
    private Context context;
    private MaterialSpinner spinner;
    private FirebaseDatabase database;
    private DatabaseReference requests;
    private APIService mService;

    public AdapterOrderStatus(List<RequestWithKey> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_layout, parent, false);

        mService = Common.getFCMService();

        return new OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder viewHolder, int position) {

        //Init Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference().child("RestaurantGenie").child(Common.currentUser.getBusinessNumber()).child("Requests");


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
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());

                alertDialog.setTitle("Update Order");
                alertDialog.setMessage("Please Choose Status");

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View update_order_layout = inflater.inflate(R.layout.update_order_layout, null);

                spinner = (MaterialSpinner) update_order_layout.findViewById(R.id.statusSpinner);
                spinner.setItems("Placed","Processing","Ready, On the way to you");

                alertDialog.setView(update_order_layout);

                final String localKey = list.get(position).getKey();
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        list.get(position).setStatus(String.valueOf(spinner.getSelectedIndex()));
                        requests.child(localKey).setValue(list.get(position));
                        Log.v("errorrrr" , ""+list.get(position).getName());
                        sendOrderStatusToUser(list.get(position));
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
        });

        if (Common.userManger == true) {
            viewHolder.btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteOrder(list.get(position).getKey());
                }
            });
        }

        viewHolder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent orderDetailIntent = new Intent(context.getApplicationContext() , OrderDetail.class);
                Common.currentRequestWithKey = list.get(position);
                orderDetailIntent.putExtra("OrderId", list.get(position).getKey());
                orderDetailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(orderDetailIntent);
            }
        });

        viewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Toast.makeText(context.getApplicationContext(), "your order status is: " + Common.convertCodeToStatus(list.get(position).getStatus()), Toast.LENGTH_SHORT).show();
            }
        });

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
                                                Toast.makeText(context.getApplicationContext(), "Order was updated", Toast.LENGTH_SHORT).show();

                                            } else {
                                                Toast.makeText(context.getApplicationContext(), "Order was updated but failed to send notification !", Toast.LENGTH_SHORT).show();
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

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void deleteOrder(String key) {
        requests.child(key).removeValue();
    }
}
