package pl.com.sng.sngtwojewodociagi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import pl.com.sng.sngtwojewodociagi.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by PBronk on 28.12.2016.
 */

public class aktualnosci_view extends AppCompatActivity implements AsyncResponse{
    private ProgressDialog progress;
    WebView wv;
    String Element_Id,title;
    final ArrayList<String> Name1 = new ArrayList<String>();
    final  ArrayList<String> IdElement = new ArrayList<String>();
    final  ArrayList<String> Wartosci1 = new ArrayList<String>();
    @Override
    public void processFinish(String output) {
        String HtmlTextShortContent = "";
        String HtmlTextContent = "";
        String HtmlText = "";
        for(int i = 0 ; i< Name1.size(); i++ ){
            String testname = Name1.get(i);
            switch(testname){
                case "ShortContent":
                    HtmlTextShortContent = Wartosci1.get(i);
                    break;
                case "Content":
                    HtmlTextContent = Wartosci1.get(i);
                    break;
            }
        }

        HtmlTextShortContent = HtmlTextShortContent.replace("/portals/","http://www.sng.com.pl/portals/");
        HtmlTextContent =  HtmlTextContent.replace("/portals/","http://www.sng.com.pl/portals/");
        HtmlText = "<br><center><big><b>"+title + "</b></big></center><br><br>" +HtmlTextShortContent + " "+ HtmlTextContent+"<br><br>";

        wv.getSettings().setBuiltInZoomControls(true);   // lupa umozliwiajaca przyblizenia,
        wv.loadDataWithBaseURL(null, HtmlText , "text/html", "UTF-8", null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aktualnosci_view_info);
        wv = (WebView) findViewById(R.id.webView);
        Intent intent = getIntent();
        Element_Id = intent.getStringExtra("id_element");
        title = intent.getStringExtra("title");
        getNews(Integer.parseInt(Element_Id),this);
     //   if(getResources().getBoolean(R.bool.portrait_only)){
     //       setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
       // }


    }

    private void getNews(Integer element1, Context c) {

       final Integer element = element1;
       final Context c1 = c;

        new AsyncTask<Void, Void, Void>() {



            protected void onPreExecute() {
                progress = new ProgressDialog(c1);
                progress.setMessage("Pobieranie danych z servera");
                progress.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    JSONParseJakoscWody jPartse1 = new JSONParseJakoscWody();
                    JSONArray json1 = jPartse1.getJSONFromUrl("https://notification.sng.com.pl/api/GetWebSNGNews/",element);
                    for(int i = 0; i<json1.length(); i++)
                        try{
                            JSONObject c = json1.getJSONObject(i);
                            String ElementId = c.getString("ElementId");
                            String Name2 = c.getString("Name");
                            String wartosci1 = c.getString("ValueStr");
                            Name1.add(i,Name2);
                            IdElement.add(i,ElementId);
                            Wartosci1.add(i,wartosci1);

                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                    return null;

                } catch (Exception e) {
                    // log error
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (progress.isShowing()){
                    progress.dismiss();
                    processFinish("true");
                }
            }

        }.execute();


    }



}
