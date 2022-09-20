package pl.com.sng.sngtwojewodociagi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

//import com.example.pbronk.mojesng.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by PBronk on 20.03.2017.
 */

public class ekoplaza_mapa extends AppCompatActivity {
	MapView mMapView;
	private GoogleMap googleMap;
	ArrayList<String> lat;
	ArrayList<String> lon;
	ArrayList<String> plazaPlace;
	ArrayList<String> plazaIndicat;
	ArrayList<String> plazaMaxValue;
	ArrayList<String> plazaSample;
	ArrayList<String> plazaDateDef;
	ArrayList<String> plazaValue;
	ArrayList<String> tempValues;

	Double lati;
	Double longi;
	String[] separated;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ekoplaza_mapa);
		Intent intent = getIntent();
		lat = intent.getStringArrayListExtra("latitude");
		lon = intent.getStringArrayListExtra("longitude");
		plazaPlace = intent.getStringArrayListExtra("place");
		plazaIndicat = intent.getStringArrayListExtra("indicat");
		plazaValue = intent.getStringArrayListExtra("values");
		plazaMaxValue = intent.getStringArrayListExtra("maxvalues");
		plazaSample = intent.getStringArrayListExtra("sample");
		plazaDateDef = intent.getStringArrayListExtra("datefin");
		String[] separated;
		mMapView = (MapView) this.findViewById(R.id.ekoplazaMap);
		mMapView.onCreate(savedInstanceState);
		mMapView.onResume();
		try {
			MapsInitializer.initialize(this.getApplicationContext());
		} catch (Exception e) {
			e.printStackTrace();
		}

		mMapView.getMapAsync(new OnMapReadyCallback() {
			@Override
			public void onMapReady(GoogleMap mmap) {
				LatLngBounds.Builder builder = new LatLngBounds.Builder();
				LatLng pozycja;
				googleMap = mmap;

				for (int i = 0; i<plazaSample.size(); i++){
					ArrayList<String> tempValues = new ArrayList<String>();
					lati = Double.parseDouble(lat.get(i));
					longi = Double.parseDouble(lon.get(i));;
					//     tempValues.add("Bakterie");
					//     tempValues.add("Wartość/Norma");
					//   tempValues.add("Norma");
					//  tempValues.add("Data badania");
					boolean przekroczone = false;
					//  tempValues[i] = plazaIndicat.get(i)+'$'+ plazaValue.get(i)+"$"+plazaMaxValue.get(i);
					tempValues.add(plazaIndicat.get(i)+':'+ plazaValue.get(i)+":"+plazaMaxValue.get(i));
					//   tempValues.add(plazaValue.get(i));//+"/"+plazaMaxValue.get(i));
					//   tempValues.add(plazaMaxValue.get(i));
					//   tempValues.add(plazaDateDef.get(i));



					if (plazaValue.get(i).matches("[0-9]+") && plazaPlace.get(i).length() > 2)
						if ( Integer.parseInt(plazaValue.get(i)) - Integer.parseInt(plazaMaxValue.get(i))>0){
							przekroczone = true;
						}


					while(i< plazaSample.size()-1 && plazaSample.get(i+1).equals(plazaSample.get(i)) ){
						//  tempValues[i+1] = plazaIndicat.get(i+1)+'$'+ plazaValue.get(i+1)+"$"+plazaMaxValue.get(i+1);
						tempValues.add(plazaIndicat.get(i+1)+':'+ plazaValue.get(i+1)+":"+plazaMaxValue.get(i+1));
						//   tempValues.add(plazaValue.get(i+1));//+"/"+plazaMaxValue.get(i+1));
						//   tempValues.add(plazaMaxValue.get(i+1));
						// tempValues.add(plazaDateDef.get(i+1));
						if (plazaValue.get(i+1).matches("[0-9]+") && plazaPlace.get(i+1).length() > 2)
							if ( Integer.parseInt(plazaValue.get(i+1)) - Integer.parseInt(plazaMaxValue.get(i+1))>0){
								przekroczone = true;
							}
						if(i < plazaSample.size()-1 ){
							i = i+1;
						}
					}
					if(i <= plazaSample.size() ) {
						pozycja = new LatLng(lati, longi);
						builder.include(pozycja);

						if(przekroczone) {
							googleMap.addMarker(new MarkerOptions()
									.position(pozycja)
									.title(plazaPlace.get(i))
									.snippet(plazaDateDef.get(i))
									.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
									)).setTag(tempValues);


						}else {
							googleMap.addMarker(new MarkerOptions()
									.position(pozycja)
									.title(plazaPlace.get(i))
									.snippet(plazaDateDef.get(i))
									.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))

							).setTag(tempValues);



						}
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
														   ArrayList<String> templTag = new ArrayList<String>();
														   templTag= (ArrayList<String> ) marker.getTag();
														   // ArrayAdapter<String> ad = new ArrayAdapter<String>(ekoplaza_mapa.this,android.R.layout.simple_list_item_1,templTag);
														   View v = getLayoutInflater().inflate(R.layout.info_window_kapieliska,null);
														   TextView title_info = (TextView)v.findViewById(R.id.title_info_window_kapieliska);
														   ListView info_view_lv= (ListView) v.findViewById(R.id.listViewKapieliska);

														   info_view_lv.setAdapter(new myAdapter(ekoplaza_mapa.this,templTag));
														   TextView date_info = (TextView)v.findViewById(R.id.data_badania_window_kapieliska);
														   date_info.setText("Data badania: "+pet);
														   title_info.setText(title1);


														   return v;
													   }
												   }
					);

					if(plazaSample.size()==1){
						CameraPosition cameraPosition = new CameraPosition.Builder().target(pozycja).zoom(12).build();
						googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
					}else {
						LatLngBounds bounds = builder.build();
						CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
						googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),100));
					}


					// sprawdzenie jesli nastepna sample bedzie takie samo to bierzemy kolejne parametry i do i dodajemy 1

				}

			}
		});


	}


	static class myAdapter extends BaseAdapter {

		Context context;
		ArrayList<String>  data;
		private  static LayoutInflater inflater = null;

		public myAdapter(Context context, ArrayList<String>  data) {
			// TODO Auto-generated constructor stub
			this.context = context;
			this.data = data;
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View vi = convertView;
			if (vi == null)
				vi = inflater.inflate(R.layout.row_kapieliska_lv, null);
			TextView bakteria = (TextView) vi.findViewById(R.id.bateria);
			TextView wartosc = (TextView) vi.findViewById(R.id.wartoscbadania);
			TextView norma = (TextView) vi.findViewById(R.id.norma);
			String tempSplit = null;
			tempSplit = data.get(position);
			String[] tempt =tempSplit.split(":");
			wartosc.setTextColor(Color.BLACK);
			if (tempt[1].matches("[0-9]+") && tempt[2].length() > 2)
				if ( Integer.parseInt(tempt[1]) - Integer.parseInt(tempt[2])>0){
					wartosc.setTextColor(Color.RED);
				}




			bakteria.setText("Bakteria: "+tempt[0]);
			wartosc.setText("Wartość: "+tempt[1]);
			norma.setText("Norma: "+tempt[2]);

			return vi;
		}
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







}
