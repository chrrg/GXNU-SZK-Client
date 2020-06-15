package com.webapps.studyplatforn;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
public class MainActivity extends Activity{
    public WebView webview;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.WHITE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_main);
        webview = findViewById(R.id.webView);//赋值webview
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);// 打开本地缓存提供JS调用,至关重要
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);
        String appCachePath = getApplication().getCacheDir().getAbsolutePath();
        webSettings.setAppCachePath(appCachePath);
        webSettings.setDatabaseEnabled(true);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                if (handler != null) handler.proceed();//忽略证书的错误继续加载页面内容，不会变成空白页面
            }
            public void onPageFinished(final WebView view, String url) {
                super.onPageFinished(view, url);
                //加载完成
                //Toast.makeText(MainActivity.this,"网络不稳定",Toast.LENGTH_SHORT).show();
                String js;
                js="\"use strict\";!function(){try{if(window.is_VUEBind)return;window.is_VUEBind=!0,window.begin=!1;var e=function(e){return\"A\"==e?0:\"B\"==e?1:\"C\"==e?2:\"D\"==e?3:\"E\"==e?4:\"F\"==e&&5},n=function(){return s._route.matched[0].instances.default.currentIndex},t=function(){s._route.matched[0].instances.default.form[n()].answer=[]};window.getAuto=function(){t();for(var r=s._route.matched[0].instances.default.questions[n()].modelAnswer,c=0;c<r.length;c++)!1!==e(r[c])&&document.getElementsByClassName(\"answer-list van-checkbox-group\")[0].children[e(r[c])].click()};var r=setInterval(function(){try{s._route.matched[0].instances.default.questions[0].modelAnswer&&(begin=!0,clearInterval(r))}catch(e){}},1e3)}catch(e){}}();";
                try {
                    js = URLEncoder.encode(js, "UTF-8").replaceAll("\\+","%20");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                webview.loadUrl("javascript: "+js);
            }
        });
        String ua = webview.getSettings().getUserAgentString();//原来获取的UA
        webview.getSettings().setUserAgentString(ua+ " agentweb/4.0.2  UCBrowser/11.6.4.950");
        webview.loadUrl("http://sizheng.gxqkcm.com/app/");//显示远程网页
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webview.canGoBack()) {
                webview.goBack();   //后退
                return true;    //已处理
            } else {
                if (isTaskRoot()) {
                    moveTaskToBack(false);
                    return true;
                } else {
                    return super.onKeyDown(keyCode, event);
                }
            }
        }else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            webview.loadUrl("javascript: getAuto()");
            return true;
        }
        return false;
    }

}
