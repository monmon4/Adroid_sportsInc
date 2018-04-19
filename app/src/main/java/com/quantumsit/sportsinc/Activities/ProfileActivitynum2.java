package com.quantumsit.sportsinc.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.quantumsit.sportsinc.Aaa_data.Bitmap_functions;
import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Backend.Functions;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.R;
import com.squareup.picasso.Picasso;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ProfileActivitynum2 extends AppCompatActivity {

    static final String TAG = ProfileActivitynum2.class.getSimpleName();

    GlobalVars globalVars;
    ProgressDialog progressDialog;

    EditText Name ,Phone ,Mail;
    String NewName ,NewPhone ,NewMail;
    TextView ChangePassword;
    RoundedImageView Image;
    ImageButton Upload_Image;
    CardView cardView;
    Button Edit, Save ,Cancel;
    LinearLayout EditButtons;
    private boolean profileStatus = false;
    private boolean photoChanged = false;

    String img_add;

    //Image request code
    private int PICK_IMAGE_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Bitmap to get image from gallery
    private Bitmap bitmap;

    //Uri to store the image uri
    private Uri filePath;
    private int Counter = 0;

    Dialog verfication_dialog;
    Functions functions;
    private boolean noChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("My profile");

        globalVars = (GlobalVars) getApplication();
        functions = new Functions(getApplicationContext());
        progressDialog = new ProgressDialog(ProfileActivitynum2.this);
        progressDialog.setMessage("Saving...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        Name = findViewById(R.id.nameEditText_profile);
        Image = findViewById(R.id.imageView_profile);
        Phone = findViewById(R.id.phoneEditText_profile);
        Mail = findViewById(R.id.mailEditText_profile);
        //Gender = findViewById(R.id.profile_gender);
        //DateOfBirth = findViewById(R.id.profile_date_birth);

        ChangePassword = findViewById(R.id.profile_change_password);

        Upload_Image = findViewById(R.id.imageButton_profile);
        cardView = findViewById(R.id.cardView_profile);
        Edit = findViewById(R.id.edit_profile);
        Save = findViewById(R.id.save_profile);
        Cancel = findViewById(R.id.cancel_profile);

        EditButtons = findViewById(R.id.editButtons_profile);


        ChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeProfilePassword();
            }
        });

        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editableProfile(true);
            }
        });

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                updateProfile();
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editableProfile(false);
                fillProfileData();
            }
        });

        Upload_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestStoragePermission();
            }
        });

        fillProfileData();
    }

    //Requesting permission
    private void requestStoragePermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            showCameraOrGallery();
            return;
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

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
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ProfileActivitynum2.this);
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
        Image.setImageBitmap(bitmap);
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

    private void saveUpdateToPref() {
        SharedPreferences.Editor preferences = getSharedPreferences("UserFile", MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(globalVars.getMyAccount());
        preferences.putString("CurrentUser", json);
        preferences.apply();
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
                            dismissProgress();
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
                                saveUpdateToPref();
                                updateUserImage(ImageName);
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

    private void editableProfile(boolean editable){
        profileStatus = editable;
        enableProfile(editable);
        int visible = View.VISIBLE;
        int inVisible = View.GONE;
        int viewProfile = visible ,editProfile = inVisible;
        if (editable){
            viewProfile = inVisible;
            editProfile = visible ;
        }
        ChangePassword.setVisibility(editProfile);
        Upload_Image.setVisibility(editProfile);
        EditButtons.setVisibility(editProfile);
        Edit.setVisibility(viewProfile);
    }

    private void fillProfileData() {
        Name.setText(globalVars.getName());
        Phone.setText(globalVars.getPhone());
        Mail.setText(globalVars.getMail());
        String ImageUrl = globalVars.getImgUrl();

        if(!ImageUrl.equals("")) {
            Picasso.with(getApplicationContext()).load(Constants.profile_host + ImageUrl).into(Image, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    //progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError() {

                }
            });
        }else {
            // progressBar.setVisibility(View.GONE);
        }
    }

    private void changeProfilePassword() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ProfileActivitynum2.this);
        View mView = getLayoutInflater().inflate(R.layout.change_password_dialog,null);

        final EditText OldPassword = mView.findViewById(R.id.OldPassword);
        final EditText NewPassword = mView.findViewById(R.id.MyNewPassword);
        final EditText ConfirmPassword = mView.findViewById(R.id.ConfirmMyNewPassword);

        mBuilder.setView(mView);

        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton("Change Password", new DialogInterface.OnClickListener() {@Override public void onClick(DialogInterface dialogInterface, int i) { }});
        mBuilder.setNegativeButton("Cancel",null);
        final AlertDialog alertdialog = mBuilder.create();
        alertdialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = alertdialog.getButton((AlertDialog.BUTTON_POSITIVE));
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Write your Logic. It will never dismiss the dialog unless your condition satisifies
                        if(checkProfileOldPassword(OldPassword.getText().toString())){
                            String pass = NewPassword.getText().toString();
                            String Confir = ConfirmPassword.getText().toString();
                            if (!TextUtils.isEmpty(NewPassword.getText()) &&pass.equals(Confir)){
                                updatePassword(pass,alertdialog);
                            }
                        }
                    }
                });
            }
        });
        alertdialog.show();
    }

    private void updateProfile() {
        if (photoChanged)
            uploadImageToServer();

        NewName = Name.getText().toString();
        NewMail = Mail.getText().toString();
        NewPhone = Phone.getText().toString();

        boolean mailChange = false;

        if (!NewPhone.equals(globalVars.getPhone())) {
            if (!isValidPhone(NewPhone)){
                progressDialog.dismiss();
                Phone.setError("Invalid Phone Number...");
                return;
            }
            noChange = true;
        }

        if (!NewMail.equals(globalVars.getMail())) {
            if (!functions.isValidMail(NewMail)) {
                progressDialog.dismiss();
                Mail.setError("Invalid e-mail format");
                return;
            }
            mailChange = true;
            noChange = true;
        }
        if (!NewName.equals(globalVars.getName()) || noChange) {
            noChange = true;
            if (mailChange)
                checkMail(NewMail);
            else
                insertToDb();
        }
        else if (!photoChanged){
            dismissProgress();
        }
    }

    private void updateUserImage(String imageName) {
        try {
            JSONObject values = new JSONObject();
            values.put("ImageUrl",imageName);

            JSONObject where = new JSONObject();
            where.put("id",globalVars.getId());

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.updateData);
            final HashMap<String,String> params = new HashMap<>();
            params.put("table","users");
            params.put("where",where.toString());
            params.put("values",values.toString());

            httpCall.setParams(params);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void insertToDb() {
        try {
            JSONObject values = new JSONObject();
            values.put("name",NewName);
            values.put("phone",NewPhone);
            values.put("email",NewMail);

            JSONObject where = new JSONObject();
            where.put("id",globalVars.getId());

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.updateData);
            final HashMap<String,String> params = new HashMap<>();
            params.put("table","users");
            params.put("where",where.toString());
            params.put("values",values.toString());

            httpCall.setParams(params);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    if (checkResponse(response)){
                        globalVars.setName(NewName);
                        globalVars.setMail(NewMail);
                        globalVars.setPhone(NewPhone);
                        saveUpdateToPref();
                        editableProfile(false);
                        Toast.makeText(ProfileActivitynum2.this,"Changing saved",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(ProfileActivitynum2.this,"Edit Fail",Toast.LENGTH_SHORT).show();
                    }
                    dismissProgress();
                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private synchronized void dismissProgress() {
        Counter++;
        if (Counter >= 2 || photoChanged == false || noChange == false) {
            progressDialog.dismiss();
            editableProfile(false);
            fillProfileData();
        }
    }

    private void updatePassword(final String pass, final AlertDialog alertdialog) {
        try {
            JSONObject values = new JSONObject();
            values.put("pass",pass);

            JSONObject where = new JSONObject();
            where.put("id",globalVars.getId());

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.updateData);
            final HashMap<String,String> params = new HashMap<>();
            params.put("table","users");
            params.put("where",where.toString());
            params.put("values",values.toString());
            httpCall.setParams(params);

            progressDialog.show();
            alertdialog.dismiss();
            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    Log.d(TAG, params.toString());
                    Log.d(TAG, "Update Response: " + String.valueOf(response));
                    if(checkResponse(response)) {
                        Toast.makeText(ProfileActivitynum2.this, "Password Changed", Toast.LENGTH_SHORT).show();
                        globalVars.setPass(pass);
                        saveUpdateToPref();
                    }else {
                        Toast.makeText(ProfileActivitynum2.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // ERROR In Matching Phone //
    public static boolean isValidPhone(String phone)
    {
        String expression = "^(01([0-2]|5)[0-9]{8})$";
        CharSequence inputString = phone;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputString);
        if (matcher.matches())
        {
            return true;
        }
        else{
            return true;
        }
    }

    private boolean checkResponse(JSONArray response) {
        if (response != null){
            try {
                String result = response.getString(0);
                if (result.equals("DONE"))
                    return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean checkProfileOldPassword(String password) {
        if(!TextUtils.isEmpty(password)){
            if(password.equals(globalVars.getPass()))
                return true;
        }
        return false;
    }

    private void enableProfile(boolean b) {
        Name.setEnabled(b);
        Phone.setEnabled(b);
        Mail.setEnabled(b);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("ProfileStatus","onSave"+String.valueOf(profileStatus));
        outState.putBoolean("ProfileStatus",profileStatus);
        outState.putBoolean("PictureStatus",photoChanged);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            profileStatus = savedInstanceState.getBoolean("ProfileStatus");
            photoChanged = savedInstanceState.getBoolean("PictureStatus");
            Log.d("ProfileStatus","onRestore"+String.valueOf(profileStatus));
        }
        editableProfile(profileStatus);
    }

    @SuppressLint("StaticFieldLeak")
    private void checkMail(final String mail) {

        JSONObject where_info = new JSONObject();
        try {
            where_info.put("email",mail);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.POST);
        httpCall.setUrl(Constants.selectData);
        HashMap<String,String> params = new HashMap<>();
        params.put("table","users");
        params.put("where",where_info.toString());
        httpCall.setParams(params);

        new HttpRequest(){
            @Override
            public void onResponse(JSONArray response) {
                super.onResponse(response);

                if(response != null){
                    show_toast("Email already exists");

                } else {
                    verification(mail);
                }

            }
        }.execute(httpCall);
    }

    @SuppressLint("StaticFieldLeak")
    private void verification(String mail){
        progressDialog.dismiss();
        String verification_msg;
        Random random_num = new Random();
        final int verfication_num = random_num.nextInt(9999 - 1000) + 1000;
        Log.d("Verification","Code: "+verfication_num);

        verfication_dialog = new Dialog(ProfileActivitynum2.this);
        verfication_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        verfication_dialog.setContentView(R.layout.window_verficationcode_layout);
        verfication_dialog.setCanceledOnTouchOutside(false);

        final EditText verify_edit_text =  verfication_dialog.findViewById(R.id.verficationEditText_verify);
        Button done_button =  verfication_dialog.findViewById(R.id.doneButton_verify);
        verify_edit_text.setEnabled(true);

        done_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String verifcation = verify_edit_text.getText().toString().trim();

                if (verifcation.equals(String.valueOf(verfication_num))){
                    verfication_dialog.dismiss();
                    insertToDb();
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
                    verfication_dialog.dismiss();
                }
            }
        }.execute(httpCall);

        verfication_dialog.show();
    }
    private void show_toast(String msg) {
        progressDialog.dismiss();
        Toast.makeText(ProfileActivitynum2.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        setResult(AppCompatActivity.RESULT_OK, null);
        finish();
    }
}
