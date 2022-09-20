package pl.com.sng.twojewodociagi;
//pl.com.sng.twojewodociagi

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import pl.com.sng.twojewodociagi.R;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by PBronk on 27.10.2016.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
private static final String TAG = "MyFireBaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data playload: "+remoteMessage.getData());

         try {
             JSONObject data = new JSONObject(remoteMessage.getData());
             String jsonMessage  = data.getString("extra_information");
             Log.d(TAG,"onMessageTeceived: \n"+ "Extra Info: " +jsonMessage);
         }catch (JSONException e){
             e.printStackTrace();
         }
        }
        if(remoteMessage.getNotification() != null){

          String title = remoteMessage.getNotification().getTitle();
           String message = remoteMessage.getNotification().getBody();
            String click_action = remoteMessage.getNotification().getClickAction();


            String czyPowi = null;

            DataHandler dthand = new DataHandler(getBaseContext());
            dthand.open();
            Cursor c = dthand.returnCzyPowiadomienie();
            if(c.moveToFirst()){

                czyPowi = c.getString(c.getColumnIndex(DataHandler.CZY_POWIDADOMNIENIE_COL));
            }
            dthand.close();
            if(czyPowi.equals("1")){
                sendNotification(title, message,click_action);
            }



        }

    }

    private void sendNotification(String title, String messageBody, String click_action) {
            Intent intent ;


        switch(click_action){

            case "aktualnosci":
                intent = new Intent(this, MainActivity.class);
                intent.putExtra("pozycja","aktualnosci");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                break;
            case "kapieliska":
                intent = new Intent(this, MainActivity.class);
                intent.putExtra("pozycja","kapieliska");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                break;
            case "planowewylaczenia":
                intent = new Intent(this, MainActivity.class);
                intent.putExtra("pozycja","planowewylaczenia");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                break;
            case "awarie":
                intent = new Intent(this, MainActivity.class);
                intent.putExtra("pozycja","awarie");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                break;
            default:
                intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        }

        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0 , intent, PendingIntent.FLAG_ONE_SHOT);

        Uri nottificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notifiBuilder = new  NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Są nowe wiadomości w SNG")
              ///  .setContentText(body)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(title))
                .setAutoCancel(true)
                .setSound(nottificationSound)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notifiBuilder.build());

    }


}
