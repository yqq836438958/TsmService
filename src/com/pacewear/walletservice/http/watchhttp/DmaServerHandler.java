
package com.pacewear.walletservice.http.watchhttp;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.pacewear.walletservice.common.Constants;
import com.pacewear.walletservice.http.tos.ServerHandler;
import com.qq.jce.wup.UniPacket;
import com.tencent.tws.api.HttpManager;
import com.tencent.tws.api.HttpRequestGeneralParams;
import com.tencent.tws.api.HttpResponseListener;
import com.tencent.tws.api.HttpResponseResult;

public class DmaServerHandler extends ServerHandler {
    protected static final String TAG = "DmaServerHandler";
    private HttpManager mHttpManager = null;

    public DmaServerHandler(Context context) {
        super(context);
        mHttpManager = HttpManager.getInstance(context);
    }

    @Override
    public int reqServer(final int operType, UniPacket uniPacket) {
        final WatchHttpPost httpPost = new WatchHttpPost();
        httpPost.addBody(uniPacket.encode());
        mHttpManager.postGeneralHttpRequest((HttpRequestGeneralParams) httpPost.toParam(),
                new HttpResponseListener() {

                    @Override
                    public void onResponse(HttpResponseResult arg0) {
                        String data = arg0.mData;
                        Log.e(TAG, "onResponse:" + data);
                        onResponseSucceed(00, operType, httpPost.onParseHttpRsp(data));
                    }

                    @Override
                    public void onError(int arg0, HttpResponseResult arg1) {
                        Log.e(TAG, "onError:" + arg0 + ",desc:" + arg1.mData);
                        onResponseFailed(00, operType, arg0, "");
                    }
                });
        return 0;
    }

    @Override
    public boolean isTestEnv() {
        return false;
    }

    private byte[] onParseHttpRspData(String data) {
        byte[] decodeData = Base64.decode(data, Base64.DEFAULT);
        return null;
    }
}
