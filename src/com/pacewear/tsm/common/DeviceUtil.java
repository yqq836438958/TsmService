
package com.pacewear.tsm.common;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.pacewear.walletservice.WalletApp;
import com.pacewear.walletservice.common.QubeStringUtil;
import com.pacewear.walletservice.common.WupMgr;

import TRom.RomBaseInfo;
import qrom.component.statistic.QStatExecutor;
import qrom.component.wup.QRomQuaFactory;
import qrom.component.wup.QRomWupDataBuilder;

public class DeviceUtil {
    public static final String TAG = "DeviceUtil";
    private static Object g_lockOfRomBaseInfo = new Object();
    private static RomBaseInfo g_romBaseInfo = null;
    public static String DEFAULT_IMEI = "012345678901234";
    private static final String LC = "35661E4122F8564";
    private static String g_strImei = null;

    public static RomBaseInfo getRomBaseInfo() {
        String strGuid = WupMgr.getInstance().getGUIDStr();
        Log.v(TAG, "strGuid ============================ " + strGuid);
        if (strGuid == null)
            return null;

        boolean bGuidValidate = QRomWupDataBuilder.isGuidValidate(strGuid);
        if (bGuidValidate == false)
            return null;

        RomBaseInfo oTempRomBaseInfo = new RomBaseInfo();

        oTempRomBaseInfo.setVGUID(QubeStringUtil.hexStringToByte(strGuid));
        oTempRomBaseInfo.setSLC(LC);

        String strQImei = QStatExecutor.getQIMEI();
        oTempRomBaseInfo.setSQIMEI(strQImei);

        String strQua = QRomQuaFactory.buildQua(WalletApp.sGlobalCtx);
        oTempRomBaseInfo.setSQUA(strQua);

        oTempRomBaseInfo.setSIMEI(getPhoneIMEI());

        g_romBaseInfo = oTempRomBaseInfo;
        return g_romBaseInfo;
    }

    private static String getPhoneIMEI() {
        if (g_strImei == null || g_strImei.length() == 0) {
            try {
                g_strImei = ((TelephonyManager) WalletApp.sGlobalCtx
                        .getSystemService(Context.TELEPHONY_SERVICE))
                                .getDeviceId();
            } catch (Exception exception) {
                g_strImei = "";
            }
        }
        if (g_strImei == null || g_strImei.length() == 0) {// 对于无法获取imei的特殊情况，给予一个默认值
            g_strImei = DEFAULT_IMEI;
        }

        return g_strImei;
    }
}
