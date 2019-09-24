package com.mgc.letobox.happy.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.ledong.lib.leto.widget.ClickGuard;
import com.leto.game.base.util.BaseAppUtil;
import com.leto.game.base.util.DensityUtil;
import com.leto.game.base.util.MResource;

/**
 * 问题, 信息, 失败对话框, 统一样式: 标题, 图标, 信息, ok和cancel按钮
 */
@Keep
public class GeneralDialog extends Dialog {
	// views
	private View _cancelButton;
	private View _okButton;
	private TextView _msgLabel;

	// listener
	private OnClickListener _listener;

	public GeneralDialog(@NonNull Context context, String title, String msg) {
		this(context, title, msg, false, null);
	}

	public GeneralDialog(@NonNull Context context, String title, String msg, boolean cancelable) {
		this(context, title, msg, cancelable, null);
	}

	public GeneralDialog(@NonNull Context context, String title, String msg, boolean cancelable, OnClickListener listener) {
		super(context, MResource.getIdByName(context, "R.style.Leto_Dialog_NoFrame"));

		// load content view
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(MResource.getIdByName(context, "R.layout.lebox_general_dialog"), null);

		// init
		_listener = listener;

		// views
		TextView titleLabel = view.findViewById(MResource.getIdByName(context, "R.id.title"));
		_msgLabel = view.findViewById(MResource.getIdByName(context, "R.id.msg"));
		_cancelButton = view.findViewById(MResource.getIdByName(context, "R.id.cancel"));
		_okButton = view.findViewById(MResource.getIdByName(context, "R.id.ok"));

		// title
		titleLabel.setText(title);

		// message
		_msgLabel.setText(msg);

		// cancel button
		if(!cancelable) {
			_cancelButton.setVisibility(View.GONE);
		}
		_cancelButton.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
			@Override
			public boolean onClicked() {
				if(_listener != null) {
					_listener.onClick(GeneralDialog.this, DialogInterface.BUTTON_NEGATIVE);
				}
				dismiss();
				return true;
			}
		});

		// ok button
		_okButton.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
			@Override
			public boolean onClicked() {
				if(_listener != null) {
					_listener.onClick(GeneralDialog.this, DialogInterface.BUTTON_POSITIVE);
				}
				dismiss();
				return true;
			}
		});

		// set content view
		setContentView(view);

		Window window = getWindow();
		WindowManager.LayoutParams windowparams = window.getAttributes();
		windowparams.width = BaseAppUtil.getDeviceWidth(context)-2* DensityUtil.dip2px(context, 27);
	}

	public void setOkButtonText(String text) {
		if(_okButton instanceof Button) {
			((Button)_okButton).setText(text);
		} else if(_okButton instanceof TextView) {
			((TextView)_okButton).setText(text);
		}
	}

	@Override
	public void onBackPressed() {
		if(_listener != null) {
			_listener.onClick(GeneralDialog.this, DialogInterface.BUTTON_NEGATIVE);
		}
		super.onBackPressed();
	}
}
