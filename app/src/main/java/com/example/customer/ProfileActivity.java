package com.example.customer;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import android.widget.ImageView;

public class ProfileActivity extends AppCompatActivity {
    TextView tvName,tvEmail,tvAddress,tvPhone,tvDescription;
    de.hdodenhof.circleimageview.CircleImageView imgProfile;

    //...
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //
        tvName=findViewById(R.id.tvName);
        tvEmail=findViewById(R.id.tvMail);
        tvPhone=findViewById(R.id.tvPhone);
        tvAddress=findViewById(R.id.tvAddress);
        tvDescription=findViewById(R.id.tvDescription);
        imgProfile=findViewById(R.id.imgProfile);

        //....
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        //get reference
        //String path=firebaseAuth.getUid();
       // String path1="CustomersProfile/"+firebaseAuth.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("CustomersProfile");
        //DatabaseReference databaseReference1 = databaseReference.child(firebaseAuth.getUid());
        databaseReference.child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CustomersProfile customersProfile=dataSnapshot.getValue(CustomersProfile.class);
                tvName.setText(customersProfile.getName());
                tvAddress.setText(customersProfile.getAddress());
                tvPhone.setText(customersProfile.getPhone());
                tvEmail.setText(customersProfile.getEmail());
                tvDescription.setText(customersProfile.getShortdescription());
                Picasso.get()
                        .load(customersProfile.getImageUrl())
                        .placeholder(R.drawable.personal)
                        .fit()
                        .centerCrop()
                        .into(imgProfile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this,databaseError.getCode(),Toast.LENGTH_LONG).show();
            }
        });

    }
}
