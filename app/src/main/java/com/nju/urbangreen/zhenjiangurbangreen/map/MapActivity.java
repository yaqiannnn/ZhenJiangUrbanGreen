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
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.audiofx.BassBoost;
import android.media.audiofx.EnvironmentalReverb;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import butterknife.BindView;

import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Line;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureFillSymbol;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.events.EventRegisterActivity;
import com.nju.urbangreen.zhenjiangurbangreen.inspectRecord.InspectInfoActivity;
import com.nju.urbangreen.zhenjiangurbangreen.maintainRecord.MaintainInfoActivity;
import com.nju.urbangreen.zhenjiangurbangreen.util.ActivityCollector;
import com.nju.urbangreen.zhenjiangurbangreen.util.DownloadNewApkService;
import com.nju.urbangreen.zhenjiangurbangreen.util.WGSTOZhenjiang;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapActivity extends Activity {

    @BindView(R.id.map_main)
    public MapView map = null;

    private double distance = 500.0;

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

    //本地TPK文件图层
    private ArcGISLocalTiledLayer localTPKLayer;

    //TPK文件名称
    String tpkFileName = null;

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
    @BindView(R.id.cb_nearby)
    public CheckBox chkBoxNearby;

    //点击图层控制按钮后弹出的popup窗口
    LayerSwitchPopupWindow layerSwitchPopupWindow;

    @BindView(R.id.bottombar)
    public View bottomBar;

    GraphicsLayer greenTreeLayer;
    GraphicsLayer ancientTreeLayer;
    GraphicsLayer greenGroundLayer;
    GraphicsLayer greenBeltLayer;
    GraphicsLayer parkLayer;
    GraphicsLayer sceneLayer;
    GraphicsLayer locationLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ActivityCollector.addActivity(this);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_map);

        // bottomBar = findViewById(R.id.bottombar);
        ArcGISRuntime.setClientId("1eFHW78avlnRUPHm");

        // map = (MapView) findViewById(R.id.map_main);
        callout = map.getCallout();

        //新建一个离线地图图层并添加到mapview中
        tpkFileName = Environment.getExternalStorageDirectory().getPath() + File.separator + "nju_termitecontrol/tpk/vector.tpk";
        localTPKLayer = new ArcGISLocalTiledLayer(tpkFileName);
        map.addLayer(localTPKLayer);

        //
        addGreenLayers();

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

        //检查更新
        setUpdate();

        openGPS();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    private void createLayers(){
        Point[] pointsScene = new Point[]{new Point(496303.0,3553280.0),new Point(499445.0,3553280.0),new Point(497874.0,3555645.0)};
        Point[] pointsPark = new Point[]{new Point(483736.0,3555845.0),new Point(485307.0,3554463.0),new Point(488449.0,3555980.0),new Point(485940.0,3556828.0)};
        Point[] pointsGreenBlet = new Point[]{new Point(504058.0,3558310.0),new Point(504158.0,3555945.0),new Point(508871.0,3555945.0),new Point(507928.0,3558510.0)};

        Polygon polygonScene = new Polygon();
        Polygon polygonPark = new Polygon();
        Polygon polygonGreenBelt = new Polygon();

        Point startPoint = null;
        Point endPoint = null;

        for(int i = 0;i < pointsScene.length;i++){
            startPoint = pointsScene[i];
            endPoint = pointsScene[(i+1)%(pointsScene.length)];
            Line line = new Line();
            line.setStart(startPoint);
            line.setEnd(endPoint);
            polygonScene.addSegment(line,false);
        }
        for(int i = 0;i < pointsPark.length;i++){
            startPoint = pointsPark[i];
            endPoint = pointsPark[(i+1)%(pointsPark.length)];
            Line line = new Line();
            line.setStart(startPoint);
            line.setEnd(endPoint);
            polygonPark.addSegment(line,false);
        }
        for(int i = 0;i < pointsGreenBlet.length;i++){
            startPoint = pointsGreenBlet[i];
            endPoint = pointsGreenBlet[(i+1)%(pointsGreenBlet.length)];
            Line line = new Line();
            line.setStart(startPoint);
            line.setEnd(endPoint);
            polygonGreenBelt.addSegment(line,false);
        }

        Map<String,Object> greObj = new HashMap<>();
        greObj.put("GraphicType",0);
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(),R.drawable.texture1);
        Graphic graphicScene = new Graphic(polygonScene,new PictureFillSymbol(new BitmapDrawable(bitmap1)),greObj);
        //Graphic graphicScene = new Graphic(polygonScene,new SimpleFillSymbol(Color.MAGENTA),greObj);
        sceneLayer = new GraphicsLayer();
        sceneLayer.addGraphic(graphicScene);

        greObj.clear();
        greObj.put("GraphicType",1);
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(),R.drawable.texture2);
        Graphic graphicPark = new Graphic(polygonPark,new PictureFillSymbol(new BitmapDrawable(bitmap2)),greObj);
        //Graphic graphicPark = new Graphic(polygonPark,new SimpleFillSymbol(Color.BLUE),greObj);
        parkLayer = new GraphicsLayer();
        parkLayer.addGraphic(graphicPark);

        greObj.clear();
        greObj.put("GraphicType",2);
        Bitmap bitmap3 = BitmapFactory.decodeResource(getResources(),R.drawable.texture3);
        Graphic graphicGreenBelt = new Graphic(polygonGreenBelt,new PictureFillSymbol(new BitmapDrawable(bitmap3)),greObj);
        //Graphic graphicGreenBelt = new Graphic(polygonGreenBelt,new SimpleFillSymbol(Color.GREEN),greObj);
        greenBeltLayer = new GraphicsLayer();
        greenBeltLayer.addGraphic(graphicGreenBelt);

        greObj.clear();
        greObj.put("GraphicType",3);
        Point loc0 = new Point(491067.0,3558797.0);
        Graphic graphic0 = new Graphic(loc0,new SimpleMarkerSymbol(Color.RED,10, SimpleMarkerSymbol.STYLE.CIRCLE),greObj);

        Point loc1 = new Point(500000.000,3560000.00);
        Graphic graphic1 = new Graphic(loc1,new SimpleMarkerSymbol(Color.RED,10, SimpleMarkerSymbol.STYLE.CIRCLE),greObj);

        greObj.clear();
        greObj.put("GraphicType",4);
        Point loc2 = new Point(490000.000,3556000.00);

        Graphic graphic2 = new Graphic(loc2,new SimpleMarkerSymbol(Color.BLACK,10, SimpleMarkerSymbol.STYLE.CIRCLE),greObj);



        //PictureMarkerSymbol symbol = new PictureMarkerSymbol(new BitmapDrawable(Bitmap.createBitmap(bitmap, 0, 0, bmpWidth, bmpHeght, matrix, true)));
        //Graphic graphic2 = new Graphic(loc2,symbol);

        greenTreeLayer = new GraphicsLayer();
        greenTreeLayer.addGraphic(graphic1);
        greenTreeLayer.addGraphic(graphic0);

        ancientTreeLayer = new GraphicsLayer();
        ancientTreeLayer.addGraphic(graphic2);

    }

    private void addGreenLayers(){
        createLayers();
//        Point p1 = new Point(491067.0,3558797.0);
//        Point p2 = new Point(501540.0,3558797.0);
//        Point p3 = new Point(501540.0,3566679.0);
//        Point p4 = new Point(491067.0,3566679.0);
//        ArrayList<Point> points = new ArrayList<Point>();
//
//        points.add(p1);
//        points.add(p2);
//        points.add(p3);
//        points.add(p4);
//        Point startPoint = null;
//        Point endPoint = null;
//        Polygon polygon = new Polygon();
//        for(int i = 0;i < points.size();i++){
//            startPoint = points.get(i);
//            endPoint = points.get((i+1)%4);
//            Line line = new Line();
//            line.setStart(startPoint);
//            line.setEnd(endPoint);
//
//            polygon.addSegment(line,false);
//
//        }
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.fengjing);
//        Graphic graphicPlgn1 = new Graphic(polygon,new PictureFillSymbol(new BitmapDrawable(bitmap)));
//        Graphic graphicPlgn = new Graphic(polygon,new SimpleFillSymbol(Color.MAGENTA));
//        sceneLayer = new GraphicsLayer();
//        sceneLayer.addGraphic(graphicPlgn);
        map.addLayer(sceneLayer);

