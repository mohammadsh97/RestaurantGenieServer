package com.mohammadsharabati.restaurantgenieserver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import info.hoang8f.widget.FButton;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.mohammadsharabati.restaurantgenieserver.ViewHolder.TableViewHolder;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

public class AddTable extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference tables;
    private RecyclerView recycler_staff;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseRecyclerOptions<User> options;
    private FirebaseRecyclerAdapter<User, TableViewHolder> adapter;

    private boolean flag_btnAddTable = true;

    //Add New Menu layout
    MaterialEditText edtUserName, edtPassword, edtPhoneNumber;
    FButton btnAddTable, btnUpdateTable;

    // New Table
    User newTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_table);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Table");
        setSupportActionBar(toolbar);

        //Init firebase
        database = FirebaseDatabase.getInstance();
        tables = database.getReference().child("RestaurantGenie").child(Common.currentUser.getBusinessNumber()).child("Worker").child("Table");

        // Click on Floating Action button to add new Staff
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_table);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        //load Staff
        recycler_staff = (RecyclerView) findViewById(R.id.recycler_table);
        recycler_staff.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recycler_staff.setLayoutManager(mLayoutManager);
        loadTable();

    }

    private void loadTable() {

        options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(tables, User.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<User, TableViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull TableViewHolder holder, int position, @NonNull User model) {
                holder.user_name_table.setText(model.getName());
                holder.password_table.setText(model.getPassword());
                holder.phone_number_table.setText(model.getPhone());

                holder.btnEditTable.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showUpdateDialog(adapter.getRef(position).getKey(), adapter.getItem(position));
                    }
                });

                holder.btnRemoveTable.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteTable(adapter.getRef(position).getKey());
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
            public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.table_item, parent, false);
                return new TableViewHolder(view);
            }
        };
        adapter.startListening();
        adapter.notifyDataSetChanged(); // Refresh data if have data changed
        recycler_staff.setAdapter(adapter);
    }

    /**
     * Adding new Table
     */
    private void showDialog() {
        List<String> listNameTable = new ArrayList<>();
        List<String> listPhoneTable = new ArrayList<>();

//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddTable.this, R.style.DialogTheme);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddTable.this);
        alertDialog.setTitle("Add new Table");
        alertDialog.setMessage("Please fill full information");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_table_layout = inflater.inflate(R.layout.add_new_table_layout, null);

        edtPhoneNumber = add_table_layout.findViewById(R.id.edtPhoneNumber);
        edtUserName = add_table_layout.findViewById(R.id.edtUserName);
        edtPassword = add_table_layout.findViewById(R.id.edtPassword);

        btnAddTable = add_table_layout.findViewById(R.id.btnAddTable);

        alertDialog.setView(add_table_layout);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        tables.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    listNameTable.add(snapshot.getValue(User.class).getName());
                    listPhoneTable.add(snapshot.getValue(User.class).getPhone());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        // event for button
        btnAddTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edtUserName.getText().toString().isEmpty() && !edtPassword.getText().toString().isEmpty() && !edtPhoneNumber.getText().toString().trim().isEmpty()) {
                    if (listNameTable.contains(edtUserName.getText().toString().trim())) {
                        Toast.makeText(AddTable.this, "Your name is exist", Toast.LENGTH_SHORT).show();
                    } else if (listPhoneTable.contains(edtPhoneNumber.getText().toString().trim())) {
                        Toast.makeText(AddTable.this, "Your phone number is exist", Toast.LENGTH_SHORT).show();
                    } else {
                        //Int Firebase
                        final FirebaseDatabase db = FirebaseDatabase.getInstance();
                        final DatabaseReference table_user = db.getReference("RestaurantGenie");

                        final ProgressDialog mDialog = new ProgressDialog(AddTable.this);
                        mDialog.setMessage("Please waiting...");
                        mDialog.show();

                        table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                mDialog.dismiss();
                                for (DataSnapshot snapshot : dataSnapshot.child(Common.currentUser.getBusinessNumber()).child("Worker").child("Table").getChildren()) {

                                    User model = snapshot.getValue(User.class);

                                    // check Name and password for staff
                                    if (model.getName().equals(edtUserName.getText().toString().trim())) {
                                        Toast.makeText(AddTable.this, "The username is already exist", Toast.LENGTH_SHORT).show();
                                        flag_btnAddTable = false;
                                        break;
                                    }
                                }

                                if (flag_btnAddTable == true) {
                                    newTable = new User(Common.currentUser.getBusinessNumber(), ""
                                            , edtPhoneNumber.getText().toString(), edtUserName.getText().toString()
                                            , edtPassword.getText().toString());

                                    if (newTable != null) {
                                        tables.push().setValue(newTable);
                                    }
                                    Toast.makeText(AddTable.this, "The staff is added", Toast.LENGTH_SHORT).show();
                                } else {
                                    flag_btnAddTable = true;
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                } else
                    Toast.makeText(AddTable.this, "You must enter user name and password", Toast.LENGTH_SHORT).show();
            }
        });

        // set button
        alertDialog.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        flag_btnAddTable = true;
        alertDialog.show();
    }

    private void showUpdateDialog(String key, final User user) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddTable.this);
        alertDialog.setTitle("Update Table");
        alertDialog.setMessage("Please fill full information");

        LayoutInflater inflater = this.getLayoutInflater();
        View upload_staff_layout = inflater.inflate(R.layout.upload_table_layout, null);

        alertDialog.setView(upload_staff_layout);

        final String localKey = key;

        edtPhoneNumber = upload_staff_layout.findViewById(R.id.edtPhoneNumber);
        edtUserName = upload_staff_layout.findViewById(R.id.edtUserName);
        edtPassword = upload_staff_layout.findViewById(R.id.edtPassword);
        btnUpdateTable = upload_staff_layout.findViewById(R.id.btnUpdateTable);

        edtPhoneNumber.setText(user.getPhone());
        edtUserName.setText(user.getName());
        edtPassword.setText(user.getPassword());

        btnUpdateTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!edtUserName.getText().toString().isEmpty() && !edtPassword.getText().toString().isEmpty()) {

                    newTable = new User(Common.currentUser.getBusinessNumber(), ""
                            , edtPhoneNumber.getText().toString(), edtUserName.getText().toString()
                            , edtPassword.getText().toString());

                    if (newTable != null) {
                        tables.child(localKey).setValue(newTable);
                    }
                    Toast.makeText(AddTable.this, "The staff is Updated", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(AddTable.this, "You must enter user name and password", Toast.LENGTH_SHORT).show();

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

    /**
     * Adding delet Table
     */
    private void deleteTable(String key) {
        tables.child(key).removeValue();
        adapter.notifyDataSetChanged();

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
}
