package com.mohammadsharabati.restaurantgenieserver;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mohammadsharabati.restaurantgenieserver.Common.Common;
import com.mohammadsharabati.restaurantgenieserver.Model.Food;
import java.util.ArrayList;
/**
 * Created by Mohammad Sharabati.
 */
public class Chart extends AppCompatActivity {

    private BarChart barChart;
    private FirebaseDatabase database;
    private DatabaseReference charts;

    ArrayList<BarEntry> barEntryArrayList = new ArrayList<>();
    ArrayList<String> names = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        barChart = findViewById(R.id.chart);
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(false);

        database = FirebaseDatabase.getInstance();
        charts = database.getReference().child("RestaurantGenie").child(Common.currentUser.getBusinessNumber()).child("Foods");

        charts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                barEntryArrayList.clear();
                names.clear();
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Food foodChartModel = snapshot.getValue(Food.class);
                    barEntryArrayList.add(new BarEntry(i, Integer.parseInt(foodChartModel.getCounter())));
                    names.add(foodChartModel.getName());
                    i++;
                }

                BarDataSet barDataSet = new BarDataSet(barEntryArrayList, "Food Sales");
                barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                Description description = new Description();
                description.setText("X-Axis: Name of Food, Y-Axis: Food Sales");
                description.setTextSize(15);

                barChart.setDescription(description);

                BarData barData = new BarData(barDataSet);

                barChart.setData(barData);

                //  We need to add X-Axis value format


                barChart.getXAxis().setPosition(XAxis.XAxisPosition.TOP);
                barChart.getXAxis().setDrawGridLines(false);
                barChart.getXAxis().setDrawAxisLine(false);
                barChart.getXAxis().setGranularity(1f);
                barChart.getXAxis().setLabelCount(names.size());
                barChart.getXAxis().setLabelRotationAngle(270);
                //  Set postion of labels
                barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(names));
                barChart.fitScreen();
                barChart.animateY(2000);
                barChart.invalidate();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}