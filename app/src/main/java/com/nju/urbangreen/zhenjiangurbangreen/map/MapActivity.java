package com.nju.urbangreen.zhenjiangurbangreen.map;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureFillSymbol;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.Symbol;
import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.events.EventListActivity;
import com.nju.urbangreen.zhenjiangurbangreen.inspectRecord.InspectListActivity;
import com.nju.urbangreen.zhenjiangurbangreen.maintainRecord.MaintainListActivity;
import com.nju.urbangreen.zhenjiangurbangreen.util.ActivityCollector;
import com.nju.urbangreen.zhenjiangurbangreen.util.GeoJsonUtil;
import com.nju.urbangreen.zhenjiangurbangreen.util.WGSTOZhenjiang;
import com.nju.urbangreen.zhenjiangurbangreen.util.WebServiceUtils;
import com.nju.urbangreen.zhenjiangurbangreen.basisClass.GreenObjects;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapActivity extends Activity {

    private double distance = 500.0;

    private static final String GreenLandType = "000", AncientTreeType = "001", StreetTreeType = "002";
    private static Map<String, Symbol> symbolMap;

    //TPK文件名称
    String tpkFileName = null;

    //当前行道树图层是否可见
    private boolean isGreenTreeLayerVisible;
    private boolean isGreenTreeLayerVisibleFromSwitch;

    private boolean isNearbyOpen = false;
    Envelope fullExtent=new Envelope(480594.6020435903,3550915.606576609,512013.935715591,3574562.78928764);
    Envelope fullExtent2=new Envelope(491067.0,3558797.0,501540.0,3566679.0);
    //用于定位按钮的LocationManager
    private LocationManager locationManager;

    //用于显示周边开关的LocationManager
    private LocationManager nearbyLocMag;

    //显示周边按钮的位置监听
    private NearbyLocListener nearbyLocListener;

    //正在定位对话框
    private ProgressDialog dlgLocating;

    //当点击某个标识时弹出的callout
    private Callout callout;

    private ProgressDialog loadingDialog;

    //本地TPK文件图层
    private ArcGISLocalTiledLayer localTPKLayer;

    //定位按钮
    @BindView(R.id.imgbtn_locate)
    public ImageButton imgBtnLocate;

    //图层控制按钮
    @BindView(R.id.imgbtn_layer_switch)
    public ImageButton imgBtnLayerSwitch;

    //全局显示按钮
    @BindView(R.id.imgbtn_global_view)
    public ImageButton imgBtnGlobalView;

    //显示周边开关
    @BindView(R.id.imgbtn_nearby)
    public ImageButton imgBtnNearby;

    //点击图层控制按钮后弹出的popup窗口
    LayerSwitchPopupWindow layerSwitchPopupWindow;

    @BindView(R.id.bottombar)
    public View bottomBar;

    @BindView(R.id.tv_map_UGO_info)
    public TextView tvUGOInfo;
    public String curUGOID;

    @BindView(R.id.btn_map_maintain_record)
    public Button btnMaintainRecord;

    @BindView(R.id.btn_map_inspect_record)
    public Button btnInspectRecord;

    @BindView(R.id.btn_map_event_record)
    public Button btnEventRecord;

    @BindView(R.id.btn_map_UGO_basicInfo)
    public Button btnUGOInfo;

    @BindView(R.id.map_main)
    public MapView map = null;

    GraphicsLayer streetTreeLayer;
    GraphicsLayer ancientTreeLayer;
    GraphicsLayer greenLandLayer;
    GraphicsLayer locationLayer;

    private ArrayList<GreenObjects> greenLandList, ancientTreeList, streetTreeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ActivityCollector.addActivity(this);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_map);

        ButterKnife.bind(this);

        loadingDialog = new ProgressDialog(MapActivity.this);

        ArcGISRuntime.setClientId("1eFHW78avlnRUPHm");

        callout = map.getCallout();

        //新建一个离线地图图层并添加到mapview中
        tpkFileName = Environment.getExternalStorageDirectory().getPath() + File.separator + "nju_greenland/tpk/vector.tpk";
        localTPKLayer = new ArcGISLocalTiledLayer(tpkFileName);
        map.addLayer(localTPKLayer);

        getUGOInfo();

        //设置定位按钮并添加定位符号图层
        setLocateButton();
        map.addLayer(locationLayer);

        //设置全局显示按钮
        setGlobalViewButton();

        //设置图层开关按钮
        setLayerSwitchPopupWindow();

        //设置显示周边按钮
        setNearbyButton();

        //设置底部栏的按钮点击事件
        setBottomBar();

        //设置mapView单次tap监听
        map.setOnSingleTapListener(onSingleTapListener);

        //在第一次加载mapview的时候设置为全局显示
        map.setExtent(fullExtent2);

        openGPS();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    /*
    * 设置全局显示按钮
    * */
    private void setGlobalViewButton(){
        imgBtnGlobalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Envelope envelope = fullExtent;
                int vh = map.getMeasuredHeight();
                int vw = map.getMeasuredWidth();
                double eh = envelope.getHeight();
                double ew = envelope.getWidth();
                map.zoomToResolution(envelope.getCenter(),Math.max(eh/vh,ew/vw));
            }
        });
    }
    /*
    * 设置定位按钮
    * */
    private void setLocateButton(){
        imgBtnLocate.setOnClickListener(new LocateButtonListener());
        locationLayer = new GraphicsLayer();
        locationManager = (LocationManager)MapActivity.this.getSystemService(Context.LOCATION_SERVICE);
    }

    /*
    * 设置图层开关按钮
    * */
    private void setLayerSwitchPopupWindow(){
        layerSwitchPopupWindow = new LayerSwitchPopupWindow(this, new ILayerSwitchListener() {
            @Override
            public boolean[] getLayerState() {
                return new boolean[] {greenLandLayer.isVisible(),ancientTreeLayer.isVisible(), streetTreeLayer.isVisible()};
            }

            @Override
            public void changeLayerState(boolean[] layerState) {
                GraphicsLayer[] layers = new GraphicsLayer[]{greenLandLayer,ancientTreeLayer, streetTreeLayer};
                for(int i = 0;i < layers.length;i++){
                    layers[i].setVisible(layerState[i]);
                    isGreenTreeLayerVisible = layerState[layers.length-1];
                }

            }
        });
        imgBtnLayerSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int[] location = new int[2];
                view.getLocationOnScreen(location);
                layerSwitchPopupWindow.show(view);
            }
        });
    }

    /*
    *恢复行道树图层中树木的显示
    * */
    private void recoverGreenTreeLayer(){
        GraphicsLayer graphicsLayer = streetTreeLayer;
        int[] ids = graphicsLayer.getGraphicIDs();
        for(int i = 0;i < ids.length;i++){
            graphicsLayer.setGraphicVisible(ids[i],true);
        }
        streetTreeLayer.setVisible(isGreenTreeLayerVisible);
    }

    /*
    * 设置周边按钮
    * */
    private void setNearbyButton(){


        nearbyLocListener = new NearbyLocListener();
        nearbyLocMag = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

//        imgBtnNearby.setChecked(false);
//
//        imgBtnNearby.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                //若按钮开启，则设置位置监听，搜索附近的行道树
//                if(b){
//                    isGreenTreeLayerVisible = streetTreeLayer.isVisible();
//                    imgBtnNearby.setBackgroundResource(R.mipmap.ic_nearby_selected);
//
////                    if(nearbyLocMag.getProvider(LocationManager.NETWORK_PROVIDER) != null){
////                        if(ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
////                                ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
////                            nearbyLocMag.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,5000,5,nearbyLocListener);
////                        }
////
////                    }else if(nearbyLocMag.getProvider(LocationManager.GPS_PROVIDER) != null) {
////                        if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
////                                ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
////                            nearbyLocMag.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, nearbyLocListener);
////                        }
////                    }
//                    searchNearbyTrees(new Point(491060.0,3558790.0));
//                }
//                //否则，移除位置监听，恢复行道树图层的初始状态
//                else {
//                    //isGreenTreeLayerVisible = streetTreeLayer.isVisible();
//                    imgBtnNearby.setBackgroundResource(R.mipmap.ic_nearby_unselected);
//
//                    if(ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
//                            ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//                        nearbyLocMag.removeUpdates(nearbyLocListener);
//                    }
//                    recoverGreenTreeLayer();
//                }
//            }
//        });
    }

    /*
    * 搜索某个点附近xxx米范围内的行道树
    * */
    private void searchNearbyTrees(Point loc){
        isGreenTreeLayerVisible = streetTreeLayer.isVisible();
        GraphicsLayer graphicsLayer = streetTreeLayer;
        int[] ids = graphicsLayer.getGraphicIDs();
        for(int i = 0;i < ids.length;i++){
            Point treeLoc = (Point) graphicsLayer.getGraphic(ids[i]).getGeometry();
            if(calcDistance(loc,treeLoc) < distance){
                graphicsLayer.setGraphicVisible(ids[i],true);
            }
            else{
                graphicsLayer.setGraphicVisible(ids[i],false);
            }
        }
        graphicsLayer.setVisible(true);
        if(isGreenTreeLayerVisible != true)
            isGreenTreeLayerVisible = false;
    }

    /*
    * 计算两个点之间的距离
    * */
    private double calcDistance(Point p1,Point p2){
        return Math.sqrt((p1.getX() - p2.getX())*(p1.getX() - p2.getX()) + (p1.getY() - p2.getY()) * (p1.getY() - p2.getY()));
    }

    /*
    * 缩放至特定的位置来显示
    * */
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

    /*
    * 定位按钮的点击监听实现
    * */
    private class LocateButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            dlgLocating = new ProgressDialog(MapActivity.this);
            dlgLocating.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            dlgLocating.setTitle("正在定位...");
            dlgLocating.setCancelable(true);
            dlgLocating.show();


            //if(locationManager.getProvider(LocationManager.NETWORK_PROVIDER) != null)
            if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                if(ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,5000,5,new GPS_NetWorkLocListener());
                }

            }else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                if(ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5,new GPS_NetWorkLocListener());
                }

            }else {
                Toast.makeText(MapActivity.this,"定位失败",Toast.LENGTH_SHORT).show();
                dlgLocating.dismiss();
            }

        }
    }

    /*
    * 网络和GPS的位置监听，用于定位按钮
    * */
    private class GPS_NetWorkLocListener implements LocationListener{

        @Override
        public void onLocationChanged(Location location) {
            Point currPosition = WGSTOZhenjiang.WGS2ZJ(location.getLatitude(),location.getLongitude());
            double x = currPosition.getX();
            double y = currPosition.getY();
            if(x >= fullExtent.getXMin() && x <= fullExtent.getXMax() &&
                    y >= fullExtent.getYMin() && y <= fullExtent.getYMax()){


                BitmapDrawable image = (BitmapDrawable) MapActivity.this.getResources().getDrawable(R.drawable.ic_curr_lcocation);
                Bitmap bitmap = image.getBitmap();
                int bmpWidth = bitmap.getWidth();
                int bmpHeght = bitmap.getHeight();
                Matrix matrix = new Matrix();
                matrix.postScale(112.0f / bmpWidth,108.0f / bmpHeght);
                PictureMarkerSymbol symbol = new PictureMarkerSymbol(new BitmapDrawable(Bitmap.createBitmap(bitmap, 0, 0, bmpWidth, bmpHeght, matrix, true)));
                Graphic graphic = new Graphic(currPosition,symbol);
                locationLayer.addGraphic(graphic);
                locationLayer.setVisible(true);
                zoomTo(currPosition);
            }else{
                Toast.makeText(MapActivity.this,"当前位置不在地图范围内",Toast.LENGTH_SHORT).show();
            }
            dlgLocating.dismiss();
            if(ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationManager.removeUpdates(this);
            }


        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {
            if(dlgLocating != null){
                //dlgLocating.dismiss();
            }
        }
    }

    /*
    * 周边按钮的位置监听
    * */
    private class NearbyLocListener implements LocationListener{

        @Override
        public void onLocationChanged(Location location) {
            Point currPosition = WGSTOZhenjiang.WGS2ZJ(location.getLatitude(),location.getLongitude());
            searchNearbyTrees(currPosition);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }

    private void highlightGraphic(Graphic graphic){
        int[] ids = new int[]{(int)graphic.getId()};
        int graphicType = (Integer)graphic.getAttributeValue("GraphicType");
        curUGOID = (String)graphic.getAttributeValue("UGO_ID");
        if(graphicType == 0){
            greenLandLayer.setSelectedGraphics(ids, true);
            tvUGOInfo.setText("绿地: " + curUGOID);
        }else if(graphicType == 1){
            ancientTreeLayer.setSelectedGraphics(ids, true);
            tvUGOInfo.setText("古树名木: " + curUGOID);
        }else if(graphicType == 2){
            streetTreeLayer.setSelectedGraphics(ids, true);
            tvUGOInfo.setText("行道树: " + curUGOID);
        }
    }

    /*
    * 实例化一个单次tap事件监听
    * */
    private OnSingleTapListener onSingleTapListener = new OnSingleTapListener() {

        @Override
        public void onSingleTap(float v, float v1) {
            if(!map.isLoaded()){
                return;
            }
            if(callout.isShowing()){
                callout.hide();
            }
            GraphicsLayer[] layers = new GraphicsLayer[]{greenLandLayer,ancientTreeLayer, streetTreeLayer};

            Integer graphicID = null;
            int layerIndex = 0;

            //在每个图层中搜索被点击的Graphics
            for(;layerIndex < layers.length;layerIndex++){
                GraphicsLayer graphicsLayer = layers[layerIndex];
                if(graphicsLayer == null){
                    continue;
                }
                graphicsLayer.clearSelection();
                if(!graphicsLayer.isVisible()){
                    continue;
                }
                int[] ids = graphicsLayer.getGraphicIDs(v,v1,25,1);
                if(ids != null && ids.length > 0){
                    graphicID = ids[0];
                    break;
                }
            }
            if(graphicID == null){
                bottomBar.setVisibility(View.GONE);
                return;
            }
            Graphic graphic = layers[layerIndex].getGraphic(graphicID);
            highlightGraphic(graphic);
            bottomBar.setVisibility(View.VISIBLE);
        }
    };


    /*
    *没有用到
    * */
    private void setNearbyLocMag(){
        nearbyLocListener = new NearbyLocListener();
        nearbyLocMag = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(nearbyLocMag.getProvider(LocationManager.NETWORK_PROVIDER) != null){
            if(ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                nearbyLocMag.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,5000,5,nearbyLocListener);
            }

        }else if(nearbyLocMag.getProvider(LocationManager.GPS_PROVIDER) != null){
            if(ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                nearbyLocMag.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5,nearbyLocListener);
            }

        }else {
            Toast.makeText(MapActivity.this,"无法获取您的当前位置",Toast.LENGTH_SHORT).show();
            dlgLocating.dismiss();
        }
    }

    /*
    *打开定位服务设置界面
    * */
    private void openGPS(){

        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            AlertDialog dialog = null;
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
//            builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//
//                }
//            });
            builder.setCancelable(true);
            dialog = builder.create();
            dialog.show();
        }

    }

    /**
     * 设置bottom bar中按钮的点击事件
     */
    private void setBottomBar(){
        btnMaintainRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapActivity.this, MaintainListActivity.class);
                intent.putExtra("UGO_ID", curUGOID);
                startActivity(intent);
            }
        });

        btnInspectRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapActivity.this, InspectListActivity.class);
                intent.putExtra("UGO_ID", curUGOID);
                startActivity(intent);
            }
        });

        btnEventRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapActivity.this, EventListActivity.class);
                intent.putExtra("UGO_ID", curUGOID);
                startActivity(intent);
            }
        });

        btnUGOInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void getUGOInfo() {
        greenLandLayer = new GraphicsLayer();
        ancientTreeLayer = new GraphicsLayer();
        streetTreeLayer = new GraphicsLayer();
        map.addLayer(greenLandLayer);
        map.addLayer(ancientTreeLayer);
        map.addLayer(streetTreeLayer);
        greenLandList = new ArrayList<>();
        ancientTreeList = new ArrayList<>();
        streetTreeList = new ArrayList<>();
        loadingDialog.setMessage("正在获取数据...");
        loadingDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String errorMsg[] = new String[1];
                List<GreenObjects> UGO_list = WebServiceUtils.getUGOInfoExceptST(errorMsg);
                createUGOLayers(UGO_list);
                loadingDialog.dismiss();
            }
        }).start();
    }

    private void createUGOLayers(List<GreenObjects> list) {
        symbolMap = new HashMap<>();
        symbolMap.put(GreenLandType, new PictureFillSymbol(new BitmapDrawable(
                BitmapFactory.decodeResource(getResources(), R.drawable.green_land))));
        symbolMap.put(AncientTreeType, new PictureMarkerSymbol(new BitmapDrawable(
                BitmapFactory.decodeResource(getResources(), R.drawable.ancient_tree))));
        symbolMap.put(StreetTreeType, new PictureMarkerSymbol(new BitmapDrawable(
                BitmapFactory.decodeResource(getResources(), R.drawable.street_tree))));

        for(GreenObjects obj : list) {
            Geometry geometry = GeoJsonUtil.String2Geometry(obj.UGO_Geo_Location);
            if(geometry != null) {
                Map<String,Object> greenObj = new HashMap<>();
                if (obj.UGO_ClassType_ID.equals(GreenLandType)) {
                    greenLandList.add(obj);
                    greenObj.put("UGO_ID", obj.UGO_ID);
                    greenObj.put("GraphicType", 0);
                    Graphic graphic = new Graphic(geometry, symbolMap.get(GreenLandType), greenObj);
                    greenLandLayer.addGraphic(graphic);
                }
                else if (obj.UGO_ClassType_ID.equals(AncientTreeType)) {
                    ancientTreeList.add(obj);
                    greenObj.put("UGO_ID", obj.UGO_ID);
                    greenObj.put("GraphicType", 1);
                    Graphic graphic = new Graphic(geometry, symbolMap.get(AncientTreeType), greenObj);
                    ancientTreeLayer.addGraphic(graphic);
                }
                else if (obj.UGO_ClassType_ID.equals(StreetTreeType)){
                    streetTreeList.add(obj);
                    greenObj.put("UGO_ID", obj.UGO_ID);
                    greenObj.put("GraphicType", 2);
                    Graphic graphic = new Graphic(geometry, symbolMap.get(StreetTreeType), greenObj);
                    streetTreeLayer.addGraphic(graphic);
                }
            }
        }
    }
}
