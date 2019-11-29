package com.mgc.letobox.happy.dialog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ledong.lib.leto.config.AppConfig;
import com.ledong.lib.leto.main.WebViewActivity;
import com.ledong.lib.leto.trace.LetoTrace;
import com.ledong.lib.leto.widget.ClickGuard;
import com.ledong.lib.leto.widget.ModalDialog;
import com.leto.game.base.db.LoginControl;
import com.leto.game.base.util.IntentConstant;
import com.leto.game.base.util.MResource;

@Keep
public class PrivacyWebDialog extends Dialog {
	// views
	private TextView _okButton;
	private View _cancelButton;
	private View _closeButton;
	private ImageView _titleView;
	private WebView _webView;

	// listener
	private OnClickListener _listener;

	/**
	 * 显示隐私协议对话框
	 * @param act activity
	 * @param disagreeable true表示显示不同意按钮, false表示只有确定按钮
	 */
	public static void show(final Activity act, String content, boolean disagreeable) {
		try {
			final PrivacyWebDialog privacyDialog = new PrivacyWebDialog(act, "温馨提示", content);
			privacyDialog.setOnClickListener(new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface d, int which) {
					if (which == DialogInterface.BUTTON_POSITIVE) {
						LoginControl.setPrivateShowStatus(act, false);
					} else if (which == DialogInterface.BUTTON_NEGATIVE) {
						ModalDialog dialog = new ModalDialog(act);
						dialog.setMessage("您需要同意《 用户条款&隐私协议 》才能继续使用我们的产品及服务");
						dialog.setLeftButton("退出应用", new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								if (privacyDialog != null) {
									privacyDialog.dismiss();
								}
								act.finish();
							}
						});
						dialog.setRightButton("返回", new View.OnClickListener() {
							@Override
							public void onClick(View v) {
							}
						});
						dialog.setMessageTextColor("#666666");
						dialog.setMessageTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
						dialog.setLeftButtonTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
						dialog.setRightButtonTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
						dialog.setLeftButtonTextColor("#999999");
						dialog.setRightButtonTextColor("#FF3D9AF0");
						dialog.show();
					}
				}
			});
			if(!disagreeable) {
				privacyDialog.setNegativeButtonVisible(false);
				privacyDialog.setPositiveButtonTitle("确定");
			}
			privacyDialog.show();
		} catch (Throwable e) {
		}
	}

	public PrivacyWebDialog(@NonNull final Context context, String title, String url) {
		super(context, MResource.getIdByName(context, "R.style.Leto_Dialog_NoFrame"));

		// load content view
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(MResource.getIdByName(context, "R.layout.leto_mgc_dialog_privacy"), null);

		// views
		TextView titleLabel = view.findViewById(MResource.getIdByName(context, "R.id.title"));
		_webView = view.findViewById(MResource.getIdByName(context, "R.id.web"));
		_titleView = view.findViewById(MResource.getIdByName(context, "R.id.titleView"));
		_okButton = view.findViewById(MResource.getIdByName(context, "R.id.ok"));
		_cancelButton = view.findViewById(MResource.getIdByName(context, "R.id.cancel"));
		_closeButton = view.findViewById(MResource.getIdByName(context, "R.id.close"));

		_webView.getSettings().setTextZoom(80);
		_webView.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
		_webView.getSettings().setDefaultTextEncodingName("utf-8");

		// title
		titleLabel.setText(title);

		// web view
		if(url.startsWith("http")) {
			_webView.loadUrl(url);
		} else {
			_webView.loadDataWithBaseURL(null, url, "text/html", "utf-8", null);
		}

		_webView.setWebViewClient(new WebViewClient(){
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				LetoTrace.e("Webview onPageStarted", "url=" + url);

			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.startsWith("http") || url.startsWith("https") || url.startsWith("ftp")) {
					WebViewActivity.start(context, "用户条款&隐私政策", url, 4, IntentConstant.REQUEST_TYPE_GET, AppConfig.ORIENTATION_PORTRAIT, false);

//					LetoWebContainerActivity.start(context, url, AppConfig.ORIENTATION_PORTRAIT);

					return true;
				} else {
					try {
						Intent intent = new Intent();
						intent.setAction(Intent.ACTION_VIEW);
						intent.setData(Uri.parse(url));
						view.getContext().startActivity(intent);
					} catch (ActivityNotFoundException e) {
						Toast.makeText(view.getContext(), "手机还没有安装支持打开此网页的应用！", Toast.LENGTH_SHORT).show();
					}

					return true;
				}
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);

			}

			@Override
			public WebResourceResponse shouldInterceptRequest(WebView view, String url) {

				return super.shouldInterceptRequest(view, url);
			}

			@TargetApi(Build.VERSION_CODES.LOLLIPOP)
			@Override
			public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
				String url = request.getUrl().toString();
				LetoTrace.d("InterceptRequest", String.format("url=%s", url));

				return super.shouldInterceptRequest(view, request);
			}


		});

		_cancelButton.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
			@Override
			public boolean onClicked() {
				if(_listener != null) {
					_listener.onClick(PrivacyWebDialog.this, DialogInterface.BUTTON_NEGATIVE);
				}
				return true;
			}
		});
//		// ok button
//		_closeButton.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
//			@Override
//			public boolean onClicked() {
//				if(_listener != null) {
//					_listener.onClick(PrivacyWebDialog.this, DialogInterface.BUTTON_NEGATIVE);
//				}
//				dismiss();
//				return true;
//			}
//		});

		// ok button
		_okButton.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
			@Override
			public boolean onClicked() {
				if(_listener != null) {
					_listener.onClick(PrivacyWebDialog.this, DialogInterface.BUTTON_POSITIVE);
				}
				dismiss();
				return true;
			}
		});

		// set content view
		setContentView(view);

		setCancelable(false);
		setCanceledOnTouchOutside(false);

		Window window = getWindow();
		window.setGravity(Gravity.CENTER);
	}

	public void setOnClickListener(OnClickListener listener){
		_listener = listener;
	}

	public void setNegativeButtonVisible(boolean visible) {
		_cancelButton.setVisibility(visible ? View.VISIBLE : View.GONE);
	}

	public void setPositiveButtonTitle(String title) {
		_okButton.setText(title);
	}
}
