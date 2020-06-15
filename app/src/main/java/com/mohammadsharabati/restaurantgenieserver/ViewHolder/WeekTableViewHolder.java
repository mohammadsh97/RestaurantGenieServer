package com.mohammadsharabati.restaurantgenieserver.ViewHolder;

import android.view.View;
import android.widget.TextView;
import com.mohammadsharabati.restaurantgenieserver.Interface.ItemClickListener;
import com.mohammadsharabati.restaurantgenieserver.R;
import com.mohammadsharabati.restaurantgenieserver.Utils.LetterImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WeekTableViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView tvWeek;
    public LetterImageView ivLetter;

    public ItemClickListener itemClickListener;

    public WeekTableViewHolder(@NonNull View itemView) {
        super(itemView);
        tvWeek = itemView.findViewById(R.id.tvWeek);
        ivLetter = itemView.findViewById(R.id.ivLetter);

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
