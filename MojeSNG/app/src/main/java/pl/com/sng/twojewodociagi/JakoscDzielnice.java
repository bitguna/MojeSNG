package pl.com.sng.twojewodociagi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import pl.com.sng.twojewodociagi.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by PBronk on 08.12.2016.
 */

public class JakoscDzielnice extends Fragment implements AsyncResponse {
    View myView;
    String urlGetCityName = "https://notification.sng.com.pl/api/GetLocalizationCity/";
    String urlGetStreetName = "https://notification.sng.com.pl/api/GetLocalizationStreet";
    String urlGetNrBud = "https://notification.sng.com.pl/api/GetLocalizationPoint";
    String urlGetUjecie = "https://notification.sng.com.pl/api/GetLocalizationPointIntakes";
    Spinner spinnerCity ;
    //Spinner spinnerStreet ;
    AutoCompleteTextView spinnerStreet;
    AutoCompleteTextView spinnerPoint ;
    String[] arraySpinner;
    String[] arraySpinnerValues;
    String[] arraySpinnerStreet;
    String[] arraySpinnerValuesStreet;
    String[] arraySpinnerPoint;
    String[] arraySpinnerValuesPoint;
    ArrayList<String> arraySpinnerUjecieContent;
    ArrayList<String> arraySpinnerValuesUjecieLat;
    ArrayList<String> arraySpinnerValuesUjecieLon;
    GetJakosc asyncTask;
    GetJakoscStreet asyncStreet ;
    GetJakoscNrBud asyncPoint;
    GetJakoscUjecie asyncUjecie;
    ListView lv;
    String jakieCombo;

