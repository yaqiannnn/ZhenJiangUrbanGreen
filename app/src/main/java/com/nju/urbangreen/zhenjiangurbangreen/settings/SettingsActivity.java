package com.nju.urbangreen.zhenjiangurbangreen.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.google.gson.Gson;
import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.startup.LoginActivity;
import com.nju.urbangreen.zhenjiangurbangreen.util.ActivityCollector;
import com.nju.urbangreen.zhenjiangurbangreen.util.DownloadNewApkService;
import com.nju.urbangreen.zhenjiangurbangreen.util.MyApplication;
import com.nju.urbangreen.zhenjiangurbangreen.util.SPUtils;
import com.nju.urbangreen.zhenjiangurbangreen.util.WebServiceUtils;
import com.nju.urbangreen.zhenjiangurbangreen.widget.TitleBarLayout;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends Activity {

    public static String DOWNLOAD_URL = "";
    public static int NEW_Version;
    public static final String SOAP_ACTION = "http://tempuri.org/CheckUpdate";
    public static final String OPERATION_NAME = "CheckUpdate";
    public static final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";
    public static final String SOAP_ADDRESS = "http://192.168.0.106:82/WebService.asmx";

    private ProgressDialog progressDialog;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            ((AlertDialog.Builder) msg.obj).show();
        }
    };

    @BindView(R.id.btn_check_update)
    public Button btnHandUpdate;

    @BindView(R.id.btn_logout)
    public Button btnLogout;

    @BindView(R.id.ly_settings_title_bar)
    public TitleBarLayout titleBarLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityCollector.addActivity(this);
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        setTitleBar();
        setHandUpdateButton();
        setLogoutButton();
    }

    @Override
    protected void onDestroy() {
        ActivityCollector.removeActivity(this);
        super.onDestroy();
    }

    private void setHandUpdateButton(){
        btnHandUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(SettingsActivity.this);
                progressDialog.setMessage("正在检查更新...");
                progressDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String[] errMsg = new String[1];
                        Map<String, Object> resultsUpdate = WebServiceUtils.checkUpdate(errMsg);
                        progressDialog.dismiss();

                        if (resultsUpdate != null) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                            builder.setTitle("更新提示");
                            builder.setMessage("检测到软件有更新，是否现在安装?");
                            builder.setCancelable(false);
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent1 = new Intent(SettingsActivity.this, DownloadNewApkService.class);
                                    startService(intent1);

                                }
                            });
                            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            try {
                                Thread.sleep(800);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Message message = new Message();
                            message.obj = builder;
                            handler.sendMessage(message);
                        } else if(errMsg[0] != null) {
                            Looper.prepare();
                            Toast.makeText(SettingsActivity.this, errMsg[0], Toast.LENGTH_LONG).show();
                            Looper.loop();
                        }
                    }
                }).start();
            }
        });
    }

    private void setLogoutButton(){
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setTitle("退出提示");
                builder.setMessage("确定要退出登录吗?");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCollector.finishAll();
                        SPUtils.remove("username");
                        SPUtils.remove("password");
                        Intent intent1 = new Intent(SettingsActivity.this,LoginActivity.class);
                        startActivity(intent1);
                        finish();

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
    }


    private void setTitleBar(){
        titleBarLayout.setTitleText("设置");
        titleBarLayout.setBtnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public int getVersion() {
        try {
            PackageManager manager = getPackageManager();
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
