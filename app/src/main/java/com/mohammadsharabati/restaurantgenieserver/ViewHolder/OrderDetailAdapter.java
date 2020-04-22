package com.mohammadsharabati.restaurantgenieserver.ViewHolder;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mohammadsharabati.restaurantgenieserver.Model.Order;
import com.mohammadsharabati.restaurantgenieserver.OrderDetail;
import com.mohammadsharabati.restaurantgenieserver.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


class MyViewHolder extends RecyclerView.ViewHolder {
    TextView name, quantity, price, discount;
    public ImageView order_image;

    MyViewHolder(View itemView) {
        super(itemView);
        order_image = (ImageView) itemView.findViewById(R.id.order_image);
        name = (TextView) itemView.findViewById(R.id.product_name);
        quantity = (TextView) itemView.findViewById(R.id.product_quantity);
        price = (TextView) itemView.findViewById(R.id.product_price);
        discount = (TextView) itemView.findViewById(R.id.product_discount);

    }
}

public class OrderDetailAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private List<Order> myOrder;
    private OrderDetail orderDetail;

    public OrderDetailAdapter(List<Order> myOrder,OrderDetail orderDetail) {
        this.myOrder = myOrder;
        this.orderDetail = orderDetail;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.order_detail_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Order order = myOrder.get(position);
        Picasso.with(orderDetail.getBaseContext())
                .load(myOrder.get(position).getImage())
                .resize(70, 70) // 70dp
                .centerCrop()
                .into(holder.order_image);
        holder.name.setText(String.format("Name : %s", order.getProductName()));
        holder.quantity.setText(String.format("Quantity : %s", order.getQuantity()));
        holder.price.setText(String.format("Price : ₪ %s", order.getPrice()));
        holder.discount.setText(String.format("Discount : %s", order.getDiscount()));

    }

    @Override
    public int getItemCount() {
        return myOrder.size();
    }
}

