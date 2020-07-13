package com.mgc.letobox.happy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgc.leto.game.base.LetoComponent;
import com.mgc.leto.game.base.LetoEvents;
import com.mgc.leto.game.base.bean.LoginResultBean;
import com.mgc.leto.game.base.login.LoginInteract;
import com.mgc.leto.game.base.login.view.MgcLoginView;
import com.mgc.leto.game.base.mgc.thirdparty.ResetIDCardRequest;
import com.mgc.leto.game.base.mgc.thirdparty.ThirdpartyResult;
import com.mgc.leto.game.base.utils.ColorUtil;
import com.mgc.leto.game.base.utils.MResource;
import com.mgc.leto.game.base.utils.StatusBarUtil;
import com.mgc.leto.game.base.utils.ToastUtil;
import com.mgc.leto.game.base.widget.ClickGuard;
import com.mgc.letobox.happy.util.DialogUtil;

public class LeBoxMobileLoginActivity extends BaseActivity {
	private static final String TAG = LeBoxMobileLoginActivity.class.getSimpleName();
	// views
	private ImageView _backBtn;
	private FrameLayout _content;
	private TextView _userAgreementLabel;
	private TextView _privacyAgreementLabel;

	public static void start(Context context) {
		if(null != context) {
			Intent intent = new Intent(context, LeBoxMobileLoginActivity.class);
			context.startActivity(intent);
		}
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set status bar color
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			StatusBarUtil.setStatusBarColor(this, ColorUtil.parseColor("#ffffff"));
		}

		// set content view
		setContentView(MResource.getIdByName(this, "R.layout.activity_lebox_mobile_login"));

		// find views
		_backBtn = findViewById(MResource.getIdByName(this, "R.id.iv_back"));
		_content = findViewById(MResource.getIdByName(this, "R.id.content"));
		_userAgreementLabel = findViewById(MResource.getIdByName(this, "R.id.user_agreement"));
		_privacyAgreementLabel = findViewById(MResource.getIdByName(this, "R.id.privacy_agreement"));

		// back click
		_backBtn.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
			@Override
			public boolean onClicked() {
				finish();
				return true;
			}
		});

		// agreement label underline & click
		_userAgreementLabel.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		_userAgreementLabel.getPaint().setAntiAlias(true);
		_privacyAgreementLabel.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		_privacyAgreementLabel.getPaint().setAntiAlias(true);
		_userAgreementLabel.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
			@Override
			public boolean onClicked() {
				DialogUtil.showAgreement(LeBoxMobileLoginActivity.this, "user.html");
				return true;
			}
		});
		_privacyAgreementLabel.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
			@Override
			public boolean onClicked() {
				DialogUtil.showAgreement(LeBoxMobileLoginActivity.this, "privacy.html");
				return true;
			}
		});

		// add mgc login view
		MgcLoginView loginView = new MgcLoginView(this, MResource.getIdByName(this, "R.layout.lebox_mobile_login_view"));
		_content.addView(loginView);

		loginView.setLogListener(new LoginInteract.LoginListener() {
			@Override
			public void onSuccess(LoginResultBean data) {
				//防沉迷
				if(LetoComponent.supportFcm() && LetoEvents.getResetIDCardListener() != null) {
					ResetIDCardRequest request = new ResetIDCardRequest() {
						@Override
						public void notifyResetIDCardResult(ThirdpartyResult result) {
							ToastUtil.s(LeBoxMobileLoginActivity.this, "登录成功");
							finish();
						}
					};
					request.setScene(1); //设置登陆场景
					LetoEvents.getResetIDCardListener().notify(LeBoxMobileLoginActivity.this, request);

				} else {
					ToastUtil.s(LeBoxMobileLoginActivity.this, "登录成功");
					finish();
				}
			}

			@Override
			public void onFail(String code, String message) {
				ToastUtil.s(LeBoxMobileLoginActivity.this, message);
			}

			@Override
			public void onFinish() {
				dismissLoading();
			}
		});
	}
}
