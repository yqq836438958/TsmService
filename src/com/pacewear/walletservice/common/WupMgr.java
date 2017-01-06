
package com.pacewear.walletservice.common;

import java.lang.ref.WeakReference;

import qrom.component.wup.QRomComponentWupManager;
import qrom.component.wup.QRomWupReqExtraData;
import qrom.component.wup.QRomWupRspExtraData;
import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

import com.pacewear.walletservice.WalletApp;
import com.qq.jce.wup.UniPacket;

/**
 * @author maxwang
 */
public class WupMgr extends QRomComponentWupManager {
    public interface WupResultObserver {
        void onReceiveAllData(int fromModelType, int nReqId, int operType, byte[] oResponse);

        void onReceiveError(int fromModelType, int nReqId, int operType, int nErrCode,
                String strDescription);
    }

    private SparseArray<WeakReference<WupResultObserver>> m_oWupResultObserver;
    private Object m_oLock = new Object();
    private static final String TAG = WupMgr.class.getName();
    private static volatile WupMgr g_instance = null;
    private static Object g_instanceLock = new Object();

    public static WupMgr getInstance() {
        if (g_instance == null) {
            synchronized (g_instanceLock) {
                if (g_instance == null)
                    g_instance = new WupMgr(WalletApp.sGlobalCtx);
            }
        }

        return g_instance;
    }

    @Override
    public void onGuidChanged(byte[] arg0) {
        // TODO Auto-generated method stub

    }

    public int reqWup(int nFromModelType, int operType, UniPacket oPacket, long lTimeOut,
            WupResultObserver oObserver) {
        synchronized (m_oLock) {
            int nReqId = requestWupNoRetry(nFromModelType, operType, oPacket, lTimeOut);
            m_oWupResultObserver.put(nReqId,
                    new WeakReference<WupMgr.WupResultObserver>(oObserver));
            return nReqId;
        }
    }

    @Override
    public void onReceiveAllData(int fromModelType, int nReqId, int operType,
            QRomWupReqExtraData wupReqExtraData, QRomWupRspExtraData rspExtraData,
            String strServcieName,
            byte[] oReponse) {
        // TODO Auto-generated method stub

        WupResultObserver oObserver = getObserverOfReqId(nReqId);

        if (oObserver == null)
            return;

        oObserver.onReceiveAllData(fromModelType, nReqId, operType, oReponse);
    }

    @Override
    public void onReceiveError(int fromModelType, int nReqId, int operType,
            QRomWupReqExtraData wupReqExtraData, QRomWupRspExtraData wupRspExtraData,
            String strServiceName,
            int nErrCode, String strDescription) {
        // TODO Auto-generated method stub
        WupResultObserver oObserver = getObserverOfReqId(nReqId);

        if (oObserver == null)
            return;

        oObserver.onReceiveError(fromModelType, nReqId, operType, nErrCode, strDescription);
    }

    private WupMgr(Context oContext) {
        m_oWupResultObserver = new SparseArray<WeakReference<WupResultObserver>>();
        startup(oContext);
    }

    private WupResultObserver getObserverOfReqId(int nReqId) {
        WupResultObserver oObserver = null;

        synchronized (m_oLock) {
            WeakReference<WupResultObserver> oObserverRef = m_oWupResultObserver.get(nReqId);

            if (oObserverRef == null)
                Log.e(TAG, "Err, cant find observer of reqid" + String.valueOf(nReqId));
            else
                oObserver = oObserverRef.get();

            m_oWupResultObserver.remove(nReqId);
        }

        return oObserver;
    }

}
