package com.mgc.letobox.happy.me;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ledong.lib.leto.Leto;
import com.ledong.lib.minigame.bean.ModifyUserInfoResultBean;
import com.ledong.lib.minigame.bean.SetPortraitResultBean;
import com.ledong.lib.minigame.util.ApiUtil;
import com.mgc.leto.game.base.MgcAccountManager;
import com.mgc.leto.game.base.bean.LoginResultBean;
import com.mgc.leto.game.base.db.LoginControl;
import com.mgc.leto.game.base.event.DataRefreshEvent;
import com.mgc.leto.game.base.http.HttpCallbackDecode;
import com.mgc.leto.game.base.listener.SyncUserInfoListener;
import com.mgc.leto.game.base.login.LoginManager;
import com.mgc.leto.game.base.utils.BaseAppUtil;
import com.mgc.leto.game.base.utils.ColorUtil;
import com.mgc.leto.game.base.utils.MResource;
import com.mgc.leto.game.base.utils.StatusBarUtil;
import com.mgc.leto.game.base.utils.ToastUtil;
import com.mgc.leto.game.base.widget.ClickGuard;
import com.mgc.letobox.happy.BaseActivity;
import com.mgc.letobox.happy.LeBoxMobileLoginActivity;
import com.mgc.letobox.happy.dialog.InputDialog;
import com.mgc.letobox.happy.imagepicker.ImagePickerCallback;
import com.mgc.letobox.happy.imagepicker.LetoImagePicker;
import com.mgc.letobox.happy.me.holder.AvatarHolder;
import com.mgc.letobox.happy.me.holder.CommonViewHolder;
import com.mgc.letobox.happy.me.holder.SimpleUserInfoHolder;
import com.mgc.letobox.happy.util.DialogUtil;
import com.mgc.letobox.happy.view.ActionSheet;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AboutMeActivity extends BaseActivity {

	// views
	private ImageView _backBtn;
	private TextView _titleLabel;
	private TextView _appVersionLabel;
	private TextView _letoLabel;

	private TextView _agreementLabel;
	private TextView _provacyLabel;


	public static void start(Context context) {
		if(null != context) {
			Intent intent = new Intent(context, AboutMeActivity.class);
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
		setContentView(MResource.getIdByName(this, "R.layout.activity_about_me"));

		// find views
		_backBtn = findViewById(MResource.getIdByName(this, "R.id.iv_back"));
		_titleLabel = findViewById(MResource.getIdByName(this, "R.id.tv_title"));
		_appVersionLabel = findViewById(MResource.getIdByName(this, "R.id.tv_app_version"));
		_letoLabel = findViewById(MResource.getIdByName(this, "R.id.tv_leto_version"));

		_agreementLabel = findViewById(MResource.getIdByName(this, "R.id.tv_user_agrement"));
		_provacyLabel = findViewById(MResource.getIdByName(this, "R.id.tv_provacy"));

		_appVersionLabel.setText(String.format("V%s", BaseAppUtil.getAppVersionName(this)));
		_letoLabel.setText(String.format("V%s", Leto.getVersion()));


		// back click
		_backBtn.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
			@Override
			public boolean onClicked() {
				finish();
				return true;
			}
		});

		// title
		_titleLabel.setText("关于我们");

		_agreementLabel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogUtil.showAgreement(AboutMeActivity.this, "user.html");
			}
		});


		_provacyLabel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogUtil.showAgreement(AboutMeActivity.this, "privacy.html");
			}
		});


	}


	@Override
	public void onDestroy() {
		super.onDestroy();


	}
}
