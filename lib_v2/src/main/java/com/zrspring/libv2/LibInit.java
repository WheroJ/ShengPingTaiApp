package com.zrspring.libv2;

import android.content.Context;

import com.zhy.http.okhttp.https.HttpsUtils;
import com.zrspring.libv2.util.SPUtil;

/**
 * Created by shopping on 2017/12/22 11:03.
 * https://github.com/wheroj
 */

public class LibInit {
    private static LibInit instance;

    private boolean isDebug;
    private Context context;
    private HttpsUtils.SSLParams sslParams;

    private LibInit() {

    }

    public static LibInit getInstance() {
        if (instance == null) {
            synchronized (LibInit.class) {
                if (instance == null) {
                    instance = new LibInit();
                }
            }
        }
        return instance;
    }

    public HttpsUtils.SSLParams getSslParams() {
        return sslParams;
    }

    public void init(Context context) {
        this.context = context;

        SPUtil.init(context);
//        try { todo 需要添加证书  httpsrequest.cer 证书名称，根据实际证书名称更改
//            sslParams = HttpsUtils.getSslSocketFactory(new InputStream[]{context.getAssets().open("httpsrequest.cer")}
//            , null, null);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public Context getContext() {
        return context;
    }
    public void setDebug(boolean isDebug) {
        this.isDebug = isDebug;
    }

    public boolean getDebug() {
        return isDebug;
    }
}
