package pl.com.sng.sngtwojewodociagi;

import  android.support.v4.app.Fragment ;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import com.example.pbronk.mojesng.R;

/**
 * Created by PBronk on 04.11.2016.
 */

public class contact extends Fragment {

    View myView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.contact, container,false);
        return myView;
    }
}
