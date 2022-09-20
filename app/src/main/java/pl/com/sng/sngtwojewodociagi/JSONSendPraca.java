package pl.com.sng.sngtwojewodociagi;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pbronk on 05.07.2018.
 */

public class JSONSendPraca {
     private String Stanowisko;
    private String imie;
    private String Nazwisko;
    private String DataUrodzenia;
    private String Miejscowosc;
    private String KodPocztowy;
    private String Ulica;
    private String NrBudynku;
    private String Mail;
    private String NrTelefonu;
    private String RodzajWyksz;
    private String Kierunek;
    private String Staz;
    private String Angielski;
    private String Niemiecki;
    private String Francuski;
    private String Rosyjski;
    private String InnyJezyk;
    private String Inny;
    private String OblsugaKomp;
    private String PrawoJazdy;
    private String InneUmiej;
    private String Oczekiwania;
    private String Uwagi;
    private String PlikCv;
    private String NazwaPliku;

    public JSONSendPraca(String Stanowisko1, String imie1, String Nazwisko1, String DataUrodzenia1, String Miejscowosc1, String KodPocztowy1, String Ulica1, String NrBudynku1, String Mail1, String NrTelefonu1, String RodzajWyksz1,
                         String Kierunek1,String Staz1,String Angielski1,String Niemiecki1,String Francuski1,String Rosyjski1,String InnyJezyk1,String Inny1,String OblsugaKomp1,String PrawoJazdy1,String InneUmiej1,String Oczekiwania1,
                                 String Uwagi1,String PlikCv1,String NazwaPliku1) throws IOException, JSONException {
        this.Stanowisko = Stanowisko1;
        this.imie = imie1;
        this.Nazwisko = Nazwisko1;
        this.DataUrodzenia = DataUrodzenia1;
        this.Miejscowosc = Miejscowosc1;
        this.KodPocztowy = KodPocztowy1;
        this.Ulica = Ulica1;
        this.NrBudynku = NrBudynku1;
        this.Mail = Mail1;
        this.NrTelefonu = NrTelefonu1;
        this.RodzajWyksz = RodzajWyksz1;
        this.Kierunek = Kierunek1;
        this.Staz = Staz1;
        this.Angielski = Angielski1;
        this.Niemiecki = Niemiecki1;
        this.Francuski = Francuski1;
        this.Rosyjski = Rosyjski1;
        this.InnyJezyk = InnyJezyk1;
        this.Inny = Inny1;
        this.OblsugaKomp = OblsugaKomp1;
        this.PrawoJazdy = PrawoJazdy1;
        this.InneUmiej = InneUmiej1;
        this.Oczekiwania = Oczekiwania1;
        this.Uwagi = Uwagi1;
        this.NazwaPliku = NazwaPliku1;


        if (PlikCv1 == null) {
            this.PlikCv = "brak";
        } else {
            this.PlikCv = PlikCv1;
        }

        getJSONFromUrl("https://notif2.sng.com.pl/api/FormularzSend/");
    }
    public boolean getJSONFromUrl(String url) throws IOException, JSONException {
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        List nameValuePairs = new ArrayList<String>(1);
        nameValuePairs.add(new BasicNameValuePair("Stanowisko", Stanowisko));
        nameValuePairs.add(new BasicNameValuePair("Imie", imie));
        nameValuePairs.add(new BasicNameValuePair("Nazwisko", Nazwisko));
        nameValuePairs.add(new BasicNameValuePair("DataUrodzenia", DataUrodzenia));
        nameValuePairs.add(new BasicNameValuePair("Miejscowosc", Miejscowosc));
        nameValuePairs.add(new BasicNameValuePair("KodPocztowy", KodPocztowy));
        nameValuePairs.add(new BasicNameValuePair("Ulica", Ulica));
        nameValuePairs.add(new BasicNameValuePair("NrBudynku", NrBudynku));
        nameValuePairs.add(new BasicNameValuePair("Mail", Mail));
        nameValuePairs.add(new BasicNameValuePair("NrTelefonu", NrTelefonu));
        nameValuePairs.add(new BasicNameValuePair("RodzajWyksz", RodzajWyksz));
        nameValuePairs.add(new BasicNameValuePair("Kierunek", Kierunek));
        nameValuePairs.add(new BasicNameValuePair("Staz", Staz));
        nameValuePairs.add(new BasicNameValuePair("Angielski", Angielski));
        nameValuePairs.add(new BasicNameValuePair("Niemiecki", Niemiecki));
        nameValuePairs.add(new BasicNameValuePair("Francuski", Francuski));
        nameValuePairs.add(new BasicNameValuePair("Rosyjski", Rosyjski));
        nameValuePairs.add(new BasicNameValuePair("InnyJezyk", InnyJezyk));
        nameValuePairs.add(new BasicNameValuePair("Inny", Inny));
        nameValuePairs.add(new BasicNameValuePair("OblsugaKomp", OblsugaKomp));
        nameValuePairs.add(new BasicNameValuePair("PrawoJazdy", PrawoJazdy));
        nameValuePairs.add(new BasicNameValuePair("InneUmiej", InneUmiej));
        nameValuePairs.add(new BasicNameValuePair("Oczekiwania", Oczekiwania));
        nameValuePairs.add(new BasicNameValuePair("Uwagi", Uwagi));
        nameValuePairs.add(new BasicNameValuePair("PlikCv", PlikCv));
        nameValuePairs.add(new BasicNameValuePair("NazwaPliku", NazwaPliku));

        post();

        return false;
    }


