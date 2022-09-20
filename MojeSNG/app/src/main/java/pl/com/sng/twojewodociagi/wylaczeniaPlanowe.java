package pl.com.sng.twojewodociagi;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import pl.com.sng.twojewodociagi.R;

/**
 * Created by PBronk on 07.12.2016.
 */
public class wylaczeniaPlanowe extends Fragment {
    View myView;
    TextView tv;
String textrelise;
    DataHandler  dataHandler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.wylaczeniaplanowe, container, false);

        dataHandler = new DataHandler(getContext());
        checkNetConection cc = new checkNetConection(getContext());

        if(!cc.isOnline()){
            Toast.makeText(getContext(),"Praca w trybie offline!!",Toast.LENGTH_LONG).show();
        }
        dataHandler.open();
        Cursor cp = dataHandler.returnPlanoweWyl();
        if (cp.moveToFirst()) {
            do {

                textrelise = cp.getString(cp.getColumnIndex(DataHandler.VALUE_WYL));

            } while (cp.moveToNext());
        }
            dataHandler.close();

        tv = (TextView)myView.findViewById(R.id.wylaczeniaplanoweview);

        tv.setText(Html.fromHtml(textrelise));

        return myView;



    }
}
