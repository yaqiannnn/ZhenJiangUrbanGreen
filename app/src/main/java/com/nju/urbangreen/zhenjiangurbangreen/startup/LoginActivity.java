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
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class LoginActivity extends Activity {

    public static String DOWNLOAD_URL = "";
    public static int NEW_Version;
    public static final String SOAP_ACTION = "http://tempuri.org/CheckUpdate";
    public static final String OPERATION_NAME = "CheckUpdate";
    public static final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";
    public static final String SOAP_ADDRESS = "http://192.168.0.106:82/WebService.asmx";
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;
    private EditText etUserName;
    private EditText etPassword;
    private Button btnLogin;
    private String username;
    private String password;
    private Boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        //判断网络是否可用
        connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

//        new AsyncTask<Void,Void,Void>(){
//
//            @Override
//            protected Void doInBackground(Void... voids) {
//
//                //if(networkInfo == null || !networkInfo.isAvailable()){
//                    try{
//                        Thread.sleep(800);
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//                    //return null;
//                //}
//
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
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                super.onPostExecute(aVoid);
//
//
//            }
//
//        }.execute();
        setLoginButton();
    }

    public void setLoginButton(){

        etUserName = (EditText) findViewById(R.id.edit_username);
        etPassword = (EditText) findViewById(R.id.edit_password);
        btnLogin = (Button) findViewById(R.id.btn_login);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncTask<Void,Void,Void>(){

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        username = etUserName.getText().toString();
                        password = etPassword.getText().toString();
                    }

                    @Override
                    protected Void doInBackground(Void... voids) {
                        try{
                            Thread.sleep(800);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        Intent mainIntent;
                        String[] errMsg = new String[1];

                        Map<String,Object> results = WebServiceUtils.login(username,password,errMsg);
                        if(results != null){
                            flag = true;
                            Map<String, Object> resultsUpdate = WebServiceUtils.checkUpdate(errMsg);
                            String APK_URL = resultsUpdate.get("url").toString();
                            mainIntent = new Intent(LoginActivity.this,MapActivity.class);
                            if (APK_URL != null) {
                                mainIntent.putExtra("apk_url", APK_URL);
                            }
                            startActivity(mainIntent);
                            finish();
                        }
                        else{
                            mainIntent = new Intent(LoginActivity.this,MapActivity.class);
                            startActivity(mainIntent);
                        }
                        return null;
                    }


                }.execute();
//                if(etPassword.getText().toString().equals("123")  && etUserName.getText().toString().equals("zlw")){
//                    SPUtils.put(MyApplication.getContext(),"username","zlw");
//                    SPUtils.put(MyApplication.getContext(),"password","123");
//                    Intent intent = new Intent(LoginActivity.this,MapActivity.class);
//                    if(!DOWNLOAD_URL.equals("")){
//                        intent.putExtra("apk_url",DOWNLOAD_URL);
//                    }
//                    startActivity(intent);
//                    finish();
//                }else {
//                    Toast.makeText(LoginActivity.this,"用户名或密码有误~",Toast.LENGTH_SHORT).show();
//                    Log.i("hehehe","用户名或密码有误");
//                }
                Log.i("登录",flag.toString());
                Log.i("登录",username.toString());
                Log.i("登录",password.toString());
            }
        });
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



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }
}
