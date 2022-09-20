package pl.com.sng.twojewodociagi;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import pl.com.sng.twojewodociagi.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class jakosc_wody_mapa extends AppCompatActivity {
    MapView mMapView;
    private GoogleMap googleMap;

    Double latiAve;
    Double longAve;
    ArrayList<String> lat;
    ArrayList<String> lon;
    ArrayList<String> context;
String tempMarker;
    String[] separated;

    Double lati,longi;

    @Override
    public  void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jakosc_wody_mapa);

        Intent intent = getIntent();
        lat = intent.getStringArrayListExtra("latitude");
        lon = intent.getStringArrayListExtra("longitude");
        context = intent.getStringArrayListExtra("context");
        mMapView = (MapView) this.findViewById(R.id.mapViewer);
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
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                googleMap = mMap;
                latiAve = null;
                longAve = null;

                LatLng pozycja;
                for (int i = 0 ; i<context.size();i++ ) {
                    // For showing a move to my location button
                    tempMarker = context.get(i);
                    separated = tempMarker.split("</br>");

                       lati = Double.parseDouble(lat.get(i));
                       longi = Double.parseDouble(lon.get(i));;
                                // For dropping a marker at a point on the Map
                                pozycja = new LatLng(lati, longi);
                                  builder.include(pozycja);
                                googleMap.addMarker(new MarkerOptions()
                                        .position(pozycja)
                                        .title(separated[0])
                                        .snippet(separated[1]));
                                                }
                int padding = 0; // offset from edges of the map in pixels
                pozycja = new LatLng(lati, longi);
                googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter()
                                               {
                                                   @Override
                                                   public View getInfoWindow(Marker marker) {
                                                       return null;
                                                   }
                                                   @Override
                                                   public View getInfoContents(Marker marker) {
                                                       LatLng clickMarkerLatLng = marker.getPosition();
                                                      String title1 =  marker.getTitle();
                                                       String pet = marker.getSnippet();
                                                       View v = getLayoutInflater().inflate(R.layout.info_window_jakoscmapa,null);
                                                       TextView title_info = (TextView)v.findViewById(R.id.title_info_window);
                                                       TextView info_view = (TextView)v.findViewById(R.id.info_window);


                                                       title_info.setText(title1);
                                                       info_view.setText(pet);
                                                       return v;
                                                   }
                                               }
                );


                // For zooming automatically to the location of the marker
              //  CameraPosition cameraPosition = new CameraPosition.Builder().target(pozycja).zoom(10).build();
                if(context.size()==1){
                     CameraPosition cameraPosition = new CameraPosition.Builder().target(pozycja).zoom(12).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }else {
                    LatLngBounds bounds = builder.build();
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),100));
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
