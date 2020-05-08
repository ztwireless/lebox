package com.mgc.letobox.happy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.ledong.lib.leto.widget.LoadingDialog;
import com.leto.game.base.util.MResource;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by liu hong liang on 2017/4/27.
 */

public class BaseActivity extends FragmentActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();

    LoadingDialog mDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mDialog = new LoadingDialog(this);
    }



    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);

    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);

    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        if(mDialog!=null &&mDialog.isShowing()){
            mDialog.dismiss();
        }

        mDialog = null;
    }


    public void showLoading(Boolean cancelable) {
        if(!isDestroyed()) {
            if(mDialog==null){
                mDialog = new LoadingDialog(this);
            }
            mDialog.setCancelable(cancelable);
            mDialog.show(getResources().getString(MResource.getIdByName(this, "R.string.loading")));
        }
    }

    public void showLoading(Boolean cancelable, String message) {
        if( !isDestroyed()) {

            if(mDialog==null){
                mDialog = new LoadingDialog(this);
            }

            mDialog.setCancelable(cancelable);
            mDialog.show(message);
        }
    }

    public void dismissLoading() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }
}
