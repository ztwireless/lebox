package com.mgc.letobox.happy.find.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.find.bean.GameBean;


public class GameBaseView extends LinearLayout implements IGameLayout {
    private static final String TAG = GameBaseView.class.getSimpleName();

    ImageView ivGameIcon;
    TextView tvGameName;
    TextView tvGameScore;
    LinearLayout llGameTag;
    TextView tvType;
    TextView tvSupport;
    TextView tvFavoriteNum;

    Context mContext;

    GameBean gameBean;

    public GameBaseView(Context context) {
        super(context);
        initUI();

    }

    public GameBaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
        mContext = context;
    }

    public GameBaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI();
        mContext = context;
    }

    private void initUI() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_game_base, this, true);

        // find views
        ivGameIcon = view.findViewById(R.id.iv_game_icon);
        tvGameName = view.findViewById(R.id.tv_game_name);
        tvGameScore = view.findViewById(R.id.iv_game_score);
        llGameTag = view.findViewById(R.id.ll_game_tag);
        tvType = view.findViewById(R.id.tv_type);
        tvSupport = view.findViewById(R.id.tv_support);
        tvFavoriteNum = view.findViewById(R.id.tv_favorite_num);
    }

    @Override
    public void setGameBean(GameBean gameBean) {
        this.gameBean = gameBean;
        if(TextUtils.isEmpty(gameBean.getType())){
            tvType.setText("未知");
        }else{
            tvType.setText(gameBean.getType());
        }
        tvSupport.setText(gameBean.getCompany_name());
        tvFavoriteNum.setText(""+gameBean.getCollect_count());
        if (0.0f == gameBean.getScore()) {
            tvGameScore.setText("暂无评论");
            tvGameScore.setTextSize(8);
        } else {
            tvGameScore.setText("" + gameBean.getScore());
            tvGameScore.setTextSize(18);
        }


        //设置图片
        Glide.with(getContext()).load(gameBean.getIcon()).into(ivGameIcon);

        tvGameName.setText(gameBean.getGamename());
        //tvGameType.setText(gameBean.getOneword());

        //setGameTag();

    }

    @Override
    public GameBean getGameBean() {
        return null;
    }


    public void setGameTag() {
        if(TextUtils.isEmpty(gameBean.getType().trim())){
            return;
        }
        llGameTag.setVisibility(VISIBLE);
        llGameTag.removeAllViews();

        String[] tags = gameBean.getType().trim().split(",");
        for (String tag: tags ){
            TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.add_list_game_tag, llGameTag, false);
            textView.setText(tag);
            LayoutParams params = (LayoutParams) textView.getLayoutParams();
            params.leftMargin = 5;
            params.rightMargin = 5;
            textView.setLayoutParams(params);

            llGameTag.addView(textView);
        }
    }
}
