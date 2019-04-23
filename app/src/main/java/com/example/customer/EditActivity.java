package com.example.customer;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditActivity extends AppCompatActivity {

    //*******Start of code related to confirm button to transmit info to another activity
    public static final String EXTRA_NAME="com.example.costumer.EXTRA_NAME";
    public static final String EXTRA_LASTNAME="com.example.costumer.EXTRA_LASTNAME";
    public static final String EXTRA_PHONE="com.example.costumer.EXTRA_PHONE";
    public static final String EXTRA_MAIL="com.example.costumer.EXTRA_MAIL";
    public static final String EXTRA_ADDRESS="com.example.costumer.EXTRA_ADDRESS";
    public static final String EXTRA_DESCRIPTION="com.example.costumer.EXTRA_DESCRIPTION";
    public static final String EXTRA_IMAGE="com.example.costumer.EXTRA_IMAGE";
   //End of code related to confirm button in this part

    //***********Define variables to save them
    String myUri;
    private Uri image_uri;
    private de.hdodenhof.circleimageview.CircleImageView viewImage;
    private EditText txt_Name,txt_Lastname,txt_Mail,txt_Phone,txt_Address,txt_Description;
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
    //*********Define variables to read from camera

    private Bitmap imageBitmap;
    private static final int PICK_IMAGE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 3;
    //declare views
    ImageButton btnSelectPhoto;

    // End

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);


        //****************************** Camera
        btnSelectPhoto=findViewById(R.id.btnSelectPhoto);
        viewImage=findViewById(R.id.viewImage);
        btnSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage();
            }
        });
        // End

        //************* Define menubar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //end of code related to toolbar

        //********* Define variable for each view
        txt_Name = findViewById(R.id.etName);
        txt_Lastname = findViewById(R.id.etLastname);
        txt_Mail = findViewById(R.id.etMail);
        txt_Phone = findViewById(R.id.etPhone);
        txt_Address = findViewById(R.id.etAddress);
        txt_Description = findViewById(R.id.etDescription);
        //End of defining variables

        //***Checking validation of Name
        txt_Name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateName(txt_Name.getText().toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        //******* Checking validation of last name
        txt_Lastname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateLastname(txt_Lastname.getText().toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        //**********Checking validation of address
        txt_Address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateAddress(txt_Address.getText().toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        //********Check validation of phone
        txt_Phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatePhone(txt_Phone.getText().toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });



        //*********Start of clicking on Cancel button
        Button btn_Cancel = findViewById(R.id.btnCancel);
        btn_Cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                openMainActivity_cancel();
            }
        });
        //End of click on button CANCEL

        //*********Start of clicking on Confirm button
        Button btn_Confirm = findViewById(R.id.btnConfirm);
        btn_Confirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (!validateName(txt_Name.getText().toString()))
                {
                    txt_Name.requestFocus();

                } else if (!validateLastname(txt_Lastname.getText().toString()))
                {
                    txt_Lastname.requestFocus();
                } else if (!validatePhone(txt_Phone.getText().toString()))
                {
                    txt_Phone.requestFocus();
                }
                else if (!validateEmail(txt_Mail.getText().toString()))
                {
                    txt_Mail.requestFocus();
                } else if (!validateAddress(txt_Address.getText().toString()))
                {
                    txt_Address.requestFocus();
                } else {
                    openMainActivity_confirm();
                    saveData();
                }
            }
        });
        //End of click on button Confirm

        //*****************
        loadData();
        updateViews();
    }
    //************Validate each view
    private boolean validateName(String Name) {
        int characters = Name.trim().length();
        if (characters > 20) {
            txt_Name.setError("Name is too long ( maximum is 20)");
            return false;
        } else if (characters<1){
            txt_Name.setError("Name can not be empty");
            return false;
        }
        else {
            txt_Name.setError(null);
            return true;
        }
    }
    //*********Last name
    private boolean validateLastname(String Lastname) {
        int characters = Lastname.trim().length();
        if (characters > 20) {
            txt_Lastname.setError("Last name is too long ( maximum is 20)");
            return false;
        } else if (characters<1){
            txt_Lastname.setError("Last name can not be empty");
            return false;
        }
        else {
            txt_Lastname.setError(null);
            return true;
        }
    }
    //******************8
    private boolean validateAddress(String Address) {
        int characters = Address.trim().length();
        if (characters > 35) {
            txt_Address.setError("address is too long (max is 35)");
            return false;
        }
        else if (characters<1){
            txt_Address.setError("Address Can not be empty");
            return false;
        } else {
            txt_Address.setError(null);
            return true;
        }
    }
    //*************Validate Phone
    private boolean validatePhone(String Phone) {

        int characters = Phone.trim().length();
        if (characters<1) {
            txt_Phone.setError("Phone can not be empty");
            return false;
        }
        else if (characters > 15 || characters <10){
            txt_Phone.setError("Invalid phone number");
            return false;
        } else {
            String phonePatteren = "^([0-9\\+]|\\(\\d{1,3}\\))[0-9\\-\\. ]{3,15}$";
            Pattern pattern = Pattern.compile(phonePatteren);
            Matcher matcher = pattern.matcher(Phone);
            if (matcher.matches())
            {
                return true;
            }
            else {
                txt_Phone.setError("Invalid phone number");
                return false;
            }
        }
    }
    //...........
    //*******Validate Email
    private boolean validateEmail(String Email) {
        int characters = Email.trim().length();
        if (characters<1){
            return true;
        } else if (characters>25){
            txt_Mail.setError("Invalid Email address(it is too long)");
            return false;
        } else {
            String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
            Pattern pattern = Pattern.compile(emailPattern);
            Matcher matcher = pattern.matcher(Email);
            if (matcher.matches())
            {
                return true;
            }
            else {
                txt_Mail.setError("Invalid Email address ");
                return false;
            }
        }
    }
    //....................
    //**********These codes belong to what toolbar is doing
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.backmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle toolbar item clicks here.
        int id = item.getItemId();
        //If Edit_button has been pressed go to the Edit activity
        if (id == R.id.btn_back) {
            openMainActivity_cancel();
        }
        return super.onOptionsItemSelected(item);
    }
    //End of code related to the toolbar

    //****Rest of codes belong to Cancel button
    public void openMainActivity_cancel(){
        Intent intent= new Intent(this,MainActivity.class);
        intent.putExtra(EXTRA_IMAGE, image_uri);
        setResult(Activity.RESULT_OK, intent);
        startActivity(intent);
        savePhoto();
    }
    //End of Cancel button
    @Override
    protected void onPause(){
        super.onPause();
        savePhoto();
    }

    //****Rest of codes belong to Confirm button
    public void openMainActivity_confirm(){
        //We want to transmit all Views that have been changed
        EditText txt_Name = findViewById(R.id.etName);
        String name=txt_Name.getText().toString();

        EditText txt_Lastname = findViewById(R.id.etLastname);
        String lastname=txt_Lastname.getText().toString();

        EditText txt_Mail = findViewById(R.id.etMail);
        String mail=txt_Mail.getText().toString();

        EditText txt_Phone = findViewById(R.id.etPhone);
        String phone=txt_Phone.getText().toString();

        EditText txt_Address = findViewById(R.id.etAddress);
        String address=txt_Address.getText().toString();

        EditText txt_Description = findViewById(R.id.etDescription);
        String description=txt_Description.getText().toString();
        // TRANSMIT ALL VALUES AS EXTRNAL
        Intent intent= new Intent(this,MainActivity.class);
        intent.putExtra(EXTRA_NAME,name);
        intent.putExtra(EXTRA_LASTNAME,lastname);
        intent.putExtra(EXTRA_PHONE,phone);
        intent.putExtra(EXTRA_MAIL,mail);
        intent.putExtra(EXTRA_ADDRESS,address);
        intent.putExtra(EXTRA_DESCRIPTION,description);
        intent.putExtra(EXTRA_IMAGE, image_uri);
        setResult(Activity.RESULT_OK, intent);
        //finish();
        startActivity(intent);
    }
    //End of Confirm button

    // *****************Camera
    // *****************This part create dialog box
    private void selectImage() {
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Delete" };
        AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);

        builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    openCamera();
                    savePhoto();
                }
                else if (options[item].equals("Choose from Gallery"))
               {
                   openGallery();
                   savePhoto();
                }
                else if (options[item].equals("Delete")) {
                    int drawableResource = R.drawable.ic_account;
                    Drawable d = getResources().getDrawable(drawableResource);
                    image_uri = Uri.parse("android.resource://com.example.myapplication/drawable/" +R.drawable.ic_account);
                    viewImage.setImageDrawable(d);
                    savePhoto();
                    dialog.dismiss();
                }
            }
        });


        builder.show();
    }

    //..........................
    //*****Open Gallery
    public void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
    //.....................

    //***********Open Camera
    public void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    //........................
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            image_uri = data.getData();
            viewImage.setImageURI(image_uri);
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            image_uri = getImageUri(this, imageBitmap);
            viewImage.setImageURI(image_uri);
        }
    }
    //..................
    //*****
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    //..................
    //******* We want when we rotate screen image does not change
    //We use these 2 below fuctions
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (image_uri != null) {
            outState.putString("image", image_uri.toString());
        }
    }
   @Override
   protected void onRestoreInstanceState(Bundle savedInstanceState) {
       super.onRestoreInstanceState(savedInstanceState);
       String image=savedInstanceState.getString("image",""); // Value that was saved will restore to variable
       image_uri = Uri.parse(image);
       viewImage.setImageURI(image_uri);
   }
   //...................................
    //*********save photo

    public   void savePhoto(){
        //Save
        SharedPreferences sharedPreferences= getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString(image_save,String.valueOf(image_uri));
        editor.apply();
        // Load
        myUri=sharedPreferences.getString(image_save,"null");
        // Update
        if (myUri != null) {
            image_uri = Uri.parse(myUri);
            viewImage.setImageURI(image_uri);
        } else {
            int drawableResource = R.drawable.ic_account;
            Drawable d = getResources().getDrawable(drawableResource);
            viewImage.setImageDrawable(d);
        }
   }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length == 1 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Intent ris = new Intent(this, MainActivity.class);
                setResult(Activity.RESULT_CANCELED, ris);
                finish();
            }
        }
    }

    // ****************Svae data by using share preference
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
        Toast.makeText(this,"Data saved",Toast.LENGTH_SHORT).show();

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
        if (myUri != null) {
            image_uri = Uri.parse(myUri);
            viewImage.setImageURI(image_uri);
        } else {
            int drawableResource = R.drawable.ic_account;
            Drawable d = getResources().getDrawable(drawableResource);
            viewImage.setImageDrawable(d);
        }
    }

}
