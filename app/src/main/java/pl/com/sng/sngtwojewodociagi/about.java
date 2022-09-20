package pl.com.sng.sngtwojewodociagi;

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

//import com.example.pbronk.mojesng.R;


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

        textrelise = "Od 28. lat Saur Neptun Gdańsk na mocy umowy kontraktowej z Miastem Gdańsk dostarcza wodę mieszkańcom. Akcjonariuszami spółki są francuska Grupa SAUR - 51% oraz Miasto Gdańsk - 49%. <br>Jako ekspert w dziedzinie wod-kan spółka świadczy usługi komercyjne:<br>• projektowanie sieci wod-kan <br>• usługi laboratoryjne<br>• zarządzanie i eksploatacja siecią wod-kan<br>• przeglądy sieci kamerą TV<br>• sektoryzacja i monitoring zużycia<br>• ekspertyzy procesów technologicznych i wiele innych.<br>Wysoka jakość procesów i usług to wizytówka SNG. Dlatego SNG może się pochwalić szerokim portfolio certyfikatów ISO: Certyfikat Zintegrowanego Systemu Zarządzania QSE na zgodność postępowania z normami (ISO 9001, ISO 14001, ISO 45001), Certyfikat na zgodność z normą ISO 22000:2005 (system zarządzania bezpieczeństwem żywności), Certyfikat na zgodność z normą ISO 37001:2016 (system zarządzania działaniami antykorupcyjnymi), Certyfikat na zgodność z normą PN-N-ISO/IEC 17025:2018 (system zarządzania laboratorium).<br>Wykorzystują nowoczesne technologie SNG dba o jak najwyższy poziom świadczonych usług oraz zadowolenie każdego Klienta. Do całodobowego kontaktu z Klientami przez 7 dni w tygodniu z uruchomiono aplikację SNG Twoje Wodociągi. W czasie rzeczywistym Klienci otrzymują informacje o awarii, pracach planowych, mogą sprawdzić parametry jakościowe wody w swoich domach, a także zgłosić awarię." +
                "<br><br>Saur Neptun Gdańsk SA<br> ul. Wałowa 46 <br>80-858 Gdańsk <br> Call Center: +48 58 301 30 91<br>Fax: +48 58 301 45 13<br>e-mail: " +
                "<a href=\"mailto:info@sng.com.pl\"><font color=\"FF00CC\">info@sng.com.pl</font></a>";
        if(!TargetActivity.oabaut.getText().isEmpty()) {
            textrelise = TargetActivity.oabaut.getText();
        }
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
