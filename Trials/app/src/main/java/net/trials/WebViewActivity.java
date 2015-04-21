package net.trials;

import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.MailTo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


public class WebViewActivity extends ActionBarActivity {

    WebView webView;
    String currentURL;
    SwipeRefreshLayout swipeView;
    Controller aController;
    Boolean isInternetPresent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_activity);
        String UA = "DPSRKP-Live-UAString-1504061234";
        Intent callIntent = getIntent();
        aController = (Controller) getApplicationContext();

        Toolbar toolBar = (Toolbar) findViewById(R.id.webViewAction);
        toolBar.setTitle(callIntent.getStringExtra("Title"));
        toolBar.setSubtitle(R.string.app_name);

        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        currentURL = callIntent.getStringExtra("URL");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.outline_ic);
            ActivityManager.TaskDescription td = new ActivityManager.TaskDescription("DPS RKP", bm, 0xFF00810d);
            setTaskDescription(td);
            currentURL += "&androidOS=lollipop";
        }
        swipeView = (SwipeRefreshLayout) findViewById(R.id.webActivitySwipe);
        webView = (WebView) findViewById(R.id.webView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true); // adjust
        // width
        webView.getSettings().setDisplayZoomControls(false);

        if (callIntent.getStringExtra("Title").equals("School Schedule")){
            webView.getSettings().setUseWideViewPort(false);
            toolBar.setBackgroundColor(0xffFFB300);
        }
        else webView.getSettings().setUseWideViewPort(true);

        webView.getSettings().setAppCacheMaxSize(1024 * 256);
        webView.getSettings().setAppCachePath(
                "/data/data/net.dpsrkp.dpsrkp/cache");
        webView.getSettings().setAppCacheEnabled(true);
        //mWebView.getSettings().setPluginsEnabled(true);
        webView.setBackgroundColor(Color.parseColor("#00ffffff"));
        //mWebView.setBackgroundResource(R.drawable.webview_main);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        //mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setUserAgentString(UA);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        webView.setWebViewClient(new WebClient());

        isInternetPresent = aController.isConnectingToInternet();
        if (isInternetPresent) {
            webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
            webView.loadUrl(currentURL);
        } else {
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ONLY);
            webView.loadUrl(currentURL);
        }

        swipeView.setColorSchemeResources(R.color.ColorPrimary,
                R.color.orange,
                R.color.blue,
                R.color.red);

        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeView.setRefreshing(true);
                webView.reload();
            }
        });
    }

    private class WebClient extends WebViewClient {
        @Override
        public void onReceivedError(WebView view,int errorCode, String description, String failingUrl) {
            webView.loadUrl("file:///android_res/raw/error.html");
        }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, null);
            swipeView.setRefreshing(true);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            swipeView.setRefreshing(false);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (url.startsWith("mailto:")) {
                MailTo mt = MailTo.parse(url);
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{mt.getTo()});
                i.putExtra(Intent.EXTRA_SUBJECT, mt.getSubject());
                startActivity(i);
                view.reload();
                return true;
            } else
                return false;

        }

    }

    @Override
    public boolean onKeyDown(int KeyCode, KeyEvent event)
    {
        if ((KeyCode)== KeyEvent.KEYCODE_BACK && webView.canGoBack())   //going to previous webpage from within
        {
            webView.goBack();
            return true;

        } else if ((KeyCode)== KeyEvent.KEYCODE_BACK)   //going to previous webpage from within
        {
            this.finish();
            //overridePendingTransition  (0, R.anim.right_slide_out);

        }
        return super.onKeyDown(KeyCode, event);
    }

    @Override
    public void onBackPressed() {
        this.finish();
        //overridePendingTransition(0, R.anim.right_slide_out);
        return;
    }


}
