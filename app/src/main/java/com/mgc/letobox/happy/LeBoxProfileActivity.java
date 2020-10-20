package com.mgc.letobox.happy;

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
import android.widget.Toast;

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

public class LeBoxProfileActivity extends BaseActivity implements ActionSheet.ActionSheetListener, ImagePickerCallback {
	// view type
	private static final int TYPE_AVATAR = 0;
	private static final int TYPE_INFO = 1;

	// request code
	private static final int REQ_CAMERA_ACCESS = 1001;
	private static final int REQ_ALBUM_ACCESS = 1002;

	// views
	private ImageView _backBtn;
	private TextView _titleLabel;
	private RecyclerView _listView;
	private View _signOutBtn;

	// action sheet
	private ActionSheet _actionSheet;

	// image picker
	LetoImagePicker _imagePicker;

	// model
	private LoginResultBean _loginInfo;
	private List<Pair<String, String>> _infos;
	private List<String> _genders = Arrays.asList(
		"未知",
		"男",
		"女"
	);
	private SetPortraitResultBean _setPortraitResult;

	// string
	private String _loading;
	private String _cancel;
	private String _from_camera;
	private String _from_album;
	private String _lebox_set_portrait_failed;

	public static void start(Context context) {
		if(null != context) {
			Intent intent = new Intent(context, LeBoxProfileActivity.class);
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

		// init
		_loginInfo = LoginManager.getUserLoginInfo(this);
		_infos = new ArrayList<>();
		buildModel();
		_imagePicker = LetoImagePicker.getInstance(this);

		// set content view
		setContentView(MResource.getIdByName(this, "R.layout.activity_profile"));

		// find views
		_backBtn = findViewById(MResource.getIdByName(this, "R.id.iv_back"));
		_titleLabel = findViewById(MResource.getIdByName(this, "R.id.tv_title"));
		_listView = findViewById(MResource.getIdByName(this, "R.id.list"));
		_signOutBtn = findViewById(MResource.getIdByName(this, "R.id.sign_out"));

		// load strings
		_loading = getString(MResource.getIdByName(this, "R.string.leto_loading"));
		_cancel = getString(MResource.getIdByName(this, "R.string.leto_cancel"));
		_from_camera = getString(MResource.getIdByName(this, "R.string.from_camera"));
		_from_album = getString(MResource.getIdByName(this, "R.string.from_album"));
		_lebox_set_portrait_failed = getString(MResource.getIdByName(this, "R.string.lebox_set_portrait_failed"));

		// back click
		_backBtn.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
			@Override
			public boolean onClicked() {
				finish();
				return true;
			}
		});

