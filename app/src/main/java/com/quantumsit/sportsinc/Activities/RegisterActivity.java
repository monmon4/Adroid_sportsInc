package com.quantumsit.sportsinc.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;

import com.google.gson.Gson;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.hbb20.CountryCodePicker;
import com.makeramen.roundedimageview.RoundedImageView;
import com.quantumsit.sportsinc.Aaa_data.Bitmap_functions;
import com.quantumsit.sportsinc.Aaa_data.Config;
import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Backend.Functions;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.Activities.HomeActivity;
import com.quantumsit.sportsinc.R;
import com.quantumsit.sportsinc.util.ConnectionUtilities;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.michaelrocks.libphonenumber.android.NumberParseException;


public class RegisterActivity extends AppCompatActivity {


    EditText name_edittext, lastname_edittext, phone_edittext, mail_edittext, pass_edittext, repass_edittext;

    //Spinner gender_spinner;

    String user_name, phone, mail, pass, repass;

    CardView cardView;
    ImageButton imageButton; RoundedImageView imageView;
    private boolean photoChanged = false;
    //Image request code
    private int PICK_IMAGE_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Bitmap to get image from gallery
    private Bitmap bitmap;

    //Uri to store the image uri
    private Uri filePath;
    private int THUMBNAIL_SIZE = 150;
    private int Counter = 0;

    //PopupWindow verfication_popup_window;
    //private Context register_Context;
    //private RelativeLayout register_rl;
    ProgressDialog progressDialog;

    GlobalVars globalVars;
    CountryCodePicker ccp;
    Functions functions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Sign Up");
        functions = new Functions(getApplicationContext());

