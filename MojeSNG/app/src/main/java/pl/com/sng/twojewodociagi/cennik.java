package pl.com.sng.twojewodociagi;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by PBronk on 29.12.2016.
 */

public class cennik extends AppCompatActivity {
    View myView;
    @Nullable
    @Override
    public void onCreate(final Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);

        WebView webView=new WebView(cennik.this);

        Intent intent = getIntent();
        String pdfURL = intent.getStringExtra("andresWWW");

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setWebViewClient(new Callback());

    //    String pdfURL = "http://www.sng.com.pl/Portals/2/dok/cenniki/cennik_uslugi.pdf";
        webView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + pdfURL);

        setContentView(webView);

    }
    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(
                WebView view, String url) {
            return (false);
        }

    }
}
