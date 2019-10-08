package com.mgc.letobox.happy.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.TextView;

import com.leto.game.base.util.BaseAppUtil;
import com.leto.game.base.util.DensityUtil;
import com.leto.game.base.util.MResource;

@Keep
public class WebDialog extends Dialog {
	// views
	private View _okButton;
	private WebView _webView;

	public WebDialog(@NonNull Context context, String title, String url) {
		super(context, MResource.getIdByName(context, "R.style.Leto_Dialog_NoFrame"));

		// load content view
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(MResource.getIdByName(context, "R.layout.lebox_web_dialog"), null);

		// views
		TextView titleLabel = view.findViewById(MResource.getIdByName(context, "R.id.title"));
		_webView = view.findViewById(MResource.getIdByName(context, "R.id.web"));
		_okButton = view.findViewById(MResource.getIdByName(context, "R.id.ok"));

		// title
		titleLabel.setText(title);

		// web view
		if(url.startsWith("http")) {
			_webView.loadUrl(url);
		} else {
			_webView.loadData(url, "text/html", "utf-8");
		}

		// ok button
		_okButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dismiss();
			}
		});

		// set content view
		setContentView(view);

		Window window = getWindow();
		WindowManager.LayoutParams windowparams = window.getAttributes();
		windowparams.width = BaseAppUtil.getDeviceWidth(context);
	}
}
