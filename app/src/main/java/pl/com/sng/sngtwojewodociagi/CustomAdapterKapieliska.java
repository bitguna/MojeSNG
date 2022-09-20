package pl.com.sng.sngtwojewodociagi;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import pl.com.sng.sngtwojewodociagi.R;

import java.util.ArrayList;

/**
 * Created by pbronk on 30.05.2017.
 */
public class CustomAdapterKapieliska extends ArrayAdapter<String> {

    private final Context context;
    private final ArrayList<String> tit;
    private final ArrayList<String> b1;
    private final ArrayList<String> v1;

    private final ArrayList<String> v2;



    private  final ListView lvx;


    public CustomAdapterKapieliska(Context context, ArrayList<String> title, ArrayList<String> baki1, ArrayList<String> values1, ArrayList<String> values2, ListView lv) {
        super(context, R.layout.dialog_kapieliska, title);
        // TODO Auto-generated constructor stub
        this.lvx = lv;
        this.context=context;
      this.tit = title;
        this.b1=baki1;
        this.v1=values1;
        this.v2=values2;    }


    public View getView(int position, View view, ViewGroup parent) {
try {
    MyViewHolder holder = null;

    if (view == null) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.dialog_kapieliska, parent, false);
        holder = new MyViewHolder();

        holder.txtTitle = (TextView) view.findViewById(R.id.textView6);
        holder.bak1hold = (TextView) view.findViewById(R.id.bakteria1);
        holder.var1hold = (TextView) view.findViewById(R.id.poziom_bart1);
        holder.bak2hold = (TextView) view.findViewById(R.id.barteria2);
        holder.var2hold = (TextView) view.findViewById(R.id.poziom_bart2);
        view.setTag(holder);
    } else {

        holder = (MyViewHolder) view.getTag();

    }


    holder.txtTitle.setText(tit.get(position));
    holder.bak1hold.setText(b1.get(position * 2) + ":");
    holder.bak2hold.setText(b1.get(position * 2 + 1) + ":");
    holder.var1hold.setText(v1.get(position * 2) + "/" + v2.get(position * 2));
    holder.var2hold.setText(v1.get(position * 2 + 1) + "/" + v2.get(position * 2 + 1));


    if (Integer.parseInt(v1.get(position * 2)) > Integer.parseInt(v2.get(position * 2))) {
        holder.var1hold.setTextColor(Color.RED);
    }//else
   // {
   ///     holder.var1hold.setTextColor(Color.GREEN);
  //  }


    if (Integer.parseInt(v1.get(position * 2 + 1)) > Integer.parseInt(v2.get(position * 2 + 1))) {
        holder.var2hold.setTextColor(Color.RED);
    }//else
   // {
   //     holder.var2hold.setTextColor(Color.GREEN);
   // }
    //     rowView.setTag(holder);
}catch (Exception e){
   TextView txtTitle = (TextView) view.findViewById(R.id.textView6);
    txtTitle.setText(tit.get(position));


}


        return view;

    };

    static class MyViewHolder {
        TextView txtTitle;
        TextView bak1hold;
        TextView bak2hold;
        TextView var1hold;
        TextView var2hold;

    }

}