//        Point loc0 = new Point(491067.0,3558797.0);
//        Graphic graphic0 = new Graphic(loc0,new SimpleMarkerSymbol(Color.CYAN,10, SimpleMarkerSymbol.STYLE.CIRCLE));
//
//        Point loc1 = new Point(500000.000,3560000.00);
//        Graphic graphic1 = new Graphic(loc1,new SimpleMarkerSymbol(Color.CYAN,10, SimpleMarkerSymbol.STYLE.CIRCLE));
//
//        Point loc2 = new Point(490000.000,3556000.00);
//        Graphic graphic2 = new Graphic(loc2,new SimpleMarkerSymbol(Color.BLACK,10, SimpleMarkerSymbol.STYLE.CIRCLE));
//
//        Point loc3 = new Point(490000.000,3570000.00);
//        Graphic graphic3 = new Graphic(loc3,new SimpleMarkerSymbol(Color.BLACK,10, SimpleMarkerSymbol.STYLE.CIRCLE));
//
//        Point loc4 = new Point(501000.000,3570000.00);
//        Graphic graphic4 = new Graphic(loc4,new SimpleMarkerSymbol(Color.CYAN,10, SimpleMarkerSymbol.STYLE.CIRCLE));

//        greenTreeLayer = new GraphicsLayer();
//        greenTreeLayer.addGraphic(graphic1);
//        greenTreeLayer.addGraphic(graphic0);
        map.addLayer(greenTreeLayer);
        greenTreeLayer.setVisible(false);

