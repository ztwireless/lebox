package com.mgc.letobox.happy.me.holder;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ledong.lib.leto.mgc.util.MGCApiUtil;
import com.leto.game.base.http.HttpCallbackDecode;

import com.leto.game.base.util.MResource;
import com.leto.game.base.util.ToastUtil;
import com.leto.game.base.view.recycleview.ScrollRecyclerView;
import com.mgc.letobox.happy.me.adapter.SignInAdapter;
import com.mgc.letobox.happy.me.bean.MeModuleBean;
import com.mgc.letobox.happy.me.bean.SigninBean;
import com.mgc.letobox.happy.me.bean.SigninStatusResultBean;

import java.util.ArrayList;
import java.util.List;


public class MeSigninHolder extends CommonViewHolder<MeModuleBean> {
    View _splitSpace;
    ScrollRecyclerView _recyclerView;
    SignInAdapter _adapter;

    Context _context;

    List<SigninBean> _signinList;


    public static MeSigninHolder create(Context ctx, ViewGroup parent) {
        // load game row, and leave a gap so that next column can be seen
        View view = LayoutInflater.from(ctx)
                .inflate(MResource.getIdByName(ctx, "R.layout.leto_mgc_me_signin"), parent, false);
        return new MeSigninHolder(ctx, view);
    }

    public MeSigninHolder(Context context, View view) {
        super(view);
        _context = context;
        Context ctx = view.getContext();
        _splitSpace = itemView.findViewById(MResource.getIdByName(ctx, "R.id.split_space"));
        this._recyclerView = view.findViewById(MResource.getIdByName(ctx, "R.id.recyclerView"));

        _signinList = new ArrayList<>();
        _adapter = new SignInAdapter(context, _signinList);

        // setup views
        _recyclerView.setLayoutManager(new StaggeredGridLayoutManager(7,
                StaggeredGridLayoutManager.VERTICAL));

        _recyclerView.setAdapter(_adapter);

        _recyclerView.setNestedScrollingEnabled(false);


    }

    @Override
    public void onBind(final MeModuleBean signin, final int position) {
        // name & desc
        final Context ctx = itemView.getContext();
        _splitSpace.setVisibility(position == 0 ? View.GONE : View.VISIBLE);

        getSignInStatus();

        _adapter.setAdContainer(_adContainer);
    }



    public void getSignInStatus() {

        MGCApiUtil.getSigninStatus(_context, new HttpCallbackDecode<SigninStatusResultBean>(_context, null) {
            @Override
            public void onDataSuccess(final SigninStatusResultBean data) {
                if (null != data) {
                    try {
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                if (_signinList != null && data.getSignlist() != null) {

                                    _signinList.clear();

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

}