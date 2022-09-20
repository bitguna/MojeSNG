package pl.com.sng.twojewodociagi;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import pl.com.sng.twojewodociagi.R;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

public class wybierzDzielnice extends AppCompatActivity  {
    private ListView list ;
    ListView listView;


    ArrayList<dzielnice> dzielnicaList;
    private ArrayAdapter<String> adapter ;
    DataHandler dHr;
    ArrayList<String> wszystkiedzielnice = new ArrayList<String>();
    MyCustomAdapter dataAdapter = null;
    MyCustomAdapter tempAdapter = null;
    private ProgressDialog progress;
    CheckBox select_all;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wybierz_dzielnice);
        Button wyslij_dzielnice;
        wyslij_dzielnice = (Button)findViewById(R.id.but_zap_dziel);
       final ListView listaDzielnic;
        final Context con = this;
        listaDzielnic = (ListView)findViewById(R.id.listadzielnic);
        final CheckBox select_all = (CheckBox) findViewById(R.id.checkBox);




        select_all.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(select_all.isChecked())
                {
                    DataHandler  dHr1 = new DataHandler(getBaseContext());

                        dHr1.open();
                        dHr1.selectAll();
                        dHr1.close();

                    for ( int i=0; i < dzielnicaList.size(); i++) {
                        tempAdapter = (MyCustomAdapter) listaDzielnic.getAdapter();
                        tempAdapter.getItem(i).setSelected(true);

                    }


                }
                if(!select_all.isChecked())
                {
                    DataHandler  dHr1 = new DataHandler(getBaseContext());
                    dHr1.open();
                    dHr1.unSelectAll();
                    dHr1.close();

                    for ( int i=0; i < dzielnicaList.size(); i++) {
                        tempAdapter = (MyCustomAdapter) listaDzielnic.getAdapter();
                        tempAdapter.getItem(i).setSelected(false);               }
                }
                listView.setAdapter(tempAdapter);
            }
        });




        wyslij_dzielnice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList <String> DzielniceWys = new ArrayList<String>();
                DzielniceWys.add(0,"Delete");
                String token;
                token = FirebaseInstanceId.getInstance().getToken();

                DataHandler dHr2 = new DataHandler(con);
                dHr2.open();



                Cursor cp = dHr2.returnDzielniceActive();

               if (cp.getCount() == 0) {
                   wyslijDane(token, DzielniceWys, con);
               }
                if (cp.moveToFirst()) {
                    do {
                        DzielniceWys.add(cp.getString(cp.getColumnIndex(DataHandler.ID_DZIENICE)));

                    } while (cp.moveToNext());

                    checkNetConection cc = new checkNetConection(con);
                    if(cc.isOnline()){

                        wyslijDane(token,DzielniceWys, con);

                    } else{
                        Toast.makeText(con,"Praca w trybie offline!!",Toast.LENGTH_SHORT).show();
                    }
                }
                dHr2.close();
            }
        });
       Boolean allCheck = displayListView();
        if(allCheck == true){
            select_all.setChecked(true);
        }


    }
    private boolean displayListView() {
        dzielnicaList = new ArrayList<dzielnice>();

        dHr = new DataHandler(this);

        dHr.open();
        Cursor cp1 = dHr.returnDzielnice();
        int tempSelectAll = 0;
        Boolean tempSelectAllBool = false;
        if (cp1.moveToFirst()) {
            do {
                boolean tempMark ;

                if(cp1.getInt(cp1.getColumnIndex(DataHandler.ZAZN_DZIENLICA)) == 1 ){tempMark = true;} else{tempMark = false;};

                dzielnice dziel = new dzielnice(cp1.getString(cp1.getColumnIndex(DataHandler.MAISTO)),cp1.getString(cp1.getColumnIndex(DataHandler.DZIELNICA)),tempMark);
                dzielnicaList.add(dziel);
                if(tempMark == true ){
                    tempSelectAll = tempSelectAll+1;                }


            } while (cp1.moveToNext());
        if(tempSelectAll == cp1.getCount()) {
                tempSelectAllBool = true;
            }
        }
        dHr.close();

        //create an ArrayAdaptar from the String Array
        dataAdapter = new MyCustomAdapter(this,
                R.layout.listadzielnicrow, dzielnicaList);
       listView = (ListView) findViewById(R.id.listadzielnic);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text

            //    Toast.makeText(getApplicationContext(),"Clicked on Row: " + dzielll.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        return tempSelectAllBool;
    }

    private class MyCustomAdapter extends ArrayAdapter<dzielnice> {

        private ArrayList<dzielnice> countryList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<dzielnice> countryList) {
            super(context, textViewResourceId, countryList);
            this.countryList = new ArrayList<dzielnice>();
            this.countryList.addAll(countryList);
        }

        private class ViewHolder {
            TextView code;
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.listadzielnicrow, null);

                holder = new ViewHolder();
                holder.code = (TextView) convertView.findViewById(R.id.code);
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);

                holder.name.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {

                        CheckBox cb = (CheckBox) v ;

                        dzielnice country = (dzielnice) cb.getTag();
                        String textButton = (String) cb.getText();

                     //   Toast.makeText(getApplicationContext(),"Clicked on Checkbox: " + cb.getText() + " is " + cb.isChecked(),
                           //     Toast.LENGTH_SHORT).show();
                        country.setSelected(cb.isChecked());

                        DataHandler  dHr1 = new DataHandler(getContext());
                        if(cb.isChecked()){
                            dHr1.open();
                            dHr1.updateTableDzielnice(textButton ,1);
                            dHr1.close();

                        }else{
                            dHr1.open();
                            dHr1.updateTableDzielnice(textButton ,0);
                            dHr1.close();
                        }

                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            dzielnice country = countryList.get(position);
            holder.code.setText(" (" +  country.getCode() + ")");
            holder.name.setText(country.getName());
            holder.name.setChecked(country.isSelected());
            holder.name.setTag(country);
            return convertView;
        }
    }

    public void wyslijDane(String tokend, ArrayList<String> listaDzielnic, Context con) {
        final  ArrayList<String> tempArray = listaDzielnic;
        final String tempToken = tokend;
        final  Context c = con;

        new AsyncTask<Void, Void, Void>() {
            protected void onPreExecute() {
                progress = new ProgressDialog(c);
                progress.setMessage("Zapisywanie dzielnic");
                progress.show();
            }
            @Override
            protected Void doInBackground(Void... params) {
                try {

                    String temp;

                 for (int i = 0; i < tempArray.size();i++) {
                     temp =tempArray.get(i);

                     new JSONParseInsertDzielnice(temp,tempToken);
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
                }
                Toast.makeText(c,"Dzienice zostały zapisane!!!",Toast.LENGTH_SHORT).show();

            }

        }.execute();


    }



}
