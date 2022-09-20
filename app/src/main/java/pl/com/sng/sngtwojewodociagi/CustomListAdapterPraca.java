package pl.com.sng.sngtwojewodociagi;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import android.graphics.Color;

/**
 * Created by pbronk on 29.06.2018.
 */

public class CustomListAdapterPraca extends ArrayAdapter<String>  {
    private final Activity  context;
    private final ArrayList<String> itemname;
    private  final  ListView lvx;



    public CustomListAdapterPraca(Activity context, ArrayList<String> itemname,ListView lv) {
        super(context, R.layout.rowlayoutpraca, itemname);
        this.lvx = lv;
        this.context=context;
        this.itemname=itemname;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.rowlayoutpraca, null,true);


        TextView txtTitle = (TextView) rowView.findViewById(R.id.pracatext);
        txtTitle.setText(itemname.get(position));

    if (position % 2 == 1) {
        txtTitle.setBackgroundColor(Color.BLUE);

      } else {
        txtTitle.setBackgroundColor(Color.CYAN);
      }

        return rowView;

    };


}