    public void  post() throws IOException, JSONException {

        org.json.JSONObject requestJson = new org.json.JSONObject();
        String url = "https://notif2.sng.com.pl/api/FormularzSend/";            //method call for generating json
        requestJson = generateJSON();
        URL myurl = new URL(url);
        HttpURLConnection con = (HttpURLConnection)myurl.openConnection();
        con.setDoOutput(true);
        con.setDoInput(true);

        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Method", "POST");
        OutputStream os = con.getOutputStream();
        os.write(requestJson.toString().getBytes("UTF-8"));
        os.close();


        StringBuilder sb = new StringBuilder();
        int HttpResult =con.getResponseCode();
        if(HttpResult == HttpURLConnection.HTTP_OK){
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(),"utf-8"));

            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            System.out.println(""+sb.toString());

        }else{
            System.out.println(con.getResponseCode());
            System.out.println(con.getResponseMessage());
        }

    }

    public  org.json.JSONObject generateJSON () throws MalformedURLException, JSONException
    {
        org.json.JSONObject reqparam = new org.json.JSONObject();
        reqparam.put("Stanowisko",Stanowisko);
        reqparam.put("Imie", imie);
        reqparam.put("Nazwisko", Nazwisko);
        reqparam.put("DataUrodzenia", DataUrodzenia);
        reqparam.put("Miejscowosc", Miejscowosc);
        reqparam.put("KodPocztowy", KodPocztowy);
        reqparam.put("Ulica", Ulica);
        reqparam.put("NrBudynku", NrBudynku);
        reqparam.put("Mail", Mail);
        reqparam.put("NrTelefonu",NrTelefonu);
        reqparam.put("RodzajWyksz",RodzajWyksz);
        reqparam.put("Kierunek",Kierunek);
        reqparam.put("Staz", Staz);
        reqparam.put("Angielski", Angielski);
        reqparam.put("Niemiecki", Niemiecki);
        reqparam.put("Francuski", Francuski);
        reqparam.put("Rosyjski", Rosyjski);
        reqparam.put("InnyJezyk", InnyJezyk);
        reqparam.put("Inny", Inny);
        reqparam.put("OblsugaKomp", OblsugaKomp);
        reqparam.put("PrawoJazdy",PrawoJazdy);
        reqparam.put("InneUmiej",InneUmiej);
        reqparam.put("Oczekiwania",Oczekiwania);
        reqparam.put("Uwagi", Uwagi);
        reqparam.put("PlikCv", PlikCv);
        reqparam.put("NazwaPliku", NazwaPliku);

        return reqparam;
    }




}
