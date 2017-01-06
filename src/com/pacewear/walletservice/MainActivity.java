
package com.pacewear.walletservice;

import android.support.v7.app.ActionBarActivity;

import com.pacewear.tsm.common.DeviceUtil;
import com.pacewear.tsm.server.tosservice.SyncRemoteCardStatus;
import com.pacewear.walletservice.common.Utils;
import com.pacewear.walletservice.http.tos.ServerHandler;
import com.pacewear.walletservice.http.watchhttp.DmaServerHandler;
import com.qq.jce.wup.UniPacket;
import com.tencent.tws.phoneside.device.wup.DeviceInfoWupDataFactory;
import com.tencent.tws.phoneside.framework.RomBaseInfoHelper;

import TRom.CardBaseInfo;
import TRom.CardStatusContextReq;
import TRom.DeviceBaseInfo;
import TRom.TSMHead;
import qrom.component.wup.QRomWupDataBuilder;

import android.os.Bundle;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testJCE();
    }

    private void testJCE() {
        Utils.getWorkerHandler().post(new Runnable() {

            @Override
            public void run() {
                postHttpReq();
            }
        });
    }

    protected void postHttpReq() {
        DeviceBaseInfo deviceBaseInfo = getDeviceBaseInfo();
        CardBaseInfo cardBaseInfo = new CardBaseInfo(
                "479044204700E753010060500105009375194810000000510000042D4593893880010000000000503841");
        TSMHead stTSMHead = new TSMHead(null, cardBaseInfo);
        CardStatusContextReq req = new CardStatusContextReq(stTSMHead);
        UniPacket packet = QRomWupDataBuilder.createReqUnipackageV3(
                "tsmapi", "SyncRemoteCardStatus",
                "stReq", req);
        ServerHandler.getInstance(this).reqServer(3002, packet);
    }

    private DeviceBaseInfo getDeviceBaseInfo() {
        DeviceBaseInfo tmp = new DeviceBaseInfo();
        tmp.stPhoneBaseInfo = DeviceUtil.getRomBaseInfo();
        tmp.stWatchBaseInfo = DeviceInfoWupDataFactory.getInstance().getWatchRomBaseInfo();
        return tmp;
    }

    private void invoke() {
        // SyncRemoteCardStatus syncRemoteCardStatus = new Syncre
    }
}
