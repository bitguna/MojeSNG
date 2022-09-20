package pl.com.sng.twojewodociagi;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pl.com.sng.twojewodociagi.R;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

/**
 * Created by PBronk on 14.12.2016.
 */

public class zgloszenie_awarii extends Fragment implements AsyncResponse {
    AutoCompleteTextView wpisz_miasto, wpisz_ulice,wpisz_nrbud,rodzaj_awari;
    EditText imie,telefon,email,opis_awari;
    Button wyslij_awarie;
    View myView;
    ArrayList<String>miasta,miastaWartosc,ulice,uliceWartosc,nrbud,nrbudWartosc,rodzajAwarii;
    String jakieCombo;
    Button pokaz_mapa, dodaj_zdjecie;
    GetUlice asynchUlice;
    GetNrBudynkow asynhNrBud;
    CameraPhoto cameraPhoto;
    String fotoPath ;
    Bitmap bitMap ;
    String sBitMap ;
    final int CAMERA_REQUEST = 13323;
    final int PUNKT_REQUEST = 13324;
    String sendLati, sendLong;
    private Bundle[] myDataTransfer = { null };
    GetMiasta asyhMiasta;
    String urlGetCityName = "https://notification.sng.com.pl/api/GetLocalizationCity/";
    String urlGetStreetName = "https://notification.sng.com.pl/api/GetLocalizationStreet";
    String urlGetNrBud = "https://notification.sng.com.pl/api/GetLocalizationPoint";
    String wysData,wysMiasto,wysUlica,wysNrBud,wysRodzAw,wysOpis,wysImieNaz,wysNrTel,wysMail,title,message;
    @Override
    public void processFinish(String output){
        if(jakieCombo.equals("miasta")) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, miasta);
            wpisz_miasto.setAdapter(adapter);
        }else if(jakieCombo.equals("ulice")){
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, ulice);
            wpisz_ulice.setAdapter(adapter);

        }else if(jakieCombo.equals("nrBud")){
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, nrbud);
            wpisz_nrbud.setAdapter(adapter);
         //   ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, rodzajAwarii);
         //   rodzaj_awari.setAdapter(adapter1);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
     if(resultCode == RESULT_OK){
         if(requestCode == CAMERA_REQUEST){
             fotoPath =    cameraPhoto.getPhotoPath();
             try {
                 bitMap = ImageLoader.init().from(fotoPath).requestSize(800,600).getBitmap();
                 sBitMap = encodeToBase64(bitMap,Bitmap.CompressFormat.JPEG, 100);
             } catch (FileNotFoundException e) {
                 e.printStackTrace();
             }

     if(sBitMap != null) {
         dodaj_zdjecie.setText("Dodano");
         dodaj_zdjecie.setBackgroundColor(Color.GREEN);
     }
         } else if(requestCode == PUNKT_REQUEST){

            String punktMiasto = data.getStringExtra("miasto");
             String punktUlica = data.getStringExtra("ulica");
             String punktNrDomu = data.getStringExtra("nr_domu");
             Double punktLat = data.getDoubleExtra("lat",0);
             Double punktLon = data.getDoubleExtra("lon",0);

             sendLati = punktLat.toString();
             sendLong = punktLon.toString();
             wpisz_miasto.setText(punktMiasto);
             wpisz_ulice.setText(punktUlica);
             wpisz_nrbud.setText(punktNrDomu);
             if(punktLat != null && punktLat != 0 ) {
                 pokaz_mapa.setText("Zaznaczono");
                 pokaz_mapa.setBackgroundColor(Color.GREEN);
             }
         }
     }
    }
    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.zgloszenie_awari, container, false);
        wpisz_miasto = (AutoCompleteTextView)myView.findViewById(R.id.miasto_awaria);
        wpisz_ulice =(AutoCompleteTextView)myView.findViewById(R.id.ulica_awaria);
        wpisz_nrbud = (AutoCompleteTextView)myView.findViewById(R.id.nrbud_awaria);
        rodzaj_awari = (AutoCompleteTextView)myView.findViewById(R.id.rodzaj_awariii);
        opis_awari = (EditText)myView.findViewById(R.id.opis_awari);

        wyslij_awarie = (Button)myView.findViewById(R.id.wyslij_awarie);
        asyhMiasta = new GetMiasta(getContext());
        asyhMiasta.delegate = this;
        jakieCombo = "miasta";
        pokaz_mapa = (Button)myView.findViewById(R.id.mapapokaz);
        cameraPhoto = new CameraPhoto(getContext());

        dodaj_zdjecie = (Button) myView.findViewById(R.id.zdjecie);
        checkNetConection cc1 = new checkNetConection(getContext());
        if(cc1.isOnline()) {
            asyhMiasta.execute();
        }else{

            Toast.makeText(getContext(),"Praca w trybie offline!!",Toast.LENGTH_SHORT).show();
        }
        rodzajAwarii = new ArrayList<String>();
        rodzajAwarii.add("brak pokrywy/włazu");
        rodzajAwarii.add("brak wody");
        rodzajAwarii.add("brudna woda");
        rodzajAwarii.add("inna");
        rodzajAwarii.add("niskie ciśnienie wody");
        rodzajAwarii.add("wyciek w gnieździe wodomierzowym");
        rodzajAwarii.add("wyciek z hydrantu");
        rodzajAwarii.add("wylew ścieków");
        rodzajAwarii.add("wylew wody");
        rodzajAwarii.add("zapadnięcie wokół pokrywy/włazu");
        rodzajAwarii.add("zator");

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, rodzajAwarii);
        rodzaj_awari.setAdapter(adapter1);

        dodaj_zdjecie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


              //  Intent in = new Intent(zgloszenie_awarii.this, SubActivity.class);
                try {
                    startActivityForResult(cameraPhoto.takePhotoIntent(),CAMERA_REQUEST);
                    cameraPhoto.addToGallery();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });

        opis_awari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0 && s.subSequence(s.length()-1, s.length()).toString().equalsIgnoreCase("\n")) {
                  //  imie.setFocusableInTouchMode(true);
                 //   imie.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>0 && s.subSequence(s.length()-1, s.length()).toString().equalsIgnoreCase("\n")) {
                 String temp = s.toString();
                 String temp1 = temp.trim();
                    opis_awari.setText(temp1);
                }

            }
        });




            wpisz_miasto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {
                String nazwaIdpoint = (String) arg0.getItemAtPosition(arg2);

                Integer IdPointId = 0;
                for (int i = 0; i < miasta.size(); i++) {
                    if (miasta.get(i).equals(nazwaIdpoint)) {
                        IdPointId = i;
                        break;
                    }
                }
                String idPoint = miastaWartosc.get(IdPointId);

                asynchUlice = new zgloszenie_awarii.GetUlice(getContext(),urlGetStreetName,Integer.parseInt(idPoint),asyhMiasta.delegate);
                jakieCombo = "ulice";
                checkNetConection cc2 = new checkNetConection(getContext());

                if(cc2.isOnline()) {
                    asynchUlice.execute();

                }else{

                    Toast.makeText(getContext(),"Praca w trybie offline!!",Toast.LENGTH_SHORT).show();
                }

                wpisz_miasto.setError(null);
                wpisz_ulice.setFocusableInTouchMode(true);
                wpisz_ulice.requestFocus();

            }
        });
        wpisz_ulice.setOnItemClickListener(new AdapterView.OnItemClickListener() {



            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {
                //      String msupplier1=spinnerStreet.getSelectedItem().toString();


                String nazwaIdpoint2 = (String) arg0.getItemAtPosition(arg2);
                Integer IdPointId2 = 0;

                for (int i = 0; i < ulice.size(); i++) {
                    if (ulice.get(i).equals(nazwaIdpoint2)) {
                        IdPointId2 = i;
                        break;
                    }
                }
                String idPoint3 = uliceWartosc.get(IdPointId2);

                asynhNrBud = new zgloszenie_awarii.GetNrBudynkow(getContext(),urlGetNrBud,Integer.parseInt(idPoint3),asyhMiasta.delegate);
                jakieCombo = "nrBud";
                checkNetConection cc3 = new checkNetConection(getContext());

                if(cc3.isOnline()) {
                    asynhNrBud.execute();

                }else{

                    Toast.makeText(getContext(),"Praca w trybie offline!!",Toast.LENGTH_SHORT).show();
                }
                wpisz_ulice.setError(null);
                wpisz_nrbud.setFocusableInTouchMode(true);
                wpisz_nrbud.requestFocus();

            }
        });


        wpisz_nrbud.setOnItemClickListener(new AdapterView.OnItemClickListener() {



            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {
                //      String msupplier1=spinnerStreet.getSelectedItem().toString();

                rodzaj_awari.setFocusableInTouchMode(true);
                rodzaj_awari.requestFocus();


            }
        });

        rodzaj_awari.setOnItemClickListener(new AdapterView.OnItemClickListener() {



            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {
                //      String msupplier1=spinnerStreet.getSelectedItem().toString();

                opis_awari.setFocusableInTouchMode(true);
                opis_awari.requestFocus();


            }
        });


                wpisz_miasto.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // WORKS IF USED ON TOUCH

                wpisz_miasto.showDropDown();
                return false;
            }
        });
        wpisz_ulice.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // WORKS IF USED ON TOUCH

                wpisz_ulice.showDropDown();
                return false;
            }
        });
        wpisz_nrbud.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // WORKS IF USED ON TOUCH

                wpisz_nrbud.showDropDown();
                return false;
            }
        });
        rodzaj_awari.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // WORKS IF USED ON TOUCH

                rodzaj_awari.showDropDown();
                rodzaj_awari.setError(null);
                return false;
            }
        });

        pokaz_mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), punkt_awari.class);

                if(!wpisz_miasto.getText().toString().isEmpty() || !wpisz_miasto.getText().equals("")){
                    intent.putExtra("miasto",wpisz_miasto.getText().toString());
                    if(!wpisz_ulice.getText().toString().isEmpty() || !wpisz_ulice.getText().equals("")) {
                        intent.putExtra("ulica",wpisz_ulice.getText().toString());
                        if(!wpisz_nrbud.toString().isEmpty() || !wpisz_nrbud.getText().equals("")) {
                            intent.putExtra("nr_domu",wpisz_nrbud.getText().toString());
                        }
                    }
                }



                startActivityForResult(intent , PUNKT_REQUEST);
            }

        });
        wyslij_awarie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                   Boolean miasto = true;
                   Boolean ulica = true;
                   Boolean rodzaj_awarii = true;


                wysOpis = opis_awari.getText().toString();
                wysRodzAw = rodzaj_awari.getText().toString();
                wysMiasto = wpisz_miasto.getText().toString();
                wysUlica= wpisz_ulice.getText().toString();
                wysNrBud = wpisz_nrbud.getText().toString();


                if(wysMiasto.trim().equals("")){
                    wpisz_miasto.setError("Miasto jest wymagane");
                    miasto = false;
                }
                if(wysUlica.trim().equals("")){
                    wpisz_ulice.setError("Ulica jest wymagana");
                    ulica = false;
                }
                if(wysRodzAw.trim().equals("")){
                    rodzaj_awari.setError("Rodzaj awarii jest wymagany");
                    rodzaj_awarii = false;
                }


            if(miasto.equals(false) || ulica.equals(false)|| rodzaj_awarii.equals(false) ) {

                Toast.makeText(getActivity(), "Uzupełnij wymagane dane.", Toast.LENGTH_SHORT).show();
            }
                else{
                try {
                    AlertDialog.Builder mBuldier = new AlertDialog.Builder(getContext());
                     View myView = inflater.inflate(R.layout.dialog_data_user, container, false);
                     final EditText imie_nazwisko = (EditText)myView.findViewById(R.id.zgloszenie_imie);
                     final EditText telefon= (EditText)myView.findViewById(R.id.zgloszenie_telefon);
                     final EditText maill = (EditText)myView.findViewById(R.id.zgłoszenie_email);
                    Button dialogButtonWyslij = (Button)myView.findViewById(R.id.zgloszenie_wyslij);
                    mBuldier.setView(myView);
                   final AlertDialog dialog = mBuldier.create();

                    dialogButtonWyslij.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            checkNetConection cc5 = new checkNetConection(getContext());

                            wysImieNaz = imie_nazwisko.getText().toString();
                            wysNrTel = telefon.getText().toString();
                            wysMail = maill.getText().toString();

                            if (cc5.isOnline()) {
                                wyslijAwarie(wysMiasto, wysUlica, wysNrBud, wysRodzAw, wysOpis, wysImieNaz, wysNrTel, wysMail,sBitMap);
                                dialog.dismiss();
                            } else {

                                Toast.makeText(getContext(), "Praca w trybie offline, złoszenie nie zostało wysłane!!", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });




                    dialog.show();



                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), "Praca w trybie offline!!.", Toast.LENGTH_SHORT).show();
                }

            }

    }
});
        return myView;
        }


