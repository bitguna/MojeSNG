package pl.com.sng.sngtwojewodociagi;




import android.app.ProgressDialog;
import android.content.Context;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import android.widget.Toast;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;

import android.widget.ListView;


import pl.com.sng.sngtwojewodociagi.R;

/**
 * Created by PBronk on 17.03.2017.
 */

public class kapieliska extends Fragment implements AsyncResponse {
    View myView;
    AutoCompleteTextView autoCity;
    GetEkoPlazaCity asyncTask;
    GetEkoPlazaPoint ekoPlazaPoint;
    ListView lv;
    String jakieCombo;
    String EkoPlazaCitys = "https://notification.sng.com.pl/api/GetEkoPlazaCity";
    String EkoPlazaJson = "https://notification.sng.com.pl/api/GetEkoPlazaPoint/";

    ArrayList<String> miasta, idMiasta;

    ArrayList<String> plazaPlace;
    ArrayList<String> plazaPlace1;
    ArrayList<String> plazaIndicat;
    ArrayList<String> plazaValue;
    ArrayList<String> plazaMaxValue;
    ArrayList<String> plazaSample;
    ArrayList<String> plazaDateDef;
    ArrayList<String> plazaLati;
    ArrayList<String> plazaLong;
    ListView list1;

    @Override
    public void processFinish(String output) {
        if(jakieCombo.equals("miasta")) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, miasta);
            autoCity.setAdapter(adapter);
        }

        else if(jakieCombo.equals("punktyPlazy")){

            ArrayAdapter<String> saveAdapter = new ArrayAdapter<String>(getContext(), R.layout.jakoscrowlv,R.id.contenctJakosc ,plazaPlace){

                public String getItem(int position)
                {
                   return String.valueOf(Html.fromHtml(plazaPlace.get(position)));
                }
            };
// spróbować przesłać kursor z cała tabela i wtedy zwracam dla poszegolnych wartosci poszegulne elementy
          CustomAdapterKapieliska saveAdapter1 = new CustomAdapterKapieliska(getContext(),plazaPlace1,plazaIndicat,plazaValue,plazaMaxValue,lv);

            lv.setAdapter(saveAdapter1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.kapieliska, container, false);
        autoCity = (AutoCompleteTextView)myView.findViewById(R.id.kapie_spiler);
        Button pokaz_kapie_mapa = (Button) myView.findViewById(R.id.pokaz_na_mapie_kapie);
        lv = (ListView)myView.findViewById(R.id.listViewKapie);
        asyncTask = new GetEkoPlazaCity(getContext());
        asyncTask.delegate = this;
        checkNetConection cc = new checkNetConection(getContext());
        if(cc.isOnline()){

           jakieCombo = "miasta";
            asyncTask.execute();

        } else{
            Toast.makeText(getContext(),"Praca w trybie offline!!",Toast.LENGTH_SHORT).show();
        }

        pokaz_kapie_mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (plazaLati == null) {
                    Toast.makeText(getActivity(),"Brak  punktów kąpielisk do pokazania.", Toast.LENGTH_SHORT).show();
                } else {

                    for(int i =0 ;  i < plazaMaxValue.size() ;i++){
                        String temp;
                        temp = plazaMaxValue.get(i);
                        if(temp.equals(null) || temp == null){
                            plazaMaxValue.set(i,"");
                        }

                    }

                    Intent ww = new Intent(getActivity(), ekoplaza_mapa.class);
                    ww.putExtra("place", plazaPlace);
                    ww.putExtra("indicat",plazaIndicat);
                    ww.putExtra("values",plazaValue);
                    ww.putExtra("maxvalues",plazaMaxValue);
                    ww.putExtra("sample",plazaSample);
                    ww.putExtra("datefin", plazaDateDef);
                    ww.putExtra("latitude", plazaLati);
                    ww.putExtra("longitude", plazaLong);
                    startActivity(ww);
                }
            }
        });


        autoCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {

                String nazwaIdpoint = (String) arg0.getItemAtPosition(arg2);
                InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

                Integer IdPointId = 0;
                for (int i = 0; i < miasta.size(); i++) {
                    if (miasta.get(i).equals(nazwaIdpoint)) {
                        IdPointId = i;
                        break;
                    }
                }
                String idPoint = idMiasta.get(IdPointId);


                ekoPlazaPoint = new GetEkoPlazaPoint(getContext(),EkoPlazaJson,Integer.parseInt(idPoint),asyncTask.delegate);
                jakieCombo = "punktyPlazy";
                checkNetConection cc2 = new checkNetConection(getContext());

                if(cc2.isOnline()) {
                    ekoPlazaPoint.execute();

                }else{

                    Toast.makeText(getContext(),"Praca w trybie offline!!",Toast.LENGTH_SHORT).show();
                }

                autoCity.setError(null);

            }
        });

        autoCity.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                autoCity.showDropDown();
                return false;
            }
        });
        return myView;
    }

    private class GetEkoPlazaPoint extends AsyncTask<String, Void, String> {
        private ProgressDialog progress;
        private Context context;
        private  String url2;
        private Integer header3;
        public AsyncResponse delegate4 = null;

        public  GetEkoPlazaPoint(Context c, String url, Integer header, AsyncResponse delegate){
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
            JSONArray json34 = jsonParseJakoscWody.getJSONFromUrl(url2,header3);
            DataHandler ekodata = new DataHandler(context);
            plazaPlace = new ArrayList<String>();
            plazaIndicat = new ArrayList<String>();
            plazaValue = new ArrayList<String>();
            plazaMaxValue = new ArrayList<String>();
            plazaSample = new ArrayList<String>();
            plazaDateDef = new ArrayList<String>();
            plazaLati = new ArrayList<String>();
            plazaLong = new ArrayList<String>();
            ekodata.open();
            ekodata.deleteAllEkoPoint();
            ekodata.deleteAllEkoBakt();
            for(int i = 0; i<json34.length(); i++)
                try{
                    JSONObject c = json34.getJSONObject(i);
                    String place = c.getString("place");
                    String indicat = c.getString("indicat");
                    String value_desig = c.getString("value_desig");
                    String maxvalue = c.getString("maxvalue");
                    String latitude = c.getString("latitude");
                    String longitude = c.getString("longitude");
                    String datefin = c.getString("datefin");
                    String sample = c.getString("sample");
                    plazaPlace.add(i,place+"("+datefin+")");
                    plazaIndicat.add(i,indicat);
                    plazaValue.add(i ,value_desig);
                    plazaMaxValue.add(i,maxvalue);
                    plazaLati.add(i ,latitude);
                    plazaLong.add(i ,longitude);
                    plazaSample.add(i ,sample);
                    plazaDateDef.add(i,datefin);
                    ekodata.insert_eko_point(place,sample,datefin,latitude,longitude);
                    ekodata.insert_eko_bakt(indicat,sample,value_desig,maxvalue);

                }catch(JSONException e){
                    e.printStackTrace();
                }
            plazaPlace1 = new ArrayList<String>();
            int temp = (plazaPlace.size())/2;
                for(int i = 0; i<temp;i++){

                    plazaPlace1.add(i,plazaPlace.get(i*2));

                }

            ekodata.close();

            return null;
        }

    }

    private class GetEkoPlazaCity extends AsyncTask<String, Void, String> {
        private ProgressDialog progress;
        private Context context;
        public AsyncResponse delegate = null;
        public GetEkoPlazaCity(Context c) {
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
            JSONArray json2 = jPartse2.getJSONFromUrl(EkoPlazaCitys);
            miasta = new ArrayList<String>();
            idMiasta = new ArrayList<String>();
            for(int i = 0; i<json2.length(); i++)
                try{
                    JSONObject c = json2.getJSONObject(i);
                    String idCity = c.getString("IdWinLabo");
                    String nazwaCity = c.getString("Nazwa");
                    miasta.add(i,nazwaCity);
                    idMiasta.add(i,idCity);
                }catch(JSONException e){
                    e.printStackTrace();
                }

            return null;
        }

    }
}
