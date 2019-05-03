package com.example.customer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    ListView orderListView;
    DatabaseReference databaseReference;
    ArrayList<ItemsOrdered> allItemsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        orderListView = (ListView) findViewById(R.id.ordersListView);
        databaseReference = FirebaseDatabase.getInstance().getReference("itemsOrdered");

        /* We take the items sent by the previous activity*/
        Intent myIntent = getIntent();

        allItemsList = new ArrayList<>();


    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allItemsList.clear();
                for(DataSnapshot itemSnapshot : dataSnapshot.getChildren()){
                    ItemsOrdered items = itemSnapshot.getValue(ItemsOrdered.class);
                    allItemsList.add(items);
                }
                ItemsOrderedAdapter itemsOrderAdapter = new ItemsOrderedAdapter(getApplicationContext(), allItemsList);
                orderListView.setAdapter(itemsOrderAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
