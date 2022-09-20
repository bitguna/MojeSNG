package pl.com.sng.sngtwojewodociagi;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import pl.com.sng.sngtwojewodociagi.R;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
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

/**
 * Created by PBronk on 21.02.2017.
 */

public class punkt_awari extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    MapView mMapViewpunkt;
    Context con;
    double latMarker;
    String miasto1, ulica1, nr_domu1, in_miasto, in_ulica, in_nrdomu;
    Double lat1, lon1;
    double lngMarker;
    Geocoder gc, gc1;
    GoogleMap googleMap;
    GoogleApiClient googleClient;
    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.punkt_awari_new);
        //  if(getResources().getBoolean(R.bool.portrait_only)){
        //      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // }


        mMapViewpunkt = (MapView) this.findViewById(R.id.mapFragment);
        mMapViewpunkt.onCreate(savedInstanceState);
        mMapViewpunkt.onResume();
        con = this;
        int height = 100;
        int width = 100;
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.zielonymarker);
        Bitmap b = bitmapdraw.getBitmap();
        final Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


        et = (EditText) findViewById(R.id.searchmap);
        Intent intent = getIntent();
        in_miasto = intent.getStringExtra("miasto");
        in_ulica = intent.getStringExtra("ulica");
        in_nrdomu = intent.getStringExtra("nr_domu");
        if (!in_miasto.toString().isEmpty() || !in_miasto.equals("")) {
            et.setText(in_miasto);
            if (!in_ulica.toString().isEmpty() || !in_ulica.equals("")) {
                et.setText(in_miasto + ", " + in_ulica);
                if (!in_nrdomu.toString().isEmpty() || !in_nrdomu.equals("")) {
                    et.setText(in_miasto + ", " + in_ulica + " " + in_nrdomu);
                }
            }


        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_marker));
        fab.setSize(FloatingActionButton.SIZE_NORMAL);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LatLng address1 = googleMap.getCameraPosition().target;
                latMarker = address1.latitude;
                lngMarker = address1.longitude;
                lat1 = latMarker;
                lon1 = lngMarker;

                googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {


                    @Override
                    public View getInfoWindow(Marker marker) {
                        return null;
                    }


                    @Override
                    public View getInfoContents(Marker marker) {
                        View v = getLayoutInflater().inflate(R.layout.markerawarie, null);
                        TextView tvLocal = (TextView) v.findViewById(R.id.tvlocal);
                        //TextView tvLat = (TextView)v.findViewById(R.id.tvlat);
                        //TextView tvLong = (TextView)v.findViewById(R.id.tvlong);
                        TextView tvOpis = (TextView) v.findViewById(R.id.tvopis);
                        LatLng ll = marker.getPosition();
                        tvLocal.setText(marker.getTitle());
                        //    tvLat.setText("Latitude: "+ll.latitude);
                        //   tvLong.setText("Longitude: "+ll.longitude);
                        tvOpis.setText(marker.getSnippet());
                        return v;
                    }
                });

                List<Address> lista = null;
                gc1 = new Geocoder(punkt_awari.this);
                try {
                    lista = gc1.getFromLocation(latMarker, lngMarker, 1);


                } catch (IOException e) {
                    e.printStackTrace();
                }
                Address add = lista.get(0);

                miasto1 = add.getLocality();
                ulica1 = add.getThoroughfare();
                nr_domu1 = add.getSubThoroughfare();


                MarkerOptions options = new MarkerOptions()
                        .title(add.getLocality() + " " + add.getPostalCode())
                        .snippet(add.getThoroughfare() + " " + add.getSubThoroughfare())
                        .draggable(true)
                        .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                        .position(new LatLng(latMarker, lngMarker));

                Marker marker = googleMap.addMarker(options);

                marker.showInfoWindow();
                googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

                    @Override
                    public void onMapLongClick(LatLng arg0) {
                        // TODO Auto-generated method stub
                        googleMap.clear();

                        List<Address> lista1 = null;
                        lat1 = arg0.latitude;
                        lon1 = arg0.longitude;
                        Geocoder gcTemp = new Geocoder(punkt_awari.this);
                        try {
                            lista1 = gcTemp.getFromLocation(lat1, lon1, 1);


                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Address add1 = lista1.get(0);

                        miasto1 = add1.getLocality();
                        ulica1 = add1.getThoroughfare();
                        nr_domu1 = add1.getSubThoroughfare();
                        MarkerOptions options1;
                        if (add1.getPostalCode() == null || add1.getPostalCode() == "") {

                            options1 = new MarkerOptions()
                                    .title(add1.getLocality())
                                    .snippet(add1.getThoroughfare() + " " + add1.getSubThoroughfare())
                                    .draggable(true)
                                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                    .position(new LatLng(arg0.latitude, arg0.longitude));
                        } else {
                            options1 = new MarkerOptions()
                                    .title(add1.getLocality() + " " + add1.getPostalCode())
                                    .snippet(add1.getThoroughfare() + " " + add1.getSubThoroughfare())
                                    .draggable(true)
                                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                    .position(new LatLng(arg0.latitude, arg0.longitude));
                        }

                        Marker marker1 = googleMap.addMarker(options1);


                        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {


                            @Override
                            public View getInfoWindow(Marker marker) {


                                return null;
                            }


                            @Override
                            public View getInfoContents(Marker marker) {
                                View v = getLayoutInflater().inflate(R.layout.markerawarie, null);
                                TextView tvLocal = (TextView) v.findViewById(R.id.tvlocal);
                                //TextView tvLat = (TextView)v.findViewById(R.id.tvlat);
                                //TextView tvLong = (TextView)v.findViewById(R.id.tvlong);
                                TextView tvOpis = (TextView) v.findViewById(R.id.tvopis);
                                LatLng ll = marker.getPosition();
                                tvLocal.setText(marker.getTitle());
                                //    tvLat.setText("Latitude: "+ll.latitude);
                                //   tvLong.setText("Longitude: "+ll.longitude);
                                tvOpis.setText(marker.getSnippet());

                                return v;
                            }
                        });
                        marker1.showInfoWindow(); //  m.showInfoWindow();


                        // Log.d("cos","cos");
                    }
                });


                googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                    @Override
                    public void onMarkerDragStart(Marker marker) {

                    }

                    @Override
                    public void onMarkerDrag(Marker marker) {

                    }

                    @Override
                    public void onMarkerDragEnd(Marker marker) {

                    }
                });


                Snackbar.make(view, "Kliknij i przytrzymaj punkt na mapie, aby zmielić lokalizację wskaźnika.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final LocationManager lm = (LocationManager) con.getSystemService(Context.LOCATION_SERVICE);
        final boolean[] gps_enabled = {false};
        final boolean[] network_enabled = {false};
        try {
            MapsInitializer.initialize(this.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapViewpunkt.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap1) {
                googleMap = googleMap1;
                if (ActivityCompat.checkSelfPermission(con, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(con, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                googleMap.setMyLocationEnabled(true);
                googleClient = new GoogleApiClient.Builder(con)
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) con)
                        .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) con)
                        .build();

                try {
                    gps_enabled[0] = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                } catch (Exception ex) {
                }

                try {
                    network_enabled[0] = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                } catch (Exception ex) {
                }

                if (!gps_enabled[0] && !network_enabled[0]) {
                    // notify user
                    AlertDialog.Builder dialog = new AlertDialog.Builder(con);
                    dialog.setMessage(con.getResources().getString(R.string.gps_network_not_enabled));
                    dialog.setPositiveButton(con.getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            // TODO Auto-generated method stub
                            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            con.startActivity(myIntent);
                            //get gps
                        }
                    });
                    dialog.setNegativeButton(con.getString(R.string.Cancel), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                            onLocationChanged(null);
                        }
                    });
                    dialog.show();
                }
                try {
                    googleClient.connect();
                } catch (Exception e) {

                }

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap1) {
        googleMap = googleMap1;
        if (ActivityCompat.checkSelfPermission(con, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(con, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(false);
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.google_profile, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.normal) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMapViewpunkt.requestFocusFromTouch();
            return true;
        }

        if (id == R.id.hybrydowa) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            mMapViewpunkt.requestFocusFromTouch();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapViewpunkt.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapViewpunkt.onPause();
    }


    @Override
    public void onBackPressed() {

        Intent intent = getIntent();
        intent.putExtra("miasto", miasto1);
        intent.putExtra("ulica", ulica1);
        intent.putExtra("nr_domu", nr_domu1);
        intent.putExtra("lat", lat1);
        intent.putExtra("lon", lon1);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapViewpunkt.onDestroy();


    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapViewpunkt.onLowMemory();
    }

    private void goToLocationZoom(double lat, double lng, float zoom) {
        final double lat1 = lat;
        final double lng2 = lng;
        final float zoom1 = zoom;
        mMapViewpunkt.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                LatLng ll = new LatLng(lat1, lng2);
                CameraPosition cameraPosition = new CameraPosition.Builder().target(ll).zoom(zoom1).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });


    }

    private void goToLocationZoom1(LatLng lll, float zoom) {
        final LatLng l = lll;
        mMapViewpunkt.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;


                CameraPosition cameraPosition = new CameraPosition.Builder().target(l).zoom(15).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


            }
        });

    }

    public void geoLocate(View view) throws IOException {


        String location = et.getText().toString();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
        gc = new Geocoder(this);
        List<Address> list = gc.getFromLocationName(location, 1);
        Address address = list.get(0);
        String locality = address.getLocality();
        double lat = 0;
        double lng = 0;
        lat = address.getLatitude();
        lng = address.getLongitude();

        if (lat == 0 || lng == 0) {
            Toast.makeText(this, "Nie znaleziono adresu.", Toast.LENGTH_SHORT).show();
        } else {
            goToLocationZoom(lat, lng, 16);
        }
    }

    LocationRequest mLocationRequest;

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //  mLocationRequest.setInterval(1000);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    public void onLocationChanged(Location location) {

        if(location == null){
            goToLocationZoom(54.35,18.6666667,10);

        }else{
            LatLng ll  = new LatLng(location.getLatitude(),location.getLongitude());
            goToLocationZoom1(ll,13);
        }
    }

    public void addMarker(View view) {

        miasto1 = null;
        ulica1= null;
        nr_domu1 = null;
        lat1 = null;
        lon1 = null;

        googleMap.clear();

    }
    public void zapiszDoWyslania(View view) {
        Intent intent = getIntent();
        intent.putExtra("miasto", miasto1);
        intent.putExtra("ulica", ulica1);
        intent.putExtra("nr_domu", nr_domu1);
        intent.putExtra("lat", lat1);
        intent.putExtra("lon", lon1);
        setResult(RESULT_OK, intent);
        Toast.makeText(this,"Zapisano punkt!!",Toast.LENGTH_SHORT).show();
        finish();

    }
}
