package com.quantumsit.sportsinc.Backend;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bassam on 1/8/2018.
 */

public class HttpRequest extends AsyncTask<HttpCall, String, JSONArray> {
    private static final String TAG = HttpRequest.class.getSimpleName();

    private static final String UTF_8 = "UTF-8";

    @Override
    protected JSONArray doInBackground(HttpCall... params) {
        HttpURLConnection urlConnection = null;
        HttpCall httpCall = params[0];
        StringBuilder response = new StringBuilder();
        JSONArray object = null;
        try{
            String dataParams = getDataString(httpCall.getParams(), httpCall.getMethodtype());
            URL url = new URL(httpCall.getMethodtype() == HttpCall.GET ? httpCall.getUrl() + dataParams : httpCall.getUrl());
            Log.d(TAG,"URL :: "+String.valueOf(url));
            urlConnection = (HttpURLConnection) url.openConnection();
            Log.d(TAG,"Connected");
            urlConnection.setRequestMethod(httpCall.getMethodtype() == HttpCall.GET ? "GET":"POST");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            if(httpCall.getParams() != null && httpCall.getMethodtype() == HttpCall.POST){

                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, UTF_8));
                writer.append(dataParams);
                writer.flush();
                writer.close();
                os.close();
            }
            int responseCode = urlConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                String line ;
                BufferedReader br = new BufferedReader( new InputStreamReader(urlConnection.getInputStream()));
                while ((line = br.readLine()) != null){
                    response.append(line);
                }
                Log.d(TAG,"Response:"+String.valueOf(response));
                JSONTokener tokener = new JSONTokener(response.toString());
                Log.d(TAG,"Response:"+String.valueOf(response));
                JSONObject data = new JSONObject(tokener);
                Log.d(TAG,String.valueOf(data));
                object = data.getJSONArray("data");
                Log.d(TAG,String.valueOf(object));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return object;
    }

    @Override
    protected void onPostExecute(JSONArray s) {
        super.onPostExecute(s);
        onResponse(s);
    }

    public void onResponse(JSONArray response){

    }

    private String getDataString(HashMap<String,String> params, int methodType) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean isFirst = true;
        for(Map.Entry<String,String> entry : params.entrySet()){
            if (isFirst){
                isFirst = false;
                if(methodType == HttpCall.GET){
                    result.append("?");
                }
            }else{
                result.append("&");
            }
            result.append(URLEncoder.encode(entry.getKey(), UTF_8));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), UTF_8));
        }
        return result.toString();
    }
}