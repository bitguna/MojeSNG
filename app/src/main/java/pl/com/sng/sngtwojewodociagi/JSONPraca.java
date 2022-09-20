package pl.com.sng.sngtwojewodociagi;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PBronk on 08.12.2016.
 */

public class JSONPraca {
    static InputStream iStream = null;
    static JSONArray jarray = null;
    static String json = "";


    public JSONPraca() {
    }

    public JSONArray getJSONFromUrl(String url, String  modul, String Elemnet, String name){

        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        List nameValuePairs = new ArrayList<String>(3);
        //     nameValuePairs.add(new BasicNameValuePair("key",Integer.toString(header)));
        nameValuePairs.add(new BasicNameValuePair("Module", modul));
        nameValuePairs.add(new BasicNameValuePair("Element", Elemnet));
        nameValuePairs.add(new BasicNameValuePair("name", name));

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = client.execute(httpPost);
            org.apache.http.StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if(statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));

                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            }else {
                Log.e("==>", "Fail to dowload ");

            }
        }catch (ClientProtocolException e){
            e.printStackTrace();

        }catch (IOException e){
            e.printStackTrace();

        }


        try{

            jarray  = new JSONArray(builder.toString());

        }catch (JSONException e){
            Log.e("JSON Parser", "Error parsing data" + e.toString());
        }
        return jarray;

    }

    public JSONArray getJSONFromUrl(String url, String headerString){

        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        List nameValuePairs = new ArrayList<String>(1);
        nameValuePairs.add(new BasicNameValuePair("",headerString));
        try {
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Accept", "application/json");
            httpPost.setEntity(new ByteArrayEntity(headerString.getBytes("UTF8")));
            HttpResponse response = client.execute(httpPost);
            org.apache.http.StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if(statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));

                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            }else {
                Log.e("==>", "Fail to dowload ");

            }
        }catch (ClientProtocolException e){
            e.printStackTrace();

        }catch (IOException e){
            e.printStackTrace();

        }


        try{

            jarray  = new JSONArray(builder.toString());

        }catch (JSONException e){
            Log.e("JSON Parser", "Error parsing data" + e.toString());
        }
        return jarray;

    }


}
