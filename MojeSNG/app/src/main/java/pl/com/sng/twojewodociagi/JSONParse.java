package pl.com.sng.twojewodociagi;


import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
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
 * Created by PBronk on 15.11.2016.
 */

public class JSONParse {

    static InputStream iStream = null;
    static JSONArray jarray = null;
    static String json = "";


    public JSONParse() {
    }

    public JSONArray getJSONFromUrl(String url){

        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        List nameValuePairs = new ArrayList<String>(2);
        nameValuePairs.add(new BasicNameValuePair("Dzielnica", "Wrzeszcz"));
        nameValuePairs.add(new BasicNameValuePair("Token", "testy"));


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



}
