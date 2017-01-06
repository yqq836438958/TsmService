
package com.pacewear.walletservice.http.watchhttp;

import android.util.Base64;
import android.util.Log;

import com.pacewear.walletservice.http.HttpHeader;
import com.pacewear.walletservice.http.HttpUrl;
import com.pacewear.walletservice.http.IHttpPost;
import com.tencent.tws.api.HttpRequestCommand;
import com.tencent.tws.api.HttpRequestGeneralParams;

import java.io.IOException;

import qrom.component.wup.base.utils.TEACoding;
import qrom.component.wup.base.utils.ZipUtils;

public class WatchHttpPost implements IHttpPost {
    public static final String TAG = "WatchHttpPost";
    private HttpRequestGeneralParams mParams = null;
    private boolean mIsGZIPHandle = true;

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
        mParams.addHeader(HttpHeader.REQ.HOST, HttpUrl.get().getHttpHost());
        mParams.addHeader(HttpHeader.REQ.QGUID, "12C7CF7026FDB81AF0F93F1FB281F5C7"); // TODO
        mParams.addHeader(HttpHeader.REQ.ACCEPT_ENCODING, HttpHeader.WUP_HEADER_GZIP_VALUE);
        mParams.addHeader(HttpHeader.REQ.QQ_S_ZIP, HttpHeader.WUP_HEADER_GZIP_VALUE);
    }

    @Override
    public void addHeader(String key, String val) {
        mParams.addHeader(key, val);
    }

    @Override
    public void addBody(byte[] dataPacket) {
        byte[] zipData = dataPacket;
        if (mIsGZIPHandle) {
            try {
                zipData = ZipUtils.gZip(dataPacket);
            } catch (IOException e) {
                Log.e(TAG, "error:" + e.getMessage());
                e.printStackTrace();
            }
            if (zipData == null) {
                return;
            }
        }
        String data = Base64.encodeToString(zipData, Base64.DEFAULT);
        mParams.addBodyEntity(data, "base64-default");
    }

    @Override
    public Object toParam() {
        return mParams;
    }

    public byte[] onParseHttpRsp(String rsp) {
        byte[] decodeRsp = Base64.decode(rsp, Base64.DEFAULT);
        if (decodeRsp == null) {
            return null;
        }
        byte[] result = decodeRsp;

        if (mIsGZIPHandle) {
            try {
                result = ZipUtils.unGzip(decodeRsp);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return result;
    }

}
