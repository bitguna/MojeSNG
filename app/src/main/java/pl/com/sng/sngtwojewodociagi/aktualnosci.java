package pl.com.sng.sngtwojewodociagi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import pl.com.sng.sngtwojewodociagi.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by PBronk on 27.12.2016.
 */

public class aktualnosci extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AsyncResponse {
    View myView;

   final ArrayList<String> titlevalue = new ArrayList<String>();
   final  ArrayList<String> IdElement = new ArrayList<String>();
    final  ArrayList<String> ImageValues = new ArrayList<String>();
    private ProgressDialog progress;
    SwipeRefreshLayout mSwiftRefresh;

    TextView dataAktualizacji2;



    ListView list1;
    @Override
    public void processFinish(String output) {
       // final ArrayAdapter<String> saveAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,titlevalue);
        CustomListAdapter saveAdapter = new CustomListAdapter(getActivity(),titlevalue,ImageValues,list1);
        list1.setAdapter(saveAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.aktualnosci, container, false);
        list1 = myView.findViewById(R.id.aktualnosciNewsLista);
        checkNetConection cc = new checkNetConection(getContext());
        if(cc.isOnline()){
            getNewsTitle();
        } else{
            Toast.makeText(getContext(),"Praca w trybie offline!!",Toast.LENGTH_SHORT).show();
        }


        mSwiftRefresh = myView.findViewById(R.id.SwiftRefreshing);


        mSwiftRefresh.setOnRefreshListener(this);
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        dataAktualizacji2 = myView.findViewById(R.id.dataaktualcjiAktualiz);
        dataAktualizacji2.setText("Ostatnia aktualizacji: " + currentDateTimeString);



       list1.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener()
              {
                @Override
                      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                             checkNetConection cc1 = new checkNetConection(getContext());
                                             if(cc1.isOnline()){
                                                 String kategoria = IdElement.get(position);
                                                 String tytul = titlevalue.get(position);
                                                 Intent w = new Intent(getActivity(), aktualnosci_view.class);
                                                 w.putExtra("id_element",kategoria);
                                                 w.putExtra("title",tytul);
                                                 startActivity(w);

                                             } else{
                                                 Toast.makeText(getContext(),"Praca w trybie offline!!",Toast.LENGTH_SHORT).show();
                                             }

                                         }
                                    }
       );
     //   myView.setBackgroundColor(Color.WHITE);
        return myView;

    }

    private void getNewsTitle() {
        new AsyncTask<Void, Void, Void>() {
            protected void onPreExecute() {
                progress = new ProgressDialog(getContext());
                progress.setMessage("Pobieranie danych z servera");
                progress.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    JSONParse1 jPartse1 = new JSONParse1();
                    JSONArray json1 = jPartse1.getJSONFromUrl("https://notification.sng.com.pl/api/GetWebSNGNewsTitle/");

                    if  (json1.length()>0)
                    {
                        titlevalue.clear();
                        IdElement.clear();
                    }

                    for(int i = 0; i<json1.length(); i++)
                        try{
                            JSONObject c = json1.getJSONObject(i);
                           String ElementId = c.getString("ElementId");
                           String TitleValue1 = c.getString("ValueStr");
                            String NameValue1 = c.getString("Name");

                        if(NameValue1.equals("Title")){
                            IdElement.add(ElementId);
                            titlevalue.add(TitleValue1);}

                        else if(NameValue1.equals("Image")){
                           String titleValue2 =  TitleValue1.replace("/portals/","http://www.sng.com.pl/portals/");
                            ImageValues.add(titleValue2);
                        }
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

    @Override
    public void onRefresh() {
        new AsyncTask<Void, Void, Void>() {
            protected void onPreExecute() {
            //    progress = new ProgressDialog(getContext());
            //    progress.setMessage("Pobieranie danych z servera");
            //    progress.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    JSONParse1 jPartse1 = new JSONParse1();
                    JSONArray json1 = jPartse1.getJSONFromUrl("https://notification.sng.com.pl/api/GetWebSNGNewsTitle/");

                    if  (json1.length()>0)
                    {
                        titlevalue.clear();
                        IdElement.clear();
                    }

                    for(int i = 0; i<json1.length(); i++)
                        try{
                            JSONObject c = json1.getJSONObject(i);
                            String ElementId = c.getString("ElementId");
                            String TitleValue1 = c.getString("ValueStr");
                            String NameValue1 = c.getString("Name");

                            if(NameValue1.equals("Title")){
                                IdElement.add(ElementId);
                                titlevalue.add(TitleValue1);}

                            else if(NameValue1.equals("Image")){
                                String titleValue2 =  TitleValue1.replace("/portals/","http://www.sng.com.pl/portals/");
                                ImageValues.add(titleValue2);
                            }
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
                if (mSwiftRefresh.isEnabled()){
                //    progress.dismiss();
                     processFinish("true");
                    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                    dataAktualizacji2 = myView.findViewById(R.id.dataaktualcjiAktualiz);
                    dataAktualizacji2.setText("Ostatnia aktualizacji: " + currentDateTimeString);

                    mSwiftRefresh.setRefreshing( false );
                }


            }

        }.execute();
    }
}