private class GetMiasta extends AsyncTask<String, Void, String> {
    private ProgressDialog progress;
    private Context context;
    public AsyncResponse delegate = null;
    public GetMiasta(Context c) {
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
        miasta = new ArrayList<String>();
        miastaWartosc = new ArrayList<String>();
        for(int i = 0; i<json2.length(); i++)
            try{
                JSONObject c = json2.getJSONObject(i);
                String idwyl = c.getString("Id");
                String nazwawyl = c.getString("Name");
                miasta.add(i,nazwawyl);
                miastaWartosc.add(i,idwyl);


            }catch(JSONException e){
                e.printStackTrace();
            }
        return null;

        }
    }
    private class GetUlice extends AsyncTask<String, Void, String> {
        private ProgressDialog progress;
        private Context context;
        private  String url1;
        private Integer header1;
        public AsyncResponse delegate1 = null;
        public GetUlice(Context c, String url, Integer header, AsyncResponse delegate) {
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
            ulice = new ArrayList<String>();
            uliceWartosc = new ArrayList<String>();
            for(int i = 0; i<json2.length(); i++)
                try{
                    JSONObject c = json2.getJSONObject(i);
                    String idUlicy = c.getString("Id");
                    String nazwisko = c.getString("Name");
                    String imie = c.getString("Name1");
                    if(imie != null && !imie.isEmpty() && !imie.equals("null")){

                        ulice.add(i,nazwisko+ " " + imie);
                    }else {
                        ulice.add(i,nazwisko);
                    }
                    uliceWartosc.add(i,idUlicy);
                }catch(JSONException e){
                    e.printStackTrace();
                }
            return null;

        }
    }
    private class GetNrBudynkow extends AsyncTask<String, Void, String> {
        private ProgressDialog progress;
        private Context context;
        private  String url1;
        private Integer header2;
        public AsyncResponse delegate2 = null;
        public GetNrBudynkow(Context c, String url, Integer header, AsyncResponse delegate) {
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
            nrbud = new ArrayList<String>();
            nrbudWartosc = new ArrayList<String>();
            for(int i = 0; i<json3.length(); i++)
                try{
                    JSONObject c = json3.getJSONObject(i);
                    String idNrBud = c.getString("Id");
                    String nrBud = c.getString("Name");
                    nrbud.add(i,nrBud);
                    nrbudWartosc.add(i,idNrBud);
                }catch(JSONException e){
                    e.printStackTrace();
                }
            return null;

        }
    }

    private void wyslijAwarie(String cit, String stre, String sstrenr, String fail,String desc,String nam,String pho,String mai, String bitm) {
        final String city = cit;
        final String street = stre;
        final String sstrenrumer = sstrenr;
        final String failler = fail;
        final String descrip = desc;
        final String name = nam;
        final String phone = pho;
        final String mail = mai;
        final String bitMapka = bitm;



        new AsyncTask<Void, Void, Void>() {

            ProgressDialog progress;
            @Override
            protected void onPreExecute() {

                progress = new ProgressDialog(getContext());
                progress.setMessage("Wysyłanie zgłoszenia");
                progress.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    new JSONParseSendEmail(city, street, sstrenrumer,failler,descrip,name,phone,mail, bitMapka, sendLati,sendLong);
                } catch (Exception e) {
                    Toast.makeText(getContext(),"Nie wsyłano zgłoszenia,",Toast.LENGTH_SHORT).show();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (progress.isShowing()){
                    progress.dismiss();
                }
              Toast.makeText(getContext(),"Zgłoszono awarie",Toast.LENGTH_SHORT).show();
            }

        }.execute();


    }

}
