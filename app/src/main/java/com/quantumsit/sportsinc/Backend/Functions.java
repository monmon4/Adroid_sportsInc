package com.quantumsit.sportsinc.Backend;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Base64;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.quantumsit.sportsinc.Aaa_data.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Mona on 18-Mar-18.
 */

public class Functions {

    Context context;

    public Functions(Context context) {
        this.context = context;
    }

    public HttpCall searchUser(String phone , String mail) {
        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.POST);
        httpCall.setUrl(Constants.checkUser);
        HashMap<String,String> params = new HashMap<>();
        params.put("phone",phone);
        params.put("mail",mail);
        httpCall.setParams(params);

        return httpCall;

    }
    public HttpCall joinDB(String table1, String table2, JSONObject where, String on){
        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.POST);
        httpCall.setUrl(Constants.join);

        HashMap<String, String> params = new HashMap<>();
        params.put("table1", table1);
        params.put("table2", table2);

        params.put("where", where.toString());
        params.put("on", on);

        httpCall.setParams(params);
        return httpCall;
    }

    public HttpCall joinDB(String table1, String table2, JSONObject where, String on, String select){
        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.POST);
        httpCall.setUrl(Constants.join);

        HashMap<String, String> params = new HashMap<>();
        params.put("table1", table1);
        params.put("table2", table2);

        params.put("where", where.toString());
        params.put("on", on);
        params.put("select", select);

        httpCall.setParams(params);
        return httpCall;
    }

    public HttpCall searchDB(String table_name, JSONObject where_info) {


        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.POST);
        httpCall.setUrl(Constants.selectData);
        HashMap<String,String> params = new HashMap<>();
        params.put("table",table_name);
        params.put("where",where_info.toString());
        httpCall.setParams(params);

        return httpCall;

    }

    public HttpCall updateDB(String table_name, JSONObject where_info, JSONObject values) {

        final JSONArray[] result = {new JSONArray()};

        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.POST);
        httpCall.setUrl(Constants.updateData);
        HashMap<String,String> params = new HashMap<>();
        params.put("table",table_name);
        params.put("where",where_info.toString());
        params.put("values",values.toString());
        httpCall.setParams(params);

        return httpCall;

    }

    public HttpCall insertToDB(String table_name, JSONObject values) {

        final JSONArray[] result = {new JSONArray()};

        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.POST);
        httpCall.setUrl(Constants.insertData);
        HashMap<String,String> params = new HashMap<>();
        params.put("table",table_name);
        params.put("values",values.toString());
        httpCall.setParams(params);
        return httpCall;

    }


    public String getEmojiByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }

    public String getEmoji(String notes) {

        String[] data = notes.split(" ");
        StringBuilder full_notes = new StringBuilder();

        if (data[0].equals("Excellent") || data[0].equals("Good") || data[0].equals("Average") || data[0].equals("Bad")) {
            full_notes.append(data[0]).append(" ").append(getEmojiByUnicode(Integer.valueOf(data[1])));
            data[0] = data[1] = "";
        }

        for (int i=0; i<data.length; i++) {
            full_notes.append(" ").append(data[i]);
        }
        return full_notes.toString();
    }


    public String isValidPhone(String phone_num, String country)
    {
        boolean isValid;
        String phone = "";
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber NumberProto = phoneUtil.parse(phone_num, country);            //with default country
            isValid = phoneUtil.isValidNumber(NumberProto);                  //returns true
            if (isValid) {
                phone = phoneUtil.format(NumberProto, PhoneNumberUtil.PhoneNumberFormat.NATIONAL); //(202) 555-0100
            }

        }  catch (com.google.i18n.phonenumbers.NumberParseException e) {
            e.printStackTrace();
        }

        return phone;
    }

    public boolean isValidMail(String mail)
    {
        String expression = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(mail);
        if (matcher.matches())
        {
            return true;
        }
        else{
            return false;
        }
    }


    public static String encode_base64(Bitmap bitmap){

        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] image_bytes = stream.toByteArray();

        String img_base64 = Base64.encodeToString(image_bytes, Base64.DEFAULT);
        return img_base64;
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        return bitmap;
    }


    public static Bitmap rotateBitmapOrientation(String photoFilePath) {
        // Create and configure BitmapFactory
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoFilePath, bounds);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(photoFilePath, opts);
        // Read EXIF Data
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(photoFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
        // Rotate Bitmap
        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
        // Return result
        return rotatedBitmap;
    }

}
