
package com.pacewear.walletservice.http.tos;

import android.content.Context;
import android.util.Log;

import com.pacewear.walletservice.common.Constants;
import com.pacewear.walletservice.common.RunEnv;
import com.pacewear.walletservice.http.tos.IServerHandler;
import com.pacewear.walletservice.http.tos.IServerHandlerListener;
import com.pacewear.walletservice.http.watchhttp.DmaServerHandler;
import com.pacewear.walletservice.http.wuphttp.WupServerHandler;
import com.qq.jce.wup.UniPacket;
import com.qq.taf.jce.JceStruct;

import java.util.ArrayList;
import java.util.Iterator;

import TRom.CardStatusContextRsp;

/**
 * @author baodingzhou
 */

abstract public class ServerHandler implements IServerHandler,
        IServerHandlerListener {

    private static final String TAG = "ServerHandler";

    private static final int WUP_MOUDLE_ID = 11;

    private static volatile IServerHandler sInstance = null;

    private ArrayList<IServerHandlerListener> mListener = new ArrayList<IServerHandlerListener>();

    protected boolean mRequestEncrypt = false;

    private boolean mTest = true;

    public ServerHandler(Context context) {
        Log.d(TAG, "ServerHandler");
    }

    public static IServerHandler getInstance(Context context) {
        if (sInstance == null) {
            synchronized (ServerHandler.class) {
                if (sInstance == null) {
                    switch (RunEnv.getPlatform()) {
                        case RunEnv.PLATFORM_DM:
                            sInstance = new WupServerHandler(context);
                            break;
                        case RunEnv.PLATFORM_WATCH:
                            sInstance = new DmaServerHandler(context);
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        return sInstance;
    }

    @Override
    public boolean registerServerHandlerListener(IServerHandlerListener listener) {
        Log.d(TAG, "registerServerHandlerListener");
        synchronized (mListener) {
            if (!mListener.contains(listener)) {
                return mListener.add(listener);
            }

        }
        return false;
    }

    @Override
    public boolean unregisterServerHandlerListener(IServerHandlerListener listener) {
        Log.d(TAG, "unregisterServerHandlerListener");
        synchronized (mListener) {
            return mListener.remove(listener);
        }
    }

    private final static UniPacket decodePacket(byte[] data) {
        UniPacket packet = new UniPacket();
        packet.setEncodeName(Constants.UTF8);
        packet.decode(data);
        return packet;
    }

    private JceStruct doTest(byte[] response) {
        UniPacket packet = decodePacket(response);
        JceStruct rsp = null;
        int error = 0;
        if (packet != null) {
            error = -1111;
            rsp = testparse(packet);
        }
        return rsp;
    }

    private final JceStruct testparse(UniPacket packet) {
        if (packet == null) {
            return null;
        }

        JceStruct rsp = new CardStatusContextRsp();
        if (rsp == null) {
            return null;
        }
        return packet.getByClass("stRsp", rsp);
    }

    @Override
    public boolean onResponseSucceed(int reqID, int operType, byte[] response) {
        Log.d(TAG, "onResponseSucceed");
        boolean handle = false;
        Iterator<IServerHandlerListener> iterator = null;
        IServerHandlerListener listener = null;

        if (mTest) {
            JceStruct respData = doTest(response);
            handle = false;
        }
        synchronized (mListener) {
            iterator = mListener.iterator();
            if (iterator != null) {
                while (iterator.hasNext()) {
                    listener = iterator.next();
                    handle = listener.onResponseSucceed(reqID, operType, response);
                    if (handle) {
                        break;
                    }
                }
            }
        }

        if (handle) {
            unregisterServerHandlerListener(listener);
        }

        return handle;
    }

    @Override
    public boolean onResponseFailed(int reqID, int operType, int errorCode, String description) {
        Log.d(TAG, "onResponseFailed");
        boolean handle = false;
        Iterator<IServerHandlerListener> iterator = null;
        IServerHandlerListener listener = null;
        synchronized (mListener) {
            iterator = mListener.iterator();
            if (iterator != null) {
                while (iterator.hasNext()) {
                    listener = iterator.next();
                    handle = listener.onResponseFailed(reqID, operType, errorCode, description);
                    if (handle) {
                        break;
                    }
                }
            }
        }

        if (handle) {
            unregisterServerHandlerListener(listener);
        }

        return handle;
    }

    @Override
    public void setRequestEncrypt(boolean encrypt) {
        mRequestEncrypt = encrypt;
    }
}