    @Override
    public void processFinish(String output){

        if(jakieCombo.equals("miasta")) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, arraySpinner);
            spinnerCity.setAdapter(adapter);
        }else if(jakieCombo.equals("ulica")){
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, arraySpinnerStreet);
            spinnerStreet.setAdapter(adapter);

        }else if(jakieCombo.equals("numer")){
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, arraySpinnerPoint);
            spinnerPoint.setAdapter(adapter);
        }
        else if(jakieCombo.equals("ujecie")){

              ArrayAdapter<String> saveAdapter = new ArrayAdapter<String>(getContext(), R.layout.jakoscrowlv,R.id.contenctJakosc ,arraySpinnerUjecieContent){
                public String getItem(int position)
                {
                    return String.valueOf(Html.fromHtml(arraySpinnerUjecieContent.get(position)));
                }

            };

            lv.setAdapter(saveAdapter);
        }

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.jakoscwody, container, false);
        spinnerCity = (Spinner)myView.findViewById(R.id.spinner2);
        spinnerStreet = (AutoCompleteTextView)myView.findViewById(R.id.spinner3);
        spinnerPoint = (AutoCompleteTextView)myView.findViewById(R.id.spinner4);
        Button pokaz_jakosc_mapa = (Button) myView.findViewById(R.id.pokaz_na_mapie_jakosc);
        asyncTask =new GetJakosc(getContext());
        asyncTask.delegate = this;
         lv = (ListView)myView.findViewById(R.id.listViewJakosc);


        checkNetConection cc = new checkNetConection(getContext());
        if(cc.isOnline()){

            jakieCombo = "miasta";
            asyncTask.execute();

        } else{
            Toast.makeText(getContext(),"Praca w trybie offline!!",Toast.LENGTH_SHORT).show();
        }
       spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

           @Override
           public void onItemSelected(AdapterView<?> arg0, View arg1,
                                      int arg2, long arg3) {

               checkNetConection cc1 = new checkNetConection(getContext());

               if(cc1.isOnline()) {

                   String msupplier=spinnerCity.getSelectedItem().toString();
                   String idCity = arraySpinnerValues[arg2];
                   asyncStreet = new GetJakoscStreet(getContext(),urlGetStreetName,Integer.parseInt(idCity),asyncTask.delegate);
                   jakieCombo = "ulica";
                   asyncStreet.execute();



               }else{

                   Toast.makeText(getContext(),"Praca w trybie offline!!",Toast.LENGTH_SHORT).show();
               }


               String msupplier=spinnerCity.getSelectedItem().toString();
                String idCity = arraySpinnerValues[arg2];
               asyncStreet = new GetJakoscStreet(getContext(),urlGetStreetName,Integer.parseInt(idCity),asyncTask.delegate);
               jakieCombo = "ulica";
               asyncStreet.execute();


               Log.e("Selected item : ",msupplier);
           }

           @Override
           public void onNothingSelected(AdapterView<?> arg0) {


           }
       });
        spinnerStreet.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
          //      String msupplier1=spinnerStreet.getSelectedItem().toString();
                checkNetConection cc = new checkNetConection(getContext());

                if(cc.isOnline()) {
                    String nazwaIdpoint = (String) arg0.getItemAtPosition(arg2);
                    Integer IdPointId = 0;
                    for (int i = 0; i < arraySpinnerStreet.length; i++) {
                        if (arraySpinnerStreet[i].equals(nazwaIdpoint)) {
                            IdPointId = i;
                            break;
                        }
                    }
                    String idPoint = arraySpinnerValuesStreet[IdPointId];

                    asyncPoint = new GetJakoscNrBud(getContext(),urlGetNrBud,Integer.parseInt(idPoint),asyncTask.delegate);
                    jakieCombo = "numer";
                    asyncPoint.execute();
                    spinnerPoint.setFocusableInTouchMode(true);
                    spinnerPoint.requestFocus();

                }else{

                    Toast.makeText(getContext(),"Praca w trybie offline!!",Toast.LENGTH_SHORT).show();
                }


          //      Log.e("Selected item : ",msupplier1);
            }
        });


        spinnerPoint.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {

                checkNetConection cc2 = new checkNetConection(getContext());

                if(cc2.isOnline()) {

                    InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

                    String nazwaIdpoint1 = (String) arg0.getItemAtPosition(arg2);
                    Integer IdPointId1 = 0;
                    for (int i = 0; i < arraySpinnerStreet.length; i++) {
                        if (arraySpinnerStreet[i].equals(nazwaIdpoint1)) {
                            IdPointId1 = i;
                            break;
                        }
                    }
                    String idPoint1 = arraySpinnerValuesPoint[IdPointId1];


                    asyncUjecie = null;
                    asyncUjecie = new GetJakoscUjecie(getContext(),urlGetUjecie,Integer.parseInt(idPoint1),asyncTask.delegate);
                    jakieCombo = "ujecie";
                    asyncUjecie.execute();
                }else{

                    Toast.makeText(getContext(),"Praca w trybie offline!!",Toast.LENGTH_SHORT).show();
                }

            }
        });

        spinnerStreet.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // WORKS IF USED ON TOUCH
            //  spinnerStreet.setInputType(InputType.TYPE_NULL);
                spinnerStreet.showDropDown();


                return false;
            }
        });


        spinnerPoint.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // WORKS IF USED ON TOUCH

            spinnerPoint.showDropDown();
                return false;
            }
        });
        pokaz_jakosc_mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (arraySpinnerUjecieContent.isEmpty()) {
                   Toast.makeText(getActivity(),"Brak ujęć wody do pokazania", Toast.LENGTH_SHORT).show();
                } else {
                    Intent w = new Intent(getActivity(), jakosc_wody_mapa.class);
                    w.putExtra("context", arraySpinnerUjecieContent);
                    w.putExtra("latitude", arraySpinnerValuesUjecieLat);
                    w.putExtra("longitude", arraySpinnerValuesUjecieLon);
                    startActivity(w);
                }
            }
        });

        return myView;
    }



    private class GetJakosc extends AsyncTask<String, Void, String> {
        private ProgressDialog progress;
        private Context context;
        public AsyncResponse delegate = null;
        public GetJakosc(Context c) {
            this.context = c;
        }



        protected void onPreExecute() {
            progress = new ProgressDialog(this.context);
            progress.setMessage("Pobieranie danych");
            progress.show();
        }

        @Override
        protected void onPostExecute(String result) {
            delegate.processFinish(result);
            if (progress.isShowing()){
                progress.dismiss();
            }
        }
        @Override
        protected String doInBackground(String... params) {

            JSONParse1 jPartse2 = new JSONParse1();
            JSONArray json2 = jPartse2.getJSONFromUrl(urlGetCityName);
            arraySpinner = new String[json2.length()];
            arraySpinnerValues = new String[json2.length()];
            for(int i = 0; i<json2.length(); i++)
                try{
                    JSONObject c = json2.getJSONObject(i);
                    String idwyl = c.getString("Id");
                    String nazwawyl = c.getString("Name");
                    arraySpinner[i] = nazwawyl;
                    arraySpinnerValues[i] = idwyl;


                }catch(JSONException e){
                    e.printStackTrace();
                }
            return null;

        }
    }
    private class GetJakoscStreet extends AsyncTask<String, Void, String> {
        private ProgressDialog progress;
        private Context context;
        private  String url1;
        private Integer header1;
        public AsyncResponse delegate1 = null;
        public GetJakoscStreet(Context c, String url, Integer header, AsyncResponse delegate) {
            this.context = c;
            this.url1 = url;
            this.header1 = header;
            this.delegate1 = delegate;
        }
        protected void onPreExecute() {
            progress = new ProgressDialog(this.context);
            progress.setMessage("Pobieranie danych");
            progress.show();
        }

        @Override
        protected void onPostExecute(String result) {
            delegate1.processFinish(result);
            if (progress.isShowing()){
                progress.dismiss();
            }
        }
        @Override
        protected String doInBackground(String... params) {

            JSONParseJakoscWody jsonParseJakoscWody = new JSONParseJakoscWody();
            JSONArray json2 = jsonParseJakoscWody.getJSONFromUrl(url1,header1);
            arraySpinnerStreet = new String[json2.length()];
            arraySpinnerValuesStreet = new String[json2.length()];
            for(int i = 0; i<json2.length(); i++)
                try{
                    JSONObject c = json2.getJSONObject(i);
                    String idUlicy = c.getString("Id");
                    String nazwisko = c.getString("Name");
                    String imie = c.getString("Name1");
                    if(imie != null && !imie.isEmpty() && !imie.equals("null")){

                        arraySpinnerStreet[i] = nazwisko + " " + imie;
                    }else {
                        arraySpinnerStreet[i] = nazwisko;
                    }
                    arraySpinnerValuesStreet[i] = idUlicy;
                }catch(JSONException e){
                    e.printStackTrace();
                }
            return null;

        }
    }
    private class GetJakoscNrBud extends AsyncTask<String, Void, String> {
        private ProgressDialog progress;
        private Context context;
        private  String url1;
        private Integer header2;
        public AsyncResponse delegate2 = null;
        public GetJakoscNrBud(Context c, String url, Integer header, AsyncResponse delegate) {
            this.context = c;
            this.url1 = url;
            this.header2 = header;
            this.delegate2 = delegate;
        }
        protected void onPreExecute() {
            progress = new ProgressDialog(this.context);
            progress.setMessage("Pobieranie danych");
            progress.show();
        }

        @Override
        protected void onPostExecute(String result) {
            delegate2.processFinish(result);
            if (progress.isShowing()){
                progress.dismiss();
            }
        }
        @Override
        protected String doInBackground(String... params) {

            JSONParseJakoscWody jsonParseJakoscWody = new JSONParseJakoscWody();
            JSONArray json3 = jsonParseJakoscWody.getJSONFromUrl(url1,header2);
            arraySpinnerPoint = new String[json3.length()];
            arraySpinnerValuesPoint = new String[json3.length()];
            for(int i = 0; i<json3.length(); i++)
                try{
                    JSONObject c = json3.getJSONObject(i);
                    String idNrBud = c.getString("Id");
                    String nrBud = c.getString("Name");
                    arraySpinnerPoint[i] = nrBud;
                    arraySpinnerValuesPoint[i] = idNrBud;
                }catch(JSONException e){
                    e.printStackTrace();
                }
            return null;

        }
    }
    private class GetJakoscUjecie extends AsyncTask<String, Void, String> {
        private ProgressDialog progress;
        private Context context;
        private  String url2;
        private Integer header3;
        public AsyncResponse delegate4 = null;
        public GetJakoscUjecie(Context c, String url, Integer header, AsyncResponse delegate) {
            this.context = c;
            this.url2 = url;
            this.header3 = header;
            this.delegate4 = delegate;
        }
        protected void onPreExecute() {

            progress = new ProgressDialog(this.context);
            progress.setMessage("Pobieranie danych");
            progress.show();

        }

        @Override
        protected void onPostExecute(String result) {
            delegate4.processFinish(result);
            if (progress.isShowing()){
                progress.dismiss();
            }
        }
        @Override
        protected String doInBackground(String... params) {

            JSONParseJakoscWody jsonParseJakoscWody = new JSONParseJakoscWody();
            JSONArray json4 = jsonParseJakoscWody.getJSONFromUrl(url2,header3);
            arraySpinnerUjecieContent = new ArrayList<String>();
            arraySpinnerValuesUjecieLat = new ArrayList<String>();
            arraySpinnerValuesUjecieLon = new ArrayList<String>();
            for(int i = 0; i<json4.length(); i++)
                try{
                    JSONObject c = json4.getJSONObject(i);
                    String latitude = c.getString("latitude");
                    String longitude = c.getString("longitude");
                    String Content = c.getString("Content");
                    arraySpinnerUjecieContent.add(i,Content);
                    arraySpinnerValuesUjecieLat.add(i ,latitude);
                    arraySpinnerValuesUjecieLon.add(i,longitude);
                }catch(JSONException e){
                    e.printStackTrace();
                }
            return null;

        }
    }

}
