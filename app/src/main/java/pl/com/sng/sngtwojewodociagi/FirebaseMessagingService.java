package pl.com.sng.sngtwojewodociagi;
//pl.com.sng.sngtwojewodociagi

import android.app.LauncherActivity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;


import pl.com.sng.sngtwojewodociagi.R;
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

         try {
             JSONObject data = new JSONObject(remoteMessage.getData());
             String jsonMessage  = data.getString("extra_information");
             //sendNotification(data.getString("title"), data.getString("body"),data.getString("click_action"));
         }catch (Exception e){
             e.printStackTrace();

         }
        }

        if(remoteMessage.getNotification() != null){
          String title = remoteMessage.getNotification().getTitle();

           String message = remoteMessage.getNotification().getBody();
            String click_action = remoteMessage.getNotification().getClickAction();
                sendNotification(title, message,click_action);


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
            Context context = this;
            if(Build.VERSION.SDK_INT< Build.VERSION_CODES.O) {
                NotificationCompat.Builder notifiBuilder = new NotificationCompat.Builder(context, FirebaseMessagingService.class.getSimpleName())//NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle(title)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(title))
                        .setAutoCancel(true)
                        .setSound(nottificationSound)
                        .setChannelId("test")
                        .setContentIntent(pendingIntent);

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                int importance = NotificationManager.IMPORTANCE_LOW;
                notificationManager.notify(0, notifiBuilder.build());
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(FirebaseMessagingService.this, "default");
            }else
            {

            NotificationUtils mNotificationUtils = new NotificationUtils(this);
            Notification.Builder nb = mNotificationUtils.
                    getAndroidChannelNotification( title,messageBody,pendingIntent);
            mNotificationUtils.getManager().notify(101, nb.build());

        }}

/**
    public void initChannels(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("default",
                "Channel name",
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Channel description");
        notificationManager.createNotificationChannel(channel);
    }
*/
}