        globalVars = (GlobalVars) getApplication();
        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.configure));
        progressDialog.setCanceledOnTouchOutside(false);

        //register_Context = getApplicationContext();
        //register_rl =  findViewById(R.id.register_rl);


        name_edittext =  findViewById(R.id.firstnameEditText_register);
        lastname_edittext =  findViewById(R.id.lastnameEditText_register);
        phone_edittext =  findViewById(R.id.phoneEditText_register);
        mail_edittext =  findViewById(R.id.mailEditText_register);
        pass_edittext =  findViewById(R.id.passEditText_register);
        repass_edittext =  findViewById(R.id.repassEditText_register);
        ccp = findViewById(R.id.ccp_register);
        ccp.registerCarrierNumberEditText(phone_edittext);

        imageView = findViewById(R.id.imageView_register);
        cardView = findViewById(R.id.cardView_register);
        imageButton = findViewById(R.id.imageButton_register);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestStoragePermission();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestStoragePermission();
            }
        });
    }

    private boolean checkConnection() {
        // first, check connectivity
        if (ConnectionUtilities
                .checkInternetConnection(this)) {
            return true;
        }
        return false;
    }


    private boolean validateForm() {

        user_name = name_edittext.getText().toString();
        phone = phone_edittext.getText().toString();
        mail = mail_edittext.getText().toString();
        pass = pass_edittext.getText().toString();
        repass = repass_edittext.getText().toString();

        if (TextUtils.isEmpty(phone)) {
            phone_edittext.setError("Required.");
            return false;
        } else if (functions.isValidPhone(ccp.getFullNumber(), ccp.getSelectedCountryNameCode()).equals("")){
            phone_edittext.setFocusable(true);
            phone_edittext.setError("Invalid phone number");
            return false;
        } else {
            phone = functions.isValidPhone(ccp.getFullNumber(), ccp.getSelectedCountryNameCode());
        }

        if (TextUtils.isEmpty(user_name)) {
            name_edittext.setError("Required.");
            return false;
        }
        if (TextUtils.isEmpty(mail)) {
            mail_edittext.setError("Required.");
            return false;
        } else if (!functions.isValidMail(mail)){
            mail_edittext.setError("Invalid email");
            return false;
        }
        if (TextUtils.isEmpty(pass)) {
            pass_edittext.setError("Required.");
            return false;
        } else if (pass_edittext.length()<8){
            pass_edittext.setError("Password should be more than 8 characters");
            return false;
        }
        if (!pass.equals(repass)){
            repass_edittext.setError("Passwords don't match");
            return false;
        }

        user_name +=  lastname_edittext.getText().toString();
        mail_edittext.setError(null);
        pass_edittext.setError(null);
        repass_edittext.setError(null);

        return true;
    }

    public void done_register(View view) {
        if (!checkConnection()){
            show_toast(getResources().getString(R.string.no_connection));
            return;
        }

        progressDialog.show();

        if (!validateForm()) {
            if (photoChanged)
                uploadImageToServer();
            progressDialog.dismiss();
            return;
        }

        checkPhone();

    }

    @SuppressLint("StaticFieldLeak")
    private void checkMail() {

        JSONObject where_info = new JSONObject();

        try {
            where_info.put("email",mail);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpCall httpCall = functions.searchDB("users", where_info);

        new HttpRequest(){
            @Override
            public void onResponse(JSONArray response) {
                super.onResponse(response);
                if(response != null){
                    progressDialog.dismiss();
                    mail_edittext.setError("Email already exists");
                } else {
                    insert_to_DB();
                    //verfication();
                }
            }
        }.execute(httpCall);
    }

    @SuppressLint("StaticFieldLeak")
    private void checkPhone() {

        JSONObject where_info = new JSONObject();

        try {
            where_info.put("phone",phone);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpCall httpCall = functions.searchDB("users", where_info);

        new HttpRequest(){
            @Override
            public void onResponse(JSONArray response) {
                super.onResponse(response);

                if(response != null){
                    progressDialog.dismiss();
                    phone_edittext.setError("Phone already exists");

                } else {
                    checkMail();
                    //verfication();
                }

            }
        }.execute(httpCall);
    }

   /* @SuppressLint("StaticFieldLeak")
    private void verfication(){
        progressDialog.dismiss();
        String verification_msg;
        Random random_num = new Random();
        final int verfication_num = random_num.nextInt(9999 - 1000) + 1000;
        Log.d("Verfication","Code: "+verfication_num);
        //verification_msg = "" + verfication_num;


        LayoutInflater inflater = (LayoutInflater) register_Context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.window_verficationcode_layout,null);

        verfication_popup_window = new PopupWindow(
                customView,
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );

        if(Build.VERSION.SDK_INT>=21){
            verfication_popup_window.setElevation(5.0f);
        }

        final EditText verify_edit_text =  customView.findViewById(R.id.verficationEditText_verify);
        Button done_button =  customView.findViewById(R.id.doneButton_verify);
        verify_edit_text.setEnabled(true);

        verfication_popup_window.showAtLocation(register_rl, Gravity.CENTER,0,0);
        verfication_popup_window.setFocusable(true);
        verify_edit_text.setFocusable(true);
        verfication_popup_window.setOutsideTouchable(false);
        verfication_popup_window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        verfication_popup_window.update();

        done_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String verifcation = verify_edit_text.getText().toString().trim();

                if (verifcation.equals(String.valueOf(verfication_num))){
                    insert_to_DB();
                } else {
                    show_toast("Wrong code");
                }

            }
        } );


        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.POST);
        httpCall.setUrl(Constants.sendSMS);
        HashMap<String,String> params = new HashMap<>();
        params.put("phone",phone);
        params.put("message",String.valueOf(verfication_num));
        httpCall.setParams(params);

        new HttpRequest(){
            @Override
            public void onResponse(JSONArray response) {
                super.onResponse(response);

                if(response != null){
                    show_toast("Code has been sent");

                } else {
                    show_toast("An error has occurred");
                    verfication_popup_window.dismiss();
                }

            }
        }.execute(httpCall);
    }*/

    @SuppressLint("StaticFieldLeak")
    public void insert_to_DB(){
        progressDialog.setMessage(getResources().getString(R.string.log_in));
        progressDialog.show();

       /* verfication_popup_window.dismiss();
        globalVars.setType(5);
        finish();*/
        SharedPreferences tokenPref = getSharedPreferences(Config.SHARED_PREF, MODE_PRIVATE);
        String user_token = tokenPref.getString("regId", "");


        JSONObject info = new JSONObject();
        try {
            info.put("name",user_name);
            info.put("phone",phone);
            info.put("email",mail);
            info.put("pass",pass);
            info.put("type",5);
            if (!user_token.equals(""))
                info.put("token",user_token);

            HttpCall httpCall = functions.insertToDB("users", info);
            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);

                    if(response != null){
                        try {
                            //verfication_popup_window.dismiss();
                            int ID = response.getInt(0);
                            logIn(ID);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        //verfication_popup_window.dismiss();
                        show_toast("An error occurred");
                    }

                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void logIn(int id) {
        Log.d("Verfication","ID: "+id);
        globalVars.settAll(user_name, "", phone, pass, mail, id, 5, 0, "0");
        SharedPreferences.Editor preferences = getSharedPreferences("UserFile", MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(globalVars.getUser());
        preferences.putString("CurrentUser", json);
        preferences.apply();
        progressDialog.dismiss();
        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void show_toast(String msg){
        progressDialog.dismiss();
        Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RegisterActivity.this , startPageActivity.class));
        finish();
    }

    private void requestStoragePermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            showFileChooser();
            return;
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                showFileChooser();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Can't access Storage...", Toast.LENGTH_LONG).show();
            }
        }
    }

    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = Bitmap_functions.getThumbnail(filePath,this,THUMBNAIL_SIZE);
                imageView.setImageBitmap(bitmap);
                //cardView.setVisibility(View.GONE);
                imageButton.setVisibility(View.GONE);
                photoChanged = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    private void uploadImageToServer() {
        filePath = Bitmap_functions.getImageUri(getApplicationContext() , bitmap);
        String ImagePath = Bitmap_functions.getPath(filePath, this);
        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();
            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, Constants.UPLOAD_URL)
                    .addFileToUpload(ImagePath, "image_upload_file") //Adding file
                    .addParameter("type",Constants.profile)//Adding text parameter to the request
                    .setDelegate(new UploadStatusDelegate() {
                        @Override
                        public void onProgress(Context context, UploadInfo uploadInfo) {
                            Log.e("gotev", "onProgress eltstr=" + uploadInfo.getElapsedTimeString() + " elt=" + uploadInfo.getElapsedTime() + " ratestr=" +
                                    uploadInfo.getUploadRateString() + " prc=" + uploadInfo.getProgressPercent() + " tb=" + uploadInfo.getTotalBytes() + " ub=" + uploadInfo.getUploadedBytes());
                        }

                        @Override
                        public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                            Toast.makeText(getApplicationContext(),"Error while uploading",Toast.LENGTH_LONG).show();
                            //dismissProgress();
                        }

                        @Override
                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                            String response = serverResponse.getBodyAsString();
                            try {
                                JSONObject object = new JSONObject(response);
                                JSONObject data = object.getJSONObject("data");
                                JSONObject upload_data = data.getJSONObject("upload_data");
                                String ImageName = upload_data.getString("file_name");
                                globalVars.setImgUrl(ImageName);
                                //saveUpdateToPref();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            dismissProgress();
                        }

                        @Override
                        public void onCancelled(Context context, UploadInfo uploadInfo) {
                            Toast.makeText(getApplicationContext(),"Uploading has been canceled",Toast.LENGTH_LONG).show();
                            dismissProgress();
                        }
                    })
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private synchronized void dismissProgress() {
        Counter++;
        if (Counter >= 2 || photoChanged == false)
            progressDialog.dismiss();
    }

}
