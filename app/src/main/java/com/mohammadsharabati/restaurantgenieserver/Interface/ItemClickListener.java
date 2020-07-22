package com.mohammadsharabati.restaurantgenieserver.Interface;

import android.view.View;

public interface ItemClickListener {
    void onClick(View view, int position, boolean isLongClick);
    void onRemove(View view,int position, boolean isLongClick);
    void onDetail(int position);
}
