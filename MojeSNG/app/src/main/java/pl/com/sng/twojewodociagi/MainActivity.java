package pl.com.sng.twojewodociagi;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import pl.com.sng.twojewodociagi.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public  class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ProgressDialog progress;
    CustomizeDialog mCustomizeDialog;
    CountDownTimer mCountDownTimer;
    private static final String url = "https://notification.sng.com.pl/api/GetAlerts/";
    private static final String Id = "Id";
    private static final String Nazwa = "Name";
    private static final String Wartosc = "value";
    private static final String urlWyl = "https://notification.sng.com.pl/api/GetPlanoweWylaczenia/";
    private static final String IdWyl = "ElementId";
    private static final String NazwaWyl = "Name";
    private static final String WartoscWyl = "ValueStr";
    FragmentManager fragmentManager = getSupportFragmentManager();
    ArrayList<HashMap<String ,String >> jsonlist = new ArrayList<HashMap<String ,String >>();
    String token;
    Integer target = 0;
    Cursor cr;
    SharedPreferences prefs = null;

    boolean doubleBackToExitPressedOnce = false;






    @Override
    protected void  onNewIntent (Intent intent)  {


        if (prefs.getBoolean("firstrun", true)) {
            DataHandler data12 = new DataHandler(getBaseContext());
            data12.open();
            data12.insert_start_tab("1","Aktualności");
            data12.insert_start_tab("2","Jakość wody");
            data12.insert_start_tab("3","Awarie");
            data12.insert_start_tab("4","Zgłoszenie awarii");
            data12.updatePowiadoniemia("1");

            data12.close();
            prefs.edit().putBoolean("firstrun", false).commit();
        }
       // startActivity(intent);
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, new start1())
                .commit();
        //  }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!isTaskRoot()) {
            Intent reorderIntent = getIntent();
            reorderIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
         //   startActivity(reorderIntent);
        }

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //     fab.setOnClickListener(new View.OnClickListener() {
        //        @Override
        //          public void onClick(View view) {
        //            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //                    .setAction("Action", null).show();
        //         }
        //     }); SCzerown
        // czernowe kółko na dole

        checkNetConection cc = new checkNetConection(this);

        if(cc.isOnline()){
            new GetClass(this).execute();
            new GetDzielnice(this).execute();

        } else{
            Toast.makeText(this,"Praca w trybie offline!!",Toast.LENGTH_LONG).show();
        }

        prefs = getSharedPreferences("pl.com.sng.twojewodociagi", MODE_PRIVATE);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);  /// wyłaczanie koloru szarego w iconach w navi menu bar
        View headerview = navigationView.getHeaderView(0);
        LinearLayout header = (LinearLayout) headerview.findViewById(R.id.hederView);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, new start1())
                        .commit();
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

            }
        });
        navigationView.setNavigationItemSelectedListener(this);

            onNewIntent(getIntent());

        Intent intent3 = getIntent(); /// pobiera intent
        String cos = intent3.getStringExtra("pozycja");
        String temp = intent3.getAction();

        if(cos != null) {

            switch(cos){

                case "aktualnosci":
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, new aktualnosci())
                            .commit();
                    break;
                case "awarie":
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, new wiadomosci())
                            .commit();
                    break;

                case "kapieliska":

                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, new kapieliska())
                            .commit();
                    break;
                case "planowewylaczenia":
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, new wylaczeniaPlanowe())
                            .commit();
                    break;
            }

        }
        else if(temp!=null){

            switch(temp){

                case "aktualnosci":
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, new aktualnosci())
                            .commit();
                    break;
                case "awarie":
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, new wiadomosci())
                            .commit();
                    break;

                case "kapieliska":

                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, new kapieliska())
                            .commit();
                    break;
                case "planowewylaczenia":
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, new wylaczeniaPlanowe())
                            .commit();
                    break;
            }

        }
        }


    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Naciśnij WSTECZ jeszcze raz, aby zamknąć aplikację", Toast.LENGTH_SHORT).show();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, new start1())
                .commit();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        checkNetConection cc1 = new checkNetConection(this);
        if(cc1.isOnline()) {
            if (id == R.id.action_settings) {
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, new settings())
                        .commit();
                return true;
            }
        }else{
            Toast.makeText(this,"Do zmiany ustawień potrzebne jest połączenie internetowe.",Toast.LENGTH_LONG).show();
            try {
               AlertDialog alertDialog = new AlertDialog.Builder(this).create();

              alertDialog.setTitle("Info");
                alertDialog.setMessage("Do zmiany ustawień aplikacji potrzebne jest połączenie internetowe.");
               alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int which) {

                       Intent intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                    //   intent.setClassName("com.android.phone", "com.android.phone.ACTION_WIRELESS_SETTINGS");
                       startActivity(intent);
                  }
                });

              alertDialog.show();
        }catch (Exception e){
      String cos = e.toString();

            }
        }

        return super.onOptionsItemSelected(item);
    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
     switch(id) {
         case R.id.imageView:
             fragmentManager.beginTransaction()
                     .replace(R.id.content_frame, new start1())
                     .commit();
             break;

         case R.id.zgloszenie_awarii:
             fragmentManager.beginTransaction()
                     .replace(R.id.content_frame, new zgloszenie_awarii())
                     .commit();
             break;

         case R.id.kapieliska:
             fragmentManager.beginTransaction()
                 .replace(R.id.content_frame, new kapieliska())
                 .commit();
         break;


         case R.id.wylaczenia_planowe:
             fragmentManager.beginTransaction()
                     .replace(R.id.content_frame, new wylaczeniaPlanowe())
                     .commit();
             break;
         case R.id.wiadomosci:

             fragmentManager.beginTransaction()
                     .replace(R.id.content_frame, new wiadomosci())
                     .commit();
             break;

           case R.id.about:

             fragmentManager.beginTransaction()
                     .replace(R.id.content_frame, new about())
                     .commit();
            break;
         case R.id.jakosc_wody:
             fragmentManager.beginTransaction()
                     .replace(R.id.content_frame, new JakoscDzielnice())
                     .commit();
                break;
         case R.id.cennik:
             fragmentManager.beginTransaction()
                     .replace(R.id.content_frame, new cennik1())
                     .commit();
                    break;
         case R.id.aktualnosci:
             this.findViewById(R.id.content_main).setBackgroundColor(Color.WHITE);

             fragmentManager.beginTransaction()
                     .replace(R.id.content_frame, new aktualnosci())
                     .commit();
                        break;

         //   else if (id == R.id.ustawienia) {

         //         fragmentManager.beginTransaction()
         //                 .replace(R.id.content_frame, new settings())
         //                   .commit();
         //     }

     }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    public final class GetClass extends AsyncTask<String, Void, Void> {

        private final Context context;

        public GetClass(Context c) {
            this.context = c;
            CustomizeDialog mCustomizeDialog;
        }
        public void onPreExecute() {
         ///   progress = new ProgressDialog(this.context);
        //    progress.setMessage("Pobieranie danych");
         //   progress.show();
        //    mCustomizeDialog = new CustomizeDialog(MainActivity.this);
        //    mCustomizeDialog.setTitle("");
        //    mCustomizeDialog.setMessage("");

        //        mCustomizeDialog.show();
        }

        @Override
        public void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);
        }

        @Override
        public Void doInBackground(String... params) {
            token = FirebaseInstanceId.getInstance().getToken();


            try {
                new JSONParseAddToken(token);   /// dodawanie tokienow
            }catch(Exception e){
                e.printStackTrace();
            }

            DataHandler data = new DataHandler(context);
            data.open();
            data.deleteAllAlerts();


            JSONParse jPartse = new JSONParse();
            JSONArray json = jPartse.getJSONFromUrl(url);
            for(int i = 0; i<json.length(); i++)
                try{
                    JSONObject c = json.getJSONObject(i);
                    String id = c.getString(Id);
                    String nazwa = c.getString(Nazwa);
                    String wartosc = c.getString(Wartosc);
                    data.insertAlert(id,nazwa,wartosc);
                }catch(JSONException e){
                    e.printStackTrace();
                }

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
    public class GetDzielnice extends AsyncTask<String, Void, Boolean> {
        private static final String urlDzienice = "https://notification.sng.com.pl/api/GetDzielnice/";
        private final Context context;

        private static final String DzieniceId = "DzielniceId";
        private static final String Miasto = "Miasto";
        private static final String Dzielnica = "Dzielnica";

        public GetDzielnice(Context c) {
            this.context = c;
        }

        protected void onPreExecute() {
            //    progress = new ProgressDialog(this.context);
            //   progress.setMessage("Pobieranie danych z servera");
            //   progress.show();
        }
        @Override
        protected Boolean doInBackground(String... params) {
            DataHandler data = new DataHandler(context);

            data.open();
            Cursor cr;
            cr = data.returnDzielnice();
            if(cr.getCount() > 0 ) {
                return null;
            }
            data.deleteAllDzielnice();
            JSONParse1 jPartse1 = new JSONParse1();
            JSONArray json1 = jPartse1.getJSONFromUrl(urlDzienice);
            for(int i = 0; i<json1.length(); i++)
                try{
                    JSONObject c = json1.getJSONObject(i);
                    String id = c.getString(DzieniceId);
                    String miasto = c.getString(Miasto);
                    String wartosc = c.getString(Dzielnica);
                    data.insertDzielnice(id,miasto,wartosc);
                }catch(JSONException e){
                    e.printStackTrace();
                }

            data.close();



            return false;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            mCountDownTimer=new CountDownTimer(2000,1000){
                @Override
                public void onTick(long millisUntilFinished) {

                }
                @Override
                public void onFinish() {
            //        if (mCustomizeDialog.isShowing()){
            //            mCustomizeDialog.dismiss();
            //        }
                }
            };
            mCountDownTimer.start();

        }
    }

}

