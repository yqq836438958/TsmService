
package qrom.component.config;

import qrom.component.log.QRomLogBaseConfig;

public class QRomLogConfig extends QRomLogBaseConfig {

    @Override
    public int getLogMode() {
        return QRomLogBaseConfig.LOG_BOTH;
    }

    @Override
    public String getPackageName() {
        return "com.pacewear.walletservice";
    }

}
