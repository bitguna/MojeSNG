package pl.com.sng.sngtwojewodociagi;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import  android.support.v4.app.Fragment ;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import pl.com.sng.sngtwojewodociagi.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by PBronk on 04.11.2016.
 */

public class settings extends Fragment {
    private ListView list ;
    private ArrayAdapter<String> adapter ;
    View myView;
    CheckBox chAktualnosc;
    CheckBox chKapie;
    CheckBox chPlan;
    CheckBox chAwarie;
    private ProgressDialog progress;
    String token;
    @Override
    public void onResume() {

        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {

            @SuppressLint("Range")
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP || keyCode == KeyEvent.KEYCODE_BACK){

                    checkNetConection cc1 = new checkNetConection(getContext());
                    if(cc1.isOnline()) {


                        String czyPowi = null;
                        DataHandler dHr123 = new DataHandler(getContext());
                        DataHandler dthand = new DataHandler(getContext());
                        dthand.open();
                        Cursor c = dthand.returnCzyPowiadomienie();
                        if(c.moveToFirst()){

                            czyPowi = c.getString(c.getColumnIndex(DataHandler.CZY_POWIDADOMNIENIE_COL));
                        }
                        dthand.close();
                        if(czyPowi.equals("0")){
                           wyslijDane(token,"delete",getContext());
                        }else {


                            dHr123.open();
                            Cursor cur = dHr123.returnKtorePowEnable();
                            String tempToSend = "";
                            if (cur.moveToFirst()) {

                                do {

                                    String tempCos = cur.getString(cur.getColumnIndex(DataHandler.KTORE_POW));
                                    if (!tempToSend.equals("")) {
                                        tempToSend = tempToSend + "," + tempCos;
                                    } else {

                                        tempToSend = tempCos;
                                    }

                                } while (cur.moveToNext());
                                if (tempToSend.equals(null) || tempToSend.equals("")) {
                                    tempToSend = "delete";
                                }

                                wyslijDane(token, tempToSend, getContext());

                            } else {
                                if (tempToSend.equals(null) || tempToSend.equals("")) {
                                    tempToSend = "delete";
                                }

                                wyslijDane(token, tempToSend, getContext());
                            }
                        }

                        dHr123.close();

                    }



                    else{

                        Toast.makeText(getContext(),"Ustawienia nie zostały zapisane. Brak podłączenia do internetu.",Toast.LENGTH_LONG).show();

                    }



                    FragmentManager fm = getFragmentManager();
                    fm.beginTransaction()
                            .replace(R.id.content_frame, new start1())
                            .commit();
                    return true;

                }

             return true;
            }
        });
    }



    @SuppressLint("Range")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       myView = inflater.inflate(R.layout.settings, container,false);
        ListView lista1;
        final Switch czyPowiadomienia = (Switch) myView.findViewById(R.id.switch1);
        chAktualnosc = (CheckBox) myView.findViewById(R.id.checkBox1);
        chKapie = (CheckBox)myView.findViewById(R.id.checkBox2);
        chPlan = (CheckBox)myView.findViewById(R.id.checkBox3);
        chAwarie= (CheckBox)myView.findViewById(R.id.checkBox4);
        token = FirebaseInstanceId.getInstance().getToken();
        String czyPowi = null;

        DataHandler  dHr12 = new DataHandler(getContext());

        dHr12.open();
        Cursor cursor = dHr12.returnKtorePow();
        if(cursor.getCount() == 0){
            dHr12.insertKtorePowiadomienia("aktualnosci","1");
            dHr12.insertKtorePowiadomienia("kapieliska","1");
            dHr12.insertKtorePowiadomienia("planowewylaczenia","1");
            dHr12.insertKtorePowiadomienia("awarie","1");
            ;
        }
        if(cursor.moveToFirst()){
            do{
           @SuppressLint("Range") String tempCos =  cursor.getString(cursor.getColumnIndex(DataHandler.KTORE_POW));
           @SuppressLint("Range") String tempCzyZaznaczone =    cursor.getString(cursor.getColumnIndex(DataHandler.KTORE_WYSL));

                switch (tempCos){
                 case "aktualnosci":
                     if(tempCzyZaznaczone.equals("1")){
                     chAktualnosc.setChecked(true);
                     }else{
                         chAktualnosc.setChecked(false);
                     }

                     break;
                    case "kapieliska":
                        if(tempCzyZaznaczone.equals("1")){
                            chKapie.setChecked(true);
                        }else{
                            chKapie.setChecked(false);
                        }

                        break;
                    case "planowewylaczenia":
                        if(tempCzyZaznaczone.equals("1")){
                            chPlan.setChecked(true);
                        }else{
                            chPlan.setChecked(false);
                        }

                        break;
                    case "awarie":
                        if(tempCzyZaznaczone.equals("1")){
                            chAwarie.setChecked(true);
                        }else{
                            chAwarie.setChecked(false);
                        }
                        break;
                }
            }while(cursor.moveToNext());
            dHr12.close();
        }

        dHr12.close();
        chAktualnosc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                               @Override
                                               public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                                   DataHandler  dHr1 = new DataHandler(getContext());
                                                   String tempAKTU = buttonView.getText().toString();
                                                   dHr1.open();
                                                   if(isChecked) {
                                                       dHr1.updateKtorePoowiadomienia("aktualnosci", "1");
                                                   }else {
                                                       dHr1.updateKtorePoowiadomienia("aktualnosci", "0");
                                                   }
                                                   dHr1.close();

                                               }
                                           }
        );
        chKapie.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                    @Override
                                                    public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                                        DataHandler  dHr1 = new DataHandler(getContext());

                                                        dHr1.open();
                                                        if(isChecked) {
                                                            dHr1.updateKtorePoowiadomienia("kapieliska" , "1");
                                                        }else {
                                                            dHr1.updateKtorePoowiadomienia("kapieliska" , "0");
                                                        }
                                                        dHr1.close();
                                                    }
                                                }
        );
        chPlan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                               @Override
                                               public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {

                                                   DataHandler  dHr1 = new DataHandler(getContext());

                                                   dHr1.open();
                                                   if(isChecked) {
                                                       dHr1.updateKtorePoowiadomienia("planowewylaczenia", "1");
                                                   }else {
                                                       dHr1.updateKtorePoowiadomienia("planowewylaczenia", "0");
                                                   }
                                                   dHr1.close();
                                               }
                                           }
        );
        chAwarie.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                              @Override
                                              public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                                  DataHandler  dHr1 = new DataHandler(getContext());

                                                  dHr1.open();
                                                  if(isChecked) {
                                                      dHr1.updateKtorePoowiadomienia("awarie", "1");
                                                  }else {
                                                      dHr1.updateKtorePoowiadomienia("awarie", "0");
                                                  }

                                                  dHr1.close();




                                              }
                                          }
        );



        DataHandler dth =  new DataHandler(getContext());
        dth.open();

        Cursor cp =  dth.returnCzyPowiadomienie();


        Integer cos = cp.getCount();
        if(cp.moveToFirst()) {
                czyPowi = cp.getString(cp.getColumnIndex(DataHandler.CZY_POWIDADOMNIENIE_COL));
        }
        dth.close();
        if(czyPowi.equals("0")) {
            czyPowiadomienia.setChecked(false);
            chAktualnosc.setEnabled(false);
            chPlan.setEnabled(false);
            chAwarie.setEnabled(false);
            chKapie.setEnabled(false);
        }else{
            czyPowiadomienia.setChecked(true);
               chAktualnosc.setEnabled(true);
            chPlan.setEnabled(true);
            chAwarie.setEnabled(true);
            chKapie.setEnabled(true);
        }





        lista1 = (ListView) myView.findViewById(R.id.lista1);
        String cars[] = {"Wybierz dzielnice powiadomień"};


        czyPowiadomienia.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                DataHandler dth1 =  new DataHandler(getContext());
                dth1.open();
                if(isChecked) {
                    dth1.updatePowiadoniemia("1");
                    chAktualnosc.setEnabled(true);
                    chPlan.setEnabled(true);
                    chAwarie.setEnabled(true);
                    chKapie.setEnabled(true);
                }else{
                    dth1.updatePowiadoniemia("0");

                    chAktualnosc.setEnabled(false);
                    chPlan.setEnabled(false);
                    chAwarie.setEnabled(false);
                    chKapie.setEnabled(false);
                }
                dth1.close();
            }
        });


       final ArrayList<String> dzielniA = new ArrayList<String>();
        dzielniA.addAll( Arrays.asList(cars) );
        adapter = new ArrayAdapter<String>(getContext(), R.layout.setting_list,R.id.textRowSetting, dzielniA);
        lista1.setAdapter(adapter);
        lista1.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String kategoria = dzielniA.get(position);


                switch (kategoria) {

                    case "Wybierz dzielnice powiadomień":
                        checkNetConection cc = new checkNetConection(getContext());
                        if(cc.isOnline()){

                            Intent w = new Intent(getContext(), wybierzDzielnice.class);
                            startActivity(w);
                            break;

                        } else{
                            Toast.makeText(getContext()," Opcja nie dostępna w trybie offline!!",Toast.LENGTH_SHORT).show();
                        }

                    case "Wybierz kategorie":
                        break;

                }
            }
        });


        return myView;
    }








    public void wyslijDane(String tokend, String wybraneUstawienia, Context con) {

        final String tempToken = tokend;
        final String TempUstawienia = wybraneUstawienia;
        final  Context c = con;

        new AsyncTask<Void, Void, Void>() {
            protected void onPreExecute() {

            }
            @Override
            protected Void doInBackground(Void... params) {
                try {

                        new JSONInsertTypeNotypi(TempUstawienia,tempToken);


                    return null;

                } catch (Exception e) {
                    // log error
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {

                Toast.makeText(c,"Ustawienia zostały zapisane!!!",Toast.LENGTH_LONG).show();

            }

        }.execute();


    }



}