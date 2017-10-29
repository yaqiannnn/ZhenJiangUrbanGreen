package com.nju.urbangreen.zhenjiangurbangreen.map;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.event.OnLongPressListener;
import com.esri.android.map.event.OnPanListener;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.Symbol;
import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.basisClass.BaseActivity;
import com.nju.urbangreen.zhenjiangurbangreen.basisClass.GreenObject;
import com.nju.urbangreen.zhenjiangurbangreen.util.FileUtil;
import com.nju.urbangreen.zhenjiangurbangreen.util.GeoJsonUtil;
import com.nju.urbangreen.zhenjiangurbangreen.util.WebServiceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lxs on 17-10-27.
 */

public class BufferMapActivity  extends BaseActivity{

    private ArcGISLocalTiledLayer localTPKLayer;
    private Envelope fullExtent = new Envelope(491067.0,3558797.0,501540.0,3566679.0);
    private LocationManager locationManager;
    private LocationDisplayManager locationDisplayManager;
    private GraphicsLayer streetTreeLayer;
    private GraphicsLayer ancientTreeLayer;
    private GraphicsLayer greenLandLayer;
    private GraphicsLayer bufferCenterLayer;

    private static final String GreenLandType = "000", AncientTreeType = "001", StreetTreeType = "002";
    private static Map<String, Symbol> symbolMap;
    private ArrayList<GreenObject> selectGreenLand, selectAncientTree, selectStreetTree;

    private ProgressDialog loadingDialog;
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

