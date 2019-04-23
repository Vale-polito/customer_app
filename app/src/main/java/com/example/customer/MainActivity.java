package com.example.customer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //***********Define variables to save them
    private TextView txt_Name,txt_Lastname,txt_Mail,txt_Phone,txt_Address,txt_Description;
    private de.hdodenhof.circleimageview.CircleImageView imageView;
    String myUri;
    private Uri image_uri;
    public static final String SHARED_PREFS="sharedPrefs";
    public static final String name_save="name_save";
    public static final String lastname_save="lastname_save";
    public static final String phone_save="phone_save";
    public static final String address_save="address_save";
    public static final String mail_save="mail_save";
    public static final String description_save="description_save";
    public static final String image_save="image_save";
    private String name_s,lastname_s,phone_s,mail_s,address_s,description_s;
    //End of save for this part

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //***********start of code related to create a toolbar
        //we have to write codes in different functions
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //end of code related to toolbar

        //*********Start of getting information from another activity
        Intent intent= getIntent();
        String name= intent.getStringExtra(EditActivity.EXTRA_NAME);
        String lastname= intent.getStringExtra(EditActivity.EXTRA_LASTNAME);
        String phone= intent.getStringExtra(EditActivity.EXTRA_PHONE);
        String mail= intent.getStringExtra(EditActivity.EXTRA_MAIL);
        String address= intent.getStringExtra(EditActivity.EXTRA_ADDRESS);
        String description= intent.getStringExtra(EditActivity.EXTRA_DESCRIPTION);
        String image =intent.getStringExtra(EditActivity.EXTRA_IMAGE);



        txt_Name = (TextView) findViewById(R.id.etName);
        txt_Lastname = (TextView) findViewById(R.id.etLastname);
        txt_Phone = (TextView) findViewById(R.id.etPhone);
        txt_Mail = (TextView) findViewById(R.id.etMail);
        txt_Address = (TextView) findViewById(R.id.etAddress);
        txt_Description = (TextView) findViewById(R.id.etDescription);
        imageView= findViewById(R.id.imageView);

        txt_Name.setText(name);
        txt_Lastname.setText(lastname);
        txt_Phone.setText(phone);
        txt_Mail.setText(mail);
        txt_Address.setText(address);
        txt_Description.setText(description);
        if (myUri != null) {
            image_uri = Uri.parse(myUri);
            imageView.setImageURI(image_uri);
        } else {
            int drawableResource = R.drawable.ic_account;
            Drawable d = getResources().getDrawable(drawableResource);
            image_uri = Uri.parse("android.resource://com.example.myapplication/drawable/" +R.drawable.ic_account);
            imageView.setImageDrawable(d);
        }

        //End of getting info, we also have changed the manifest file and select parent activity
        //******we use these 2 function for save in local storage or shared preference
        loadData();
        updateViews();
        //........



    }
    //**************These codes belong to what toolbar is doing
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.editmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle toolbar item clicks here.
        int id = item.getItemId();
        //If Edit_button has been pressed go to the Edit activity
        if (id == R.id.btn_edit) {
            Intent i = new Intent(this, EditActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
    //End of code related to the toolbar

    @Override
    protected void onPause(){
        super.onPause();
        saveData();
    }

    // ****************Svae data by using shared preference
    //**** first we have to save every thing
    public void saveData(){
        SharedPreferences sharedPreferences= getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString(name_save,txt_Name.getText().toString());
        editor.putString(lastname_save,txt_Lastname.getText().toString());
        editor.putString(mail_save,txt_Mail.getText().toString());
        editor.putString(phone_save,txt_Phone.getText().toString());
        editor.putString(address_save,txt_Address.getText().toString());
        editor.putString(description_save,txt_Description.getText().toString());
        editor.putString(image_save,String.valueOf(image_uri));
        editor.apply();
         }
    //*******************Load date
    public void loadData(){
        SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        name_s=sharedPreferences.getString(name_save,"");
        lastname_s=sharedPreferences.getString(lastname_save,"");
        mail_s=sharedPreferences.getString(mail_save,"");
        phone_s=sharedPreferences.getString(phone_save,"");
        address_s=sharedPreferences.getString(address_save,"");
        description_s=sharedPreferences.getString(description_save,"");
        myUri=sharedPreferences.getString(image_save,"null");

    }
    //*************Update
    public void updateViews(){
        txt_Name.setText(name_s);
        txt_Lastname.setText(lastname_s);
        txt_Mail.setText(mail_s);
        txt_Phone.setText(phone_s);
        txt_Address.setText(address_s);
        txt_Description.setText(description_s);
        if (myUri != null && myUri.compareTo("null") != 0) {
            image_uri = Uri.parse(myUri);
            imageView.setImageURI(image_uri);
        } else {
            int drawableResource = R.drawable.ic_account;
            Drawable d = getResources().getDrawable(drawableResource);
            imageView.setImageDrawable(d);
        }

    }
    //End of shared preference
}
