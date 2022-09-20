package pl.com.sng.sngtwojewodociagi;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class JSONParseInsertDzielnice {

    static InputStream iStream = null;
    static JSONArray jarray = null;
    static String json = "";
    String tokenn,dzienicaa;

    public JSONParseInsertDzielnice(String Dzielnica, String tokenn) {
        this.tokenn = tokenn;
        this.dzienicaa = Dzielnica;
        getJSONFromUrl("https://notification.sng.com.pl/api/InsertDzielnice/");
    }
    public boolean getJSONFromUrl(String url) {
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        List nameValuePairs = new ArrayList<String>(1);
        nameValuePairs.add(new BasicNameValuePair("Dzielnica", dzienicaa));
        nameValuePairs.add(new BasicNameValuePair("Token", tokenn));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = client.execute(httpPost);
            org.apache.http.StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));

                String line;
                line = reader.readLine();
                if (line == "true"){
                    Log.e("==>", "Token wysłany ");
                    return true;
                }else if(line == "false"){
                    Log.e("==>", "Token nie wysłany ");
                    return false;
                }

            } else {
                Log.e("==>", "Fail to dowload ");

            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        }
        return false;
    }}


