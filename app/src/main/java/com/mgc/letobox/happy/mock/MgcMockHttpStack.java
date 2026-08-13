package com.mgc.letobox.happy.mock;

import android.util.Log;

import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.Request;
import com.kymjs.rxvolley.http.URLHttpResponse;
import com.kymjs.rxvolley.interf.IHttpStack;
import com.kymjs.rxvolley.toolbox.HttpParamsEntry;
import com.mgc.leto.game.base.http.HttpCallbackDecode;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

/** RxVolley transport that short-circuits only the retired MGC test host. */
final class MgcMockHttpStack implements IHttpStack {
    private static final String TAG = "MgcMock";
    private final IHttpStack delegate;

    MgcMockHttpStack(IHttpStack delegate) {
        this.delegate = delegate;
    }

    @Override
    public URLHttpResponse performRequest(Request<?> request, ArrayList<HttpParamsEntry> additionalHeaders) {
        if (!MgcMockResponses.handles(request.getUrl())) {
            return delegate.performRequest(request, additionalHeaders);
        }

        // The retired API encrypted response data with a per-request auth key.
        // Local JSON is trusted test data, so disable decoding for this callback.
        HttpCallback callback = request.getCallback();
        if (callback instanceof HttpCallbackDecode) {
            ((HttpCallbackDecode<?>) callback).setAuthkey(null);
        }

        byte[] body = MgcMockResponses.responseFor(request.getUrl())
                .getBytes(StandardCharsets.UTF_8);
        URLHttpResponse response = new URLHttpResponse();
        response.setResponseCode(200);
        response.setResponseMessage("OK (local mock)");
        response.setContentType("application/json; charset=utf-8");
        response.setContentEncoding("utf-8");
        response.setContentLength(body.length);
        response.setContentStream(new ByteArrayInputStream(body));
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json; charset=utf-8");
        response.setHeaders(headers);
        Log.d(TAG, "RxVolley mock: " + request.getUrl());
        return response;
    }
}
