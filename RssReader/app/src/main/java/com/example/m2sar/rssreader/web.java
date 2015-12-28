package com.example.m2sar.rssreader;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.FileOutputStream;


public class web extends ActionBarActivity {
    WebView myWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        myWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

       /* WebClientClass webViewClient = new WebClientClass();
        myWebView.setWebViewClient(webViewClient);
        this.setContentView(myWebView);*/


        /*webView.setClickable(true);
        webView.setFocusableInTouchMode(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        WebClientClass webViewClient = new WebClientClass();
        webView.setWebViewClient(webViewClient);
        getActivity().setContentView(webView);*/
        // According to the official documentation, click on a link in a WebView will launch an application that handles URLs
        // the line below solved the problem
        myWebView.setWebViewClient(new WebViewClient(){
            private ProgressBar mProgress;
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                super.onPageStarted(view, url, favicon);
                findViewById(R.id.progress).setVisibility(View.VISIBLE);            }

            @Override
            public void onPageFinished(WebView view, String url) {

               findViewById(R.id.progress).setVisibility(View.GONE);

            }
        });
        myWebView.loadUrl(getIntent().getStringExtra("URL"));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_web, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if (id== R.id.favoris){
            writeCellule(getIntent().getStringExtra("TITLE"),getIntent().getStringExtra("DATE"),getIntent().getStringExtra("URL"));

        }else if(id==R.id.history){
            Intent intent = new Intent(getApplicationContext(),Archives.class);
            startActivity(intent);
                  }

        return super.onOptionsItemSelected(item);
    }

    void writeCellule(String title,String date, String url){
        String FILENAME = "MyFile";
        FileOutputStream fos =null;
        try{
            fos = openFileOutput(FILENAME,getApplicationContext().MODE_APPEND);
            fos.write(title.getBytes());
            fos.write(("\n").getBytes());
            fos.write(date.getBytes());
            fos.write(("\n").getBytes());
            fos.write(url.getBytes());
            fos.write(("\n").getBytes());
            fos.close();
            Toast.makeText(getApplicationContext()," Votre Données ont été bien archivé  ",Toast.LENGTH_LONG).show();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

}
