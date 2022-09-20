package pl.com.sng.twojewodociagi;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by PBronk on 23.11.2016.
 */

public class checkNetConection extends Activity {

    Context con;

    public checkNetConection(Context context) {
        this.con = context;

      //  if (isOnline(con)){

   //     }else{
            // ask the user to activate it
    //        new AlertDialog.Builder(con)
    //                .setTitle("Połaczenie nie powiodło się.")
    //                .setMessage("Wysłanie danych wymaga podłaczenia z internetem " +
    //                        "podłacz się mobilnie bądz przez Wi-Fi")
     //               .setPositiveButton("Włacz", new DialogInterface.OnClickListener() {
     //                   @Override
     //                   public void onClick(DialogInterface dialog, int which) {
//                            startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
     //                   }
     //               })
     //               .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
     //                   @Override
     //                   public void onClick(DialogInterface dialog, int which) {
      //                      finish();
     //                   }
     //               })
     //               .show();
    //    }
    }
    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = conMgr.getActiveNetworkInfo();
        if (i == null){ return false;}
        if (!i.isConnected())
        {return false;}
        if (!i.isAvailable())
        {return false;}
        return true;
    }
}
