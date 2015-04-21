package net.trials;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.MailTo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Avichal Rakesh on 3/27/2015.
 */
public class WebViewFragment extends Fragment {

    //    private String currentURL = "http://impranav.ddns.net:8080/experiments/cards/main-rss/?feeds=";
    private String currentURL = Config.SCHOOL_RSS;

    WebView mWebView;
    SwipeRefreshLayout swipeLayout;
    Controller aController;

    public void init(String url) {
        currentURL = url;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("SwA", "WVF onCreateView");
        View v = inflater.inflate(R.layout.webview, container, false);

        if (currentURL != null) {
            Log.d("SwA", "Current URL  1[" + currentURL + "]");
            String UA = "DPSRKP-Live-UAString-1504061234";
            swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
            mWebView = (WebView) v.findViewById(R.id.webview_fragment);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setLoadWithOverviewMode(true); // adjust
            // width
            mWebView.getSettings().setDisplayZoomControls(false);
            mWebView.getSettings().setUseWideViewPort(true);
            mWebView.getSettings().setAppCacheMaxSize(1024 * 256);
            mWebView.getSettings().setAppCachePath(
                    "/data/data/net.dpsrkp.dpsrkp/cache");
            mWebView.getSettings().setAppCacheEnabled(true);
//            mWebView.getSettings().setPluginsEnabled(true);
            mWebView.setBackgroundColor(Color.parseColor("#00ffffff"));
            //mWebView.setBackgroundResource(R.drawable.webview_main);
            mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            //mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
            mWebView.setVerticalScrollBarEnabled(false);
            mWebView.setHorizontalScrollBarEnabled(false);
            mWebView.getSettings().setAllowFileAccessFromFileURLs(true);
            mWebView.getSettings().setUserAgentString(UA);
            mWebView.setDownloadListener(new DownloadListener() {
                @Override
                public void onDownloadStart(String url, String userAgent,
                                            String contentDisposition, String mimetype,
                                            long contentLength) {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
            mWebView.setWebViewClient(new WebClient());
            //mWebView.setWebChromeClient(new WebChromeClient());
//            mWebView.loadDataWithBaseURL("file:///android_assets/",currentURL,"text/html","utf-8",null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                currentURL += "&androidOS=lollipop";
            }


            aController = (Controller) getActivity().getApplicationContext();
            Boolean isInternetPresent = aController.isConnectingToInternet();
            if (isInternetPresent) {
                mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
                mWebView.loadUrl(currentURL);
            } else {
                mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ONLY);
                mWebView.loadUrl(currentURL);
            }

            swipeLayout.setColorSchemeResources(R.color.ColorPrimary,
                    R.color.orange,
                    R.color.blue,
                    R.color.red);


            swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipeLayout.setRefreshing(true);
                    mWebView.reload();
                }
            });

        }
        return v;
    }

    public boolean checkBackConsumed() {

        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        } else
            return false;
    }

 /*   public void updateUrl(String url) {
        Log.d("SwA", "Update URL [" + url + "] - View [" + getView() + "]");
        currentURL = url;

        WebView wv = (WebView) getView().findViewById(R.id.webview_fragment);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.loadUrl(url);

    }*/

    private class WebClient extends WebViewClient {
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

            mWebView.loadUrl("file:///android_res/raw/error.html");
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, null);
            swipeLayout.setRefreshing(true);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            swipeLayout.setRefreshing(false);
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

            /*else if (url.equals("http://www.exunclan.com/")) {
                Intent intent = new Intent(MainActivity.this, Exun.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_slide_out,
                        R.anim.right_slide_out);
                return true;
            } else if (url.equals("http://lnexun.com/")) {
                Intent intent = new Intent(MainActivity.this, Exun.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_slide_out,
                        R.anim.right_slide_out);
                return true;
            } else if (url.equals("http://digex.exunclan.com/")) {
                Intent intent = new Intent(MainActivity.this, Digex.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_slide_out,
                        R.anim.right_slide_out);
                return true;
            } else if (url
                    .equals("http://dpsrkp.net/notices/?categ=interschoolactivities/")) {
                Intent intent = new Intent(MainActivity.this,
                        Activities_out_school.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_slide_out,
                        R.anim.right_slide_out);
                return true;
            } else if (url.equals("http://dpsrkp.net/notices/?categ=notices/")) {
                Intent intent = new Intent(MainActivity.this,
                        Schedules_Notices.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_slide_out,
                        R.anim.right_slide_out);
                return true;
            } else if (url
                    .equals("https://www.google.com/calendar/embed?title=Delhi+Public+School+R.K.Puram+Events+Schedule&showPrint=0&showCalendars=0&showTz=0&height=1000&wkst=2&hl=en_GB&bgcolor=%23ffffff&src=fs5nat910ju6ch9lbnjcthrbgk@group.calendar.google.com&color=%230D7813&ctz=Asia/Calcutta/")) {
                Intent intent = new Intent(MainActivity.this,
                        School_Calendar.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_slide_out,
                        R.anim.right_slide_out);
                return true;
            } else if (url.equals("http://www.educomponline.com/SignIn.aspx")) {
                Intent intent = new Intent(MainActivity.this,
                        Educomp_Online.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_slide_out,
                        R.anim.right_slide_out);
                return true;
            } else if (url
                    .equals("http://dpsrkp.net/notices/?categ=special_highlights/")) {
                Intent intent = new Intent(MainActivity.this,
                        Special_highlights.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_slide_out,
                        R.anim.right_slide_out);
                return true;
            } else if (url
                    .equals("http://dpsrkp.net/notices/?categ=achievements/")) {
                Intent intent = new Intent(MainActivity.this,
                        Achievements.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_slide_out,
                        R.anim.right_slide_out);
                return true;
            } else if (url.equals("http://dpsrkp.net/notices/?categ=sports/")) {
                Intent intent = new Intent(MainActivity.this, Sports.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_slide_out,
                        R.anim.right_slide_out);
                return true;
            } else if (url.equals("http://dpsrkp.net/")) {
                Intent intent = new Intent(MainActivity.this,
                        Vist_Official_web.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_slide_out,
                        R.anim.right_slide_out);
                return true;
            } else if (url
                    .equals("http://dpsrkp.net/notices/?categ=activities/")) {
                Intent intent = new Intent(MainActivity.this,
                        Activities_in_school.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_slide_out,
                        R.anim.right_slide_out);
                return true;
            } else if (url
                    .equals("http://dpsrkp.net/notices/?categ=international_events/")) {
                Intent intent = new Intent(MainActivity.this,
                        International_events.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_slide_out,
                        R.anim.right_slide_out);
                return true;
            } else if (url.startsWith("market:")) {
                String appPackageName = getPackageName();
                Intent marketIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + appPackageName));
                marketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY
                        | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                startActivity(marketIntent);
                return true;
            } else {

                view.loadUrl(url);
                setProgressBarIndeterminateVisibility(true);
                return true;
            }*/
        }

    }


}
