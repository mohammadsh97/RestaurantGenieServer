package com.mohammadsharabati.restaurantgenieserver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import info.hoang8f.widget.FButton;
import io.paperdb.Paper;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mohammadsharabati.restaurantgenieserver.Common.Common;
import com.mohammadsharabati.restaurantgenieserver.Interface.ItemClickListener;
import com.mohammadsharabati.restaurantgenieserver.Model.User;
import com.mohammadsharabati.restaurantgenieserver.ViewHolder.StaffViewHolder;
import com.rengwuxian.materialedittext.MaterialEditText;


public class AddStaff extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference Staffs;
    private RecyclerView recycler_staff;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseRecyclerOptions<User> options;
    private FirebaseRecyclerAdapter<User, StaffViewHolder> adapter;
    private boolean flag_btnAddStaff = true;


    //Add New Menu layout
    MaterialEditText edtUserName, edtPassword, edtPhoneNumber, edtEmail;
    FButton btnAddStaff, btnUpdateStaff;

    // New Staff
    User newStaff;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_staff);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Staff");
        setSupportActionBar(toolbar);

        //Init firebase
        database = FirebaseDatabase.getInstance();
        Staffs = database.getReference().child("RestaurantGenie").child(Common.currentUser.getBusinessNumber()).child("Worker").child("Staff");

        // Click on Floating Action button to add new Staff
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        //load Staff
        recycler_staff = (RecyclerView) findViewById(R.id.recycler_staff);
        recycler_staff.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recycler_staff.setLayoutManager(mLayoutManager);
        loadStaff();


    }

    private void loadStaff() {
        options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(Staffs, User.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<User, StaffViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull StaffViewHolder holder, int position, @NonNull User model) {
                holder.user_name_staff.setText(model.getName());
                holder.password_staff.setText(model.getPassword());
                holder.phone_number_staff.setText(model.getPhone());
                holder.email_staff.setText(model.getEmail());

                holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showUpdateDialog(adapter.getRef(position).getKey(), adapter.getItem(position));
                    }
                });

                holder.btnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteStaff(adapter.getRef(position).getKey());
                    }
                });
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                    }
                });
            }

            @NonNull
            @Override
            public StaffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.staff_item, parent, false);
                return new StaffViewHolder(view);
            }
        };
        adapter.startListening();
        adapter.notifyDataSetChanged(); // Refresh data if have data changed
        recycler_staff.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Fix click back on FoodDetail and get no item in FoodList
        if (adapter != null)
            adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null)
            adapter.stopListening();
    }

    /**
     * Adding new Staff
     */

    private void showDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddStaff.this, R.style.DialogTheme);
        alertDialog.setTitle("Add new Staff");
        alertDialog.setMessage("Please fill full information");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_staff_layout = inflater.inflate(R.layout.add_new_staff_layout, null);

        edtEmail = add_staff_layout.findViewById(R.id.edtEmail);
        edtPhoneNumber = add_staff_layout.findViewById(R.id.edtPhoneNumber);
        edtUserName = add_staff_layout.findViewById(R.id.edtUserName);
        edtPassword = add_staff_layout.findViewById(R.id.edtPassword);

        btnAddStaff = add_staff_layout.findViewById(R.id.btnAddStaff);

        alertDialog.setView(add_staff_layout);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        // event for button
        btnAddStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edtUserName.getText().toString().trim().isEmpty() && !edtPassword.getText().toString().trim().isEmpty() && !edtPhoneNumber.getText().toString().trim().isEmpty()) {


                    //Int Firebase
                    final FirebaseDatabase db = FirebaseDatabase.getInstance();
                    final DatabaseReference table_user = db.getReference("RestaurantGenie");

                    final ProgressDialog mDialog = new ProgressDialog(AddStaff.this);
                    mDialog.setMessage("Please waiting...");
                    mDialog.show();

                    table_user.addListenerForSingleValueEvent(new  ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            mDialog.dismiss();
                            for (DataSnapshot snapshot : dataSnapshot.child(Common.currentUser.getBusinessNumber()).child("Worker").child("Staff").getChildren()) {

                                User model = snapshot.getValue(User.class);

                                // check Name and password for staff
                                if (model.getName().equals(edtUserName.getText().toString().trim())) {
                                    Toast.makeText(AddStaff.this, "The username is already exist", Toast.LENGTH_SHORT).show();
                                    flag_btnAddStaff = false;
                                    break;
                                }
                            }

                            if (flag_btnAddStaff == true) {
                                newStaff = new User(Common.currentUser.getBusinessNumber(), edtEmail.getText().toString()
                                        , edtPhoneNumber.getText().toString(), edtUserName.getText().toString()
                                        , edtPassword.getText().toString());

                                if (newStaff != null) {
                                    Staffs.push().setValue(newStaff);
                                }
                                Toast.makeText(AddStaff.this, "The staff is added", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                flag_btnAddStaff = true;
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                } else
                    Toast.makeText(AddStaff.this, "You must enter user name and password and phone number", Toast.LENGTH_SHORT).show();
            }
        });


        // set button
        alertDialog.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        flag_btnAddStaff = true;
        alertDialog.show();
    }

    /**
     * Adding delet Staff
     */
    private void deleteStaff(String key) {
        Staffs.child(key).removeValue();
        adapter.notifyDataSetChanged();

    }


    private void showUpdateDialog(String key, final User user) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddStaff.this);
        alertDialog.setTitle("Update Staff");
        alertDialog.setMessage("Please fill full information");

        LayoutInflater inflater = this.getLayoutInflater();
        View upload_staff_layout = inflater.inflate(R.layout.upload_staff_layout, null);

        alertDialog.setView(upload_staff_layout);

        final String localKey = key;

        edtEmail = upload_staff_layout.findViewById(R.id.edtEmail);
        edtPhoneNumber = upload_staff_layout.findViewById(R.id.edtPhoneNumber);
        edtUserName = upload_staff_layout.findViewById(R.id.edtUserName);
        edtPassword = upload_staff_layout.findViewById(R.id.edtPassword);
        btnUpdateStaff = upload_staff_layout.findViewById(R.id.btnUpdateStaff);

        edtEmail.setText(user.getEmail());
        edtPhoneNumber.setText(user.getPhone());
        edtUserName.setText(user.getName());
        edtPassword.setText(user.getPassword());

        btnUpdateStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!edtUserName.getText().toString().isEmpty() && !edtPassword.getText().toString().isEmpty()) {

                    newStaff = new User(Common.currentUser.getBusinessNumber(), edtEmail.getText().toString()
                            , edtPhoneNumber.getText().toString(), edtUserName.getText().toString()
                            , edtPassword.getText().toString());

                    if (newStaff != null) {
                        Staffs.child(localKey).setValue(newStaff);
                    }
                    Toast.makeText(AddStaff.this, "The staff is Updated", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(AddStaff.this, "You must enter user name and password", Toast.LENGTH_SHORT).show();

                adapter.notifyDataSetChanged(); //Add to update item all
            }
        });

        alertDialog.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }
}
