package pl.com.sng.twojewodociagi;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by PBronk on 27.10.2016.
 */

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFireBaseIDService";

    @Override
    public void onTokenRefresh() {
      String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG,"New Token:"+ token);
        registerToken(token);

    }

    private  void  registerToken(String token){

// tutaj wysyłamy do web servicu tokena


    }

}
