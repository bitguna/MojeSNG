package pl.com.sng.sngtwojewodociagi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class praca_view extends AppCompatActivity implements AsyncResponse
{


String ElementId, Module_Id,Name_Values ;
    final ArrayList<String> Values = new ArrayList<String>();

    WebView pracaWV;
    private ProgressDialog progress;

    @Override
    public void processFinish(String output) {
        String HtmlTextOut = "";
        String HtmlTextOut1 = "";
        String HtmlText = "";
            HtmlText= Values.get(0);
        HtmlTextOut1 = HtmlText.replace("<a href=\"https://www.sng.com.pl/pl-pl/Praca/Praca.aspx\" target=\"_blank\">","<a href=\"mojeSNG://formularz_praca\">");
        HtmlTextOut =  HtmlTextOut1.replace("<a href=\"https://www.sng.com.pl/pl-pl/praca/praca.aspx\" target=\"_blank\">","<a href=\"mojeSNG://formularz_praca\">");

     //   HtmlTextContent =  HtmlTextContent.replace("/portals/","http://www.sng.com.pl/portals/");
      //  HtmlText = "<br><center><big><b>"+title + "</b></big></center><br><br>" +HtmlTextShortContent + " "+ HtmlTextContent+"<br><br>

      //  pracaWV.getSettings().setBuiltInZoomControls(true);   // lupa umozliwiajaca przyblizenia,
      //  pracaWV.loadDataWithBaseURL(null, HtmlTextOut , "text/html", "UTF-8", null);
        pracaWV.loadData(HtmlTextOut , "text/html", "UTF-8");
        pracaWV.setWebViewClient(new MyWebViewClient(this));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_praca_view);
        pracaWV = (WebView) findViewById(R.id.praca_web_view);
        Intent intent = getIntent();
        ElementId = intent.getStringExtra("id_element");
        Module_Id = intent.getStringExtra("modul");
        Name_Values = intent.getStringExtra("name");
        getJobs(Module_Id,ElementId,"Content", this);

    }

    private void getJobs(String  mod, String Element, String name  , Context c) {

        final String modu = mod;
        final String Elem = Element;
        final String name1 = name;
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
                    JSONPraca jPartse1 = new JSONPraca();
                    JSONArray json1 = jPartse1.getJSONFromUrl("https://notif2.sng.com.pl/api/GetWebPraca/",modu,Elem,name1);
                    for(int i = 0; i<json1.length(); i++)
                        try{
                            JSONObject c = json1.getJSONObject(i);
                            String TempValues = c.getString("ValueStr");
                            Values.add(i,TempValues);

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
