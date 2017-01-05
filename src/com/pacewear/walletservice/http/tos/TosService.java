
package com.pacewear.walletservice.http.tos;

import android.util.Log;

import com.pacewear.walletservice.WalletApp;
import com.pacewear.walletservice.common.Constants;
import com.pacewear.walletservice.common.SeqGenerator;
import com.qq.jce.wup.UniPacket;
import com.qq.taf.jce.JceStruct;

import TRom.TSMHead;
import qrom.component.wup.QRomWupDataBuilder;

/**
 * @author baodingzhou
 */

public abstract class TosService implements IServerHandlerListener, ITosService, IResponseObserver {

    private static final String TAG = "TosService";

    private static SeqGenerator sSeqGenerator = SeqGenerator.getInstance();

    private long mUniqueSeq = -1;

    private int mReqID = -1;

    private IResponseObserver mResponseObserver = null;
    private String mMouduleName = "";
    private String mReqName = REQ_NAME;
    private String mRspName = RSP_NAME;

    public TosService() {
        mUniqueSeq = sSeqGenerator.uniqueSeq();
    }

    public TosService(String _module, String _req, String _rsp) {
        this();
        mMouduleName = _module;
        mReqName = _req;
        mRspName = _rsp;
    }

    @Override
    public final long getUniqueSeq() {
        return mUniqueSeq;
    }

    @Override
    public boolean invoke(IResponseObserver observer) {
        boolean handled = false;

        mResponseObserver = observer;

        int operType = getOperType();
        if (operType == OPERTYPE_UNKNOWN) {
            return handled;
        }

        TSMHead tsmHead = getTsmHead();

        if (tsmHead == null) {
            Log.e(TAG, "PayReqHead is null");
            return handled;
        }

        JceStruct req = getReq(tsmHead);
        if (req == null) {
            return handled;
        }

        Log.d(
                TAG,
                String.format("mUniqueSeq:%d req:%s", mUniqueSeq,
                        JceStruct.toDisplaySimpleString(req)));

        UniPacket packet = QRomWupDataBuilder.createReqUnipackageV3(
                mMouduleName, getFunctionName(),
                mReqName, req);

        IServerHandler serverHandler = ServerHandler.getInstance(WalletApp.sGlobalCtx);

        serverHandler.registerServerHandlerListener(this);
        serverHandler.setRequestEncrypt(getRequestEncrypt());
        mReqID = serverHandler.reqServer(operType, packet);

        if (mReqID >= 0) {
            handled = true;
        }

        if (!handled) {
            serverHandler.unregisterServerHandlerListener(this);
        }

        return handled;
    }

    protected boolean getRequestEncrypt() {
        return false;
    }

    private final static UniPacket decodePacket(byte[] data) {
        UniPacket packet = new UniPacket();
        packet.setEncodeName(Constants.UTF8);
        packet.decode(data);
        return packet;
    }

    @Override
    public final JceStruct parse(UniPacket packet) {
        if (packet == null) {
            return null;
        }

        JceStruct rsp = getRspObject();
        if (rsp == null) {
            return null;
        }
        return packet.getByClass(mRspName, rsp);
    }

    @Override
    public final boolean onResponseSucceed(int reqID, int operType, byte[] response) {
        if (mReqID == reqID) {
            int error = ERR_DOCODE_ERROR;
            UniPacket packet = decodePacket(response);
            JceStruct rsp = null;
            if (packet != null) {
                error = ERR_PARSE_ERROR;
                rsp = parse(packet);
            }

            if (rsp != null) {
                Log.d(
                        TAG,
                        String.format("mUniqueSeq:%d rsp:%s", mUniqueSeq,
                                JceStruct.toDisplaySimpleString(rsp)));
                onResponseSucceed(mUniqueSeq, operType, rsp);
            } else {
                // Impossible if code right.
                onResponseFailed(mUniqueSeq, operType, error, "");
            }

            return true;
        }
        return false;
    }

    @Override
    public final boolean onResponseFailed(int reqID, int operType, int errorCode,
            String description) {
        if (mReqID == reqID) {
            Log.d(TAG, String.format("mUniqueSeq:%d errorCode:%d description:%s", mUniqueSeq,
                    errorCode, description));
            onResponseFailed(mUniqueSeq, operType, errorCode, description);

            return true;
        }
        return false;
    }

    @Override
    public final void onResponseSucceed(long uniqueSeq, int operType, JceStruct response) {
        if (mResponseObserver != null) {
            mResponseObserver.onResponseSucceed(uniqueSeq, operType, response);
        }
    }

    @Override
    public final void onResponseFailed(long uniqueSeq, int operType, int errorCode,
            String description) {
        if (mResponseObserver != null) {
            mResponseObserver.onResponseFailed(uniqueSeq, operType, errorCode, description);
        }
    }

    protected abstract TSMHead getTsmHead();

}