		// sign out click
		_signOutBtn.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
			@Override
			public boolean onClicked() {
				DialogUtil.showConfirmDialog(LeBoxProfileActivity.this, "确定退出登录吗?", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(which == DialogInterface.BUTTON_POSITIVE) {
							// first we need clear local cache to switch a temp account
							_loginInfo = Leto.switchToTempAccount(LeBoxProfileActivity.this);

							// then we sync this temp account
							MgcAccountManager.syncAccount(LeBoxProfileActivity.this, "", _loginInfo.getMobile(), false, new SyncUserInfoListener() {
								@Override
								public void onSuccess(LoginResultBean data) {
									EventBus.getDefault().post(new DataRefreshEvent());
									finish();
								}

								@Override
								public void onFail(String code, String message) {
									ToastUtil.s(LeBoxProfileActivity.this, "退出登录失败");
								}
							});
						}
					}
				});
				return true;
			}
		});

		// title
		_titleLabel.setText("账号管理");

		// setup list
		_listView.setLayoutManager(new LinearLayoutManager(this));
		_listView.setAdapter(new ProfileAdapter());

		// register context menu
		registerForContextMenu(_listView);
	}

	@Override
	public void onBackPressed() {
		if(_actionSheet != null) {
			_actionSheet.dismiss();
		} else {
			super.onBackPressed();
		}
	}

	private void buildModel() {
		_infos.clear();
		_infos.add(Pair.create("我的头像", _loginInfo.getPortrait()));
		_infos.add(Pair.create("我的昵称", _loginInfo.getNickname()));
		_infos.add(Pair.create("我的性别", (_loginInfo.getGender() < 0 || _loginInfo.getGender() >= _genders.size()) ? _genders.get(0) : _genders.get(_loginInfo.getGender())));
		_infos.add(Pair.create("当前版本", String.format("V%s", BaseAppUtil.getAppVersionName(this))));
		_infos.add(Pair.create("leto版本", String.format("V%s", Leto.getVersion())));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		// unregister
		unregisterForContextMenu(_listView);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch(requestCode) {
			case REQ_CAMERA_ACCESS:
				if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					_imagePicker.pickFromCamera("avatar.jpg", 256, 256, true, false, this);
				}
				break;
			case REQ_ALBUM_ACCESS:
				if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					_imagePicker.pickFromAlbum("avatar.jpg", 256, 256, false, this);
				}
				break;
			default:
				super.onRequestPermissionsResult(requestCode, permissions, grantResults);
				break;
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		for(String item : _genders) {
			menu.add(item);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		super.onContextItemSelected(item);

		// save change, no matter success or fail
		int gender = _genders.indexOf(item.getTitle());
		_loginInfo.setGender(gender);
		LoginControl.saveLoginInfo(LeBoxProfileActivity.this, _loginInfo);

		// modify
		doModifyInfo("", gender);

		// return
		return true;
	}

	private void doModifyUserPortrait() {
		ApiUtil.modifyUserInfo(LeBoxProfileActivity.this, "", _setPortraitResult.getPortrait(), _loginInfo.getGender(), new HttpCallbackDecode<ModifyUserInfoResultBean>(this, null) {
			@Override
			public void onDataSuccess(ModifyUserInfoResultBean data) {
				if(data != null) {
					// save to local
					_loginInfo.setPortrait(data.getPortrait());
					LoginControl.saveLoginInfo(LeBoxProfileActivity.this, _loginInfo);

					// reload ui
					buildModel();
					_listView.getAdapter().notifyDataSetChanged();

					// trigger event to let outer activity updated
					EventBus.getDefault().post(new DataRefreshEvent());
				}
				com.mgc.leto.game.base.utils.DialogUtil.dismissDialog();
			}

			@Override
			public void onFailure(String code, String msg) {
				super.onFailure(code, msg);
				com.mgc.leto.game.base.utils.DialogUtil.dismissDialog();
			}
		});
	}

	private void doModifyInfo(String nickname, int gender) {
		ApiUtil.modifyUserInfo(LeBoxProfileActivity.this, nickname, "", gender, new HttpCallbackDecode<ModifyUserInfoResultBean>(this, null) {
			@Override
			public void onDataSuccess(ModifyUserInfoResultBean data) {
				if(data != null) {
					buildModel();
					_listView.getAdapter().notifyDataSetChanged();

					// trigger event to let outer activity updated
					EventBus.getDefault().post(new DataRefreshEvent());
				}
			}
		});
	}

	private void popupGender() {
		openContextMenu(_listView);
	}

	private void showNickInput() {
		DialogUtil.showInputDialog(LeBoxProfileActivity.this, _loginInfo.getNickname(), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(which == DialogInterface.BUTTON_POSITIVE) {
					// save in local
					_loginInfo.setNickname(((InputDialog) dialog).getText());
					LoginControl.saveLoginInfo(LeBoxProfileActivity.this, _loginInfo);

					// modify
					doModifyInfo(_loginInfo.getNickname(), _loginInfo.getGender());
				}
			}
		});
	}

	private void showAvatarPicker() {
		_actionSheet = ActionSheet.createBuilder(this, getSupportFragmentManager())
			.setCancelButtonTitle(_cancel)
			.setOtherButtonTitles(_from_camera, _from_album)
			.setCancelableOnTouchOutside(true)
			.setListener(this)
			.show();
	}

	@Override
	public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
		_actionSheet = null;
	}

	@Override
	public void onOtherButtonClick(ActionSheet actionSheet, int index) {
		switch(index) {
			case 0: // from camera
			{
				List<String> perms = new ArrayList<>();
				if(Build.VERSION.SDK_INT >= 23) {
					if(ContextCompat.checkSelfPermission(this,
						android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
						perms.add(android.Manifest.permission.CAMERA);
					}
					if(ContextCompat.checkSelfPermission(this,
						Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
						perms.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
					}
					if(ContextCompat.checkSelfPermission(this,
						Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
						perms.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
					}
				}
				if(perms.isEmpty()) {
					_imagePicker.pickFromCamera("avatar.jpg", 256, 256, true, false, this);
				} else if(Build.VERSION.SDK_INT >= 23) {
					requestPermissions(perms.toArray(new String[0]), REQ_CAMERA_ACCESS);
				}
				break;
			}
			case 1: // from album
			{
				List<String> perms = new ArrayList<>();
				if(Build.VERSION.SDK_INT >= 23) {
					if(ContextCompat.checkSelfPermission(this,
						Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
						perms.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
					}
					if(ContextCompat.checkSelfPermission(this,
						Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
						perms.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
					}
				}
				if(perms.isEmpty()) {
					_imagePicker.pickFromAlbum("avatar.jpg", 256, 256, false, this);
				} else if(Build.VERSION.SDK_INT >= 23) {
					requestPermissions(perms.toArray(new String[0]), REQ_ALBUM_ACCESS);
				}
				break;
			}
		}
	}

	@Override
	public void onImagePicked(String path) {
		try {
			// load avatar jpg file
			File f = new File(path);
			Uri uri = Uri.fromFile(f);
			int len = (int) f.length();
			byte[] buf = new byte[len];
			ParcelFileDescriptor pfd = getContentResolver().openFileDescriptor(uri, "r");
			InputStream fileStream = new FileInputStream(pfd.getFileDescriptor());
			fileStream.read(buf, 0, buf.length);

			// modify portrait
			doUploadPortrait(buf);
		} catch(IOException e) {
		}
	}

	@Override
	public void onImagePickingCancelled() {

	}

	private void doUploadPortrait(final byte[] buf) {
		com.mgc.leto.game.base.utils.DialogUtil.showDialog(this, _loading);
		ApiUtil.modifyPortrait(LeBoxProfileActivity.this, buf, new HttpCallbackDecode<SetPortraitResultBean>(this, null) {
			@Override
			public void onDataSuccess(SetPortraitResultBean data) {
				if(data != null) {
					// save response
					_setPortraitResult = data;

					// modify user portrait
					doModifyUserPortrait();
				} else {
					hintRetryUploadPortrait(buf);
				}
			}

			@Override
			public void onFailure(String code, String msg) {
				super.onFailure(code, msg);
				hintRetryUploadPortrait(buf);
			}
		});
	}

	private void hintRetryUploadPortrait(final byte[] buf) {
		com.mgc.leto.game.base.utils.DialogUtil.dismissDialog();

		// show dialog
		DialogUtil.showRetryDialog(this, _lebox_set_portrait_failed, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(which == DialogInterface.BUTTON_POSITIVE) {
					com.mgc.leto.game.base.utils.DialogUtil.showDialog(LeBoxProfileActivity.this, _loading);
					doUploadPortrait(buf);
				}
			}
		});
	}

	private class ProfileAdapter extends RecyclerView.Adapter<CommonViewHolder<Pair<String, String>>> {
		@NonNull
		@Override
		public CommonViewHolder<Pair<String, String>> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			switch(viewType) {
				case TYPE_AVATAR:
					return AvatarHolder.create(LeBoxProfileActivity.this, parent);
				default:
					return SimpleUserInfoHolder.create(LeBoxProfileActivity.this, parent);
			}
		}

		@Override
		public int getItemViewType(int position) {
			switch(position) {
				case 0:
					return TYPE_AVATAR;
				default:
					return TYPE_INFO;
			}
		}

		@SuppressLint("RecyclerView")
		@Override
		public void onBindViewHolder(@NonNull CommonViewHolder<Pair<String, String>> holder, final int position) {
			holder.onBind(_infos.get(position), position);
			holder.getItemView().setOnClickListener(new ClickGuard.GuardedOnClickListener() {
				@Override
				public boolean onClicked() {
					switch(position) {
						case 0:
							showAvatarPicker();
							break;
						case 1:
							showNickInput();
							break;
						case 2:
							popupGender();
							break;
					}
					return true;
				}
			});
		}

		@Override
		public int getItemCount() {
			return _infos.size();
		}
	}
}
