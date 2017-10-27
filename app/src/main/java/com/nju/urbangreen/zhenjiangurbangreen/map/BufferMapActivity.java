package com.nju.urbangreen.zhenjiangurbangreen.map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.basisClass.BaseActivity;
import com.nju.urbangreen.zhenjiangurbangreen.util.FileUtil;
import com.nju.urbangreen.zhenjiangurbangreen.util.WebServiceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lxs on 17-10-27.
 */

public class BufferMapActivity  extends BaseActivity{

    @BindView(R.id.map_buffer)
    public MapView map;

    @BindView(R.id.imgbtn_map_buffer_locate)
    public ImageButton btnLocate;

    @BindView(R.id.et_map_buffer)
    public EditText etBuffer;

    @BindView(R.id.btn_map_buffer_search)
    public AppCompatButton btnBufferSearch;

    @BindView(R.id.Toolbar_simple)
    public Toolbar toolbar;

    private ArcGISLocalTiledLayer localTPKLayer;
    private Envelope fullExtent = new Envelope(491067.0,3558797.0,501540.0,3566679.0);
    private LocationManager locationManager;
    private LocationDisplayManager locationDisplayManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buffer_map);
        ButterKnife.bind(this);
        initToolbar();
        setMap();
        openGPS();
    }

    private void initToolbar() {
        toolbar.setTitle("图上选择绿化对象");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setMap() {
        ArcGISRuntime.setClientId("1eFHW78avlnRUPHm");
        String tpkFileName = FileUtil.getBaseMapTPKDir() + WebServiceUtils.BaseMapFileNames[0];
        localTPKLayer = new ArcGISLocalTiledLayer(tpkFileName);
        map.addLayer(localTPKLayer);
        map.setExtent(fullExtent);

        locationDisplayManager = map.getLocationDisplayManager();
        locationDisplayManager.setAccuracyCircleOn(false);
        locationDisplayManager.setShowLocation(false);
        locationDisplayManager.setAutoPanMode(LocationDisplayManager.AutoPanMode.LOCATION);
        locationDisplayManager.setShowPings(false);
        locationDisplayManager.start();//开始定位

        btnLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Point curP = locationDisplayManager.getPoint();
                if(curP.getX() >= fullExtent.getXMin() && curP.getX() <= fullExtent.getXMax()
                        && curP.getY() >= fullExtent.getYMin() && curP.getY() <= fullExtent.getYMax()) {
                    zoomTo(locationDisplayManager.getPoint());
                }
                else {
                    Toast.makeText(BufferMapActivity.this, "当前位置不在地图范围内", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 缩放至特定的位置来显示
     */
    private void zoomTo(Geometry geometry){
        Envelope envelope = new Envelope();
        geometry.queryEnvelope(envelope);
        int vh = map.getMeasuredHeight();
        int vw = map.getMeasuredWidth();
        double eh = envelope.getHeight();
        double ew = envelope.getWidth();
        double res = Math.max(eh/vh,ew/vw);
        map.zoomToResolution(envelope.getCenter(),res);
    }

    /**
     * 打开定位服务设置界面
     **/
    private void openGPS(){
        locationManager = (LocationManager)BufferMapActivity.this.getSystemService(Context.LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("系统提示");
            builder.setMessage("是否现在打开GPS?");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);

                }
            });
            builder.setCancelable(true);
            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }
}
