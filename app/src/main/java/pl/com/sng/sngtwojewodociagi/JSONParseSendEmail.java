package pl.com.sng.sngtwojewodociagi;

import android.graphics.Bitmap;
import android.util.Base64;


import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class JSONParseSendEmail {

    static InputStream iStream = null;
    static JSONArray jarray = null;
    static String json = "";
    String city,street,streetNumber,failureKind,description,name,phone,mail,lat,lon;
    String images;

    public JSONParseSendEmail(String cit, String stre, String sstrenr, String fail, String desc, String nam, String pho, String mai, String bitmap, String latitu, String longht ) throws IOException, JSONException {
        this.city = cit;
        this.street = stre;
        this.streetNumber = sstrenr;
        this.failureKind = fail;
        this.description = desc;
        this.name = nam;
        this.phone = pho;
        this.mail = mai;
        if(bitmap ==  null ){
            this.images = "brak";
        }else {
            this.images = bitmap;
        }
        if(latitu ==  null ){
            this.lat = "brak";
        }else {
            this.lat = latitu;
        }
        if(longht ==  null ){
            this.lon = "brak";
        }else {
            this.lon = longht;
        }

         getJSONFromUrl("https://notification.sng.com.pl/api/SendFailure/");
    }


        public void  post() throws IOException, JSONException {

            org.json.JSONObject requestJson = new org.json.JSONObject();
            String url = "https://notification.sng.com.pl/api/SendFailure/";            //method call for generating json
            requestJson = generateJSON();
            URL myurl = new URL(url);
            HttpURLConnection con = (HttpURLConnection)myurl.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);

            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Method", "POST");
            OutputStream os = con.getOutputStream();
            os.write(requestJson.toString().getBytes("UTF-8"));
            os.close();


            StringBuilder sb = new StringBuilder();
            int HttpResult =con.getResponseCode();
            if(HttpResult == HttpURLConnection.HTTP_OK){
                BufferedReader br = new BufferedReader(new   InputStreamReader(con.getInputStream(),"utf-8"));

                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
                System.out.println(""+sb.toString());

            }else{
                System.out.println(con.getResponseCode());
                System.out.println(con.getResponseMessage());
            }

        }
    private String getStringFromBitmap(Bitmap bitmapPicture) {

        final int COMPRESSION_QUALITY = 100;
        String encodedImage;
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        bitmapPicture.compress(Bitmap.CompressFormat.PNG, COMPRESSION_QUALITY,
                byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }


    public  org.json.JSONObject generateJSON () throws MalformedURLException, JSONException
    {
        org.json.JSONObject reqparam = new org.json.JSONObject();
        reqparam.put("City",city);
        reqparam.put("Street", street);
        reqparam.put("StreetNumber", streetNumber);
        reqparam.put("FailureKind", failureKind);
        reqparam.put("Description", description);
        reqparam.put("Name", name);
        reqparam.put("Phone", phone);
        reqparam.put("Mail", mail);
        reqparam.put("Foto", images);
        reqparam.put("Lati",lat);
        reqparam.put("Long",lon);
        return reqparam;
    }



    public boolean getJSONFromUrl(String url) throws IOException, JSONException {
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        List nameValuePairs = new ArrayList<String>(1);
        nameValuePairs.add(new BasicNameValuePair("City", city));
        nameValuePairs.add(new BasicNameValuePair("Street", street));
        nameValuePairs.add(new BasicNameValuePair("StreetNumber", streetNumber));
        nameValuePairs.add(new BasicNameValuePair("FailureKind", failureKind));
        nameValuePairs.add(new BasicNameValuePair("Description", description));
        nameValuePairs.add(new BasicNameValuePair("Name", name));
        nameValuePairs.add(new BasicNameValuePair("Phone", phone));
        nameValuePairs.add(new BasicNameValuePair("Mail", mail));
        post();

        //   try {
            //      httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            //      HttpResponse response = client.execute(httpPost);
            //       org.apache.http.StatusLine statusLine = response.getStatusLine();
            //      int statusCode = statusLine.getStatusCode();
            //      if (statusCode == 200) {
                //         HttpEntity entity = response.getEntity();
                //         InputStream content = entity.getContent();
                //       BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                //      } else {
                //          Log.e("==>", "Fail to dowload ");

                //       }
            //    } catch (ClientProtocolException e) {
            //      e.printStackTrace();

            //   } catch (IOException e) {
            //    e.printStackTrace();

      // }
        return false;
    }

}


