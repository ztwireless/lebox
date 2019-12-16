package com.mgc.letobox.happy.me.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ledong.lib.leto.Leto;
import com.ledong.lib.leto.mgc.MGCConst;
import com.ledong.lib.leto.mgc.bean.AddCoinResultBean;
import com.ledong.lib.leto.mgc.model.MGCSharedModel;
import com.ledong.lib.leto.mgc.thirdparty.IMintage;
import com.ledong.lib.leto.mgc.thirdparty.MintageRequest;
import com.ledong.lib.leto.mgc.thirdparty.MintageResult;
import com.ledong.lib.leto.mgc.util.MGCApiUtil;
import com.ledong.lib.leto.mgc.util.MGCDialogUtil;
import com.ledong.lib.leto.utils.DeviceInfo;
import com.ledong.lib.leto.widget.ClickGuard;
import com.leto.game.base.bean.LetoError;
import com.leto.game.base.event.DataRefreshEvent;
import com.leto.game.base.http.HttpCallbackDecode;
import com.leto.game.base.util.DialogUtil;
import com.leto.game.base.util.MResource;
import com.leto.game.base.util.ToastUtil;
import com.mgc.letobox.happy.me.bean.TaskResultBean;

import org.greenrobot.eventbus.EventBus;

/**
 * 问题, 信息, 失败对话框, 统一样式: 标题, 图标, 信息, ok和cancel按钮
 */
@Keep
public class TaskCoinDialog extends Dialog {
	public interface GameEndCoinDialogListener {
		/**
		 * 退出时调用
		 * @param video true表示用户看了视频
		 * @param coinGot 用户最后获得的金币数
		 */
		void onExit(boolean video, int coinGot);
	}

	// views
	private View _okView;
	private TextView _taskLabel;
	private TextView _titleLabel;
	private TextView _coinLabel;

	// when done, onClick is called
	private GameEndCoinDialogListener _doneListener;

	private boolean _coinAdded;

	// strings
	private String _loading;
	private String _leto_mgc_video_add_coin_failed;
	private String _leto_mgc_video_coin_got_ok;


	TaskResultBean _taskBean;

	public TaskCoinDialog(@NonNull Context context, String title, TaskResultBean taskBean, GameEndCoinDialogListener doneListener) {
		super(context, MResource.getIdByName(context, "R.style.LetoCustomDialog"));

		// init
		_doneListener = doneListener;

		_taskBean = taskBean;

		// load content view
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(MResource.getIdByName(context, "R.layout.leto_mgc_dialog_game_task_coin"), null);

		// views
		_titleLabel = view.findViewById(MResource.getIdByName(context, "R.id.title"));
		_taskLabel = view.findViewById(MResource.getIdByName(context, "R.id.task_desc"));
		_okView = view.findViewById(MResource.getIdByName(context, "R.id.task_ok"));

		_coinLabel = view.findViewById(MResource.getIdByName(context, "R.id.coin"));

		_titleLabel.setText(title);
		_taskLabel.setText(taskBean.getTask_desc());
		// add coin,
		_coinLabel.setText("+"+taskBean.getAward_coins());

		// get strings
		_loading = context.getString(MResource.getIdByName(context, "R.string.loading"));
		_leto_mgc_video_add_coin_failed = context.getString(MResource.getIdByName(context, "R.string.leto_mgc_video_add_coin_failed"));
		_leto_mgc_video_coin_got_ok = context.getString(MResource.getIdByName(context, "R.string.leto_mgc_video_coin_got_ok"));

		// auxiliary button
		_okView.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
			@Override
			public boolean onClicked() {
				IMintage mintageInterface = Leto.getInstance().getThirdpartyMintage();
				if(MGCSharedModel.thirdpartyCoin && mintageInterface != null) {
					addThirdpartyCoin(_taskBean.getAward_coins());
				} else {
					addCoin("", _taskBean.getAward_coins());
				}

				return true;
			}
		});

		// set content view
		setContentView(view);
		setCanceledOnTouchOutside(false);

		//通过window来设置位置、高宽
		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		window.setGravity(Gravity.CENTER);
		params.width = DeviceInfo.getWidth(context);

		// set flag
		_coinAdded = true;


	}

	@Override
	public void onBackPressed() {
		if(_coinAdded) {
			exit();
		} else {
			// ignore back if user doesn't get coin yet
		}
	}

	public void setTitle(String t) {
		_titleLabel.setText(t);
	}


	private void exit() {
		if(_doneListener != null) {
			_doneListener.onExit(false, _taskBean.getAward_coins());
		}
		dismiss();
	}


	private void exitWithMsg(String msg) {
		DialogUtil.dismissDialog();
		ToastUtil.s(getContext(), msg);
		exit();
	}

	/**
	 * 开始第三方发币流程
	 */
	private void addThirdpartyCoin(int coin) {
		// show loading
		Context ctx = getContext();
		DialogUtil.showDialog(ctx, _loading);

		// request mintage
		IMintage mintageInterface = Leto.getInstance().getThirdpartyMintage();
		if(mintageInterface != null && coin > 0) {
			mintageInterface.requestMintage(ctx, new MintageRequest(ctx, "", coin) {
				@Override
				public void notifyMintageResult(MintageResult result) {
					if(result.getErrCode() == 0) {
						exitWithMsg(_leto_mgc_video_coin_got_ok);
					} else {
						onCoinAddFailed(_leto_mgc_video_add_coin_failed);
					}
				}
			});
		} else {
			onCoinAddFailed(_leto_mgc_video_add_coin_failed);
		}
	}

	private void onCoinAddFailed(String msg) {
		exitWithMsg(msg);
	}


	private int getScene() {
		return  MGCConst.ADD_COIN_BY_NEWPLAYER_TASK;
	}


	private void addCoin(String token, int coin) {
		final Context ctx = getContext();
		MGCApiUtil.addCoin(ctx, "", coin, token, getScene(), _taskBean.getChannel_task_id(),  new HttpCallbackDecode<AddCoinResultBean>(ctx, null) {
			@Override
			public void onDataSuccess(AddCoinResultBean data) {
				dismiss();
				EventBus.getDefault().post(new DataRefreshEvent());
			}

			@Override
			public void onFailure(String code, String msg) {
				super.onFailure(code, msg);
				if (!TextUtils.isEmpty(code) && code.equalsIgnoreCase(LetoError.MGC_COIN_LIMIT)) {
					DialogUtil.dismissDialog();
					MGCDialogUtil.showCoinLimit(ctx, new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							exit();
						}
					});
					return;
				}
				onCoinAddFailed(_leto_mgc_video_add_coin_failed);
			}
		});
	}

}