    @BindView(R.id.iv_map_buffer_pin)
    public ImageView ivPin;
    private int pinGraphicID;
    private int pinLastX, pinLastY;
    private int moveWidth, moveHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buffer_map);
        ButterKnife.bind(this);
        loadingDialog = new ProgressDialog(BufferMapActivity.this);
        initToolbar();
        setMap();
        createBufferCenterLayer();
        setPinImage();
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

    private void setPinImage() {
        ivPin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(view.getId() != R.id.iv_map_buffer_pin)
                    return true;
                int action = motionEvent.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN: {
                        pinLastX = (int) motionEvent.getRawX();
                        pinLastY = (int) motionEvent.getRawY();
                        ivPin.setImageAlpha(255);
                        bufferCenterLayer.setVisible(false);
                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        int dx = (int) motionEvent.getRawX() - pinLastX;
                        int dy = (int) motionEvent.getRawY() - pinLastY;
                        int l = view.getLeft() + dx;
                        int b = view.getBottom() + dy;
                        int r = view.getRight() + dx;
                        int t = view.getTop() + dy;
                        // 下面判断移动是否超出屏幕
                        if (l < 0) {
                            l = 0;
                            r = l + view.getWidth();
                        }
                        if (t < 0) {
                            t = 0;
                            b = t + view.getHeight();
                        }
                        if (r > moveWidth) {
                            r = moveWidth;
                            l = r - view.getWidth();
                        }
                        if (b > moveHeight) {
                            b = moveHeight;
                            t = b - view.getHeight();
                        }
                        view.layout(l, t, r, b);
                        pinLastX = (int) motionEvent.getRawX();
                        pinLastY = (int) motionEvent.getRawY();
                        view.postInvalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        float ivCenterX = 0.5f * ivPin.getLeft() + 0.5f * ivPin.getRight();
                        float ivCenterY = 0.5f * ivPin.getTop() + 0.5f * ivPin.getBottom();
                        bufferCenterLayer.updateGraphic(pinGraphicID,
                                map.toMapPoint(ivCenterX, ivCenterY));
                        bufferCenterLayer.setVisible(true);
                        ivPin.setImageAlpha(0);
                        break;
                    }
                }
                return true;
            }
        });
    }

    private void setMap() {
        ArcGISRuntime.setClientId("1eFHW78avlnRUPHm");
        String tpkFileName = FileUtil.getBaseMapTPKDir() + WebServiceUtils.BaseMapFileNames[0];
        localTPKLayer = new ArcGISLocalTiledLayer(tpkFileName);
        bufferCenterLayer = new GraphicsLayer();
        greenLandLayer = new GraphicsLayer();
        ancientTreeLayer = new GraphicsLayer();
        streetTreeLayer = new GraphicsLayer();
        map.addLayer(localTPKLayer);
        map.addLayer(greenLandLayer);
        map.addLayer(ancientTreeLayer);
        map.addLayer(streetTreeLayer);
        map.addLayer(bufferCenterLayer);
        map.setExtent(fullExtent);

        map.setOnPanListener(new OnPanListener() {
            @Override
            public void prePointerMove(float v, float v1, float v2, float v3) {}
            @Override
            public void postPointerMove(float v, float v1, float v2, float v3) {}
            @Override
            public void prePointerUp(float v, float v1, float v2, float v3) {}

            @Override
            public void postPointerUp(float v, float v1, float v2, float v3) {
                Point pinScreen = map.toScreenPoint((Point) bufferCenterLayer.getGraphic(pinGraphicID).getGeometry());
                double pinScreenX = pinScreen.getX(), pinScreenY = pinScreen.getY();
                ivPin.layout((int)pinScreenX - 32, (int)pinScreenY - 32,
                        (int)pinScreenX + 32, (int)pinScreenY + 32);
                ivPin.postInvalidate();
            }
        });

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

        map.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                if(status == STATUS.INITIALIZED && o == map) {
                    moveWidth = map.getWidth();
                    moveHeight = map.getHeight();
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

    private void getUGOInfo() {
        selectGreenLand = new ArrayList<>();
        selectAncientTree = new ArrayList<>();
        selectStreetTree = new ArrayList<>();
        loadingDialog.setMessage("正在加载数据...");
        loadingDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String errorMsg[] = new String[1];
                List<GreenObject> UGO_list = WebServiceUtils.getUGOInfoExceptST(errorMsg);
                createUGOLayers(UGO_list);
                loadingDialog.dismiss();
            }
        }).start();
    }

    private void createUGOLayers(List<GreenObject> list) {
        symbolMap = new HashMap<>();
        SimpleFillSymbol greenLandSymbol = new SimpleFillSymbol(
                ResourcesCompat.getColor(getResources(), R.color.green_land, null), SimpleFillSymbol.STYLE.SOLID);
        greenLandSymbol.setOutline(new SimpleLineSymbol(
                ResourcesCompat.getColor(getResources(), R.color.green_land_border, null), 1.0f, SimpleLineSymbol.STYLE.SOLID));
        symbolMap.put(GreenLandType, greenLandSymbol);
        PictureMarkerSymbol ancientTreeSymbol = new PictureMarkerSymbol(new BitmapDrawable(
                BitmapFactory.decodeResource(getResources(), R.drawable.ancient_tree)));
        ancientTreeSymbol.setOffsetY(16);
        symbolMap.put(AncientTreeType, ancientTreeSymbol);
        symbolMap.put(StreetTreeType, new SimpleMarkerSymbol(
                ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null), 5, SimpleMarkerSymbol.STYLE.CIRCLE));

        for(GreenObject obj : list) {
            Geometry geometry = GeoJsonUtil.String2Geometry(obj.UGO_Geo_Location);
            if(geometry != null) {
                Map<String,Object> greenObj = new HashMap<>();
                if (obj.UGO_ClassType_ID.equals(GreenLandType)) {
                    selectGreenLand.add(obj);
                    greenObj.put("UGO_Ucode", obj.UGO_ID);
                    greenObj.put("GraphicType", 0);
                    Graphic graphic = new Graphic(geometry, symbolMap.get(GreenLandType), greenObj);
                    greenLandLayer.addGraphic(graphic);
                }
                else if (obj.UGO_ClassType_ID.equals(AncientTreeType)) {
                    selectAncientTree.add(obj);
                    greenObj.put("UGO_Ucode", obj.UGO_ID);
                    greenObj.put("GraphicType", 1);
                    Graphic graphic = new Graphic(geometry, symbolMap.get(AncientTreeType), greenObj);
                    ancientTreeLayer.addGraphic(graphic);
                }
                else if (obj.UGO_ClassType_ID.equals(StreetTreeType)){
                    selectStreetTree.add(obj);
                    greenObj.put("UGO_Ucode", obj.UGO_ID);
                    greenObj.put("GraphicType", 2);
                    Graphic graphic = new Graphic(geometry, symbolMap.get(StreetTreeType), greenObj);
                    streetTreeLayer.addGraphic(graphic);
                }
            }
        }
    }

    private void createBufferCenterLayer() {
        PictureMarkerSymbol pinSymbol = new PictureMarkerSymbol(new BitmapDrawable(
                BitmapFactory.decodeResource(getResources(), R.drawable.center_pin)));
        // a half of the picture's height
        pinSymbol.setOffsetY(16);
        Geometry centerGeo = new Point(fullExtent.getCenterX(), fullExtent.getCenterY());
        Graphic centerGraphic = new Graphic(centerGeo, pinSymbol);
        bufferCenterLayer.addGraphic(centerGraphic);
        pinGraphicID = bufferCenterLayer.getGraphicIDs()[0];
    }
}
