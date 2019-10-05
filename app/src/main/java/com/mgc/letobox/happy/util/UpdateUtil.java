package com.mgc.letobox.happy.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;


import com.ledong.lib.leto.api.constant.Constant;
import com.ledong.lib.leto.interfaces.IApiCallback;
import com.ledong.lib.leto.trace.LetoTrace;
import com.leto.game.base.config.FileConfig;
import com.leto.game.base.listener.IProgressListener;
import com.leto.game.base.util.BaseAppUtil;
import com.leto.game.base.util.FileUtil;
import com.leto.game.base.util.IOUtil;
import com.leto.game.base.util.MD5;
import com.leto.game.base.util.OkHttpUtil;
import com.mgc.letobox.happy.bean.VersionResultBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;

public class UpdateUtil {
    /**
     * 提示版本更新的对话框
     */

    Context mContext;

    VersionResultBean version;

    String apkFile;

    boolean cancel = false;

    public UpdateUtil(Context context){
        this.mContext  = context;
    }

    public void  setVersion(VersionResultBean version){
        this.version = version;


        File rootDir = new File(FileConfig.getDefaultSaveRootPath(mContext));
        if(!rootDir.exists()){
            rootDir.mkdir();
        }
        apkFile = FileConfig.getDefaultSaveFilePath(mContext, MD5.md5(version.getPackageurl())+ ".apk") ;

    }

    /**
     * 安装apk
     */
    public void installApk() {

        File file = new File(apkFile);
        if(!file.exists()){
            return;
        }
        BaseAppUtil.installApk(mContext, file);
    }


    public void downloadApk(final Context context, final IProgressListener listener) {
        JSONObject header = null;
        final String url = version.getPackageurl();

        try {
            Headers headers = Headers.of(OkHttpUtil.parseJsonToMap(header));
            Request request = new Request.Builder().headers(headers).url(url).build();

            OkHttpUtil.downLoadFile(request, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    if(listener!=null){
                        listener.abort();
                    }
                }

                @Override
                public void onResponse(Call call, Response response) {
                    File tempFile = null;
                    InputStream is = null;
                    FileOutputStream os = null;
                    long sum = 0, total = 0;
                    try {
                        if(TextUtils.isEmpty(apkFile)){
                            File rootDir = new File(FileConfig.getDefaultSaveRootPath(mContext));
                            if(!rootDir.exists()){
                                rootDir.mkdir();
                            }
                            apkFile = FileConfig.getDefaultSaveFilePath(mContext, MD5.md5(version.getPackageurl())+ ".apk") ;
                        }

                        tempFile = new File(apkFile);
                        is = response.body().byteStream();
                        os = new FileOutputStream(tempFile);
                        total = response.body().contentLength();

                        byte[] buffer = new byte[4096];
                        int len;
                        while ((len = is.read(buffer)) >= 0) {
                            os.write(buffer, 0, len);
                            sum += len;
                            int progress = (int) (sum * 1.0f / total * 100);

                            if(cancel){
                                LetoTrace.d("version update cancel" );
                                break;
                            }
                            if (null != listener) {
                                listener.onProgressUpdate(progress, sum, total);
                            }
                        }
                        os.flush();
                    } catch (IOException e) {
                        tempFile = null;
                        if (null != listener) {
                            listener.abort();
                        }
                    } finally {
                        IOUtil.closeAll(is, os);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            if(listener!=null){
                listener.abort();
            }
        }
    }


    public void  setCancel(boolean status){
        this.cancel = status;
    }

    public boolean isCancel(){
        return  cancel;
    }


}
