package pl.com.sng.sngtwojewodociagi.JSON;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import pl.com.sng.sngtwojewodociagi.OBJECT.OBJECTabout;

public class JSONabout {
        Context con = null;
        OBJECTabout oabout;
        public void StartUpdate(OBJECTabout _about, Context context) {
            con = context;
            oabout = _about;
            new HttpAsyncTask2().execute("https://notif2.sng.com.pl/api/GetAbout");
        }
        public String POST(String url) {
            InputStream inputStream = null;
            String result = "";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                String json = "";
                JSONObject jsonObject = new JSONObject();
//                jsonObject.accumulate("User",User);

                json = jsonObject.toString();
                StringEntity se = new StringEntity(json);
                httpPost.setEntity(se);
                httpPost.setHeader("Content-type", "application/json");
                httpPost.setHeader("Accept", "application/json");
                HttpResponse httpResponse = httpclient.execute(httpPost);
                inputStream = httpResponse.getEntity().getContent();
                if (inputStream != null) result = convertInputStreamToString(inputStream);
                else result = "Nie działa";
            } catch (Exception e) {
            }
            return result;
        }
        private class HttpAsyncTask2 extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... urls) {
                return POST(urls[0]);
            }


            @Override
            protected void onPostExecute(String result) {
                if(result.length()>5)
                {
                    try {
                        JSONArray array = new JSONArray(result);
                        for(int i = 0; i< array.length();i++)
                        {
                            oabout.setText(array.getJSONObject(i).getString("about"));
                        }

                    }catch (Exception e)
                    {

                    }
                }
                else
                {

                }
            }
        }

        private static String convertInputStreamToString(InputStream inputStream) throws IOException {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while ((line = bufferedReader.readLine()) != null) result += line;
            inputStream.close();
            return result;
        }
    }


