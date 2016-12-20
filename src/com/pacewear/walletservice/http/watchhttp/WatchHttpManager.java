
package com.pacewear.walletservice.http.watchhttp;

import android.content.Context;

import com.pacewear.walletservice.http.IHttpManager;
import com.tencent.tws.api.HttpManager;
import com.tencent.tws.api.HttpRequestGeneralParams;
import com.tencent.tws.api.HttpResponseListener;
import com.tencent.tws.api.HttpResponseResult;

public class WatchHttpManager implements IHttpManager {
    private HttpManager mHttpManager = null;

    public WatchHttpManager(Context context) {
        mHttpManager = HttpManager.getInstance(context);
    }

    @Override
    public void request(byte[] packet, final IHttpCallbcak callbcak) {
        WatchHttpPost httpPost = new WatchHttpPost();
        httpPost.addBody(packet);
        mHttpManager.postGeneralHttpRequest((HttpRequestGeneralParams) httpPost.toParam(),
                new HttpResponseListener() {

                    @Override
                    public void onResponse(HttpResponseResult arg0) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onError(int arg0, HttpResponseResult arg1) {
                        // TODO Auto-generated method stub

                    }
                });
    }

}
