package pl.com.sng.twojewodociagi;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import pl.com.sng.twojewodociagi.R;

public class TargetActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);

        EasySplashScreen config = new EasySplashScreen(TargetActivity.this)
                .withFullScreen()
                .withTargetActivity(MainActivity.class)
                .withSplashTimeOut(1500)
                .withBackgroundResource(android.R.color.white)
                .withHeaderText("")
                .withFooterText("Witamy")
                .withBeforeLogoText("")
                .withLogo(R.drawable.start)
                .withAfterLogoText("");
        //set your own animations
        myCustomTextViewAnimation(config.getFooterTextView());
        //customize all TextViews

        config.getHeaderTextView().setTextColor(Color.WHITE);
        config.getFooterTextView().setTextColor(Color.WHITE);
        //create the view
        View easySplashScreenView = config.create();
        setContentView(easySplashScreenView);
    }

    private void myCustomTextViewAnimation(TextView tv){
        Animation animation=new TranslateAnimation(0,0,480,0);
        animation.setDuration(1200);
        tv.startAnimation(animation);
    }
}
