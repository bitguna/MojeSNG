package pl.com.sng.twojewodociagi;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;


import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import pl.com.sng.twojewodociagi.R;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by PBronk on 04.11.2016.
 */

public  class wiadomosci extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AsyncResponse {
    private Context context;
    ListView list1;
    View myView;
    DataHandler dataHandler;
    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> value = new ArrayList<String>();
    ArrayList<String> value1 = new ArrayList<String>();
    MainActivity getData;
    Context con = getContext();
    SwipeRefreshLayout mSwiftRefresh;
    ArrayList<String> id_alert = new ArrayList<String>();
    TextView dataAktualizacji;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.wiadomosci, container, false);



        mSwiftRefresh = (SwipeRefreshLayout) myView.findViewById(R.id.SwiftRefreshing);


        mSwiftRefresh.setOnRefreshListener(this);
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        dataAktualizacji = (TextView)myView.findViewById(R.id.dataaktualcji);
      //  dataAktualizacji.setText("Ostatnia aktualizacji: " + currentDateTimeString );


        list1 = (ListView) myView.findViewById(R.id.testalert);
        dataHandler = new DataHandler(getContext());
        checkNetConection cc = new checkNetConection(getContext());

        if(!cc.isOnline()){
            Toast.makeText(getContext(),"Praca w trybie offline!!",Toast.LENGTH_SHORT).show();
        }

        dataHandler.open();
        Cursor cp = dataHandler.returnAlerts();

        if (cp.moveToFirst()) {
            dataAktualizacji.setText("Ostatnia aktualizacji: " + cp.getString(cp.getColumnIndex(DataHandler.TIME_O)));
            do {
                name.add(cp.getString(cp.getColumnIndex(DataHandler.NAME)));
                value.add(cp.getString(cp.getColumnIndex(DataHandler.VALUE)));
                id_alert.add(cp.getString(cp.getColumnIndex(DataHandler.ID_ALERT)));

            } while (cp.moveToNext());

            dataHandler.close();
          final  ArrayAdapter<String> saveAdapter = new ArrayAdapter<String>(getContext(), R.layout.rowlayout,R.id.label ,value);

            list1.setAdapter(saveAdapter);
            list1.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String kategoria = id_alert.get(position);
                    Intent w = new Intent(getActivity(), alert_info.class);
                    w.putExtra("id_alert",kategoria);
                    startActivity(w);
                }
            }
            );
        }else{
            value1.clear();
            dataAktualizacji.setText("Ostatnia aktualizacji: " +  DateFormat.getDateTimeInstance().format(new Date()));
            value1.add("Obecnie nie odnotowano żadnych awarii.");
            final  ArrayAdapter<String> saveAdapter = new ArrayAdapter<String>(getContext(), R.layout.rowlayout1,R.id.label ,value1);

            list1.setAdapter(saveAdapter);

        }
        dataHandler.close();
        return myView;
    }


    @Override
    public void onRefresh() {
       new refresh().execute();
    }

    @Override
    public void processFinish(String output) {



    }


    private class refresh  extends AsyncTask<String, Void, String> {
        public AsyncResponse delega = new AsyncResponse() {
            @Override
            public void processFinish(String output) {
                dataHandler = new DataHandler(getContext());
                checkNetConection cc = new checkNetConection(getContext());

                if(!cc.isOnline()){
                    Toast.makeText(getContext(),"Praca w trybie offline!!",Toast.LENGTH_SHORT).show();
                }

                dataHandler.open();

                Cursor cp = dataHandler.returnAlerts();
                name.clear();
                value.clear();
                id_alert.clear();
                if (cp.moveToFirst()) {
                    dataAktualizacji.setText("Ostatnia aktualizacja: " + cp.getString(cp.getColumnIndex(DataHandler.TIME_O)));
                    do {
                        name.add(cp.getString(cp.getColumnIndex(DataHandler.NAME)));
                        value.add(cp.getString(cp.getColumnIndex(DataHandler.VALUE)));
                        id_alert.add(cp.getString(cp.getColumnIndex(DataHandler.ID_ALERT)));

                    } while (cp.moveToNext());

                    dataHandler.close();
                    final  ArrayAdapter<String> saveAdapter = new ArrayAdapter<String>(getContext(), R.layout.rowlayout,R.id.label ,value);
                    //saveAdapter.notifyDataSetChanged();
                    list1.setAdapter(saveAdapter);
                    list1.setOnItemClickListener(new AdapterView.OnItemClickListener()
                                                 {
                                                     @Override
                                                     public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                         String kategoria = id_alert.get(position);
                                                         Intent w = new Intent(getActivity(), alert_info.class);
                                                         w.putExtra("id_alert",kategoria);
                                                         startActivity(w);
                                                     }
                                                 }
                    );
                }else{
                    value1.clear();
                dataAktualizacji.setText("Ostatnia aktualizacji: " +  DateFormat.getDateTimeInstance().format(new Date()));
                value1.add("Obecnie nie odnotowano żadnych awarii.");
                final  ArrayAdapter<String> saveAdapter = new ArrayAdapter<String>(getContext(), R.layout.rowlayout1,R.id.label ,value1);

                list1.setAdapter(saveAdapter);

            }



                dataHandler.close();
                String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                dataAktualizacji.setText("Ostatnia aktualizacji: " + currentDateTimeString );
            }

        };
        private static final String DzieniceId = "DzielniceId";
        private static final String Miasto = "Miasto";
        private static final String Dzielnica = "Dzielnica";
        private static final String url = "https://notification.sng.com.pl/api/GetAlerts/";
        private static final String Id = "Id";
        private static final String Nazwa = "Name";
        private static final String Wartosc = "value";
        private static final String urlWyl = "https://notification.sng.com.pl/api/GetPlanoweWylaczenia/";
        private static final String IdWyl = "ElementId";
        private static final String NazwaWyl = "Name";
        private static final String WartoscWyl = "ValueStr";
        private static final String urlDzienice = "https://notification.sng.com.pl/api/GetDzielnice/";
        String token;
        @Override
        protected void onPostExecute(String s) {

            delega.processFinish("end");
            mSwiftRefresh.setRefreshing( false );
         //   mSwiftRefresh.setEnabled( false );
        }

        @Override
        protected String doInBackground(String... params) {

            token = FirebaseInstanceId.getInstance().getToken();

            try {
                new JSONParseAddToken(token);   /// dodawanie tokienow
            }catch(Exception e){
                e.printStackTrace();
            }

            DataHandler data9 = new DataHandler(getContext());
            data9.open();
            data9.deleteAllAlerts();


            JSONParse jPartse = new JSONParse();
            JSONArray json = jPartse.getJSONFromUrl(url);
            for(int i = 0; i<json.length(); i++)
                try{
                    JSONObject c = json.getJSONObject(i);
                    String id = c.getString(Id);
                    String nazwa = c.getString(Nazwa);
                    String wartosc = c.getString(Wartosc);
                    data9.insertAlert(id,nazwa,wartosc);
                }catch(JSONException e){
                    e.printStackTrace();
                }
            data9.deleteAllPlanoweWyl();
            JSONParse1 jPartse1 = new JSONParse1();
            JSONArray json1 = jPartse1.getJSONFromUrl(urlWyl);
            for(int i = 0; i<json1.length(); i++)
                try{
                    JSONObject c = json1.getJSONObject(i);
                    String idwyl = c.getString(IdWyl);
                    String nazwawyl = c.getString(NazwaWyl);
                    String wartoscwyl = c.getString(WartoscWyl);
                    data9.insertWylaczenia(idwyl,nazwawyl,wartoscwyl);
                }catch(JSONException e){
                    e.printStackTrace();
                }

            JSONParse1 jPartse2 = new JSONParse1();
            JSONArray json2 = jPartse2.getJSONFromUrl(urlDzienice);
            data9.deleteAllDzielnice();
            for(int i = 0; i<json2.length(); i++)
                try{
                    JSONObject c = json2.getJSONObject(i);
                    String id = c.getString(DzieniceId);
                    String miasto = c.getString(Miasto);
                    String wartosc = c.getString(Dzielnica);
                    data9.insertDzielnice(id,miasto,wartosc);
                }catch(JSONException e){
                    e.printStackTrace();
                }

            data9.close();
            return null;
        }
    }

}
