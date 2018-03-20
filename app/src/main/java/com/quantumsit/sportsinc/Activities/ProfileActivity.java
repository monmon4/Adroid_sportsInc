package com.quantumsit.sportsinc.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileActivity extends AppCompatActivity {

    static final String TAG = ProfileActivity.class.getSimpleName();

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

    PopupWindow verfication_popup_window;
    private Context profile_Context;
    private LinearLayout profile_ll;
    private RelativeLayout profile_rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        globalVars = (GlobalVars) getApplication();
        progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setMessage("Saving...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        profile_Context = getApplicationContext();
        //profile_ll =  findViewById(R.id.profile_ll);
        profile_rl =  findViewById(R.id.profile_rl);

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

    private void saveUpdateToPref() {
        SharedPreferences.Editor preferences = getSharedPreferences("UserFile", MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(globalVars.getMyAccount());
        preferences.putString("CurrentUser", json);
        preferences.apply();
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
                Image.setImageBitmap(bitmap);

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

    //Requesting permission
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


    private void editableProfile(boolean editable){
        Log.d("ProfileStatus",String.valueOf(profileStatus));
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
        //Gender.setText(globalVars.getPersonGender());
        //DateOfBirth.setText(globalVars.getDate_of_birth());
    }

    private void changeProfilePassword() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ProfileActivity.this);
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

        if (!isValidMail(NewMail)){
            progressDialog.dismiss();
            Mail.setError("Invalid e-mail format");
            return;
        }


        if (!NewPhone.equals(globalVars.getPhone())) {
            if (!isValidPhone(NewPhone)){
                progressDialog.dismiss();
                Phone.setError("Invalid Phone Number...");
                return;
            }
            checkPhone(NewPhone);
            return;
        }

        insertToDb();
    }


    private synchronized void dismissProgress() {
        Counter++;
        if (Counter >= 2 || photoChanged == false)
            progressDialog.dismiss();
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
                        Toast.makeText(ProfileActivity.this, "Password Changed", Toast.LENGTH_SHORT).show();
                        globalVars.setPass(pass);
                        saveUpdateToPref();
                    }else {
                        Toast.makeText(ProfileActivity.this, "Failed", Toast.LENGTH_SHORT).show();
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

    public static boolean isValidMail(String mail)
    {
        String expression = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        CharSequence inputString = mail;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputString);
        if (matcher.matches())
        {
            return true;
        }
        else{
            return false;
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
    private void checkPhone(final String phone) {

        JSONObject where_info = new JSONObject();
        try {
            where_info.put("phone",phone);
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
                    show_toast("Phone already exists");

                } else {
                    verfication(phone);
                }

            }
        }.execute(httpCall);
    }

    @SuppressLint("StaticFieldLeak")
    private void verfication(String phone){

        String verification_msg;
        Random random_num = new Random();
        final int verfication_num = random_num.nextInt(9999 - 1000) + 1000;
        verification_msg = "Your verfication code: " + verfication_num;


        LayoutInflater inflater = (LayoutInflater) profile_Context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.window_verficationcode_layout,null);

        verfication_popup_window = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        if(Build.VERSION.SDK_INT>=21){
            verfication_popup_window.setElevation(5.0f);
        }

        final EditText verify_edit_text =  customView.findViewById(R.id.verficationEditText_verify);
        Button done_button =  customView.findViewById(R.id.doneButton_verify);
        verify_edit_text.setEnabled(true);

        verfication_popup_window.showAtLocation(profile_ll, Gravity.CENTER,0,0);
        verfication_popup_window.setFocusable(true);
        verify_edit_text.setFocusable(true);
        verfication_popup_window.setOutsideTouchable(false);
        verfication_popup_window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        verfication_popup_window.update();

        done_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String verifcation = verify_edit_text.getText().toString().trim();
                insertToDb();
                if (verifcation.equals(String.valueOf(verfication_num))){
                    verfication_popup_window.dismiss();
                    insertToDb();
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
        params.put("message",verification_msg);
        httpCall.setParams(params);

        new HttpRequest(){
            @Override
            public void onResponse(JSONArray response) {
                super.onResponse(response);

                if(response != null){
                    show_toast("Success");

                } else {
                    verfication_popup_window.dismiss();
                    show_toast("An error occurred");
                }

            }
        }.execute(httpCall);
    }

    private void insertToDb() {
        progressDialog.show();

        try {
            JSONObject values = new JSONObject();
            values.put("name",NewName);
            values.put("phone",NewPhone);
            values.put("email",NewMail);
            values.put("ImageUrl",globalVars.getImgUrl());

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
                        Toast.makeText(ProfileActivity.this,"Changing saved",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(ProfileActivity.this,"Edit Fail",Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void show_toast(String msg) {
        progressDialog.dismiss();
        Toast.makeText(ProfileActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        setResult(AppCompatActivity.RESULT_OK, null);
        finish();
    }
}
