
package com.pacewear.walletservice.http.watchhttp;

import com.pacewear.walletservice.http.HttpHeader;
import com.pacewear.walletservice.http.HttpUrl;
import com.pacewear.walletservice.http.IHttpPost;
import com.tencent.tws.api.HttpRequestCommand;
import com.tencent.tws.api.HttpRequestGeneralParams;

public class WatchHttpPost implements IHttpPost {
    private HttpRequestGeneralParams mParams = null;

    public WatchHttpPost() {
        mParams = new HttpRequestGeneralParams();
        mParams.setCacheResult(false, 0);
        mParams.setRequestTimeOut(3000);
        mParams.setUserAgent("tencent"); // ??
        mParams.addUrl(HttpUrl.get().getUrl());
        initDefaultPost();
    }

    private void initDefaultPost() {
        mParams.setRequestType(HttpRequestCommand.POST_WITH_STRAMRETURN);
        // mParams.addHeader(key, value);
        mParams.addHeader(HttpHeader.REQ.CONTENT_TYPE, HttpHeader.CONTENT_TYPE);
        // mParams.addHeader(HttpHeader.REQ.QGUID, mSessionRequest.getGuid()); TODO
        mParams.addHeader(HttpHeader.REQ.ACCEPT_ENCODING, HttpHeader.WUP_HEADER_GZIP_VALUE);
        mParams.addHeader(HttpHeader.REQ.QQ_S_ZIP, HttpHeader.WUP_HEADER_GZIP_VALUE);
    }

    @Override
    public void addHeader(String key, String val) {
        mParams.addHeader(key, val);
    }

    @Override
    public void addBody(byte[] dataPacket) {
        mParams.addBodyEntity(new String(dataPacket));
    }

    @Override
    public Object toParam() {
        return mParams;
    }

}