//        ancientTreeLayer = new GraphicsLayer();
//        ancientTreeLayer.addGraphic(graphic2);
        map.addLayer(ancientTreeLayer);

//        parkLayer = new GraphicsLayer();
//        parkLayer.addGraphic(graphic3);
        map.addLayer(parkLayer);

//        greenBeltLayer = new GraphicsLayer();
//        greenBeltLayer.addGraphic(graphic4);
        map.addLayer(greenBeltLayer);



    }

    /*
    * 设置全局显示按钮
    * */
    private void setGlobalViewButton(){
        // imgBtnGlobalView = (ImageButton) findViewById(R.id.imgbtn_global_view);
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
        // imgBtnLocate = (ImageButton) findViewById(R.id.imgbtn_locate);
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
                return new boolean[] {parkLayer.isVisible(),sceneLayer.isVisible(),greenBeltLayer.isVisible(),ancientTreeLayer.isVisible(),greenTreeLayer.isVisible()};
            }

            @Override
            public void changeLayerState(boolean[] layerState) {
                GraphicsLayer[] layers = new GraphicsLayer[]{parkLayer,sceneLayer,greenBeltLayer,ancientTreeLayer,greenTreeLayer};
                for(int i = 0;i < layers.length;i++){
                    layers[i].setVisible(layerState[i]);
                    isGreenTreeLayerVisible = layerState[layers.length-1];
                }

            }
        });
        // imgBtnLayerSwitch = (ImageButton) findViewById(R.id.imgbtn_layer_switch);
        imgBtnLayerSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int[] location = new int[2];
                view.getLocationOnScreen(location);
                layerSwitchPopupWindow.show(view);
                //layerSwitchPopupWindow.showAtLocation(view, Gravity.LEFT,0,0);
            }
        });
    }

    /*
    *恢复行道树图层中树木的显示
    * */
    private void recoverGreenTreeLayer(){
        GraphicsLayer graphicsLayer = greenTreeLayer;
        int[] ids = graphicsLayer.getGraphicIDs();
        for(int i = 0;i < ids.length;i++){
            graphicsLayer.setGraphicVisible(ids[i],true);
        }
        greenTreeLayer.setVisible(isGreenTreeLayerVisible);
    }

    /*
    * 设置周边按钮
    * */
    private void setNearbyButton(){


        nearbyLocListener = new NearbyLocListener();
        nearbyLocMag = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // chkBoxNearby = (CheckBox) findViewById(R.id.cb_nearby);
        chkBoxNearby.setChecked(false);

        chkBoxNearby.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //若按钮开启，则设置位置监听，搜索附近的行道树
                if(b){
                    isGreenTreeLayerVisible = greenTreeLayer.isVisible();
                    chkBoxNearby.setBackgroundResource(R.mipmap.ic_nearby_selected);

//                    if(nearbyLocMag.getProvider(LocationManager.NETWORK_PROVIDER) != null){
//                        if(ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
//                                ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//                            nearbyLocMag.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,5000,5,nearbyLocListener);
//                        }
//
//                    }else if(nearbyLocMag.getProvider(LocationManager.GPS_PROVIDER) != null) {
//                        if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
//                                ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                            nearbyLocMag.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, nearbyLocListener);
//                        }
//                    }
                    searchNearbyTrees(new Point(491060.0,3558790.0));
                }
                //否则，移除位置监听，恢复行道树图层的初始状态
                else {
                    //isGreenTreeLayerVisible = greenTreeLayer.isVisible();
                    chkBoxNearby.setBackgroundResource(R.mipmap.ic_nearby_unselected);

                    if(ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        nearbyLocMag.removeUpdates(nearbyLocListener);
                    }
                    recoverGreenTreeLayer();
                }
            }
        });
    }

    /*
    * 搜索某个点附近xxx米范围内的行道树
    * */
    private void searchNearbyTrees(Point loc){
        isGreenTreeLayerVisible = greenTreeLayer.isVisible();
        GraphicsLayer graphicsLayer = greenTreeLayer;
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
        if(graphicType == 0){
            sceneLayer.setSelectedGraphics(ids,true);
        }else if(graphicType == 1){
            parkLayer.setSelectedGraphics(ids,true);
        }else if(graphicType == 2){
            greenBeltLayer.setSelectedGraphics(ids,true);
        }else if(graphicType == 3){
            greenTreeLayer.setSelectedGraphics(ids,true);
        }else if(graphicType == 4){
            ancientTreeLayer.setSelectedGraphics(ids,true);
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
            GraphicsLayer[] layers = new GraphicsLayer[]{parkLayer,sceneLayer,greenBeltLayer,ancientTreeLayer,greenTreeLayer};

            Integer graphicID = null;
            int layerIndex = 0;

            //在每个图层中搜索被点击的“绿地”标识
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
            bottomBar.setVisibility(View.VISIBLE);
            Graphic graphic = layers[layerIndex].getGraphic(graphicID);
            highlightGraphic(graphic);
//            View view = LayoutInflater.from(MapActivity.this).inflate(R.layout.map_callout,null);
//
//
//            callout.setContent(view);
//            callout.setOffset(0,-15);
//            callout.setStyle(R.xml.callout_style);
//
//            //在点击的地方显示出callout
//            if(graphic.getGeometry().getType() == Geometry.Type.POINT){
//                callout.show((Point)graphic.getGeometry());
//            }else {
//                Point showAt;
//                showAt = map.toMapPoint(v,v1);
//                callout.show(showAt);
//            }
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
     * 设置软件更新
     */

    private void setUpdate(){
        if(getIntent().getStringExtra("apk_url") != null){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("更新提示");
            builder.setMessage("检测到软件有更新，是否现在安装?");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent1 = new Intent(MapActivity.this,DownloadNewApkService.class);
                    startService(intent1);

                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.setCancelable(false);
            builder.show();
        }
    }

    /**
     * 设置bootbar中按钮的点击事件
     */
    private void setBottomBar(){
        Button btnAddEvent = (Button) findViewById(R.id.btn_map_add_event);
        btnAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapActivity.this, EventRegisterActivity.class);
                startActivity(intent);
            }
        });

        Button btnAddMaintain = (Button) findViewById(R.id.btn_map_add_maintain);
        btnAddMaintain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapActivity.this, MaintainInfoActivity.class);
                startActivity(intent);
            }
        });

        Button btnAddInspect = (Button) findViewById(R.id.btn_map_add_inspect);
        btnAddInspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapActivity.this, InspectInfoActivity.class);
                startActivity(intent);
            }
        });

        Button btnCheckBasic = (Button) findViewById(R.id.btn_map_check_basicInfo);
        btnCheckBasic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

}
