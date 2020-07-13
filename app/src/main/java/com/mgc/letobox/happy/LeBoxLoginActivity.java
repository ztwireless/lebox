package com.mgc.letobox.happy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgc.leto.game.base.MgcAccountManager;
import com.mgc.leto.game.base.bean.LoginResultBean;
import com.mgc.leto.game.base.event.DataRefreshEvent;
import com.mgc.leto.game.base.event.GetCoinEvent;
import com.mgc.leto.game.base.http.HttpCallbackDecode;
import com.mgc.leto.game.base.listener.SyncUserInfoListener;
import com.mgc.leto.game.base.login.LoginManager;
import com.mgc.leto.game.base.mgc.util.MGCApiUtil;
import com.mgc.leto.game.base.utils.ColorUtil;
import com.mgc.leto.game.base.utils.DialogUtil;
import com.mgc.leto.game.base.utils.MResource;
import com.mgc.leto.game.base.utils.StatusBarUtil;
import com.mgc.leto.game.base.utils.ToastUtil;
import com.mgc.leto.game.base.widget.ClickGuard;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

public class LeBoxLoginActivity extends BaseActivity implements UMAuthListener, SyncUserInfoListener {
	// views
	private View _signInWechatBtn;
	private View _signInMobileBtn;
	private TextView _userAgreementLabel;
	private TextView _privacyAgreementLabel;
	private ImageView _backBtn;

	public static void start(Context context) {
		if (null != context) {
			Intent intent = new Intent(context, LeBoxLoginActivity.class);
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
		setContentView(MResource.getIdByName(this, "R.layout.activity_lebox_login"));

		// find views
		_backBtn = findViewById(MResource.getIdByName(this, "R.id.iv_back"));
		_signInWechatBtn = findViewById(MResource.getIdByName(this, "R.id.sign_in_wechat"));
		_signInMobileBtn = findViewById(MResource.getIdByName(this, "R.id.sign_in_mobile"));
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
				com.mgc.letobox.happy.util.DialogUtil.showAgreement(LeBoxLoginActivity.this, "user.html");
				return true;
			}
		});
		_privacyAgreementLabel.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
			@Override
			public boolean onClicked() {
				com.mgc.letobox.happy.util.DialogUtil.showAgreement(LeBoxLoginActivity.this, "privacy.html");
				return true;
			}
		});

		// sign in wechat
		_signInWechatBtn.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
			@Override
			public boolean onClicked() {
				getWechatAuthInfo();
				return true;
			}
		});

		// sign in mobile
		_signInMobileBtn.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
			@Override
			public boolean onClicked() {
				LeBoxMobileLoginActivity.start(LeBoxLoginActivity.this);
				return true;
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onResume() {
		super.onResume();

		// check if signed in, finish self
		if(LoginManager.isSignedIn(this)) {
			EventBus.getDefault().post(new DataRefreshEvent());
			finish();
		}
	}

	private void getWechatAuthInfo() {
		// get weixin info
		UMShareConfig config = new UMShareConfig();
		config.isNeedAuthOnGetUserInfo(true);
		UMShareAPI umShareAPI = UMShareAPI.get(this);
		umShareAPI.setShareConfig(config);
		umShareAPI.getPlatformInfo(LeBoxLoginActivity.this, SHARE_MEDIA.WEIXIN, this);
	}

	@Override
	public void onStart(SHARE_MEDIA share_media) {
	}

	@Override
	public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
		// show loading 
		DialogUtil.showDialog(this, getString(MResource.getIdByName(this, "R.string.leto_loading")));

		// sync account
		int gender =0;
		try {
			gender = Integer.parseInt(map.get("gender"));
		} catch(NumberFormatException e) {
		}

		final int userGender = gender;
		MGCApiUtil.bindWeiXin(LeBoxLoginActivity.this, map, new HttpCallbackDecode(LeBoxLoginActivity.this,null) {
			@Override
			public void onDataSuccess(Object data) {
				MgcAccountManager.syncAccount(LeBoxLoginActivity.this,
						map.get("unionid"),
						"",
						map.get("name"),
						map.get("iconurl"),
						userGender,
						true,
						LeBoxLoginActivity.this
				);
			}
			@Override
			public void onFailure(String code, String message) {
				DialogUtil.dismissDialog();
				ToastUtil.s(LeBoxLoginActivity.this, message);
			}
		});
	}

	@Override
	public void onError(SHARE_MEDIA share_media, int i, Throwable t) {
		ToastUtil.s(LeBoxLoginActivity.this, "失败：" + t.getMessage());
	}

	@Override
	public void onCancel(SHARE_MEDIA share_media, int i) {
		ToastUtil.s(LeBoxLoginActivity.this, "取消了");
	}

	@Override
	public void onSuccess(LoginResultBean data) {
	    EventBus.getDefault().post( new GetCoinEvent());
		DialogUtil.dismissDialog();
		finish();
	}

	@Override
	public void onFail(String code, String message) {
		DialogUtil.dismissDialog();
		ToastUtil.s(LeBoxLoginActivity.this, "账号刷新失败, 请重试");
	}
}
