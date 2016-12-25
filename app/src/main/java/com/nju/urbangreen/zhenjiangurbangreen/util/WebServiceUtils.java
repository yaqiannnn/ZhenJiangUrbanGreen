package com.nju.urbangreen.zhenjiangurbangreen.util;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.nju.urbangreen.zhenjiangurbangreen.map.MapActivity;
import com.nju.urbangreen.zhenjiangurbangreen.startup.WelcomeActivity;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Liwei on 2016/12/25.
 */
public class WebServiceUtils {
    public static final String SOAP_ACTION = "http://tempuri.org/CheckUpdate";
    //public static final String OPERATION_NAME = "CheckUpdate";
    public static final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";
    public static final String SOAP_ADDRESS = "http://192.168.0.106:82/WebService.asmx";

    public static final String CHECK_UPDATE = "CheckUpdate";
    public static boolean flag = false;
    static Map<String, Object> results = new HashMap<String, Object>();

    private static void callMethod(String operationName) {

        final String OPERATION_NAME = operationName;

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);
                if (OPERATION_NAME.equals(CHECK_UPDATE)) {
                    PropertyInfo pi = new PropertyInfo();
                    pi.setName("versionCode");
                    pi.setValue(getVersion());
                    pi.setType(Integer.class);
                    request.addProperty(pi);
                }
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.encodingStyle = SoapSerializationEnvelope.ENC;
                envelope.setOutputSoapObject(request);
                HttpTransportSE transport = new HttpTransportSE(SOAP_ADDRESS);
                Object response = null;
                try {
                    transport.call(WSDL_TARGET_NAMESPACE + OPERATION_NAME, envelope);
                    response = envelope.getResponse();
                    if (response != null) {
                        Gson gson = new Gson();
                        results = gson.fromJson(response.toString(), results.getClass());
                        flag = true;
                    }
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }.execute();
    }

    public static Map<String, Object> checkUpdate(String methodName) {
        callMethod(CHECK_UPDATE);
        while (!flag) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        flag = false;
        //String APK_URL = (String) results.get("url");
        //int NEW_VERSION = Integer.parseInt(results.get("version").toString());
        return results;
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
