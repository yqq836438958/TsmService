
package com.pacewear.walletservice.http;

public class HttpUrl {
    private static HttpUrl sInstance = null;

    public static HttpUrl get() {
        if (sInstance == null) {
            synchronized (HttpUrl.class) {
                sInstance = new HttpUrl();
            }
        }
        return sInstance;
    }

    private HttpUrl() {

    }

    public String getUrl() {
        return "";
    }
}
