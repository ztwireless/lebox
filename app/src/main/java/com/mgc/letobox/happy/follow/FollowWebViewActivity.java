package com.mgc.letobox.happy.follow;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgc.leto.game.base.main.BaseActivity;
import com.mgc.leto.game.base.trace.LetoTrace;
import com.mgc.leto.game.base.utils.ColorUtil;
import com.mgc.leto.game.base.utils.DialogUtil;
import com.mgc.leto.game.base.utils.IntentConstant;
import com.mgc.leto.game.base.utils.MResource;
import com.mgc.leto.game.base.utils.StatusBarUtil;
import com.mgc.leto.game.base.widget.ClickGuard;

public class FollowWebViewActivity extends BaseActivity {

    private static final String TAG = FollowWebViewActivity.class.getSimpleName();

    // views
    private ImageView _backBtn;
    private ImageView _closeBtn;
    private TextView _titleLabel;
    private TextView _titleRefresh;
    private TextView _titleService;
    private View _rlTitleView;

    Dialog _loadingDialog;


    WebView _webview;


    String _url;
    String _title;

    public static void start(Context context, String title, String url) {
        if (null != context) {
            Intent intent = new Intent(context, FollowWebViewActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra(IntentConstant.TITLE_NAME, title);
            intent.putExtra(IntentConstant.URL, url);

            context.startActivity(intent);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarUtil.setStatusBarColor(this, ColorUtil.parseColor("#ffffff"));
        }

        // set content view
        setContentView(MResource.getIdByName(this, "R.layout.activity_follow_web"));

        Intent intent = getIntent();
        if (intent != null) {
            _title = intent.getStringExtra(IntentConstant.TITLE_NAME);
            _url = intent.getStringExtra(IntentConstant.URL);
        }

        // find views
        _backBtn = findViewById(MResource.getIdByName(this, "R.id.iv_back"));
        _closeBtn = findViewById(MResource.getIdByName(this, "R.id.iv_close"));
        _titleRefresh = findViewById(MResource.getIdByName(this, "R.id.refresh"));
        _titleService = findViewById(MResource.getIdByName(this, "R.id.service"));
        _titleLabel = findViewById(MResource.getIdByName(this, "R.id.tv_title"));
        _webview = findViewById(MResource.getIdByName(this, "R.id.webview"));

        webviewInit();

        // back click
        _backBtn.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {
                if (_webview != null) {
                    if (_webview.canGoBack()) {
                        _webview.goBack();
                    } else {
                        finish();
                    }
                } else {
                    finish();
                }
                return true;
            }
        });

        _closeBtn.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {
                finish();
                return true;
            }
        });

        _titleRefresh.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {

                if (_webview != null) {
                    _webview.reload();
                }

                return true;
            }
        });

        _titleService.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {

                FollowUtil.startCommonQuestion(FollowWebViewActivity.this);

                return true;
            }
        });

        if(!TextUtils.isEmpty(_title) && _title.equalsIgnoreCase("常见问题")){
            _titleService.setVisibility(View.GONE);
        }


        // title
        if (!TextUtils.isEmpty(_title)) {
            _titleLabel.setText(_title);
        }

        if (TextUtils.isEmpty(_url)) {
            finish();
            return;
        }

        _webview.loadUrl(_url);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (_loadingDialog != null && _loadingDialog.isShowing()) {
            _loadingDialog.dismiss();
        }
        _loadingDialog = null;
    }

    private void webviewInit() {
        _webview.getSettings().setLoadsImagesAutomatically(true);
        _webview.getSettings().setDefaultTextEncodingName("UTF-8");
        _webview.getSettings().setAllowFileAccess(true);
        _webview.getSettings().setAllowFileAccessFromFileURLs(true);
        _webview.getSettings().setAllowUniversalAccessFromFileURLs(true);
        _webview.getSettings().setBuiltInZoomControls(true);
        _webview.getSettings().setDisplayZoomControls(false);
        _webview.getSettings().setSupportMultipleWindows(false);
        _webview.getSettings().setAppCacheEnabled(true);
        _webview.getSettings().setDomStorageEnabled(true);
        _webview.getSettings().setDatabaseEnabled(true);
        _webview.getSettings().setJavaScriptEnabled(true);
        _webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        _webview.getSettings().setAppCacheMaxSize(Long.MAX_VALUE);
        _webview.getSettings().setGeolocationEnabled(true);
        _webview.getSettings().setUseWideViewPort(true);

        //浮点里面的是没有回调的
//        commonJsForWeb = new MgcJsForWeb(getActivity(), "", this);
//        commonJsForWeb.setAppId(mAppId);
//        commonJsForWeb.setFromAppId(mSrcAppId);
//        _webview.addJavascriptInterface(commonJsForWeb, "mgc");
//      _webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        _webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                LetoTrace.e("Webview onPageStarted", "url=" + url);
                if (_loadingDialog != null && _loadingDialog.isShowing()) {
                    _loadingDialog.dismiss();
                    _loadingDialog = null;
                }
                _loadingDialog = DialogUtil.showDialog(FollowWebViewActivity.this, true, getString(MResource.getIdByName(FollowWebViewActivity.this, "R.string.leto_loading")));
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LetoTrace.e(TAG, "url=" + url);
                return false;

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                LetoTrace.e("Webview onPageFinished", "url=" + url);

                DialogUtil.dismissDialog(FollowWebViewActivity.this);
            }

            @Override
            public void onFormResubmission(WebView view, Message dontResend, Message resend) {
                super.onFormResubmission(view, dontResend, resend);
                resend.sendToTarget();
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                return super.shouldInterceptRequest(view, url);
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                WebResourceResponse response;

                return super.shouldInterceptRequest(view, request);
            }
        });
        _webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
//                if (!TextUtils.isEmpty(title)) {
//                    _titleLabel.setText(title);
//                } else {
//                    if (!TextUtils.isEmpty(FollowWebViewActivity.this._title)) {
//                        _titleLabel.setText(FollowWebViewActivity.this._title);
//                    }
//                }
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

        });
    }

}
