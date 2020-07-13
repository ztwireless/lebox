package com.mgc.letobox.happy.me;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgc.leto.game.base.bean.LetoError;
import com.mgc.leto.game.base.event.DataRefreshEvent;
import com.mgc.leto.game.base.http.HttpCallbackDecode;
import com.mgc.leto.game.base.main.BaseActivity;
import com.mgc.leto.game.base.mgc.bean.AddCoinResultBean;
import com.mgc.leto.game.base.mgc.util.MGCApiUtil;
import com.mgc.leto.game.base.mgc.util.MGCDialogUtil;
import com.mgc.leto.game.base.utils.ColorUtil;
import com.mgc.leto.game.base.utils.DialogUtil;
import com.mgc.leto.game.base.utils.GlideUtil;
import com.mgc.leto.game.base.utils.MResource;
import com.mgc.leto.game.base.utils.StatusBarUtil;
import com.mgc.leto.game.base.utils.ToastUtil;
import com.mgc.leto.game.base.widget.ClickGuard;
import com.mgc.letobox.happy.me.bean.JoinWechatResultBean;
import com.mgc.letobox.happy.util.LeBoxConstant;
import com.mgc.letobox.happy.util.LeBoxUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Create by zhaozhihui on 2019-09-14
 **/
public class JoinWeChatActivity extends BaseActivity {

    private static final String TAG = JoinWeChatActivity.class.getSimpleName();

    ImageView backView;
    ImageView qrCodeView;
    ImageView useImageView;

    EditText codeEditText;
    TextView rechargeBtn;

    TextView joinview;
    TextView usageTitleView;

    int _taskId;

    public static void start(Context context, int taskId) {
        Intent intent = new Intent(context, JoinWeChatActivity.class);
        intent.putExtra(LeBoxConstant.LETO_TASK_ID, taskId);
        context.startActivity(intent);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarUtil.setStatusBarColor(this, ColorUtil.parseColor("#22154D"));

        }

        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);


        // set content view
        setContentView(MResource.getIdByName(this, "R.layout.activity_joinwechat"));

        if (getIntent() != null) {
            _taskId = getIntent().getIntExtra(LeBoxConstant.LETO_TASK_ID, 0);
        }


        backView = findViewById(MResource.getIdByName(this, "R.id.iv_back"));
        codeEditText = findViewById(MResource.getIdByName(this, "R.id.rechargeCodeEditText"));

        joinview = findViewById(MResource.getIdByName(this, "R.id.join_content"));
        qrCodeView = findViewById(MResource.getIdByName(this, "R.id.qrCode"));
        useImageView = findViewById(MResource.getIdByName(this, "R.id.useImage"));
        usageTitleView = findViewById(MResource.getIdByName(this, "R.id.use_title"));


        rechargeBtn = findViewById(MResource.getIdByName(this, "R.id.rechargeBtn"));

        backView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rechargeBtn.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {

                String code = codeEditText.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    ToastUtil.s(JoinWeChatActivity.this, "请输入兑换码");
                    return false;
                }

                exchangeCode(code);

                return true;
            }
        });

        getJoinContent();

    }


    private void exchangeCode(String code) {


        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);


        LeBoxUtil.exchange(JoinWeChatActivity.this, code, new HttpCallbackDecode(JoinWeChatActivity.this, null) {
            @Override
            public void onDataSuccess(Object data) {
                Log.i(TAG, "exchange success");

                addCoin();

            }

            @Override
            public void onFailure(String code, String message) {

                if (TextUtils.isEmpty(message)) {
                    ToastUtil.s(JoinWeChatActivity.this, "兑换失败");
                } else {
                    ToastUtil.s(JoinWeChatActivity.this,  message);
                }
            }
        });

    }

    private void getJoinContent() {

        LeBoxUtil.getJoinWechatContent(JoinWeChatActivity.this, new HttpCallbackDecode<JoinWechatResultBean>(JoinWeChatActivity.this, null) {
            @Override
            public void onDataSuccess(final JoinWechatResultBean data) {
                Log.i(TAG, "exchange");
                if (data != null) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {

                            joinview.setText(data.getExplain());

                            if(!TextUtils.isEmpty(data.getQrcode())) {
                                qrCodeView.setVisibility(View.VISIBLE);
                                GlideUtil.load(JoinWeChatActivity.this, data.getQrcode(), qrCodeView);
                            }else{
                                qrCodeView.setVisibility(View.GONE);
                            }
                            if (!TextUtils.isEmpty(data.getPic())) {
                                usageTitleView.setVisibility(View.VISIBLE);
                                GlideUtil.load(JoinWeChatActivity.this, data.getPic(), useImageView);
                            } else {
                                usageTitleView.setVisibility(View.GONE);
                            }
                        }
                    });


                }

            }
        });
    }

    private void addCoin() {

        MGCApiUtil.addCoin(JoinWeChatActivity.this, "", 0, "", 8, _taskId, new HttpCallbackDecode<AddCoinResultBean>(JoinWeChatActivity.this, null) {
            @Override
            public void onDataSuccess(AddCoinResultBean data) {
                ToastUtil.s(JoinWeChatActivity.this, "兑换金币成功");
                EventBus.getDefault().post(new DataRefreshEvent());
            }

            @Override
            public void onFailure(String code, String msg) {
                super.onFailure(code, msg);
                if (!TextUtils.isEmpty(code) && code.equalsIgnoreCase(LetoError.MGC_COIN_LIMIT)) {
                    DialogUtil.dismissDialog();
                    MGCDialogUtil.showCoinLimit(JoinWeChatActivity.this, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    });
                    return;
                }
                ToastUtil.s(JoinWeChatActivity.this, "兑换金币失败");
            }
        });
    }

}