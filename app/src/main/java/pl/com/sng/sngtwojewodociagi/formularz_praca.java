package pl.com.sng.sngtwojewodociagi;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.content.ContentUris;
import android.graphics.Path;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.common.io.Files;


import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import static android.app.Activity.RESULT_OK;

public class formularz_praca extends AppCompatActivity implements AsyncResponse {
    private Calendar mcalendar = Calendar.getInstance();
    private int day,month,year;
    String Stanowsko;
    String d_cv = null;
    EditText imie, nazwisko,data_urodzenia,miejscowosc,kod_pocztowy,ulica,nr_budynku,telefon,mail;
    EditText w_kierunek,w_staz,d_obslugaKompa,d_prawojazdy;
    EditText d_inne,d_oczekiwania_brutto,d_uwagi,plika_zalacz,z_inne ;
     Spinner sp_rodzaj;
     Spinner sp_ang;
     Spinner sp_nie;
     Spinner sp_fra;
     Spinner sp_ros;
     Spinner sp_inn ;
    Button Attachment,Send;
    String attachmentFile;

    Uri URI = null;
    private static final int PICK_FROM_GALLERY = 101;
    Context context;
    @Override
    public void processFinish(String output){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formularz_praca);
        Intent intent = getIntent();
        Stanowsko = intent.getStringExtra("stanowsko");
        TextView naStanowsko = (TextView) findViewById(R.id.aplikaStanowisko);
        naStanowsko.setText(Stanowsko);
        context = this;
        plika_zalacz = (EditText) findViewById(R.id.plik_zal);
        imie = (EditText) findViewById(R.id.do_imie);
        nazwisko = (EditText) findViewById(R.id.do_nazwisko);
        data_urodzenia = (EditText) findViewById(R.id.do_data_urodzenia);
        miejscowosc = (EditText) findViewById(R.id.dk_miejscowosc);
        kod_pocztowy = (EditText) findViewById(R.id.dk_kod_pocztowy);
        ulica = (EditText) findViewById(R.id.dk_ulica);
        nr_budynku = (EditText) findViewById(R.id.dk_nr_budynku);
        telefon = (EditText) findViewById(R.id.dk_telefon);

       // PhoneNumberUtils.formatNumber(telefon.getText().toString());
        //PhoneNumberUtils.fo

        mail = (EditText) findViewById(R.id.dk_emial);
        w_kierunek = (EditText) findViewById(R.id.w_kierunek);
        w_staz = (EditText) findViewById(R.id.w_staz_pracy);
        z_inne = (EditText) findViewById(R.id.z_inne);
        d_obslugaKompa = (EditText) findViewById(R.id.d_obsluga_kompa);
        d_prawojazdy = (EditText) findViewById(R.id.d_prawo_jazdy);
        d_inne = (EditText) findViewById(R.id.d_inne);
        d_oczekiwania_brutto =(EditText) findViewById(R.id.d_oczekiw_brutto);
        d_uwagi = (EditText)findViewById(R.id.d_uwagi);


