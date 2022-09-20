package pl.com.sng.twojewodociagi;

import android.content.Intent;
import android.net.Uri;
import  android.support.v4.app.Fragment ;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import pl.com.sng.twojewodociagi.R;


/**
 * Created by PBronk on 04.11.2016.
 */

public class about extends Fragment {
    TextView tv;
    String textrelise;
View myView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       myView = inflater.inflate(R.layout.about, container,false);
        tv = (TextView)myView.findViewById(R.id.onashtml);
        ImageButton do_strony,fb,tw;

       textrelise = "Saur Neptun Gdańsk, Spółka Akcyjna, powstała w dniu 1 lipca 1992 r., jako  pierwsze w Europie Środkowej partnerstwo publiczno-prywatne (joint venture) w dziedzinie usług komunalnych. Miasto Gdańsk objęło w niej 49% udziałów, a francuska Grupa SAUR 51% udziałów. Na mocy umów kontraktowych, SNG jako operator gdańskiego systemu wodociągowo-kanalizacyjnego ma za zadanie utrzymywać wysoką jakość usług dostarczania wody i odprowadzania ścieków. Spółka obsługuje ponad 500 000 mieszkańców Gdańska i Sopotu, oczyszczając także ścieki z gmin sąsiadujących z Gdańskiem. Woda dostarczana mieszkańcom Gdańska spełnia w 100% rygorystyczne normy unijne.<br><br>Saur Neptun Gdańsk SA<br> ul. Wałowa 46 <br>80-858 Gdańsk <br> Call Center: +48 58 301 30 91<br>Fax: +48 58 301 45 13<br>e-mail: " +
               "<a href=\"mailto:info@sng.com.pl\"><font color=\"FF00CC\">info@sng.com.pl</font></a> <br>KRS 0000006553<br>NIP 583-000-67-15<br> <br><br> Rejon Sopot <br>ul. Polna 66/68 <br>81-740 Sopot <br>tel. +48 58 551 35 07";
        tv.setText(Html.fromHtml(textrelise));
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        fb = (ImageButton)myView.findViewById(R.id.butt_facebook);
        fb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0)
            {
                checkNetConection cc = new checkNetConection(getContext());

                if(cc.isOnline()){
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/SaurNeptunGdansk/"));
                    startActivity(browserIntent);

                } else{
                    Toast.makeText(getContext(),"Praca w trybie offline!!",Toast.LENGTH_SHORT).show();
                }

            }
        });

        do_strony = (ImageButton)myView.findViewById(R.id.butt_strone);
        do_strony.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0)
            {
                checkNetConection cc = new checkNetConection(getContext());

                if(cc.isOnline()){
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.sng.com.pl/"));
                    startActivity(browserIntent);

                } else{
                    Toast.makeText(getContext(),"Praca w trybie offline!!",Toast.LENGTH_SHORT).show();
                }


            }
        });

        tw = (ImageButton)myView.findViewById(R.id.butt_twitter);
        tw.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0)
            {
                checkNetConection cc = new checkNetConection(getContext());
                if(cc.isOnline()){

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/Twoje_SNG"));
                    startActivity(browserIntent);

                } else{
                    Toast.makeText(getContext(),"Praca w trybie offline!!",Toast.LENGTH_SHORT).show();
                }

            }
        });
        return myView;
    }
}
