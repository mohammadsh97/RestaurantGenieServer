package com.mohammadsharabati.restaurantgenieserver;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_time_table);
        setContentView(R.layout.activity_main);

//        Intent signIn = new Intent(MainActivity.this,TimeTable.class);
        Intent signIn = new Intent(MainActivity.this,SignIn.class);
        startActivity(signIn);
    }
}
