package com.mgc.letobox.happy.view;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.OverScroller;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.google.gson.reflect.TypeToken;
import com.leto.game.base.view.MarqueeTextView;
import com.mgc.leto.game.base.http.HttpCallbackDecode;
import com.mgc.leto.game.base.mgc.GameLevelTaskManager;
import com.mgc.leto.game.base.mgc.bean.GameLevelResultBean;
import com.mgc.leto.game.base.mgc.util.MGCApiUtil;
import com.mgc.leto.game.base.trace.LetoTrace;
import com.mgc.leto.game.base.utils.ColorUtil;
import com.mgc.leto.game.base.utils.ToastUtil;
import com.mgc.letobox.happy.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpgradeView extends FrameLayout {
	private static final String TAG = UpgradeView.class.getSimpleName();

	private AnimationDrawable mAnimationDrawable;
	private ImageView mShakeView;

	private TextSwitcher mLevelView;
	private ImageView mStatusView;
	boolean hasReward = false;

	public UpgradeView(@NonNull Context context) {
		this(context, null);

	}

	public UpgradeView(@NonNull Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
		touchSlop = 0;
	}

	private RectF edgeRatio = new RectF();

	public void setEdgeRatio(RectF edgeRatio) {
		edgeRatio.set(edgeRatio);
	}

	public void setEdgeRatio(float left, float top, float right, float bottom) {
		edgeRatio.set(left, top, right, bottom);
	}

	private List<String> _news;
	private int _nextNews;

	private Map<String, Integer> _gamelevel = new HashMap();

	// handler
	private Handler _handler;
	private Runnable _switchMarqueeRunnable = new Runnable() {
		@Override
		public void run() {
			int oldIdx = _nextNews;
			_nextNews++;
			_nextNews %= _news.size();
			if(_nextNews != oldIdx) {
				mLevelView.setVisibility(VISIBLE);
				mLevelView.setText(_news.get(_nextNews));
				_handler.postDelayed(_switchMarqueeRunnable, 2000);
			}
		}
	};


	private ViewSwitcher.ViewFactory _textFactory = new ViewSwitcher.ViewFactory() {
		@Override
		public View makeView() {

			final MarqueeTextView tv = new MarqueeTextView(getContext());
			//设置文字大小
			tv.setTextSize(9);
			TextPaint paint = tv.getPaint();
			paint.setFakeBoldText(true);
			//设置文字 颜色
			tv.setTextColor(ColorUtil.parseColor("#FFF32700"));
			tv.setSingleLine();
			tv.setEllipsize(TextUtils.TruncateAt.END);
			FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			lp.gravity = Gravity.CENTER;
			tv.setLayoutParams(lp);

			return tv;
		}
	};

	public UpgradeView(@NonNull final Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		inflate(context, R.layout.layout_upgrade, this);
		mShakeView = findViewById(R.id.redpack);
		mLevelView = findViewById(R.id.item_level);
		mStatusView = findViewById(R.id.item_status);
		mStatusView.setVisibility(VISIBLE);
		mAnimationDrawable = (AnimationDrawable) mShakeView.getDrawable();

		mLevelView.setVisibility(VISIBLE);
		_handler = new Handler();
		_news = new ArrayList<>();
		//跑马灯效果必须加
		mLevelView.setSelected(true);
		mLevelView.setFactory(_textFactory);
		restartMarquee();

		ViewConfiguration vc = ViewConfiguration.get(context);
		touchSlop = vc.getScaledTouchSlop();
		scroller = new OverScroller(context);
	}

	private PointF initialPoint = new PointF();
	private float touchSlop;
	private boolean isDragging = false;

	public void relocate(int x, int y) {
		setX(x);
		setY(y);
		settleToEdge();
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch(event.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
				initialPoint.set(event.getX(), event.getY());
				isDragging = false;
				break;
			case MotionEvent.ACTION_MOVE:
				float dx = event.getX() - initialPoint.x;
				float dy = event.getY() - initialPoint.y;
				if(Math.abs(dx) > touchSlop || Math.abs(dy) > touchSlop || Math.sqrt(dx * dx + dy + dy) > touchSlop) {
					isDragging = true;
					onDragging(event.getX(), event.getY(), dx, dy);
				}
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				if(!isDragging) {
					performClick();
				} else {
					isDragging = false;
					settleToEdge();
				}
				break;
		}
		return true;
	}

	private OverScroller scroller;

	private void settleToEdge() {
		float finalLeft;
		int screenWidth = getResources().getDisplayMetrics().widthPixels;
		if(getX() < getResources().getDisplayMetrics().widthPixels / 2f) {
			finalLeft = screenWidth * edgeRatio.left;
		} else {
			finalLeft = screenWidth - screenWidth * edgeRatio.right - getWidth();
		}
		scroller.startScroll((int) getX(), (int) getY(), (int) (finalLeft - getX()), 0, 800);
		invalidate();
	}

	private void onDragging(float x, float y, float dx, float dy) {
		this.setX(this.getX() + dx);
		this.setY(this.getY() + dy);
	}

	@Override
	public void computeScroll() {
		super.computeScroll();
		if(scroller.computeScrollOffset()) {
			setX(scroller.getCurrX());
			setY(scroller.getCurrY());
			invalidate();
		}
	}

	public void onDestroy() {
		try {
			if(_handler != null) {
				_handler.removeCallbacks(_switchMarqueeRunnable);
			}
		} catch(Throwable e) {

		}
	}

	public void notifyUpdate(String gameId, Map<String, Integer> gameInfo) {

		if(TextUtils.isEmpty(gameId) || !gameId.equalsIgnoreCase(_gameId) || gameInfo == null) {
			return;
		}

		//更新保存数据
		for(String key : gameInfo.keySet()) {
			if(!_gamelevel.containsKey(key)) {
				LetoTrace.d(TAG, "初始化");
				_gamelevel.put(key, gameInfo.get(key));
			} else {
				LetoTrace.d(TAG, "更新");
				_gamelevel.put(key, gameInfo.get(key));
			}
		}

		updateStatus(hasReward());

	}

	public void updateStatus(boolean isReward) {
		hasReward = isReward;

		if(mAnimationDrawable != null) {
			if(mAnimationDrawable.isRunning()) {
				mAnimationDrawable.stop();
			}
			mShakeView.setImageDrawable(null);
		}
		if(isReward) {
			mShakeView.setImageResource(R.drawable.anim_upgrade_get);
			mStatusView.setVisibility(View.GONE);
			_news.clear();
			_news.add("可领取");
			_nextNews = 0;
			_handler.removeCallbacks(_switchMarqueeRunnable);
			mLevelView.setCurrentText("可领取");
		} else {
			mShakeView.setImageResource(R.drawable.anim_upgrade_unget);
			mStatusView.setVisibility(View.VISIBLE);
			//更新跑马灯文案
			updateLevelText();
		}
		mAnimationDrawable = (AnimationDrawable) mShakeView.getDrawable();
		mAnimationDrawable.start();
	}


	private String _gameId;

	public void setGameId(String gameId) {
		_gameId = gameId;
	}


	public GameLevelResultBean getGameLevelResultBean() {
		List<GameLevelResultBean> resultBeanLists = GameLevelTaskManager.getGameRewardLevel(_gameId, _gamelevel);

		if(resultBeanLists == null || resultBeanLists.size() == 0) {
			return null;
		}
		return resultBeanLists.get(0);

	}

	public GameLevelResultBean.GameLevel getRewardLevel() {
		List<GameLevelResultBean> resultBeanLists = GameLevelTaskManager.getGameRewardLevel(_gameId, _gamelevel);

		if(resultBeanLists == null || resultBeanLists.size() == 0) {
			return null;
		}
		return GameLevelTaskManager.getRewardLevel(resultBeanLists.get(0), _gamelevel.get(resultBeanLists.get(0).level_id));
	}

	public void resetRewardStatus(int levelId) {

		List<GameLevelResultBean> resultBeanLists = GameLevelTaskManager.getGameRewardLevel(_gameId, _gamelevel);
		if(resultBeanLists == null || resultBeanLists.size() == 0) {
			return;
		}

		GameLevelTaskManager.setGameLevelRewarded(_gameId, resultBeanLists.get(0), levelId);

		//如果没有奖励，则更换动画和文案
		if(!hasReward()) {
			updateStatus(false);
		}
	}


	public boolean hasReward() {

		List<GameLevelResultBean> resultBeanLists = GameLevelTaskManager.getGameRewardLevel(_gameId, _gamelevel);
		if(resultBeanLists == null || resultBeanLists.size() == 0) {
			hasReward = false;
		} else {
			hasReward = true;
		}

		return hasReward;
	}

	private void restartMarquee() {
		_nextNews = 0;
		if(_news != null && _nextNews < _news.size()) {
			mLevelView.setCurrentText(_news.get(_nextNews));
			_handler.postDelayed(_switchMarqueeRunnable, 3000);
		}
	}


	public void updateLevelText() {

		List<String> gameInfo = GameLevelTaskManager.getNextRewardLevelInfo(_gameId, _gamelevel);

		_news.clear();
		_news.addAll(gameInfo);
		_handler.removeCallbacks(_switchMarqueeRunnable);
		restartMarquee();
		return;

	}

	public void getGameUpgradeSetting(Context ctx, String gameId) {

		MGCApiUtil.getGameUpgradeSettings(ctx, gameId, new HttpCallbackDecode<List<GameLevelResultBean>>(ctx, null, new TypeToken<List<GameLevelResultBean>>() {
		}.getType()) {
			@Override
			public void onDataSuccess(List<GameLevelResultBean> data) {
				try {
					if(data != null) {
						GameLevelTaskManager.addGameTask(gameId, data);
						if(_gamelevel != null && _gamelevel.size() > 0) {
							notifyUpdate(gameId, _gamelevel);
						}
					} else {
						LetoTrace.w(TAG, "游戏升级配置获取失败");
					}
				} catch(Throwable e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(String code, String msg) {
				ToastUtil.s(ctx, msg);
			}
		});


	}

}