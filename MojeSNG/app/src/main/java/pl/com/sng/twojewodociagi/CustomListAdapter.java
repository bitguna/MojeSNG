package pl.com.sng.twojewodociagi;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import pl.com.sng.twojewodociagi.R;

import java.util.ArrayList;

/**
 * Created by PBronk on 29.12.2016.
 */

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity  context;
    private final ArrayList<String> itemname;
    private final ArrayList<String> imgid;
    private  final  ListView lvx;


    public CustomListAdapter(Activity  context, ArrayList<String> itemname, ArrayList<String> imgid, ListView lv) {
        super(context, R.layout.aktualnosci_row_view, itemname);
        // TODO Auto-generated constructor stub
        this.lvx = lv;
        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.aktualnosci_row_view, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        WebView imageView = (WebView) rowView.findViewById(R.id.icon);


        txtTitle.setText(itemname.get(position));
        imageView.loadUrl(imgid.get(position));


        return rowView;

    };



}

