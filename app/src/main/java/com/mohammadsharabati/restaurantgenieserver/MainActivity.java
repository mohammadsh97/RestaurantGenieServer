package com.mohammadsharabati.restaurantgenieserver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mohammadsharabati.restaurantgenieserver.Common.Common;
import com.mohammadsharabati.restaurantgenieserver.Model.User;

public class MainActivity extends AppCompatActivity {

    public User userManger;
    public User userWorker;
    Button btnSignIn, btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);

        Paper.init(this);


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bN = Paper.book().read(Common.USER_BN);
                String user = Paper.book().read(Common.USER_KEY);
                String pwd = Paper.book().read(Common.PWD_KEY);
                if (bN != null && user != null && pwd != null) {
                    if (!bN.isEmpty() && !user.isEmpty() && !pwd.isEmpty()) {
                        login(bN, user, pwd);
                    }
                }
                else{
                    Intent signIn = new Intent(MainActivity.this,SignIn.class);
                    startActivity(signIn);
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUp = new Intent(MainActivity.this, SignUp.class);
                startActivity(signUp);
            }
        });


    }

    private void login(final String BusinessNumber, final String Name, final String password) {
        if (Common.isConnectedToInternet(getBaseContext())) {


            if (BusinessNumber.trim().length() != 0)
                signInUser(BusinessNumber, Name, password);
            else
                Toast.makeText(MainActivity.this, "Complete the blank sentences!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
            return;
        }

    }
    private void signInUser(final String BusinessNumber, final String Name, final String password) {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference users = database.getReference("RestaurantGenie");

        final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
        mDialog.setMessage("Please waiting...");
        mDialog.show();

        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Chech if user not exist in databases
                if (dataSnapshot.child(BusinessNumber).exists()) {
                    // Get user information
                    mDialog.dismiss();
                    userManger = dataSnapshot.child(BusinessNumber).child("Worker").child("Manger").getValue(User.class);
                    userWorker = dataSnapshot.child(BusinessNumber).child("Worker").child("Staff").getValue(User.class);
                    userManger.setBusinessNumber(BusinessNumber);
                    userWorker.setBusinessNumber(BusinessNumber);

                    // check Name and password
                    if (userWorker.getName().equals(Name) && userWorker.getPassword().equals(password)) {
                        // Login ok

                        Intent OrderStatusIntent = new Intent(MainActivity.this, OrderStatus.class);
                        Common.currentUser = userWorker;
                        startActivity(OrderStatusIntent);
                        finish();

                        Toast.makeText(MainActivity.this, "Im "+Name, Toast.LENGTH_SHORT).show();

                    } else if (userManger.getName().equals(Name) && userManger.getPassword().equals(password)) {

                        Intent HomeIntent = new Intent(MainActivity.this, Home.class);
                        Common.currentUser = userManger;
                        startActivity(HomeIntent);
                        finish();
                    } else {
                        mDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Wrong !!!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    mDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Restaurant not exist in Database", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
