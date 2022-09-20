package pl.com.sng.sngtwojewodociagi;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Address;
import android.location.Geocoder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import pl.com.sng.sngtwojewodociagi.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class pokaz_na_mapgoogle  extends AppCompatActivity {
    MapView mMapView;
    private GoogleMap googleMap;
    String miasto,ulica,informacja,latiAwaria,longAwaria;
    Double lati,longi;
    LatLng pozycja;
    @Override
    public  void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokaz_na_mapgoogle);



        Intent intent = getIntent();
        miasto = intent.getStringExtra("miasto");
        ulica = intent.getStringExtra("ulica");
        informacja = intent.getStringExtra("informacje");
        latiAwaria = intent.getStringExtra("latitude");
        longAwaria = intent.getStringExtra("longitude");

        mMapView = (MapView) this.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(this.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                String[] ulice_podzielone;

                try {

                    if(latiAwaria == null  || longAwaria == null ) {


                        ulice_podzielone = ulica.split(",");
                        for (int i = 0; i < ulice_podzielone.length; i++) {
                            Geocoder gc = new Geocoder(getApplicationContext());
                            String location = miasto + " " + ulice_podzielone[i];
                            final String aktualnaUlica = location;
                            List<Address> list = gc.getFromLocationName(location, 1);
                            if (list.isEmpty()) {
                                continue;
                            }
                            Address address = list.get(0);

                            final Double lat = address.getLatitude();
                            final Double lng = address.getLongitude();
                            // For showing a move to my location button
                            if (lat == null || lng == null) {
                                continue;
                            }
                            lati = lat;
                            longi = lng;
                            // For dropping a marker at a point on the Map
                            if (googleMap != null) {
                                googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                                                                   @Override
                                                                   public View getInfoWindow(Marker marker) {
                                                                       return null;
                                                                   }

                                                                   @Override
                                                                   public View getInfoContents(Marker marker) {
                                                                       String title = marker.getTitle();
                                                                       String snippet = marker.getSnippet();
                                                                       View v = getLayoutInflater().inflate(R.layout.info_window_jakoscmapa, null);
                                                                       TextView title_info = (TextView) v.findViewById(R.id.title_info_window);
                                                                       TextView info_view = (TextView) v.findViewById(R.id.info_window);
                                                                       lati = lat;
                                                                       longi = lng;

                                                                       title_info.setText(title);
                                                                       info_view.setText(snippet);
                                                                       return v;
                                                                   }
                                                               }
                                );
                            }
                            pozycja = new LatLng(lati, longi);

                            googleMap.addMarker(new MarkerOptions().position(pozycja).title(location).snippet(informacja));
                        }

                    }else{


                        if (googleMap != null) {
                            googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                                                               @Override
                                                               public View getInfoWindow(Marker marker) {
                                                                   return null;
                                                               }

                                                               @Override
                                                               public View getInfoContents(Marker marker) {
                                                                   String title = marker.getTitle();
                                                                   String snippet = marker.getSnippet();
                                                                   View v = getLayoutInflater().inflate(R.layout.info_window_jakoscmapa, null);
                                                                   TextView title_info = (TextView) v.findViewById(R.id.title_info_window);
                                                                   TextView info_view = (TextView) v.findViewById(R.id.info_window);
                                                                   lati = Double.parseDouble(latiAwaria);
                                                                   longi = Double.parseDouble(longAwaria);

                                                                   title_info.setText(title);
                                                                   info_view.setText(snippet);
                                                                   return v;
                                                               }
                                                           }
                            );
                        }

                        pozycja = new LatLng(Double.parseDouble(latiAwaria), Double.parseDouble(longAwaria));
                        googleMap.addMarker(new MarkerOptions().position(pozycja).title(miasto + " - " +ulica).snippet(informacja));

                    }
                    // For zooming automatically to the location of the marker
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(pozycja).zoom(15).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.google_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.normal) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMapView.requestFocusFromTouch();
            return true;
        }

        if (id == R.id.hybrydowa) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            mMapView.requestFocusFromTouch();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    private void initMap(){


    }
public boolean googleServicesAvailable(){
    GoogleApiAvailability api = GoogleApiAvailability.getInstance();
    int isAvailable = api.isGooglePlayServicesAvailable(this);
    if(isAvailable == ConnectionResult.SUCCESS){
        return true;
    }else if (api.isUserResolvableError(isAvailable)){
      Dialog dialog = api.getErrorDialog(this,isAvailable,0);
       dialog.show();
    }else{
        Toast.makeText(this,"Nie mozna połaczyć się z mapą google",Toast.LENGTH_SHORT).show();
    }
return false;
}

}
