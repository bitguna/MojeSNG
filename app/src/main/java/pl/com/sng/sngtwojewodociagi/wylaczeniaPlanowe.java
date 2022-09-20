package pl.com.sng.sngtwojewodociagi;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;

import pl.com.sng.sngtwojewodociagi.R;

/**
 * Created by PBronk on 07.12.2016.
 */
public class wylaczeniaPlanowe extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AsyncResponse {
    View myView;
    TextView tv;
    private static final String urlWyl = "https://notification.sng.com.pl/api/GetPlanoweWylaczenia/";
    private static final String IdWyl = "ElementId";
    private static final String NazwaWyl = "Name";
    private static final String WartoscWyl = "ValueStr";
    SwipeRefreshLayout mSwiftRefresh1;
    TextView dataAktualizacji1;
    String textrelise;
    DataHandler  dataHandler, dataHandler1;
    String currentDateTimeString1;

    @Override
    public void onRefresh() {
        new refresh(getContext()).execute();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.wylaczeniaplanowe, container, false);

        dataHandler = new DataHandler(getContext());
        checkNetConection cc = new checkNetConection(getContext());
        dataAktualizacji1 = (TextView)myView.findViewById(R.id.dataaktualcji1);

        mSwiftRefresh1 = (SwipeRefreshLayout) myView.findViewById(R.id.SwiftRefreshing);

        mSwiftRefresh1.setOnRefreshListener(this);

        if(!cc.isOnline()){
            Toast.makeText(getContext(),"Praca w trybie offline!!",Toast.LENGTH_LONG).show();
        }
        dataHandler.open();
        Cursor cp = dataHandler.returnPlanoweWyl();
        if (cp.moveToFirst()) {
            do {
                textrelise = cp.getString(cp.getColumnIndex(DataHandler.VALUE_WYL));

            } while (cp.moveToNext());
        }
            dataHandler.close();

        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        dataAktualizacji1.setText("Ostatnia aktualizacji: " + currentDateTimeString );

        tv = (TextView)myView.findViewById(R.id.wylaczeniaplanoweview);

        tv.setText(Html.fromHtml(textrelise));

        return myView;



    }

    @Override
    public void processFinish(String output) {

    }

    private class refresh  extends AsyncTask<String, Void, String> {


        public AsyncResponse delega1 = new AsyncResponse() {
            @Override
            public void processFinish(String output) {
                dataHandler1 = new DataHandler(getContext());
                checkNetConection cc = new checkNetConection(getContext());

                if(!cc.isOnline()){
                    Toast.makeText(getContext(),"Praca w trybie offline!!",Toast.LENGTH_SHORT).show();
                }


                textrelise = "";

                dataHandler1.open();
                Cursor cp = dataHandler1.returnPlanoweWyl();
                final String  textrelise1;
                if (cp.moveToFirst()) {

                   textrelise1 = cp.getString(cp.getColumnIndex(DataHandler.VALUE_WYL));
                    tv.setText(Html.fromHtml(textrelise1));

                }
                dataHandler1.close();

                currentDateTimeString1 = null;
                currentDateTimeString1 = DateFormat.getDateTimeInstance().format(new Date());
                dataAktualizacji1.setText("Ostatnia aktualizacji: " + currentDateTimeString1 );
                //   tv = (TextView)myView.findViewById(R.id.wylaczeniaplanoweview);
            }

        };





        private final Context context;

        public refresh(Context c) {
            this.context = c;
            CustomizeDialog mCustomizeDialog;
        }

        @Override
        protected void onPostExecute(String s) {

            delega1.processFinish("end");
            mSwiftRefresh1.setRefreshing( false );
            //   mSwiftRefresh.setEnabled( false );
        }



        @Override
        protected String doInBackground(String... params) {
            DataHandler data = new DataHandler(context);
            data.open();
            data.deleteAllPlanoweWyl();
            JSONParse1 jPartse1 = new JSONParse1();
            JSONArray json1 = jPartse1.getJSONFromUrl(urlWyl);
            for(int i = 0; i<json1.length(); i++)
                try{
                    JSONObject c = json1.getJSONObject(i);
                    String idwyl = c.getString(IdWyl);
                    String nazwawyl = c.getString(NazwaWyl);
                    String wartoscwyl = c.getString(WartoscWyl);
                    data.insertWylaczenia(idwyl,nazwawyl,wartoscwyl);
                }catch(JSONException e){
                    e.printStackTrace();
                }

            data.close();


            return null;
        }
    }


}
