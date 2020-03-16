package com.mgc.letobox.happy.follow;

import android.content.Context;
import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ledong.lib.leto.widget.ClickGuard;
import com.leto.game.base.ad.bean.mgc.MgcAdBean;
import com.leto.game.base.ad.net.IAdCallback;
import com.leto.game.base.http.HttpCallbackDecode;
import com.leto.game.base.util.ColorUtil;
import com.leto.game.base.util.MResource;
import com.leto.game.base.util.StatusBarUtil;
import com.leto.game.base.util.ToastUtil;
import com.leto.game.base.view.tablayout.CommonTabLayout;
import com.leto.game.base.view.tablayout.entity.TabEntity;
import com.leto.game.base.view.tablayout.listener.CustomTabEntity;
import com.leto.game.base.view.tablayout.listener.OnTabSelectListener;
import com.mgc.letobox.happy.BaseActivity;
import com.mgc.letobox.happy.find.ui.HeaderViewPagerFragment;
import com.mgc.letobox.happy.find.view.ScrollBottomView;

import java.util.ArrayList;
import java.util.List;

public class FollowInviteCodeActivity extends BaseActivity {

    // views
    private ImageView _backBtn;
    private TextView _titleLabel;
    private TextView _titleRightLabel;
    private EditText _codeEt;
    private Button _okBtn;


    public static void start(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, FollowInviteCodeActivity.class);
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarUtil.setStatusBarColor(this, ColorUtil.parseColor("#ffffff"));
        }

        // set content view
        setContentView(MResource.getIdByName(this, "R.layout.activity_follow_invite_code"));

        // find views
        _backBtn = findViewById(MResource.getIdByName(this, "R.id.iv_back"));
        _titleLabel = findViewById(MResource.getIdByName(this, "R.id.tv_title"));
        _titleRightLabel = findViewById(MResource.getIdByName(this, "R.id.ceate_circle"));
        _codeEt = findViewById(MResource.getIdByName(this, "R.id.et_code"));
        _okBtn = findViewById(MResource.getIdByName(this, "R.id.btn_ok"));

        // back click
        _backBtn.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {
                finish();
                return true;
            }
        });

        _okBtn.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {

                String code =_codeEt.getText().toString();

                if(TextUtils.isEmpty(code)){
                    ToastUtil.s(FollowInviteCodeActivity.this, "请输入邀请码");
                    return true;
                }

                doFollow(code);


                return true;
            }
        });

        _titleRightLabel.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {

                FollowUtil.startCommonQuestion(FollowInviteCodeActivity.this);

                return true;
            }
        });

        // title
        _titleLabel.setText("输入邀请码");

        _titleRightLabel.setText("常见问题");


    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void doFollow(String code){

        FollowUtil.follow(FollowInviteCodeActivity.this, code, new HttpCallbackDecode<Object>(FollowInviteCodeActivity.this, null) {
            @Override
            public void onDataSuccess(Object data) {

                ToastUtil.s(FollowInviteCodeActivity.this, "绑定成功");

                finish();
            }

            @Override
            public void onFailure(String code, String message){
                super.onFailure(code, message);
            }
        });

    }
}
