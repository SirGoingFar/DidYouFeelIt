package com.example.aolanrewaju.didyoufeelit;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by OLANREWAJU, EMMANUEL AKINTUNDE on 2/27/2018.
 */

public class Utils {

    private final static String LOG_TAG = Utils.class.getName();

    public static Event fetchData(String... urlStrings){
        //form URL
        URL url = formURL(urlStrings[0]);

        //make HTTP Request using the URL
        String JSONResponse = makeHTTPDataRequest(url);

        //parse the JSONResponse, then return the @link Event object
        Event event = extractJSONFeature(JSONResponse);
        return event;

    }


    public static URL formURL(String urlString){
        URL url = null;
        try{
            url = new URL(urlString);
        }catch (MalformedURLException e){
            Log.e(LOG_TAG,"Error: " + e);
        }
        return url;
    }

    public static String makeHTTPDataRequest(URL url){
        String JSONResponse = null;
        HttpURLConnection request = null;
        InputStream inputStream = null;

        try {
            //open a connection
            request = (HttpURLConnection) url.openConnection();

            //set all request parameters
            request.setConnectTimeout(15000);
            request.setReadTimeout(10000);
            request.setRequestMethod("GET");

            //connect to retrieve data
            request.connect();

            //check if the connection was successful i.e. Response_Code = 200
            if(request.getResponseCode() == 200){
                //get the InputStream from the connection
                inputStream = request.getInputStream();
                JSONResponse = parseInputStream(inputStream);
            }else{
                //output an error on the LogCat
                Log.e(LOG_TAG, "Connection Response Code Error: " + request.getResponseCode());
            }
        }catch (IOException e){
            Log.e(LOG_TAG, "Connection Error: " + e);
        }finally{
            if(request != null)
                request.disconnect();
            if(inputStream != null)
                try {
                    inputStream.close();
                }catch (IOException e){
                    Log.e(LOG_TAG, "Error closing InputSteam: " + e);
                }
        }

        return JSONResponse;
    }

    private static String parseInputStream(InputStream inputStream) {
        //create a new @link StringBuilder
        StringBuilder stringBuilder = new StringBuilder();

        //if the inputStream
        if(inputStream == null){
            return null;
        }

        try{
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();

            //as long as line is not empty
            while (line != null){
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
        }catch (IOException e){
            Log.e(LOG_TAG, "Stream data exhausted: " + e);
        }

        return stringBuilder.toString();
    }

    private static Event extractJSONFeature(String jsonResponse) {
        //return early if the @value jsonResponse is empty
        if(jsonResponse == null)
            return null;
        try {
            //entire JSON response
            JSONObject object = new JSONObject(jsonResponse);

            //Feature Array
            JSONArray featureArray = object.optJSONArray("features");

            //First Feature Array
            JSONObject firstFeatureArray = featureArray.getJSONObject(0);

            //Properties Object for the first element of the Feature Array
            JSONObject propertiesObject = firstFeatureArray.getJSONObject("properties");

            //Get Magnitude
            double magnitude = propertiesObject.getDouble("mag");

            //Get Place
            String place = propertiesObject.getString("place");

            //Get URL
            String url = propertiesObject.getString("url");

            //Get "People that felt it" count
            int felt = propertiesObject.getInt("felt");

            //Get the perceived strength
            double cdi = propertiesObject.getDouble("cdi");

            return new Event(magnitude, place, url, felt, cdi);
        }catch (JSONException e){
            Log.e(LOG_TAG, "Error converting String to JSON Object: " + e);
        }

        return null;
    }
}