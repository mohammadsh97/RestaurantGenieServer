package com.mohammadsharabati.restaurantgenieserver;

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
                } else {
                    Intent signIn = new Intent(MainActivity.this, SignIn.class);
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

        users.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Chech if user not exist in databases
                if (dataSnapshot.child(BusinessNumber).exists()) {
                    mDialog.dismiss();

                    for (DataSnapshot snapshot : dataSnapshot.child(BusinessNumber).child("Worker").child("Staff").getChildren()) {
                        User model = snapshot.getValue(User.class);
                        // check Name and password for staff
                        if (model.getName().equals(Name) && model.getPassword().equals(password)) {
                            // Login ok
                            Common.currentUser = model;
                            Intent OrderStatusIntent = new Intent(MainActivity.this, OrderStatus.class);
                            startActivity(OrderStatusIntent);
                            finish();

                            Toast.makeText(MainActivity.this, "Im " + Name, Toast.LENGTH_SHORT).show();
                        }
                    }

                    for (DataSnapshot snapshot : dataSnapshot.child(BusinessNumber).child("Worker").child("Manger").getChildren()) {
                        User model = snapshot.getValue(User.class);
                        // check Name and password for staff
                        if (model.getName().equals(Name) && model.getPassword().equals(password)) {
                            Common.currentUser = model;
                            Intent HomeIntent = new Intent(MainActivity.this, Home.class);
                            startActivity(HomeIntent);
                            finish();

                        }
                    }

                    if (Common.currentUser == null) {
                        Toast.makeText(MainActivity.this, "Wrong !!!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    mDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Restaurant not exist in Database", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
