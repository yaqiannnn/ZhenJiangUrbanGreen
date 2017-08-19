package com.nju.urbangreen.zhenjiangurbangreen.basisClass;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.nju.urbangreen.zhenjiangurbangreen.util.ActivityCollector;

/**
 * Created by lxs on 17-8-10.
 */

public class BaseActivity extends AppCompatActivity {
    private long exitTime;
    private final long exitDuration = 2000L;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if(System.currentTimeMillis() - exitTime > exitDuration) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            ActivityCollector.finishAll();
            System.exit(0);
        }
    }
}
