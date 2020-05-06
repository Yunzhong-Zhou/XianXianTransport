package com.transport.xianxian.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.transport.xianxian.R;
import com.transport.xianxian.activity.LoginActivity;
import com.transport.xianxian.utils.CommonUtil;
import com.transport.xianxian.utils.LocalUserInfo;
import com.transport.xianxian.utils.MyLogger;
import com.transport.xianxian.utils.aes.AES;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class OkHttpClientManager {
    private static final String TAG = "OkHttpClientManager";

//    public static String IMGHOST = "http://192.168.0.188";//图片地址
//    public static String HOST = "http://192.168.0.188";//接口地址
//    public static String IMGHOST = "http://192.168.0.200";//图片地址
//    public static String HOST = "http://192.168.0.200";

    //测试地址
//    public static String IMGHOST = "http://wuliu.zhenyongkj.com";//图片地址
//    public static String HOST = "http://wuliu.zhenyongkj.com";//接口地址

    //正式地址
    public static String IMGHOST = "http://wuliu.xianxianla.com";//图片地址
    public static String HOST = "http://wuliu.xianxianla.com";//接口地址
    public boolean isLogin = false;
    public static boolean isJiaMi = false;
    public int RequestNum = 0;

    static Context mContext;

    private static OkHttpClientManager mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;
    private Gson mGson;

    private HttpsDelegate mHttpsDelegate = new HttpsDelegate();
    private DownloadDelegate mDownloadDelegate = new DownloadDelegate();
    private DisplayImageDelegate mDisplayImageDelegate = new DisplayImageDelegate();
    private GetDelegate mGetDelegate = new GetDelegate();
    private UploadDelegate mUploadDelegate = new UploadDelegate();
    private PostDelegate mPostDelegate = new PostDelegate();

    private OkHttpClientManager() {
        mOkHttpClient = new OkHttpClient();
        //cookie enabled
        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        mDelivery = new Handler(Looper.getMainLooper());
        mGson = new Gson();

        /*just for test !!!*/
        mOkHttpClient.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

    }

    public static OkHttpClientManager getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpClientManager.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpClientManager();
                }
            }
        }
        return mInstance;
    }

    public GetDelegate getGetDelegate() {
        return mGetDelegate;
    }

    public PostDelegate getPostDelegate() {
        return mPostDelegate;
    }

    private HttpsDelegate _getHttpsDelegate() {
        return mHttpsDelegate;
    }

    private DownloadDelegate _getDownloadDelegate() {
        return mDownloadDelegate;
    }

    private DisplayImageDelegate _getDisplayImageDelegate() {
        return mDisplayImageDelegate;
    }

    private UploadDelegate _getUploadDelegate() {
        return mUploadDelegate;
    }


    public static DisplayImageDelegate getDisplayImageDelegate() {
        return getInstance()._getDisplayImageDelegate();
    }

    public static DownloadDelegate getDownloadDelegate() {
        return getInstance()._getDownloadDelegate();
    }

    public static UploadDelegate getUploadDelegate() {
        return getInstance()._getUploadDelegate();
    }

    public static HttpsDelegate getHttpsDelegate() {
        return getInstance()._getHttpsDelegate();
    }

    /**
     * ============Get方便的访问方式============
     */

    public static void getAsyn(Context context, String url, ResultCallback callback) {
        mContext = context;
        isJiaMi = false;
//        url = url + "&lang_type=" + LocalUserInfo.getInstance(mContext).getLanguage_Type();
        MyLogger.i(">>>>>>get请求地址：" + HOST + url);
        getInstance().getGetDelegate().getAsyn(HOST + url, callback, null);
    }

    //get加密  -  无用
    public static void getAsyn(Context context, String url, ResultCallback callback, boolean tag) {
        mContext = context;
        if (tag == true) {
            //需要加密
            isJiaMi = true;
            String[] urls = url.split("\\?");
            //截取?之前的字符串
            String url1 = urls[0];
            //截取?之后的字符串
            String url2 = urls[1];

            //封装成map
            String[] urls1 = url2.split("&");
            Map<String, String> params = new HashMap<>();
            for (int i = 0; i < urls1.length; i++) {
                String[] urls2 = urls1[i].split("=");
                if (urls2[0].equals("mobile")) {
                    params.put("mobile", urls2[1]);
                }
                if (urls2[0].equals("type")) {
                    params.put("type", urls2[1]);
                }
                if (urls2[0].equals("mobile_state_code")) {
                    params.put("mobile_state_code", urls2[1]);
                }
            }

            //map转json
            Gson gson = new Gson();
            String jsonStr = gson.toJson(params);
            MyLogger.i(">>>>>>>>" + jsonStr.toString());
            try {
                AES mAes = new AES();
                //string转byte
                byte[] mBytes = jsonStr.toString().getBytes("UTF-8");
                //加密
                String enString = mAes.encrypt(mBytes);
                MyLogger.i("加密后：" + enString);
//                        HttpPostData(enString);
                //拼接字符串
                url = url1 + "?param=" + enString;
            } catch (Exception e) {
                MyLogger.i(">>>>>>>>>>>数据加密失败");
            }
        } else {
            //不需要加密
            isJiaMi = false;

        }
//        url = url + "&language_type=" + LocalUserInfo.getInstance(mContext).getLanguage_Type();
        MyLogger.i(">>>>>>get请求地址：" + HOST + url);
        getInstance().getGetDelegate().getAsyn(HOST + url, callback, null);
    }
    /*public static void getAsyn(String url, ResultCallback callback, Object tag) {
        getInstance().getGetDelegate().getAsyn(url, callback, tag);
    }*/

    /**
     * ============POST方便的访问方式===============
     */
    public static void postAsyn(String url, Map<String, String> params, final ResultCallback callback) {
        getInstance().getPostDelegate().postAsyn(url, params, callback, null);
    }
    public static void postAsyn(Context context, String url, Map<String, String> params, final ResultCallback callback) {
        mContext = context;
        isJiaMi = false;
//        params.put("lang_type", LocalUserInfo.getInstance(mContext).getLanguage_Type());
        MyLogger.i(">>>>post接口：>>" + HOST + url + ">>>>>传入的参数：" + params);
        getInstance().getPostDelegate().postAsyn(HOST + url, params, callback, null);
    }

   /* public static void postAsyn(String url, String bodyStr, final ResultCallback callback) {
        getInstance().getPostDelegate().postAsyn(url, bodyStr, callback, null);

    }*/

    /*public static void postAsyn(String url, Param[] params, final ResultCallback callback, Object tag) {

        getInstance().getPostDelegate().postAsyn(url, params, callback, tag);
    }*/

    public static void postAsyn(Context context, String url, Map<String, String> params, final ResultCallback callback, boolean tag) {
        mContext = context;
        if (tag == true) {
            //需要加密
            isJiaMi = true;
            try {
                AES mAes = new AES();
                //map转json
                Gson gson = new Gson();

                String jsonStr = gson.toJson(params);
                //string转byte
                byte[] mBytes = jsonStr.toString().getBytes("UTF-8");
                //加密
                String enString = mAes.encrypt(mBytes);
                MyLogger.i("加密后：" + enString);
//                        HttpPostData(enString);
                Map<String, String> params1 = new HashMap<>();
//                params1.put("app_type", "1");//验证是否为Android
                params1.put("param", enString);
//                params1.put("lang_type", LocalUserInfo.getInstance(mContext).getLanguage_Type());
                MyLogger.i(">>>>post接口：>>" + HOST + url + ">>>>>传入的参数：" + params1);
                getInstance().getPostDelegate().postAsyn(HOST + url, params1, callback, null);
            } catch (Exception e) {
                MyLogger.i(">>>>>>>>>>>数据加密失败");
            }
        } else {
            //不需要加密
            isJiaMi = false;
//            params.put("lang_type", LocalUserInfo.getInstance(mContext).getLanguage_Type());
            MyLogger.i(">>>>post接口：>>" + HOST + url + ">>>>>传入的参数：" + params);
            getInstance().getPostDelegate().postAsyn(HOST + url, params, callback, null);
        }
        /*if (HOST.equals("")) {
//            HOST = "http://app.zcashplan.com";
            HOST = "http://192.168.0.188";
        }
        MyLogger.i(">>>>post接口：>>" + HOST + url + ">>>>>传入的参数：" + params);
        getInstance().getPostDelegate().postAsyn(HOST + url, params, callback, tag);*/
    }

    /*public static void postAsyn(String url, String bodyStr, final ResultCallback callback, Object tag) {
        getInstance().getPostDelegate().postAsyn(url, bodyStr, callback, tag);
    }*/

    //带文件上传，多个文件和其他参数
    public static void postAsyn(Context context, String url, String[] fileKeys, File[] files, Map<String, String> params, ResultCallback callback) {
        mContext = context;
        isJiaMi = false;
//        params.put("lang_type", LocalUserInfo.getInstance(mContext).getLanguage_Type());
        MyLogger.i(">>>>post接口：>>" + HOST + url + ">>>>>传入的参数：" + params);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < fileKeys.length; i++) {
            sb.append(fileKeys[i] + ": " + files[i].getAbsolutePath() + "\n");
        }
        MyLogger.i("RequestData", sb.toString() + params.toString());
        Param[] params1 = map2Params(params);
        getInstance()._getUploadDelegate().postAsyn(HOST + url, fileKeys, files, params1, callback, null);
    }

    //带文件上传，多个文件和其他参数（加密）
    public static void postAsyn(Context context, String url, String[] fileKeys, File[] files, Map<String, String> params, ResultCallback callback, boolean tag) {
        mContext = context;

        if (tag == true) {
            //需要加密
            isJiaMi = true;
            try {
                AES mAes = new AES();
                //map转json
                Gson gson = new Gson();
                String jsonStr = gson.toJson(params);
                //string转byte
                byte[] mBytes = jsonStr.toString().getBytes("UTF-8");
                //加密
                String enString = mAes.encrypt(mBytes);
                MyLogger.i("加密后：" + enString);
//                        HttpPostData(enString);
                Map<String, String> params1 = new HashMap<>();
//                params1.put("app_type", "1");//验证是否为Android
                params1.put("param", enString);
//                params1.put("lang_type", LocalUserInfo.getInstance(mContext).getLanguage_Type());
                MyLogger.i(">>>>post接口：>>" + HOST + url + ">>>>>传入的参数：" + params1);
                Param[] params2 = map2Params(params1);
                getInstance()._getUploadDelegate().postAsyn(HOST + url, fileKeys, files, params2, callback, null);
            } catch (Exception e) {
                MyLogger.i(">>>>>>>>>>>数据加密失败");
            }
        } else {
            isJiaMi = false;
//            params.put("lang_type", LocalUserInfo.getInstance(mContext).getLanguage_Type());
            MyLogger.i(">>>>post接口：>>" + HOST + url + ">>>>>传入的参数：" + params);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < fileKeys.length; i++) {
                sb.append(fileKeys[i] + ": " + files[i].getAbsolutePath() + "\n");
            }
            MyLogger.i("RequestData", sb.toString() + params.toString());
            Param[] params1 = map2Params(params);
            getInstance()._getUploadDelegate().postAsyn(HOST + url, fileKeys, files, params1, callback, null);
        }
    }

    //下载图片
    public static void downloadAsyn(String url, String destFileDir, ResultCallback callback) {
        getInstance()._getDownloadDelegate().downloadAsyn(url, destFileDir, callback);
    }

    //=============便利的访问方式===============


    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }


    private Param[] validateParam(Param[] params) {
        if (params == null)
            return new Param[0];
        else return params;
    }

    public static Param[] map2Params(Map<String, String> params) {
        if (params == null) return new Param[0];
        int size = params.size();
        Param[] res = new Param[size];
        Set<Map.Entry<String, String>> entries = params.entrySet();
        int i = 0;
        for (Map.Entry<String, String> entry : entries) {
            res[i++] = new Param(entry.getKey(), entry.getValue());
        }
        return res;
    }

    private void deliveryResult(ResultCallback callback, Request request) {
        if (callback == null) callback = DEFAULT_RESULT_CALLBACK;
        final ResultCallback resCallBack = callback;
        //UI thread
        callback.onBefore(request);
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                sendFailedStringCallback(request, mContext.getString(R.string.app_err), e, resCallBack);
                RequestNum++;
                MyLogger.i(">>>>>>>>>" + RequestNum);
                if (RequestNum >= 3) {
                    /*if (HOST.equals("http://ios.zcashplan.com")) {
                        HOST = "http://app.zcashplan.com";
                    } else {
                        HOST = "http://ios.zcashplan.com";
                    }*/
                    MyLogger.i("》》》》》更改后的地址" + HOST);
                    RequestNum = 0;
                }

            }

            @Override
            public void onResponse(final Response response) {
                RequestNum = 0;
                try {
                    String string = response.body().string();
                    if (!string.equals("")) {
                        if (isJiaMi == true) {
                            //判断不为json数据-解密
                            if (!isjson(string)) {
                                //需要解密
                                MyLogger.i("解密前数据", string);
                                string = new AES().decrypt(string);
                                MyLogger.i("解密后：" + string);
                            }
                        }

                        MyLogger.i("请求到的数据onResponse", string);
//                    sendSuccessResultCallback(string, resCallBack);
                        JSONObject mJsonObject = new JSONObject(string);
                        String result_code = mJsonObject.getString("code");
                        //1	成功
                        //2	操作数据失败
                        //3	验证失败
                        //4	未知错误
                        //5	认证成功
                        //6	认证失败
                        //7	数据不可用
                        //8	数据存在
                        //9	数据不存在
                        //10	上传文件无效
                        //11	操作限制
                        //12	无效
                        //13	过期
                        if (result_code.equals("1") || result_code.equals("8") || result_code.equals("9") || result_code.equals("200000")) {
                            //数据请求成功-解析数据
                            String result = mJsonObject.getString("data");
                            if (resCallBack.mType == String.class) {
                                sendSuccessResultCallback(string, resCallBack);
                            } else {
                                Object o = mGson.fromJson(result, resCallBack.mType);
                                sendSuccessResultCallback(o, resCallBack);
                            }
                        } else if (result_code.equals("13") || result_code.equals("12") || result_code.equals("4")) {
                            //token失效-重新登录
                            LocalUserInfo.getInstance(mContext).setUserId("");
                            CommonUtil.gotoActivity(mContext, LoginActivity.class);

                        } else if (result_code.equals("100") || result_code.equals("101") || result_code.equals("102")) {
                            String reason = mJsonObject.getString("msg");
                            //特殊情况-登录后手机不是注册时的手机
                            sendFailedStringCallback(response.request(), result_code, new Exception(reason), resCallBack);
                        } else {
                            String reason = mJsonObject.getString("msg");
                            //数据请求失败
                            sendFailedStringCallback(response.request(), reason, null, resCallBack);
                        }
                    } else {
                        sendFailedStringCallback(response.request(), mContext.getString(R.string.app_err), null, resCallBack);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    sendFailedStringCallback(response.request(), mContext.getString(R.string.app_err1), e, resCallBack);
                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), mContext.getString(R.string.app_err1), e, resCallBack);
                } catch (com.google.gson.JsonParseException e) {
                    sendFailedStringCallback(response.request(), mContext.getString(R.string.app_err1), e, resCallBack);
                } catch (Exception e) {
                    sendFailedStringCallback(response.request(), mContext.getString(R.string.app_err1), e, resCallBack);
                }

            }
        });
    }

    //判断是否为json数据
    private boolean isjson(String string) {
        try {
//            JSONObject jsonStr= JSONObject.parseObject(string);
            com.alibaba.fastjson.JSONObject jsonStr = com.alibaba.fastjson.JSONObject.parseObject(string);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    private void sendFailedStringCallback(final Request request, final String info, final Exception e, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(request, info, e);
                callback.onAfter();
            }
        });
    }

    private void sendSuccessResultCallback(final Object object, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(object);
                callback.onAfter();
            }
        });
    }

    private String getFileName(String path) {
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }

    private Request buildPostFormRequest(String url, Param[] params, Object tag) {
        if (params == null) {
            params = new Param[0];
        }
        FormEncodingBuilder builder = new FormEncodingBuilder();
        for (Param param : params) {
            builder.add(param.key, param.value);
        }
        RequestBody requestBody = builder.build();

        Request.Builder reqBuilder = new Request.Builder();
        reqBuilder.url(url)
                .post(requestBody);

        if (tag != null) {
            reqBuilder.tag(tag);
        }
        return reqBuilder.build();
    }

    public static void cancelTag(Object tag) {
        getInstance()._cancelTag(tag);
    }

    private void _cancelTag(Object tag) {
        mOkHttpClient.cancel(tag);
    }

    public static OkHttpClient getClinet() {
        return getInstance().client();
    }

    public OkHttpClient client() {
        return mOkHttpClient;
    }


    public static abstract class ResultCallback<T> {
        Type mType;

        public ResultCallback() {
            mType = getSuperclassTypeParameter(getClass());
        }

        static Type getSuperclassTypeParameter(Class<?> subclass) {
            Type superclass = subclass.getGenericSuperclass();
            if (superclass instanceof Class) {
                throw new RuntimeException("缺少类型参数");
            }
            ParameterizedType parameterized = (ParameterizedType) superclass;
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        }

        public void onBefore(Request request) {
        }

        public void onAfter() {
        }

        public abstract void onError(Request request, String info, Exception e);

        public abstract void onResponse(T response);
    }

    private final ResultCallback<String> DEFAULT_RESULT_CALLBACK = new ResultCallback<String>() {
        @Override
        public void onError(Request request, String info, Exception e) {

        }

        @Override
        public void onResponse(String response) {

        }
    };


    public static class Param {
        public Param() {
        }

        public Param(String key, String value) {
            this.key = key;
            this.value = value;
        }

        String key;
        String value;
    }

    //====================PostDelegate=======================
    public class PostDelegate {
        private final MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream;charset=utf-8");
        private final MediaType MEDIA_TYPE_STRING = MediaType.parse("text/plain;charset=utf-8");

        public Response post(String url, Param[] params) throws IOException {
            return post(url, params, null);
        }

        /**
         * 同步的Post请求
         */
        public Response post(String url, Param[] params, Object tag) throws IOException {
            Request request = buildPostFormRequest(url, params, tag);
            Response response = mOkHttpClient.newCall(request).execute();
            return response;
        }

        public String postAsString(String url, Param[] params) throws IOException {
            return postAsString(url, params, null);
        }

        /**
         * 同步的Post请求
         */
        public String postAsString(String url, Param[] params, Object tag) throws IOException {
            Response response = post(url, params, tag);
            return response.body().string();
        }

        public void postAsyn(String url, Map<String, String> params, final ResultCallback callback) {
            postAsyn(url, params, callback, null);
        }

        public void postAsyn(String url, Map<String, String> params, final ResultCallback callback, Object tag) {
            Param[] paramsArr = map2Params(params);
            postAsyn(url, paramsArr, callback, tag);
        }

        public void postAsyn(String url, Param[] params, final ResultCallback callback) {
            postAsyn(url, params, callback, null);
        }

        /**
         * 异步的post请求
         */
        public void postAsyn(String url, Param[] params, final ResultCallback callback, Object tag) {
            Request request = buildPostFormRequest(url, params, tag);
            deliveryResult(callback, request);
        }

        /**
         * 同步的Post请求:直接将bodyStr以写入请求体
         */
        public Response post(String url, String bodyStr) throws IOException {
            return post(url, bodyStr);
        }

        public Response post(String url, String bodyStr, Object tag) throws IOException {
            RequestBody body = RequestBody.create(MEDIA_TYPE_STRING, bodyStr);
            Request request = buildPostRequest(url, body, tag);
            Response response = mOkHttpClient.newCall(request).execute();
            return response;
        }

        /**
         * 同步的Post请求:直接将bodyFile以写入请求体
         */
        public Response post(String url, File bodyFile) throws IOException {
            return post(url, bodyFile);
        }

        public Response post(String url, File bodyFile, Object tag) throws IOException {
            RequestBody body = RequestBody.create(MEDIA_TYPE_STREAM, bodyFile);
            Request request = buildPostRequest(url, body, tag);
            Response response = mOkHttpClient.newCall(request).execute();
            return response;
        }

        /**
         * 同步的Post请求
         */
        public Response post(String url, byte[] bodyBytes) throws IOException {
            return post(url, bodyBytes);
        }

        public Response post(String url, byte[] bodyBytes, Object tag) throws IOException {
            RequestBody body = RequestBody.create(MEDIA_TYPE_STREAM, bodyBytes);
            Request request = buildPostRequest(url, body, tag);
            Response response = mOkHttpClient.newCall(request).execute();
            return response;
        }

        /**
         * 直接将bodyStr以写入请求体
         */
        public void postAsyn(String url, String bodyStr, final ResultCallback callback) {
            postAsyn(url, bodyStr, callback);
        }

        public void postAsyn(String url, String bodyStr, final ResultCallback callback, Object tag) {
            postAsynWithMediaType(url, bodyStr, MediaType.parse("text/plain;charset=utf-8"), callback, tag);
        }

        /**
         * 直接将bodyBytes以写入请求体
         */
        public void postAsyn(String url, byte[] bodyBytes, final ResultCallback callback) {
            postAsyn(url, bodyBytes, callback, null);
        }

        public void postAsyn(String url, byte[] bodyBytes, final ResultCallback callback, Object tag) {
            postAsynWithMediaType(url, bodyBytes, MediaType.parse("application/octet-stream;charset=utf-8"), callback, tag);
        }

        /**
         * 直接将bodyFile以写入请求体
         */
        public void postAsyn(String url, File bodyFile, final ResultCallback callback) {
            postAsyn(url, bodyFile, callback, null);
        }

        public void postAsyn(String url, File bodyFile, final ResultCallback callback, Object tag) {
            postAsynWithMediaType(url, bodyFile, MediaType.parse("application/octet-stream;charset=utf-8"), callback, tag);
        }

        /**
         * 直接将bodyStr以写入请求体
         */
        public void postAsynWithMediaType(String url, String bodyStr, MediaType type, final ResultCallback callback, Object tag) {
            RequestBody body = RequestBody.create(type, bodyStr);
            Request request = buildPostRequest(url, body, tag);
            deliveryResult(callback, request);
        }

        /**
         * 直接将bodyBytes以写入请求体
         */
        public void postAsynWithMediaType(String url, byte[] bodyBytes, MediaType type, final ResultCallback callback, Object tag) {
            RequestBody body = RequestBody.create(type, bodyBytes);
            Request request = buildPostRequest(url, body, tag);
            deliveryResult(callback, request);
        }

        /**
         * 直接将bodyFile以写入请求体
         */
        public void postAsynWithMediaType(String url, File bodyFile, MediaType type, final ResultCallback callback, Object tag) {
            RequestBody body = RequestBody.create(type, bodyFile);
            Request request = buildPostRequest(url, body, tag);
            deliveryResult(callback, request);
        }


        /**
         * post构建Request的方法
         *
         * @param url
         * @param body
         * @return
         */
        private Request buildPostRequest(String url, RequestBody body, Object tag) {
            Request.Builder builder = new Request.Builder()
                    .url(url)
                    .post(body);
            if (tag != null) {
                builder.tag(tag);
            }
            Request request = builder.build();
            return request;
        }


    }

    //====================GetDelegate=======================
    public class GetDelegate {

        private Request buildGetRequest(String url, Object tag) {
            Request.Builder builder = new Request.Builder()
                    .url(url);

            if (tag != null) {
                builder.tag(tag);
            }

            return builder.build();
        }

        /**
         * 通用的方法
         */
        public Response get(Request request) throws IOException {
            Call call = mOkHttpClient.newCall(request);
            Response execute = call.execute();
            return execute;
        }

        /**
         * 同步的Get请求
         */
        public Response get(String url) throws IOException {
            return get(url);
        }

        public Response get(String url, Object tag) throws IOException {
            final Request request = buildGetRequest(url, tag);
            return get(request);
        }


        /**
         * 同步的Get请求
         */
        public String getAsString(String url) throws IOException {
            return getAsString(url);
        }

        public String getAsString(String url, Object tag) throws IOException {
            Response execute = get(url, tag);
            return execute.body().string();
        }

        /**
         * 通用的方法
         */
        public void getAsyn(Request request, ResultCallback callback) {
            deliveryResult(callback, request);
        }

        /**
         * 异步的get请求
         */
        public void getAsyn(String url, final ResultCallback callback) {
            getAsyn(url, callback);
        }

        public void getAsyn(String url, final ResultCallback callback, Object tag) {
            final Request request = buildGetRequest(url, tag);
            getAsyn(request, callback);
        }
    }


    //====================UploadDelegate=======================

    /**
     * 上传相关的模式
     */
    public class UploadDelegate {
        /**
         * 同步基于post的文件上传:上传单个文件
         */
        public Response post(String url, String fileKey, File file, Object tag) throws IOException {
            return post(url, new String[]{fileKey}, new File[]{file}, null, tag);
        }

        /**
         * 同步基于post的文件上传:上传多个文件以及携带key-value对：主方法
         */
        public Response post(String url, String[] fileKeys, File[] files, Param[] params, Object tag) throws IOException {
            Request request = buildMultipartFormRequest(url, files, fileKeys, params, tag);
            return mOkHttpClient.newCall(request).execute();
        }

        /**
         * 同步单文件上传
         */
        public Response post(String url, String fileKey, File file, Param[] params, Object tag) throws IOException {
            return post(url, new String[]{fileKey}, new File[]{file}, params, tag);
        }

        /**
         * 异步基于post的文件上传文件:主方法
         */
        public void postAsyn(String url, String[] fileKeys, File[] files, Param[] params, ResultCallback callback, Object tag) {
            Request request = buildMultipartFormRequest(url, files, fileKeys, params, tag);
            deliveryResult(callback, request);
        }

        /**
         * 异步基于post的文件上传:单文件不带参数上传
         */
        /*public void postAsyn(String url, String fileKey, File file, ResultCallback callback, Object tag) throws IOException
        {
            postAsyn(url, new String[]{fileKey}, new File[]{file}, null, callback, tag);
        }*/

        /**
         * 异步基于post的文件上传，单文件且携带其他form参数上传
         */
        public void postAsyn(String url, String fileKey, File file, Param[] params, ResultCallback callback, Object tag) {
            postAsyn(url, new String[]{fileKey}, new File[]{file}, params, callback, tag);
        }

        private Request buildMultipartFormRequest(String url, File[] files,
                                                  String[] fileKeys, Param[] params, Object tag) {
            params = validateParam(params);

            MultipartBuilder builder = new MultipartBuilder()
                    .type(MultipartBuilder.FORM);

            for (Param param : params) {
                builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + param.key + "\""),
                        RequestBody.create(null, param.value));
            }
            if (files != null) {
                RequestBody fileBody = null;
                for (int i = 0; i < files.length; i++) {
                    File file = files[i];
                    String fileName = file.getName();
                    fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                    //TODO 根据文件名设置contentType
                    /*if (fileKeys[i].toString().equals("slMedia")) {
                        //单个视频
                        builder.addPart(Headers.of("Content-Disposition",
                                "form-data; name=\"" + fileKeys[i] + "\"; filename=\"" + fileName + "\""),
                                fileBody);
                    } else if (fileKeys[i].toString().equals("slExcelFile")) {
                        //单个excel文件
                        builder.addPart(Headers.of("Content-Disposition",
                                "form-data; name=\"" + fileKeys[i] + "\"; filename=\"" + fileName + "\""),
                                fileBody);
                    }  else if (fileKeys[i].toString().equals("pfCover")) {//封面
                        //单个文件
                        builder.addPart(Headers.of("Content-Disposition",
                                "form-data; name=\"" + fileKeys[i] + "\"; filename=\"" + fileName + "\""),
                                fileBody);
                    } else if (fileKeys[i].toString().equals("slFile")) {
                        //模拟表单 key[] = file,file,file
                        builder.addPart(Headers.of("Content-Disposition",
                                "form-data; name=\"" + fileKeys[i] + "[]\"; filename=\"" + fileName + "\""),
                                fileBody);
                    } else if (fileKeys[i].toString().equals("pfCert")) {//专家证件、
                        //模拟表单 key[] = file,file,file
                        builder.addPart(Headers.of("Content-Disposition",
                                "form-data; name=\"" + fileKeys[i] + "[]\"; filename=\"" + fileName + "\""),
                                fileBody);
                    }else {
                        //模拟表单 key[] = file,file,file
                        builder.addPart(Headers.of("Content-Disposition",
                                "form-data; name=\"" + fileKeys[i] + "[]\"; filename=\"" + fileName + "\""),
                                fileBody);
                    }*/
                    //模拟表单 key[] = file,file,file
                    /*builder.addPart(Headers.of("Content-Disposition",
                            "form-data; name=\"" + fileKeys[i] + "[]\"; filename=\"" + fileName + "\""),
                            fileBody);*/
                    if (fileKeys[i].toString().equals("pic_list")) {
                        //模拟表单 key[] = file,file,file
                        builder.addPart(Headers.of("Content-Disposition",
                                "form-data; name=\"" + fileKeys[i] + "[]\"; filename=\"" + fileName + "\""),
                                fileBody);
                    } else {
                        //一个一个上传
                        builder.addPart(Headers.of("Content-Disposition",
                                "form-data; name=\"" + fileKeys[i] + "\"; filename=\"" + fileName + "\""),
                                fileBody);
                    }
//                    MyLogger.i(">>>>>>>>fileKeys[i]:"+fileKeys[i].toString()+">>>>>>>>>filename:"+fileName);
                }

            }
            RequestBody requestBody = builder.build();
            return new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .tag(tag)
                    .build();
        }

    }

    //====================DisplayImageDelegate=======================

    /**
     * 加载图片相关
     */
    public class DisplayImageDelegate {
        /**
         * 加载图片
         */
        public void displayImage(final ImageView view, final String url, final int errorResId, final Object tag) {
            final Request request = new Request.Builder()
                    .url(url)
                    .build();
            Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    setErrorResId(view, errorResId);
                }

                @Override
                public void onResponse(Response response) {
                    InputStream is = null;
                    try {
                        is = response.body().byteStream();
                        ImageUtils.ImageSize actualImageSize = ImageUtils.getImageSize(is);
                        ImageUtils.ImageSize imageViewSize = ImageUtils.getImageViewSize(view);
                        int inSampleSize = ImageUtils.calculateInSampleSize(actualImageSize, imageViewSize);
                        try {
                            is.reset();
                        } catch (IOException e) {
                            response = mGetDelegate.get(url, tag);
                            is = response.body().byteStream();
                        }

                        BitmapFactory.Options ops = new BitmapFactory.Options();
                        ops.inJustDecodeBounds = false;
                        ops.inSampleSize = inSampleSize;
                        final Bitmap bm = BitmapFactory.decodeStream(is, null, ops);
                        mDelivery.post(new Runnable() {
                            @Override
                            public void run() {
                                view.setImageBitmap(bm);
                            }
                        });
                    } catch (Exception e) {
                        setErrorResId(view, errorResId);

                    } finally {
                        if (is != null) try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });


        }

        public void displayImage(final ImageView view, String url) {
            displayImage(view, url, -1, null);
        }

        public void displayImage(final ImageView view, String url, Object tag) {
            displayImage(view, url, -1, tag);
        }

        private void setErrorResId(final ImageView view, final int errorResId) {
            mDelivery.post(new Runnable() {
                @Override
                public void run() {
                    view.setImageResource(errorResId);
                }
            });
        }
    }


    //====================DownloadDelegate=======================

    /**
     * 下载相关的模式
     */
    public class DownloadDelegate {
        /**
         * 异步下载文件
         *
         * @param url
         * @param destFileDir 本地文件存储的文件夹
         * @param callback
         */
        public void downloadAsyn(final String url, final String destFileDir, final ResultCallback callback, Object tag) {
            final Request request = new Request.Builder()
                    .url(url)
                    .tag(tag)
                    .build();
            final Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(final Request request, final IOException e) {
                    sendFailedStringCallback(request, "", e, callback);
                }

                @Override
                public void onResponse(Response response) {
                    InputStream is = null;
                    byte[] buf = new byte[2048];
                    int len = 0;
                    FileOutputStream fos = null;
                    try {
                        is = response.body().byteStream();

                        File dir = new File(destFileDir);
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        File file = new File(dir, getFileName(url));
                        fos = new FileOutputStream(file);
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                        }
                        fos.flush();
                        //如果下载文件成功，第一个参数为文件的绝对路径
                        sendSuccessResultCallback(file.getAbsolutePath(), callback);
                    } catch (IOException e) {
                        sendFailedStringCallback(response.request(), "", e, callback);
                    } finally {
                        try {
                            if (is != null) is.close();
                        } catch (IOException e) {
                        }
                        try {
                            if (fos != null) fos.close();
                        } catch (IOException e) {
                        }
                    }

                }
            });
        }


        public void downloadAsyn(final String url, final String destFileDir, final ResultCallback callback) {
            downloadAsyn(url, destFileDir, callback, null);
        }
    }


    //====================HttpsDelegate=======================

    /**
     * Https相关模块
     */
    public class HttpsDelegate {

        public void setCertificates(InputStream... certificates) {
            setCertificates(certificates, null, null);
        }

        public TrustManager[] prepareTrustManager(InputStream... certificates) {
            if (certificates == null || certificates.length <= 0) return null;
            try {

                CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                keyStore.load(null);
                int index = 0;
                for (InputStream certificate : certificates) {
                    String certificateAlias = Integer.toString(index++);
                    keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
                    try {
                        if (certificate != null)
                            certificate.close();
                    } catch (IOException e)

                    {
                    }
                }
                TrustManagerFactory trustManagerFactory = null;

                trustManagerFactory = TrustManagerFactory.
                        getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init(keyStore);

                TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

                return trustManagers;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        public KeyManager[] prepareKeyManager(InputStream bksFile, String password) {
            try {
                if (bksFile == null || password == null) return null;

                KeyStore clientKeyStore = KeyStore.getInstance("BKS");
                clientKeyStore.load(bksFile, password.toCharArray());
                KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                keyManagerFactory.init(clientKeyStore, password.toCharArray());
                return keyManagerFactory.getKeyManagers();

            } catch (KeyStoreException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (UnrecoverableKeyException e) {
                e.printStackTrace();
            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public void setCertificates(InputStream[] certificates, InputStream bksFile, String password) {
            try {
                TrustManager[] trustManagers = prepareTrustManager(certificates);
                KeyManager[] keyManagers = prepareKeyManager(bksFile, password);
                SSLContext sslContext = SSLContext.getInstance("TLS");

                sslContext.init(keyManagers, new TrustManager[]{new MyTrustManager(chooseTrustManager(trustManagers))}, new SecureRandom());
                mOkHttpClient.setSslSocketFactory(sslContext.getSocketFactory());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            }
        }

        private X509TrustManager chooseTrustManager(TrustManager[] trustManagers) {
            for (TrustManager trustManager : trustManagers) {
                if (trustManager instanceof X509TrustManager) {
                    return (X509TrustManager) trustManager;
                }
            }
            return null;
        }


        public class MyTrustManager implements X509TrustManager {
            private X509TrustManager defaultTrustManager;
            private X509TrustManager localTrustManager;

            public MyTrustManager(X509TrustManager localTrustManager) throws NoSuchAlgorithmException, KeyStoreException {
                TrustManagerFactory var4 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                var4.init((KeyStore) null);
                defaultTrustManager = chooseTrustManager(var4.getTrustManagers());
                this.localTrustManager = localTrustManager;
            }


            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                try {
                    defaultTrustManager.checkServerTrusted(chain, authType);
                } catch (CertificateException ce) {
                    localTrustManager.checkServerTrusted(chain, authType);
                }
            }


            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }

    }

    //====================ImageUtils=======================
    public static class ImageUtils {
        /**
         * 根据InputStream获取图片实际的宽度和高度
         *
         * @param imageStream
         * @return
         */
        public static ImageSize getImageSize(InputStream imageStream) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(imageStream, null, options);
            return new ImageSize(options.outWidth, options.outHeight);
        }

        public static class ImageSize {
            int width;
            int height;

            public ImageSize() {
            }

            public ImageSize(int width, int height) {
                this.width = width;
                this.height = height;
            }

            @Override
            public String toString() {
                return "ImageSize{" +
                        "width=" + width +
                        ", height=" + height +
                        '}';
            }
        }

        public static int calculateInSampleSize(ImageSize srcSize, ImageSize targetSize) {
            // 源图片的宽度
            int width = srcSize.width;
            int height = srcSize.height;
            int inSampleSize = 1;

            int reqWidth = targetSize.width;
            int reqHeight = targetSize.height;

            if (width > reqWidth && height > reqHeight) {
                // 计算出实际宽度和目标宽度的比例
                int widthRatio = Math.round((float) width / (float) reqWidth);
                int heightRatio = Math.round((float) height / (float) reqHeight);
                inSampleSize = Math.max(widthRatio, heightRatio);
            }
            return inSampleSize;
        }

        /**
         * 根据ImageView获取当的压缩的宽和高
         *
         * @param view
         * @return
         */
        public static ImageSize getImageViewSize(View view) {

            ImageSize imageSize = new ImageSize();

            imageSize.width = getExpectWidth(view);
            imageSize.height = getExpectHeight(view);

            return imageSize;
        }

        /**
         * 根据view获得期望的高度
         *
         * @param view
         * @return
         */
        private static int getExpectHeight(View view) {

            int height = 0;
            if (view == null) return 0;

            final ViewGroup.LayoutParams params = view.getLayoutParams();
            //如果是WRAP_CONTENT，此时图片还没加载，getWidth根本无效
            if (params != null && params.height != ViewGroup.LayoutParams.WRAP_CONTENT) {
                height = view.getWidth(); // 获得实际的宽�?
            }
            if (height <= 0 && params != null) {
                height = params.height; // 获得布局文件中的声明的宽�?
            }

            if (height <= 0) {
                height = getImageViewFieldValue(view, "mMaxHeight");// 获得设置的最大的宽度
            }

            //如果宽度还是没有获取到，憋大招，使用屏幕的宽度
            if (height <= 0) {
                DisplayMetrics displayMetrics = view.getContext().getResources()
                        .getDisplayMetrics();
                height = displayMetrics.heightPixels;
            }

            return height;
        }

        /**
         * 根据view获得期望的宽度
         *
         * @param view
         * @return
         */
        private static int getExpectWidth(View view) {
            int width = 0;
            if (view == null) return 0;

            final ViewGroup.LayoutParams params = view.getLayoutParams();
            //如果是WRAP_CONTENT，此时图片还没加载，getWidth根本无效
            if (params != null && params.width != ViewGroup.LayoutParams.WRAP_CONTENT) {
                width = view.getWidth(); // 获得实际的宽�?
            }
            if (width <= 0 && params != null) {
                width = params.width; // 获得布局文件中的声明的宽�?
            }

            if (width <= 0)

            {
                width = getImageViewFieldValue(view, "mMaxWidth");// 获得设置的最大的宽度
            }
            //如果宽度还是没有获取到，憋大招，使用屏幕的宽�?
            if (width <= 0)

            {
                DisplayMetrics displayMetrics = view.getContext().getResources()
                        .getDisplayMetrics();
                width = displayMetrics.widthPixels;
            }

            return width;
        }

        /**
         * 通过反射获取imageview的某个属性性
         *
         * @param object
         * @param fieldName
         * @return
         */
        private static int getImageViewFieldValue(Object object, String fieldName) {
            int value = 0;
            try {
                Field field = ImageView.class.getDeclaredField(fieldName);
                field.setAccessible(true);
                int fieldValue = field.getInt(object);
                if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
                    value = fieldValue;
                }
            } catch (Exception e) {
            }
            return value;

        }
    }

}

