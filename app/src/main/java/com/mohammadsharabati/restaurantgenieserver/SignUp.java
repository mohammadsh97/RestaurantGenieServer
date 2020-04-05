package com.mohammadsharabati.restaurantgenieserver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

/**
 * Checking when user sign up
 */
public class SignUp extends AppCompatActivity {

    private MaterialEditText edtBusinessNumber, edtEmail, edtPhone, edtName, edtPassword;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtBusinessNumber = (MaterialEditText) findViewById(R.id.edtBusinessNumber);
        edtEmail = (MaterialEditText) findViewById(R.id.edtEmail);
        edtPhone = (MaterialEditText) findViewById(R.id.edtPhone);
        edtName = (MaterialEditText) findViewById(R.id.edtName);
        edtPassword = (MaterialEditText) findViewById(R.id.edtPassword);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);

        //Int Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("RestaurantGenie");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Common.isConnectedToInternet(getBaseContext())) {

                    final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                    mDialog.setMessage("Please waiting...");
                    mDialog.show();

                    table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            //Check if already Business Number
                            if (dataSnapshot.child(edtBusinessNumber.getText().toString()).exists()) {
                                mDialog.dismiss();
                                Toast.makeText(SignUp.this, "Business Number already register", Toast.LENGTH_SHORT).show();
                            } else {
                                mDialog.dismiss();
                                if (edtPhone.getText().toString().length() > 10) {
                                    Toast.makeText(SignUp.this, "You must enter phone number to register!", Toast.LENGTH_SHORT).show();
                                } else if (TextUtils.isEmpty(edtBusinessNumber.getText().toString())) {
                                    Toast.makeText(SignUp.this, "You must enter Business Number to register!", Toast.LENGTH_SHORT).show();
                                } else if (TextUtils.isEmpty(edtEmail.getText().toString())) {
                                    Toast.makeText(SignUp.this, "You must enter Email to register!", Toast.LENGTH_SHORT).show();
                                } else if (TextUtils.isEmpty(edtPhone.getText().toString())) {
                                    Toast.makeText(SignUp.this, "You must enter Phone number to register!", Toast.LENGTH_SHORT).show();
                                } else if (TextUtils.isEmpty(edtName.getText().toString())) {
                                    Toast.makeText(SignUp.this, "You must enter Name to register!", Toast.LENGTH_SHORT).show();
                                } else if (TextUtils.isEmpty(edtPassword.getText().toString())) {
                                    Toast.makeText(SignUp.this, "You must enter Password to register!", Toast.LENGTH_SHORT).show();
                                } else {
                                    User user = new User(edtBusinessNumber.getText().toString(), edtEmail.getText().toString(), edtPhone.getText().toString(), edtName.getText().toString(), edtPassword.getText().toString());
                                    table_user.child(edtBusinessNumber.getText().toString()).child("Worker").child("Manger").push().setValue(user);
                                    Toast.makeText(SignUp.this, "sign up successfully !", Toast.LENGTH_SHORT).show();
                                    Common.currentUser = user;
                                    Intent HomeIntent = new Intent(SignUp.this, Home.class);
                                    startActivity(HomeIntent);
                                    finish();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else {
                    Toast.makeText(SignUp.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
}