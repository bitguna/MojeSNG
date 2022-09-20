package pl.com.sng.sngtwojewodociagi;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

//import com.example.pbronk.mojesng.R;

import java.util.ArrayList;

/**
 * Created by PBronk on 29.12.2016.
 */

public class cennik1 extends Fragment {
    ListView list1;
    View myView;
    ArrayList<String> listaCennik1 = new ArrayList<String>()
    {{
        add(0,"Usługi techniczne i wynajem sprzętu");
        add(1,"Wynajem taboru samochodowego");

    }};



    ArrayList<String> listaLinkowCennik = new ArrayList<String>(){{
        add(0,"https://www.sng.com.pl/Portals/2/dok/cenniki/cennik_uslugi.pdf");
        add(1,"https://www.sng.com.pl/Portals/2/dok/cenniki/cennik_samochody.pdf");
    }};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.cennik1, container, false);

        list1 = (ListView)myView.findViewById(R.id.cennikLista);

        ArrayAdapter<String> saveAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,listaCennik1);
        list1.setAdapter(saveAdapter);


        list1.setOnItemClickListener(new AdapterView.OnItemClickListener()
                                     {
                                         @Override
                                         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                             checkNetConection cc = new checkNetConection(getContext());
                                             if(cc.isOnline()){
                                                 String www = listaLinkowCennik.get(position);
                                                 Intent w = new Intent(getActivity(), cennik.class);
                                                 w.putExtra("andresWWW",www);

                                                 startActivity(w);

                                             } else{
                                                 Toast.makeText(getContext(),"Praca w trybie offline!!",Toast.LENGTH_SHORT).show();
                                             }



                                         }
                                     }
        );



        return myView;
    }

}