        Send = (Button)  findViewById(R.id.bt_sendCV);

        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Send();
            }
        });
        data_urodzenia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog();
            }
        });

        Attachment = (Button) findViewById(R.id.bt_attachment);
        Attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFolder();
            }
        });

         sp_rodzaj = (Spinner) findViewById(R.id.w_rodzaj);
         sp_ang = (Spinner) findViewById(R.id.z_angielski);
        sp_nie = (Spinner) findViewById(R.id.z_niemiecki);
         sp_fra = (Spinner) findViewById(R.id.z_francuski);
         sp_ros = (Spinner) findViewById(R.id.z_rosyjski);
         sp_inn = (Spinner) findViewById(R.id.z_inny_poziom);
        // repozytorium
        String[] opcje_rodzaj = new String[]{
                "Rodzaj",
                "Podstawowe",
                "Zasadnicze zawodowe",
                "średnie",
                "wyższe"
        };
        final List<String> rodzajList = new ArrayList<>(Arrays.asList(opcje_rodzaj));
        String[] opcje_jezyk_angielski = new String[]{
                "Angielski",
                "nie znam",
                "słaba",
                "dobra",
                "biegła"
        };
        final List<String> jezykAngList = new ArrayList<>(Arrays.asList(opcje_jezyk_angielski));
        String[] opcje_jezyk_niemiecki = new String[]{
                "Niemiecki",
                "nie znam",
                "słaba",
                "dobra",
                "biegła"
        };
        final List<String> jezykNieList = new ArrayList<>(Arrays.asList(opcje_jezyk_niemiecki));
        String[] opcje_jezyk_francuski = new String[]{
                "Francuski",
                "nie znam",
                "słaba",
                "dobra",
                "biegła"
        };
        final List<String> jezykFraList = new ArrayList<>(Arrays.asList(opcje_jezyk_francuski));
        String[] opcje_jezyk_rosyjski = new String[]{
                "Rosyjski",
                "nie znam",
                "słaba",
                "dobra",
                "biegła"
        };
        final List<String> jezykRosList = new ArrayList<>(Arrays.asList(opcje_jezyk_rosyjski));
        String[] opcje_jezyk_inny = new String[]{
                "Inny - jaki?(poziom znajomości)",
                "nie znam",
                "słaba",
                "dobra",
                "biegła"
        };
        final List<String> jezykInnList = new ArrayList<>(Arrays.asList(opcje_jezyk_inny));

        sp_rodzaj.setAdapter(addAdapters(rodzajList));
        sp_ang.setAdapter(addAdapters(jezykAngList));
        sp_nie.setAdapter(addAdapters(jezykNieList));
        sp_fra.setAdapter(addAdapters(jezykFraList));
        sp_ros.setAdapter(addAdapters(jezykRosList));
        sp_inn.setAdapter(addAdapters(jezykInnList));
        try {
            mcalendar.set(Calendar.DAY_OF_MONTH, 1);
            mcalendar.set(Calendar.YEAR, 2000);
            mcalendar.set(Calendar.MONTH, 1);
            day = mcalendar.get(Calendar.DAY_OF_MONTH);
            year = mcalendar.get(Calendar.YEAR);
            month = mcalendar.get(Calendar.MONTH);
        }
        catch (Exception exception){
            String t = exception.toString();
        }

    }

    public void DateDialog(){
        DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                data_urodzenia.setText(year + "-" + monthOfYear + "-" +dayOfMonth );
            }};
        DatePickerDialog dpDialog=new DatePickerDialog(this, listener, year, month, day);
        dpDialog.show();
    }


    public void Send()
    {
        String mStanowisko_send, mimie, mnazwisko,mdata_urodzenia,mmiejscowosc,mkod_pocztowy,mulica,mnr_budynku,mtelefon,mmail,mw_rodzaj;
        String mw_kierunek,mw_staz,mj_angielski,mj_niemiecki,mj_francuski,mj_rosyjski,mj_inny,md_obslugaKompa,md_prawojazdy, mj_inne;
        String md_inne,md_oczekiwania_brutto,md_uwagi,md_cv;
        mStanowisko_send = Stanowsko;
        mimie = imie.getText().toString();
        mnazwisko = nazwisko.getText().toString();
        mdata_urodzenia = data_urodzenia.getText().toString();
        mmiejscowosc = miejscowosc.getText().toString();
        mkod_pocztowy = kod_pocztowy.getText().toString();
        mulica = ulica.getText().toString();
        mnr_budynku = nr_budynku.getText().toString();
        mtelefon = telefon.getText().toString();
        mmail = mail.getText().toString();
        mw_rodzaj = sp_rodzaj.getSelectedItem().toString();
        mw_kierunek = w_kierunek.getText().toString();
        mw_staz = w_staz.getText().toString();
        mj_angielski = sp_ang.getSelectedItem().toString();
        mj_francuski = sp_fra.getSelectedItem().toString();
        mj_niemiecki = sp_nie.getSelectedItem().toString();
        mj_rosyjski = sp_ros.getSelectedItem().toString();
        mj_inne = z_inne.getText().toString();
        mj_inny=sp_inn.getSelectedItem().toString();
        md_obslugaKompa = d_obslugaKompa.getText().toString();
        md_prawojazdy = d_prawojazdy.getText().toString();
        md_inne = d_inne.getText().toString();
        md_oczekiwania_brutto = d_oczekiwania_brutto.getText().toString();
        md_cv = d_cv;
        md_uwagi = d_uwagi.getText().toString();
        // wysyłanie do jasona

        checkNetConection cc5 = new checkNetConection(this);
        if (cc5.isOnline()) {
            wyslijCv(mStanowisko_send, mimie, mnazwisko, mdata_urodzenia, mmiejscowosc, mkod_pocztowy, mulica, mnr_budynku,mmail,mtelefon,mw_rodzaj,mw_kierunek,mw_staz,mj_angielski,mj_francuski,mj_niemiecki,mj_rosyjski,mj_inne,mj_inny,md_obslugaKompa,md_prawojazdy,md_inne,md_oczekiwania_brutto,md_uwagi,md_cv,plika_zalacz.getText().toString(),this);
        } else {

            Toast.makeText(this, "Praca w trybie offline, złoszenie nie zostało wysłane!!", Toast.LENGTH_SHORT).show();
        }
    }
    public void openFolder()
    {
        //Intent intent = new Intent();
        //intent = getFileChooserIntent();
         Intent target = pl.com.sng.sngtwojewodociagi.FileUtils.createGetContentIntent();
        Intent intent = Intent.createChooser(target,"Wybierz plik");


        // intent.setType("application/pdf");
       // intent.setAction(Intent.ACTION_GET_CONTENT);
        //intent.putExtra("return-data", true);
        startActivityForResult(intent, PICK_FROM_GALLERY);

    }

    private Intent getFileChooserIntent() {
        String[] mimeTypes = {"image/*", "application/pdf","application/vnd.openxmlformats-officedocument.wordprocessingml.document","text/rtf","application/doc"};

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";

            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }

            intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        }

        return intent;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK) {
          try{
           final Uri selectedImage = data.getData();
           final String path = pl.com.sng.sngtwojewodociagi.FileUtils.getPath(this,selectedImage);
             //String attachmentFile1 = selectedImage.toString();
              if(path.contains(".docx") || path.contains(".doc") ||path.contains(".pdf") ||path.contains(".rtf")   )             {


              String[] splitedArray = null;
              //attachmentFile = "/document/Dokument.docx";
                  final File dwldsPath = new File(path);
              byte[] temp = Base64.encode(Files.toByteArray(dwldsPath), Base64.DEFAULT);
                //  byte[] temp1 = Base64.encode(Files.toByteArray(dwldsPath), 0);
              d_cv = new String(temp, "UTF-8"); // for UTF-8 encoding
              splitedArray = path.split("/");
              plika_zalacz.setText(splitedArray[splitedArray.length-1]);
              Toast.makeText(this,"Plik został załączony poprawnie.",Toast.LENGTH_LONG).show();
              } else
              {
                  IOException e = new IOException();
                  throw e;
              }
          }
            catch (Exception ex)
            {
                Toast.makeText(this,"Plik o niewłaściwym formacie.",Toast.LENGTH_LONG).show();
            };
        }
    }



    public  ArrayAdapter<String> addAdapters(List<String> lista) {
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,lista){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
       return spinnerArrayAdapter;
    }

    private void wyslijCv(String mStanowisko_send, String mimie, String mnazwisko, String mdata_urodzenia, String mmiejscowosc, String mkod_pocztowy, String mulica, String mnr_budynku, String mmail,
                          String mtelefon, String mw_rodzaj, String mw_kierunek, String mw_staz, String mj_angielski, String mj_francuski, String mj_niemiecki, String mj_rosyjski, String mj_inne, String mj_inny,
                          String md_obslugaKompa, String md_prawojazdy, String md_inne, String md_oczekiwania_brutto, String md_uwagi, String md_cv, String s,Context con) {
        final String stanow = mStanowisko_send;
        final String imiee = mimie;
        final String nazwis = mnazwisko;
        final String dataur = mdata_urodzenia;
        final String miejsc = mmiejscowosc;
        final String kodpocz = mkod_pocztowy;
        final String uliczka = mulica;
        final String nrbudy = mnr_budynku;
        final String mai = mmail;
        final String tele = mtelefon;
        final String rodz = mw_rodzaj;
        final String kieru = mw_kierunek;
        final String sta = mw_staz;
        final String angel = mj_angielski;
        final String franc = mj_francuski;
        final String niem = mj_niemiecki;
        final String ruski = mj_rosyjski;
        final String jezykinny = mj_inne;
        final String jezykpoziominny = mj_inny;
        final String oblsug = md_obslugaKompa;
        final String prawko = md_prawojazdy;
        final String inneee = md_inne;
        final String oczkeiw = md_oczekiwania_brutto;
        final String uwagaaa = md_uwagi;
        final String pdfff = md_cv;
        final String nazwaplikuu = s;
        final Context c1 = con;
        new AsyncTask<Void, Void, Void>() {
            ProgressDialog progress;
            @Override
            protected void onPreExecute() {
                progress = new ProgressDialog(c1);
                progress.setMessage("Wysyłanie zgłoszenia");
                progress.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    new JSONSendPraca(stanow,imiee,nazwis,dataur,miejsc,kodpocz,uliczka,nrbudy,mai,tele,rodz,kieru,sta,angel,franc,niem,ruski,jezykinny,jezykpoziominny,oblsug,prawko,inneee,oczkeiw,uwagaaa,pdfff,nazwaplikuu);
                } catch (Exception e) {
                    Toast.makeText(c1,"Nie wsyłano zgłoszenia,",Toast.LENGTH_SHORT).show();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (progress.isShowing()){
                    progress.dismiss();
                }
                Toast.makeText(c1,"Wyslano aplikację.",Toast.LENGTH_SHORT).show();
            }

        }.execute();


    }

}
