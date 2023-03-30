package cn.lliiooll.gmca.service.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;

public class JavaUtils {
    public static boolean isConnected(String s) {
        try {
            URL u = new URL(s);
            AtomicBoolean ok = new AtomicBoolean(true);
            AtomicBoolean success = new AtomicBoolean(false);
            new Thread(() -> {
                try {
                    HttpURLConnection conn = (HttpURLConnection) u.openConnection();
                    conn.setReadTimeout(2000);
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(2000);
                    success.set(conn.getResponseCode() == HttpURLConnection.HTTP_OK);
                    ok.set(false);
                } catch (IOException ignored) {
                    success.set(false);
                    ok.set(false);
                }
            }).start();
            while (ok.get()) {
                Thread.sleep(10L);
            }
            return success.get();
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }
}
