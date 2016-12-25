package com.nju.urbangreen.zhenjiangurbangreen.startup;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.map.MapActivity;
import com.nju.urbangreen.zhenjiangurbangreen.util.MyApplication;
import com.nju.urbangreen.zhenjiangurbangreen.util.SPUtils;
import com.nju.urbangreen.zhenjiangurbangreen.util.WebServiceUtils;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.HashMap;
import java.util.Map;

public class WelcomeActivity extends Activity {

    public static String DOWNLOAD_URL = "";
    public static int NEW_Version;
    public static final String SOAP_ACTION = "http://tempuri.org/CheckUpdate";
    public static final String OPERATION_NAME = "CheckUpdate";
    public static final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";
    public static final String SOAP_ADDRESS = "http://192.168.0.106:82/WebService.asmx";
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_welcome);


        //判断网络是否可用
        connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        new AsyncTask<Void,Void,Void>(){

            @Override
            protected Void doInBackground(Void... voids) {

                if(networkInfo == null || !networkInfo.isAvailable()){
                    try{
                        Thread.sleep(800);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    return null;
                }

//                SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);
//                PropertyInfo pi = new PropertyInfo();
//                pi.setName("versionCode");
//                pi.setValue(getVersion());
//                pi.setType(Integer.class);
//                request.addProperty(pi);
//
//                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//                envelope.dotNet = true;
//                envelope.encodingStyle = SoapSerializationEnvelope.ENC;
//                envelope.setOutputSoapObject(request);
//                HttpTransportSE transport = new HttpTransportSE(SOAP_ADDRESS);
//                Object response = null;
//                try{
//                    transport.call(SOAP_ACTION,envelope);
//                    response = envelope.getResponse();
//
//                    if(response != null){
//                        Gson gson = new Gson();
//                        Map<String,Object> result = new HashMap<String, Object>();
//                        result = gson.fromJson(response.toString(),result.getClass());
//                        DOWNLOAD_URL = (String)result.get("url");
//                        NEW_Version = Integer.parseInt(result.get("version").toString());
//
//                    }
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//                try{
//                    Thread.sleep(800);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

//                if(SPUtils.contains(MyApplication.getContext(),"username") && SPUtils.contains(MyApplication.getContext(),"password")){
//                    Intent mainIntent = new Intent(WelcomeActivity.this,MapActivity.class);
//                    if(DOWNLOAD_URL != ""){
//                        mainIntent.putExtra("apk_url",DOWNLOAD_URL);
//                    }
//
//                    startActivity(mainIntent);
//                }else{
//                    Intent loginIntent = new Intent(WelcomeActivity.this,LoginActivity.class);
//                    startActivity(loginIntent);
//                }
//
//                finish();
            }

        }.execute();

        Map<String, Object> results = WebServiceUtils.checkUpdate(WebServiceUtils.CHECK_UPDATE);
        Intent mainIntent = new Intent(WelcomeActivity.this,MapActivity.class);
        String APK_URL = results.get("url").toString();
        if(APK_URL != null){
            mainIntent.putExtra("apk_url",APK_URL);
        }
        startActivity(mainIntent);
        finish();
    }

    public int getVersion(){
        try{
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(),0);
            return info.versionCode;
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }
}
