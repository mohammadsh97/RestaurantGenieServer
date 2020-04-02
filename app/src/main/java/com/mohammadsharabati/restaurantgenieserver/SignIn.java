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
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.CheckBox;

public class SignIn extends AppCompatActivity {

    private MaterialEditText edtBusinessNumber, edtName, edtPassword;
    private Button btnSignIn;
    FirebaseDatabase db;
    DatabaseReference users;
    public User userManger;
    public User userWorker;
    CheckBox ckbRemember;
//    TextView txtForgotPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtBusinessNumber = (MaterialEditText) findViewById(R.id.edtBusinessNumber);
        edtName = (MaterialEditText) findViewById(R.id.edtName);
        edtPassword = (MaterialEditText) findViewById(R.id.edtPassword);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        ckbRemember =(CheckBox)findViewById(R.id.ckbRemember);
//        txtForgotPwd =(TextView) findViewById(R.id.txtForgotPwd);

        Paper.init(this);

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

        Paper.init(this);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.isConnectedToInternet(getBaseContext())) {

                    if (edtBusinessNumber.getText().toString().trim().length() != 0)
                        signInUser(edtBusinessNumber.getText().toString(), edtName.getText().toString(), edtPassword.getText().toString());
                    else
                        Toast.makeText(SignIn.this, "Complete the blank sentences!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignIn.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
                    return;
                }
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
                    userManger = dataSnapshot.child(BusinessNumber).child("Worker").child("Manger").getValue(User.class);
                    userWorker = dataSnapshot.child(BusinessNumber).child("Worker").child("Staff").getValue(User.class);
                    userManger.setBusinessNumber(BusinessNumber);
                    userWorker.setBusinessNumber(BusinessNumber);

                    // check Name and password
                    if (userWorker.getName().equals(Name) && userWorker.getPassword().equals(password)) {
                        // Login ok

                        // Save user & Password
                        if (ckbRemember.isChecked()) {

                            Paper.book().write(Common.USER_BN, edtBusinessNumber.getText().toString());
                            Paper.book().write(Common.USER_KEY, edtName.getText().toString());
                            Paper.book().write(Common.PWD_KEY, edtPassword.getText().toString());
                        }

                        Intent OrderStatusIntent = new Intent(SignIn.this, OrderStatus.class);
                        Common.currentUser = userWorker;
                        startActivity(OrderStatusIntent);
                        finish();

                        Toast.makeText(SignIn.this, "Im "+Name, Toast.LENGTH_SHORT).show();

                    } else if (userManger.getName().equals(Name) && userManger.getPassword().equals(password)) {

                        // Save user & Password
                        if (ckbRemember.isChecked()) {

                            Paper.book().write(Common.USER_BN, edtBusinessNumber.getText().toString());
                            Paper.book().write(Common.USER_KEY, edtName.getText().toString());
                            Paper.book().write(Common.PWD_KEY, edtPassword.getText().toString());
                        }
                        Intent HomeIntent = new Intent(SignIn.this, Home.class);
                        Common.currentUser = userManger;
                        startActivity(HomeIntent);
                        finish();
                    } else {
                        mDialog.dismiss();
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
