package com.max.def.newstrey;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import im.delight.android.webview.AdvancedWebView;

public class NewsDetailsActivity extends AppCompatActivity implements AdvancedWebView.Listener
{
    private AdvancedWebView advancedWebView;

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");

        advancedWebView = findViewById(R.id.webView);
        advancedWebView.setListener(this, this);
        advancedWebView.loadUrl(url);

        MobileAds.initialize(this, "ca-app-pub-3968634238894128~1714350580");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3968634238894128/7525381782");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if (mInterstitialAd.isLoaded())
                {
                    mInterstitialAd.show();
                }
                else
                {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }

            }
        }, 900 * 1000);

        final AdView mAdView = findViewById(R.id.news_detail_adView);
        final AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        advancedWebView.onResume();
    }

    @Override
    protected void onPause() {
        advancedWebView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        advancedWebView.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        advancedWebView.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void onBackPressed()
    {
        if (!advancedWebView.onBackPressed())
        {
            return;
        }

        super.onBackPressed();
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) { }

    @Override
    public void onPageFinished(String url) { }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) { }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) { }

    @Override
    public void onExternalPageRequest(String url) { }

}