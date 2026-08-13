package com.mgc.letobox.happy.mock;

import android.util.Log;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.http.RequestQueue;
import com.kymjs.rxvolley.okhttp.OkHttpStack;
import com.mgc.leto.game.base.utils.OkHttpUtil;

import java.io.File;
import java.lang.reflect.Field;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/** Installs in-process responses for miniapi_dev.mgc-games.com. */
public final class MgcMockServer {
    private static final String TAG = "MgcMock";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private MgcMockServer() {
    }

    public static void install(File cacheDirectory) {
        OkHttpClient rxClient = new OkHttpClient();
        RequestQueue queue = RequestQueue.newRequestQueue(
                cacheDirectory,
                new MgcMockHttpStack(new OkHttpStack(rxClient)));
        if (!RxVolley.setRequestQueue(queue)) {
            Log.w(TAG, "RxVolley queue already initialized; mock queue was not installed");
        }
        installLetoOkHttpClient();
        Log.i(TAG, "Local mock enabled for " + MgcMockResponses.RETIRED_HOST);
    }

    private static void installLetoOkHttpClient() {
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) {
                            Request request = chain.request();
                            if (!MgcMockResponses.handles(request.url().toString())) {
                                return chain.proceed(request);
                            }
                            String json = MgcMockResponses.responseFor(request.url().toString());
                            Log.d(TAG, "OkHttp mock: " + request.url());
                            return new Response.Builder()
                                    .request(request)
                                    .protocol(Protocol.HTTP_1_1)
                                    .code(200)
                                    .message("OK (local mock)")
                                    .body(ResponseBody.create(JSON, json))
                                    .build();
                        }
                    })
                    .build();

            Field clientField = OkHttpUtil.class.getDeclaredField("CLIENT");
            clientField.setAccessible(true);
            clientField.set(null, client);
        } catch (Throwable e) {
            Log.e(TAG, "Unable to install Leto OkHttp mock", e);
        }
    }
}
