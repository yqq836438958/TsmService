
package com.pacewear.walletservice;

import android.app.Application;
import android.content.Context;

public class WalletApp extends Application {
    public static Context sGlobalCtx = null;

    @Override
    public void onCreate() {
        super.onCreate();

        Context ctx = getApplicationContext();
        sGlobalCtx = ctx;
    }
}
