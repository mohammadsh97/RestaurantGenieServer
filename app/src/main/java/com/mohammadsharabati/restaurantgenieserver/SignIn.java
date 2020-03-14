package com.mohammadsharabati.restaurantgenieserver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignIn extends AppCompatActivity {

    private MaterialEditText edtBusinessNumber, edtName, edtPassword;
    private Button btnSignIn;
    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtBusinessNumber = (MaterialEditText) findViewById(R.id.edtBusinessNumber);
        edtName = (MaterialEditText) findViewById(R.id.edtName);
        edtPassword = (MaterialEditText) findViewById(R.id.edtPassword);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);

        //Int Firebase
        db = FirebaseDatabase.getInstance();
        users = db.getReference("RestaurantGenie");


        // sign in
        onClickSignIn();
    }

    /**
     * Clicking sign in button
     */
    private void onClickSignIn() {
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInUser(edtBusinessNumber.getText().toString(), edtName.getText().toString(), edtPassword.getText().toString());
            }
        });
    }

    /**
     * Sign in to app
     */
    private void signInUser(final String BusinessNumber, final String Name, final String password) {
        final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
        mDialog.setMessage("Please waiting...");
        mDialog.show();

        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Chech if user not exist in databases
                if (dataSnapshot.child(BusinessNumber).exists()) {
                    // Get user information
                    mDialog.dismiss();
                    User userManger = dataSnapshot.child(BusinessNumber).child("Worker").child("Manger").getValue(User.class);
                    User userWorker = dataSnapshot.child(BusinessNumber).child("Worker").child("Staff").getValue(User.class);
                    userManger.setBusinessNumber(BusinessNumber);
                    userWorker.setBusinessNumber(BusinessNumber);

                    // check password
                    if (userWorker.getName().equals(Name) && userWorker.getPassword().equals(password)) {
                        // Login ok

                        Toast.makeText(SignIn.this, "Im Staff!", Toast.LENGTH_SHORT).show();

                    } else if (userManger.getName().equals(Name) && userManger.getPassword().equals(password)) {
                        Intent HomeIntent = new Intent(SignIn.this, Home.class);
                        Common.currentUser = userManger;
                        startActivity(HomeIntent);
                        finish();
                    } else {
                        Toast.makeText(SignIn.this, "Wrong !!!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    mDialog.dismiss();
                    Toast.makeText(SignIn.this, "Restaurant not exist in Database", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
