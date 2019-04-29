package com.example.customer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //Define variables to show image and name in header
    de.hdodenhof.circleimageview.CircleImageView imgProfileNav;
    TextView txtFullNameNav;
    //to do logout we need it
    private FirebaseAuth firebaseAuth;
    private RecyclerView  mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Easy Eat");
        setSupportActionBar(toolbar);

        // ...define firebase Auth
        firebaseAuth= FirebaseAuth.getInstance();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Put name and Image in header view
        View headView= navigationView.getHeaderView(0);
        imgProfileNav=headView.findViewById(R.id.imgProfileNav);
        txtFullNameNav=headView.findViewById(R.id.txtFullNameNav);
        //getting Image from profile
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("CustomersProfile");
        databaseReference1.child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CustomersProfile customersProfile=dataSnapshot.getValue(CustomersProfile.class);
                txtFullNameNav.setText(customersProfile.getName());
                Picasso.get()
                        .load(customersProfile.getImageUrl())
                        .placeholder(R.drawable.personal)
                        .fit()
                        .centerCrop()
                        .into(imgProfileNav);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Home.this,databaseError.getCode(),Toast.LENGTH_LONG).show();
            }
        });

        //...............................
        //Initiate Receycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_food);
        new FirebaseDatabaseHelper().readFoods(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<DailyOffer> dailyOffers, List<String> keys) {
                findViewById(R.id.loading_foods_pb).setVisibility(View.GONE);
                new RecyclerView_Config().setConfig(mRecyclerView,Home.this,dailyOffers,keys);
            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });

    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }
        if (id == R.id.action_lowPrice) {
            return true;
        }
        if (id == R.id.action_highDiscount) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_log_out) {
            // Handle the log out
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(Home.this,LoginActivity.class));

        }
        if (id == R.id.nav_profile) {
            // Handle the log out
            finish();
            startActivity(new Intent(Home.this,ProfileActivity.class));

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
