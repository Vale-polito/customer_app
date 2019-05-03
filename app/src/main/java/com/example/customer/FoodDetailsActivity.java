package com.example.customer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FoodDetailsActivity extends AppCompatActivity {
    private int i;
    String key;
    DatabaseReference databaseReference;
    DatabaseReference databaseItems;

    TextView tvCounter;
    Button btnAdd;
    ImageButton btnDecrease;
    TextView NameofFood,PriceOfFood,DiscountOfFood,DescriptionOfFood;
    ImageView imgFoodDetails;
    ItemsOrdered newItemOrdered;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);
        //
        key = getIntent().getStringExtra("key");
        NameofFood=findViewById(R.id.NameOfFood);
        PriceOfFood=findViewById(R.id.PriceOfFood);
        DiscountOfFood=findViewById(R.id.DiscountOfFood);
        DescriptionOfFood=findViewById(R.id.DescriptionOfFood);
        imgFoodDetails=findViewById(R.id.imgFoodDetails);
        btnAdd = findViewById(R.id.btnAdd);
        readInfo(key);



        //*********Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarBack);
        setSupportActionBar(toolbar);
        //end of toolbar
        //Counter
        i=0;
        ImageButton btnIncrease=findViewById(R.id.btnIncrease);
        btnDecrease=findViewById(R.id.btnDecrease);
        tvCounter=findViewById(R.id.tvCounter);

        if (i==0) btnDecrease.setEnabled(false); else btnDecrease.setEnabled(true);
        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i--;
                if (i==0) btnDecrease.setEnabled(false); else btnDecrease.setEnabled(true);
                tvCounter.setText(""+i);
            }
        });

        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i==0) btnDecrease.setEnabled(false); else btnDecrease.setEnabled(true);
                i++;
                tvCounter.setText(""+i);
            }
        });


        databaseItems = FirebaseDatabase.getInstance().getReference("itemsOrdered");


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newItemOrdered.setQuantity(i);
                if(newItemOrdered.getQuantity()>0) {
                    // CHANGE THIS TO ACTUALLY INSERT THE DATA IN THE DATABASE
                    // THE CART WILL BE OPENED WHILE PRESSING THE CART BUTTON, NOT WHILE ADDING AN ITEM
                    addItem(newItemOrdered);
                    /*Intent addCartIntent = new Intent(getApplicationContext(), CartActivity.class);
                    addCartIntent.putExtra("itemsOrdered", newItemOrdered);
                    startActivity(addCartIntent);*/
                }else{
                    Toast.makeText(getApplicationContext(), "Wrong quantity chosen!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    //********** what toolbar is doing
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.backmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.btn_back) {
            Intent intent = new Intent(this, Home.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    //End of code related to the toolbar

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("counter",i); // Save value of i which is integer into counter

    }
    //To restore the saved value
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        i=savedInstanceState.getInt("counter"); // Value that was saved will restore to variable
        tvCounter.setText(""+i); //tv.setText(Integer.toString(i));
    }
    //read selected photo
    private void readInfo(String key){

        //get reference
        databaseReference = FirebaseDatabase.getInstance().getReference("DailyFoods");
        databaseReference.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DailyOffer dailyOffer=dataSnapshot.getValue(DailyOffer.class);
                NameofFood.setText(dailyOffer.getName());
                DiscountOfFood.setText(dailyOffer.getDiscount()+"% (Off) • ");
                PriceOfFood.setText(dailyOffer.getPrice()+" € •");
                DescriptionOfFood.setText(dailyOffer.getShortdescription());

                Picasso.get()
                        .load(dailyOffer.getImageUrl())
                        .placeholder(R.drawable.personal)
                        .fit()
                        .centerCrop()
                        .into(imgFoodDetails);


                /* Create an item ordered based on the daily offer and the quantity clicked*/
                newItemOrdered = new ItemsOrdered(dailyOffer.getName(), Integer.parseInt(dailyOffer.getPrice()),0, Double.parseDouble(dailyOffer.getDiscount()), dailyOffer.getShortdescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FoodDetailsActivity.this,databaseError.getCode(),Toast.LENGTH_LONG).show();
            }


        });

    }

    /* Method to add item to the database to fill in the cart
     */
    private void addItem(ItemsOrdered currentitem){
        if(!TextUtils.isEmpty(currentitem.getName())){
            String name = databaseItems.push().getKey();
            databaseItems.child(name).setValue(currentitem);
            Toast.makeText(getApplicationContext(), "Item added to cart", Toast.LENGTH_LONG).show();

        }else{
            Toast.makeText(getApplicationContext(), "Error in the submission", Toast.LENGTH_LONG).show();
        }

    }

}
