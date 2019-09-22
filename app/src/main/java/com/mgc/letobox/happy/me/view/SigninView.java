package com.mgc.letobox.happy.me.view;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ledong.lib.leto.api.ApiContainer;
import com.ledong.lib.leto.mgc.util.MGCApiUtil;
import com.leto.game.base.http.HttpCallbackDecode;
import com.leto.game.base.util.MResource;
import com.leto.game.base.util.ToastUtil;
import com.leto.game.base.view.recycleview.ScrollRecyclerView;
import com.mgc.letobox.happy.me.adapter.SignInAdapter;
import com.mgc.letobox.happy.me.bean.SigninBean;
import com.mgc.letobox.happy.me.bean.SigninStatusResultBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by zhaozhihui on 2019-09-10
 **/
public class SigninView extends LinearLayout {

    ViewGroup _adContainer;

    ScrollRecyclerView _recyclerView;
    SignInAdapter _adapter;

    Context _context;

    List<SigninBean> _signinList;

    // api容器
    private ApiContainer _apiContainer;

    public SigninView(Context context) {
        super(context);

        _context = context;

        initUI(context);
    }

    public SigninView(Context context, AttributeSet attrs) {
        super(context, attrs);

        _context = context;

        initUI(context);
    }

    public void initUI(Context context) {
        inflate(context, MResource.getIdByName(context, "R.layout.leto_mgc_me_signin"), this);
        _recyclerView = findViewById(MResource.getIdByName(context, "R.id.recyclerView"));


        _signinList = new ArrayList<>();
        _adapter = new SignInAdapter(context, _signinList);

        // setup views
        _recyclerView.setLayoutManager(new StaggeredGridLayoutManager(7,
                StaggeredGridLayoutManager.VERTICAL));


        _recyclerView.setAdapter(_adapter);

        _recyclerView.setNestedScrollingEnabled(false);


        initData();
    }


    private void initData() {

        getSignInStatus();

    }

    public void getSignInStatus() {

        MGCApiUtil.getSigninStatus(getContext(), new HttpCallbackDecode<SigninStatusResultBean>(getContext(), null) {
            @Override
            public void onDataSuccess(final SigninStatusResultBean data) {
                if (null != data) {
                    try {
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                if (_signinList != null && data.getSignlist() != null) {
                                    _signinList.addAll(data.getSignlist());
                                }

                                _adapter.notifyDataSetChanged();
                            }
                        });

                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void onFailure(String code, String msg) {
                try {
                    ToastUtil.s(_context, msg);
                } catch (Exception e) {

                }

            }

            @Override
            public void onFinish() {

            }
        });


    }

    public void setAdContainer(ViewGroup adContainer) {
        _adContainer = adContainer;
        if (_adapter != null) {
            _adapter.setAdContainer(_adContainer);
        }
    }

}
