
package com.pacewear.walletservice;

import android.support.v7.app.ActionBarActivity;

import com.tencent.tws.api.HttpManager;
import com.tencent.tws.api.HttpRequestGeneralParams;

import android.os.Bundle;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    private void testJCE() {
//        HttpRequestGeneralParams params 
        HttpManager.getInstance(this).postGeneralHttpRequest(mParams, mHttpResponseListener);
    }
}
