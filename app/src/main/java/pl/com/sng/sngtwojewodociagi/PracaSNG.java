package pl.com.sng.sngtwojewodociagi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PracaSNG extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AsyncResponse {
    private ProgressDialog progress;
    View myView;
    SwipeRefreshLayout mSwiftRefresh;
    TextView dataAktualizacji;
    ListView list1;
    DataHandler dataHandler;
    final ArrayList<String> titlevalue = new ArrayList<String>();
    final ArrayList<String> ElementId1 = new ArrayList<String>();
    final ArrayList<String> ModId = new ArrayList<String>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.praca_w_sng, container, false);
      //  super.onCreate(savedInstanceState);
        mSwiftRefresh = (SwipeRefreshLayout) myView.findViewById(R.id.SwiftRefreshingPraca);
        mSwiftRefresh.setOnRefreshListener(this);
        dataAktualizacji = (TextView)myView.findViewById(R.id.dataaktualcjiPracy);

       // final ArrayAdapter<String> saveAdapter = new ArrayAdapter<String>(getContext(), R.layout.rowlayoutpraca,R.id.pracatext ,titlevalue);

      //  list1.setAdapter(saveAdapter);


        list1 = (ListView) myView.findViewById(R.id.PracaListV);
        dataHandler = new DataHandler(getContext());
        checkNetConection cc = new checkNetConection(getContext());
        if(cc.isOnline()){
            getNewsTitle();
            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            dataAktualizacji.setText("Ostatnia aktualizacji: " + currentDateTimeString);
        } else{
            Toast.makeText(getContext(),"Praca w trybie offline!!",Toast.LENGTH_SHORT).show();
        }




        list1.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener()
                                     {
                                         @Override
                                         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                             checkNetConection cc1 = new checkNetConection(getContext());
                                             if(cc1.isOnline()){
                                                String nrelementu = ElementId1.get(position);
                                                 String moduleId  = ModId.get(position);
                                               String name = titlevalue.get(position);
                                                 Intent w = new Intent(getActivity(), praca_view.class);
                                                  w.putExtra("id_element",nrelementu);
                                                 w.putExtra("modul",moduleId);
                                                 w.putExtra("name",name);
                                                 startActivity(w);

                                             } else{
                                                 Toast.makeText(getContext(),"Praca w trybie offline!!",Toast.LENGTH_SHORT).show();
                                             }

                                         }
                                     }
        );

        return myView;
    }


    @Override
    public void processFinish(String output) {
        final ArrayAdapter<String> saveAdapter = new ArrayAdapter<String>(getContext(), R.layout.rowlayoutpraca, R.id.pracatext, titlevalue) {
            //   CustomListAdapterPraca CLP = new CustomListAdapterPraca(getActivity(),titlevalue,list1);
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Get the current item from ListView
                View view = super.getView(position, convertView, parent);
                if (position % 2 == 1) {
                    // Set a background color for ListView regular row/item
                    view.setBackgroundColor(Color.parseColor("#DEF7FF"));
                } else {
                    // Set the background color for alternate row/item
               //     view.setBackgroundColor(Color.parseColor("#FFCCCB4C"));
                }
                return view;
            }
        };
        list1.setAdapter(saveAdapter);
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
                    JSONArray json1 = jPartse1.getJSONFromUrl("https://notif2.sng.com.pl/api/ListaOfert/");
                   if  (json1.length()>0)
                   {
                       titlevalue.clear();
                       ElementId1.clear();
                       ModId.clear();
                   }

                    for(int i = 0; i<json1.length(); i++)
                        try{
                            JSONObject c = json1.getJSONObject(i);
                            String ElementId = c.getString("Element");
                            String TitleValue1 = c.getString("Module");
                            String NameValue1 = c.getString("name");

                                titlevalue.add(NameValue1);
                                ElementId1.add(ElementId);
                                ModId.add(TitleValue1);

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
                    JSONArray json1 = jPartse1.getJSONFromUrl("https://notif2.sng.com.pl/api/ListaOfert/");
                    if  (json1.length()>0)
                    {
                        titlevalue.clear();
                        ElementId1.clear();
                        ModId.clear();
                    }
                    for(int i = 0; i<json1.length(); i++)
                        try{
                            JSONObject c = json1.getJSONObject(i);
                            String ElementId = c.getString("Element");
                            String TitleValue1 = c.getString("Module");
                            String NameValue1 = c.getString("name");

                            titlevalue.add(NameValue1);
                            ElementId1.add(ElementId);
                            ModId.add(TitleValue1);
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
                    dataAktualizacji = (TextView)myView.findViewById(R.id.dataaktualcjiPracy);
                    dataAktualizacji.setText("Ostatnia aktualizacji: " + currentDateTimeString);

                    mSwiftRefresh.setRefreshing( false );
                }


            }

        }.execute();
    }




}
