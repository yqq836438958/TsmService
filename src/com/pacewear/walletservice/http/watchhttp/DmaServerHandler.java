
package com.pacewear.walletservice.http.watchhttp;

import android.content.Context;

import com.pacewear.walletservice.common.Constants;
import com.pacewear.walletservice.http.tos.ServerHandler;
import com.qq.jce.wup.UniPacket;
import com.tencent.tws.api.HttpManager;
import com.tencent.tws.api.HttpRequestGeneralParams;
import com.tencent.tws.api.HttpResponseListener;
import com.tencent.tws.api.HttpResponseResult;

import java.io.UnsupportedEncodingException;

public class DmaServerHandler extends ServerHandler {
    private HttpManager mHttpManager = null;

    public DmaServerHandler(Context context) {
        super(context);
        mHttpManager = HttpManager.getInstance(context);
    }

    @Override
    public int reqServer(final int operType, UniPacket uniPacket) {
        WatchHttpPost httpPost = new WatchHttpPost();
        httpPost.addBody(uniPacket.encode());
        mHttpManager.postGeneralHttpRequest((HttpRequestGeneralParams) httpPost.toParam(),
                new HttpResponseListener() {

                    @Override
                    public void onResponse(HttpResponseResult arg0) {
                        try {
                            onResponseSucceed(00, operType, (arg0.mData).getBytes(Constants.UTF8));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(int arg0, HttpResponseResult arg1) {
                        onResponseFailed(00, operType, arg0, "");
                    }
                });
        return 0;
    }

    @Override
    public boolean isTestEnv() {
        return false;
    }

}
