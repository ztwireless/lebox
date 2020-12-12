package com.mgc.letobox.happy.follow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgc.leto.game.base.be.AdConst;
import com.mgc.leto.game.base.http.HttpCallbackDecode;
import com.mgc.leto.game.base.main.BaseActivity;
import com.mgc.leto.game.base.utils.ColorUtil;
import com.mgc.leto.game.base.utils.MResource;
import com.mgc.leto.game.base.utils.StatusBarUtil;
import com.mgc.leto.game.base.utils.ToastUtil;
import com.mgc.leto.game.base.widget.ClickGuard;
import com.mgc.letobox.happy.util.LeBoxConstant;


public class FollowInviteCodeActivity extends BaseActivity {

    // views
    private ImageView _backBtn;
    private TextView _titleLabel;
    private TextView _titleRightLabel;
    private EditText _codeEt;
    private Button _okBtn;

    private int reqestCode = -1;


    public static void start(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, FollowInviteCodeActivity.class);
            context.startActivity(intent);
        }
    }

    public static void startActivityByRequestCode(Activity context, int requestCode) {
        if (null != context) {
            Intent intent = new Intent(context, FollowInviteCodeActivity.class);
            intent.putExtra(LeBoxConstant.REQUEST_CODE, requestCode);

            context.startActivityForResult(intent, requestCode);
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
        setContentView(MResource.getIdByName(this, "R.layout.activity_follow_invite_code"));


        if (getIntent() != null) {
            reqestCode = getIntent().getIntExtra(LeBoxConstant.REQUEST_CODE, -1);
        }


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
                if (reqestCode > -1) {
                    Intent i = new Intent();
                    i.putExtra("result", 0);
                    setResult(reqestCode, i);
                }
                finish();
                return true;
            }
        });

        _okBtn.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {

                String code = _codeEt.getText().toString();

                if (TextUtils.isEmpty(code)) {
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
    public void onDestroy() {
        super.onDestroy();
    }


    public void doFollow(final String code) {

        FollowUtil.follow(FollowInviteCodeActivity.this, code, new HttpCallbackDecode<Object>(FollowInviteCodeActivity.this, null) {
            @Override
            public void onDataSuccess(Object data) {

                ToastUtil.s(FollowInviteCodeActivity.this, "绑定成功");
                if (reqestCode > -1) {
                    Intent i = new Intent();
                    i.putExtra("result", 1);
                    i.putExtra("invite_code", code);
                    setResult(1, i);
                }

                finish();
            }

            @Override
            public void onFailure(String code, String message) {
                super.onFailure(code, message);

                if (reqestCode > -1) {
                    Intent i = new Intent();
                    i.putExtra("result", 1);
                    i.putExtra("invite_code", code);
                    setResult(0, i);
                }

                finish();
            }

            @Override
            public void onFinish() {

                dismissLoading();
            }
        });

        showLoading("");

    }
}
