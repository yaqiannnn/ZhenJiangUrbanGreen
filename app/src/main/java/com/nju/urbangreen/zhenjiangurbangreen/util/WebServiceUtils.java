package com.nju.urbangreen.zhenjiangurbangreen.util;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nju.urbangreen.zhenjiangurbangreen.map.MapActivity;
import com.nju.urbangreen.zhenjiangurbangreen.startup.WelcomeActivity;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.EOFException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Liwei on 2016/12/25.
 */
public class WebServiceUtils {
    //public static final String SOAP_ACTION = "http://tempuri.org/CheckUpdate";
    //public static final String OPERATION_NAME = "CheckUpdate";
    //public static final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";
    public static final String WSDL_TARGET_NAMESPACE = "http://services.ui.webbos.sjf.org/";
    public static final String SOAP_ADDRESS = "http://114.212.112.41/GreenLand/EXT_GreenLand/Mobile/Services/GLService.asmx";

    public static final String CHECK_UPDATE = "CheckUpdate";
    public static final String LOGIN = "Login";

    public static final String KEY_REFLACT_OPERATION_NAME = "wmn";
    public static final String KEY_REFLACT_OPERATION_PARAM = "wmp";
    public static final String OPERATION_NAME = "RequestServices";
    public static final String OPERATION_NAME_WITHOUT_USERINFO = "RequestServicesWithoutUserInfo";
    public static final String KEY_OPERATION_PARAM = "RequestInfo";
    /**
     * 登录名
     */
    public static final String KEY_USERNAME = "ln";
    /**
     * 密码
     */
    public static final String KEY_PASSWORD = "lp";
    /**
     * 是否成功
     */
    public static final String KEY_SUCCEED = "ok";
    /**
     * 结果说明
     */
    public static final int RESULT_SUCCEED = 100;
    /**
     * 错误信息
     */
    public static final String KEY_ERRMESSAGE = "msg";
    /**
     * 返回结果
     */
    public static final String KEY_RESULT = "ret";
    //public static boolean flag = false;
    //static Map<String, Object> results = new HashMap<String, Object>();

    private static Gson gson = new GsonBuilder().serializeNulls().create();

    private static Map<String, Object> callMethod(String operationName, Map<String, Object> params) {
        return callMethod(operationName, params, true);
    }

    private static Map<String, Object> callMethod(String operationName, Map<String, Object> params, Boolean identify) {
        ConnectivityManager connectivityManager;
        NetworkInfo networkInfo;
        connectivityManager = (ConnectivityManager) MyApplication.getContext().
                getSystemService(MyApplication.getContext().CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isAvailable()) {
            try {
                Thread.sleep(800);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        Map<String, Object> results = new HashMap<String, Object>();
        Map<String, Object> requestProperty = new HashMap<String, Object>();

        SoapObject request;
        if (identify) {
            String userName = SPUtils.get(MyApplication.getContext(), "username", "").toString();
            String password = SPUtils.get(MyApplication.getContext(), "password", "").toString();
            if(operationName.equals(LOGIN)){
                userName = params.get(KEY_USERNAME).toString();
                password = params.get(KEY_PASSWORD).toString();
                params = null;
            }
            request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);
            requestProperty.put(KEY_REFLACT_OPERATION_NAME, operationName);
            requestProperty.put(KEY_USERNAME, userName);
            requestProperty.put(KEY_PASSWORD, password);
        } else {
            request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME_WITHOUT_USERINFO);
            requestProperty.put(KEY_REFLACT_OPERATION_NAME, "NoUser_" + operationName);
        }


        requestProperty.put(KEY_REFLACT_OPERATION_PARAM, params);


        request.addProperty(KEY_OPERATION_PARAM, ZipUtils.compress(gson.toJson(requestProperty)));

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
        envelope.dotNet = true;
        envelope.encodingStyle = SoapSerializationEnvelope.ENC;
        envelope.setOutputSoapObject(request);

        HttpTransportSE transport = new HttpTransportSE(SOAP_ADDRESS);

        try {
            transport.call(WSDL_TARGET_NAMESPACE + OPERATION_NAME_WITHOUT_USERINFO, envelope);
            SoapPrimitive resultPrimitive = (SoapPrimitive) envelope.getResponse();
            String tmp = resultPrimitive.toString();
            String jsonString = ZipUtils.uncompress(tmp);
            results = gson.fromJson(jsonString, results.getClass());

        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            results.clear();
            results.put(KEY_SUCCEED, 99);
            results.put(KEY_ERRMESSAGE, "网络连接超时，请检查网络设置~");
            return results;
        } catch (SocketException e) {
            e.printStackTrace();
            results.clear();
            results.put(KEY_SUCCEED, 99);
            results.put(KEY_ERRMESSAGE, "网络异常，请重试~");
            return results;
        } catch (EOFException e) {
            e.printStackTrace();
            results.clear();
            results.put(KEY_SUCCEED, 99);
            results.put(KEY_ERRMESSAGE, "网络异常，请重试~");
            return results;
        } catch (Exception e) {
            e.printStackTrace();
            results.clear();
            results.put(KEY_SUCCEED, 99);
            results.put(KEY_ERRMESSAGE, e.getMessage());
            return results;
        }

        return results;
    }

    public static Map<String, Object> checkUpdate(String[] errorMessage) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("VersionCode", getVersion());
        Map<String, Object> results = callMethod(CHECK_UPDATE, params);
        if (Integer.parseInt(results.get("ok").toString()) == 100) {
            String jsonResults = results.get("ret").toString();
            return gson.fromJson(jsonResults, new TypeToken<Map<String, Object>>() {}.getType());
        } else {
            if (errorMessage != null && results.get("msg") != null) {
                errorMessage[0] = results.get("msg").toString();
            }
            return null;
        }

    }

    public static Map<String, Object> login(String userName, String password, String[] errorMessage){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(KEY_USERNAME, userName);
        params.put(KEY_PASSWORD, password);
        Map<String, Object> results = callMethod(LOGIN, params);
        if(Integer.parseInt(results.get(KEY_SUCCEED).toString()) == RESULT_SUCCEED){
            String jsonResults = results.get(KEY_RESULT).toString();
            return gson.fromJson(jsonResults, new TypeToken<Map<String, Object>>(){}.getType());

        }else {
            if(errorMessage != null && results.get(KEY_ERRMESSAGE) != null){
                errorMessage[0] = results.get(KEY_ERRMESSAGE).toString();
            }
            return null;
        }
    }

    public static int getVersion() {
        try {
            PackageManager manager = MyApplication.getContext().getPackageManager();
            PackageInfo info = manager.getPackageInfo(MyApplication.getContext().getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
