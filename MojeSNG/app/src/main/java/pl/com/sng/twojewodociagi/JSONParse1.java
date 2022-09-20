package pl.com.sng.twojewodociagi;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JSONParse1 {

    static InputStream iStream = null;
    static JSONArray jarray = null;
    static String json = "";
    public JSONParse1() {
    }

    public JSONArray getJSONFromUrl(String url){
        StringBuilder builder = new StringBuilder();

        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
      // List nameValuePairs = new ArrayList<String>(1);
       // nameValuePairs.add(new BasicNameValuePair("", ""));
        try {
          //  httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
          //  httpPost.setEntity(new UrlEncodedFormEntity());
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
