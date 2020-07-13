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
import android.widget.EditText;
import android.widget.TextView;

import com.mgc.leto.game.base.utils.BaseAppUtil;
import com.mgc.leto.game.base.utils.DensityUtil;
import com.mgc.leto.game.base.utils.MResource;
import com.mgc.leto.game.base.widget.ClickGuard;

/**
 * 输入对话框
 */
@Keep
public class InputDialog extends Dialog {
	// views
	private View _cancelButton;
	private View _okButton;
	private EditText _inputText;

	// listener
	private OnClickListener _listener;

	public InputDialog(@NonNull Context context, String title, String msg) {
		this(context, title, msg, false, null);
	}

	public InputDialog(@NonNull Context context, String title, String msg, boolean cancelable) {
		this(context, title, msg, cancelable, null);
	}

	public InputDialog(@NonNull Context context, String title, String msg, boolean cancelable, OnClickListener listener) {
		super(context, MResource.getIdByName(context, "R.style.Leto_Dialog_NoFrame"));

		// load content view
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(MResource.getIdByName(context, "R.layout.lebox_input_dialog"), null);

		// init
		_listener = listener;

		// views
		TextView titleLabel = view.findViewById(MResource.getIdByName(context, "R.id.title"));
		_inputText = view.findViewById(MResource.getIdByName(context, "R.id.input"));
		_cancelButton = view.findViewById(MResource.getIdByName(context, "R.id.cancel"));
		_okButton = view.findViewById(MResource.getIdByName(context, "R.id.ok"));

		// title
		titleLabel.setText(title);

		// pre-text
		_inputText.setText(msg);
		_inputText.setSelection(msg.length());
		_inputText.setFocusable(true);
		_inputText.setFocusableInTouchMode(true);
		_inputText.requestFocus();
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

		// cancel button
		if(!cancelable) {
			_cancelButton.setVisibility(View.GONE);
		}
		_cancelButton.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
			@Override
			public boolean onClicked() {
				if(_listener != null) {
					_listener.onClick(InputDialog.this, DialogInterface.BUTTON_NEGATIVE);
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
					_listener.onClick(InputDialog.this, DialogInterface.BUTTON_POSITIVE);
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

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
	}

	public String getText() {
		return _inputText.getEditableText().toString();
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
			_listener.onClick(InputDialog.this, DialogInterface.BUTTON_NEGATIVE);
		}
		super.onBackPressed();
	}
}
