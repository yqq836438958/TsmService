
package com.pacewear.walletservice.http.watchhttp;

import android.content.Context;

import com.pacewear.walletservice.http.tos.ServerHandler;
import com.qq.jce.wup.UniPacket;
import com.tencent.tws.api.HttpManager;

public class DmaServerHandler extends ServerHandler {
    private HttpManager mHttpManager = null;

    public DmaServerHandler(Context context) {
        super(context);
        mHttpManager = HttpManager.getInstance(context);
    }

    @Override
    public int reqServer(int operType, UniPacket uniPacket) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isTestEnv() {
        return false;
    }

}
