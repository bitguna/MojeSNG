package pl.com.sng.sngtwojewodociagi;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//import com.example.pbronk.mojesng.R;

public class alert_info extends AppCompatActivity {

    String alert_id;
    DataHandler dataHandler1;
    String miasto,ulica, informacja, latitude,longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_info);
        TextView city = (TextView) findViewById(R.id.city);
        TextView street = (TextView) findViewById(R.id.street);
        TextView Kontent = (TextView) findViewById(R.id.Kontent);
        TextView WaterCart = (TextView) findViewById(R.id.WaterCart);
        Button pokaz_mapa = (Button) findViewById(R.id.pokaz_na_mapie);



        Intent intent = getIntent();
        alert_id = intent.getStringExtra("id_alert");
        dataHandler1 = new DataHandler(this);
        dataHandler1.open();
        Cursor cp1 = dataHandler1.returnAlertsFromId(alert_id);
        if (cp1.moveToFirst()) {
            do {
                String tempname = cp1.getString(cp1.getColumnIndex(DataHandler.NAME));

                switch (tempname) {

                    case "WaterCart":
                        WaterCart.setText(cp1.getString(cp1.getColumnIndex(DataHandler.VALUE)));

                        break;
                    case "Title":
                        city.setText(cp1.getString(cp1.getColumnIndex(DataHandler.VALUE)));

                        break;
                    case "Street":
                        street.setText(cp1.getString(cp1.getColumnIndex(DataHandler.VALUE)));
                        ulica = cp1.getString(cp1.getColumnIndex(DataHandler.VALUE));
                        break;
                    case "Content":
                        Kontent.setText(cp1.getString(cp1.getColumnIndex(DataHandler.VALUE)));
                        informacja = cp1.getString(cp1.getColumnIndex(DataHandler.VALUE));
                        break;
                    case "City":
                        miasto = cp1.getString(cp1.getColumnIndex(DataHandler.VALUE));
                        break;
                    case "Latitude":
                        latitude = cp1.getString(cp1.getColumnIndex(DataHandler.VALUE));
                        break;
                    case "Longitude":
                        longitude = cp1.getString(cp1.getColumnIndex(DataHandler.VALUE));
                        break;
                    default:
                        break;
                }
            } while (cp1.moveToNext());
        }

        pokaz_mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent w = new Intent(getBaseContext(), pokaz_na_mapgoogle.class);
                w.putExtra("miasto",miasto);
                w.putExtra("ulica",ulica);
                w.putExtra("informacje",informacja);
                w.putExtra("latitude",latitude);
                w.putExtra("longitude",longitude);
                startActivity(w);
            }
        });
    }
}