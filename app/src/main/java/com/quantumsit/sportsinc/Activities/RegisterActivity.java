package com.quantumsit.sportsinc.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
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
import android.view.Window;
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
    String img_add;

    //Uri to store the image uri
    private Uri filePath;
    private int THUMBNAIL_SIZE = 150;
    private int Counter = 0;

    //PopupWindow verfication_popup_window;
    Dialog customView;
    private Context register_Context;
    private RelativeLayout register_rl;
    ProgressDialog progressDialog;

    GlobalVars globalVars;
    CountryCodePicker ccp;
    Functions functions;
    private String ImageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(" ");
        functions = new Functions(getApplicationContext());

        globalVars = (GlobalVars) getApplication();
        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.configure));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        register_Context = getApplicationContext();
        register_rl =  findViewById(R.id.register_rl);


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

        user_name +=" " + lastname_edittext.getText().toString();
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
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);


        if (!validateForm()) {
            progressDialog.dismiss();
            return;
        }

        checkUserData();

    }

    @SuppressLint("StaticFieldLeak")
    private void checkUserData(){
        HttpCall httpCall = functions.searchUser(phone ,mail);

        new HttpRequest(){
            @Override
            public void onResponse(JSONArray response) {
                super.onResponse(response);
                if(response != null){
                    progressDialog.dismiss();
                    //Log.d("CheckUserResult",String.valueOf(response));
                    try {
                        JSONObject result = response.getJSONObject(0);
                        String value = result.getString("check_value");
                        show_toast(value);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    verification();
                }
            }
        }.execute(httpCall);

    }

    @SuppressLint("StaticFieldLeak")
    private void verification(){
        progressDialog.dismiss();
        String verification_msg;
        Random random_num = new Random();
        final int verfication_num = random_num.nextInt(9999 - 1000) + 1000;
        Log.d("Verification","Code: "+verfication_num);

        customView = new Dialog(RegisterActivity.this);
        customView.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customView.setContentView(R.layout.window_verficationcode_layout);
        customView.setCanceledOnTouchOutside(false);

        final EditText verify_edit_text =  customView.findViewById(R.id.verficationEditText_verify);
        Button done_button =  customView.findViewById(R.id.doneButton_verify);
        verify_edit_text.setEnabled(true);

        done_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String verifcation = verify_edit_text.getText().toString().trim();

                if (verifcation.equals(String.valueOf(verfication_num))){
                    upload_save_to_DB();
                } else {
                    show_toast("Wrong code");
                }

            }
        } );


        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.POST);
        httpCall.setUrl(Constants.sendMail);
        HashMap<String,String> params = new HashMap<>();
        //Log.d("Verification","Mail: "+mail+" , code: "+verfication_num);
        params.put("email",mail);
        params.put("code",String.valueOf(verfication_num));
        httpCall.setParams(params);

        new HttpRequest(){
            @Override
            public void onResponse(JSONArray response) {
                super.onResponse(response);
                Log.d("verificationResponse",String.valueOf(response));
                if(response != null){
                    show_toast("Code has been sent");

                } else {
                    show_toast("An error has occurred");
                    customView.dismiss();
                }
            }
        }.execute(httpCall);

        customView.show();
    }

    private void upload_save_to_DB() {
        progressDialog.setMessage(getResources().getString(R.string.log_in));
        progressDialog.show();

        customView.dismiss();

        if (photoChanged)
            uploadImageToServer();
        else
            insert_to_DB();
    }

    @SuppressLint("StaticFieldLeak")
    public void insert_to_DB(){

        SharedPreferences tokenPref = getSharedPreferences(Config.SHARED_PREF, MODE_PRIVATE);
        String user_token = tokenPref.getString("regId", "");

        JSONObject info = new JSONObject();
        try {
            info.put("name",user_name);
            info.put("phone",phone);
            info.put("email",mail);
            info.put("pass",pass);
            info.put("ImageUrl",ImageName);
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
                            int ID = response.getInt(0);
                            logIn(ID);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
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
        globalVars.settAll(user_name, ImageName, phone, pass, mail, id, 5, 0, "0");
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

    //Requesting permission
    private void requestStoragePermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            showCameraOrGallery();
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
                showCameraOrGallery();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Can't access Storage...", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showCameraOrGallery(){
        final CharSequence[] items = {"Take Photo using Camera", "Choose from Gallery"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo using Camera")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, Constants.REQUEST_CAMERA);

                } else if (items[item].equals("Choose from Gallery")) {

                    Intent intent = new Intent(
                            Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent,Constants.SELECT_FILE);

                } else if (items[item].equals("Cancel")) {

                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        String path;
        switch(requestCode) {
            case 1: //request camera
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    path = getRealPathFromURI(selectedImage);
                    bitmap = Functions.rotateBitmapOrientation(path);
                    set_pic(bitmap);
                    photoChanged = true;
                }

                break;
            case 2: //choose from gallery
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    path = getRealPathFromURI(selectedImage);
                    bitmap = Functions.rotateBitmapOrientation(path);
                    set_pic(bitmap);
                    photoChanged = true;
                }
                break;
        }
    }

    private void set_pic(Bitmap bit_map){

        img_add = Functions.encode_base64(bit_map);
        Bitmap bitmap = Functions.decodeBase64(img_add);
        imageView.setImageBitmap(bitmap);
        photoChanged = true;

    }

    public String getRealPathFromURI(Uri contentUri)
    {
        try
        {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        catch (Exception e)
        {
            return contentUri.getPath();
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
                            //Toast.makeText(getApplicationContext(),"Error while uploading",Toast.LENGTH_LONG).show();
                            insert_to_DB();
                        }

                        @Override
                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                            String response = serverResponse.getBodyAsString();
                            try {
                                JSONObject object = new JSONObject(response);
                                JSONObject data = object.getJSONObject("data");
                                JSONObject upload_data = data.getJSONObject("upload_data");
                                ImageName = upload_data.getString("file_name");
                                insert_to_DB();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancelled(Context context, UploadInfo uploadInfo) {
                            Toast.makeText(getApplicationContext(),"Uploading has been canceled",Toast.LENGTH_LONG).show();
                        }
                    })
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
