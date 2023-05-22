package net.ncie.cutemap.fragment;

import static net.ncie.cutemap.bean.Categories.DEFAULT_DISPLACEMENT;
import static net.ncie.cutemap.bean.Categories.DEFAULT_FASTEST_INTERVAL;
import static net.ncie.cutemap.bean.Categories.DEFAULT_INTERVAL;
import static net.ncie.cutemap.bean.Categories.DEFAULT_MAX_WAIT_TIME;
import static net.ncie.cutemap.util.MapUtil.addPointAnnotationInMap;
import static net.ncie.cutemap.util.MapUtil.moveCameraTo;
import static net.ncie.cutemap.util.MapUtil.setCurrentLocation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.MapboxMap;
import com.mapbox.maps.plugin.Plugin;
import com.mapbox.maps.plugin.animation.CameraAnimationsPlugin;
import com.mapbox.maps.plugin.annotation.AnnotationPlugin;
import com.mapbox.maps.plugin.annotation.AnnotationType;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.compass.CompassPlugin;
import com.mapbox.maps.plugin.locationcomponent.LocationComponentPlugin;
import com.mapbox.maps.plugin.scalebar.ScaleBarPlugin;
import com.tencent.mmkv.MMKV;

import net.ncie.cutemap.R;
import net.ncie.cutemap.act.SearchAct;
import net.ncie.cutemap.bean.Categories;

import java.util.ArrayList;

public class MapFragment extends Fragment implements View.OnClickListener , LocationEngineCallback<LocationEngineResult> {
    private int isChoose = 0;
    private PointAnnotationManager pointAnnotationManager;
    private AnnotationPlugin annotationPlugin;
    private double latitude;
    private double longitude;
    private MapView mapView;
    private MapboxMap mapboxMap;
    private static final double sf = 8.0;   // 初始地图缩放大小
    private CameraAnimationsPlugin plugin;
    private ImageView menu_map;
    private ImageView My_position;
    private ImageView search_ic;
    private LocationEngine locationEngine;
    private String markLongitude;
    private String markLatitude;
    private LocationEngineRequest locationEngineRequest;

    private Context context;

    private PopupWindow popupWindow;

    private View contentView;
    private RelativeLayout bar;
    public MapFragment(Context context){
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.act_map, container, false);
        initView(rootView);
        initDate();
        initListener();
        return rootView;
    }

    public void initView(View rootView){
        mapView = rootView.findViewById(R.id.mapview);
        My_position = rootView.findViewById(R.id.My_position);
        menu_map = rootView.findViewById(R.id.menu_map);
        bar = rootView.findViewById(R.id.bar);

        CompassPlugin compassPlugin = mapView.getPlugin(Plugin.MAPBOX_COMPASS_PLUGIN_ID);
        if (compassPlugin != null) {
            compassPlugin.setMarginTop(160);
        }

        ScaleBarPlugin scaleBarPlugin = mapView.getPlugin(Plugin.MAPBOX_SCALEBAR_PLUGIN_ID);
        //  比例尺
        if (scaleBarPlugin != null) {
            scaleBarPlugin.setMarginTop(180);
        }


    }

    public void initDate(){
        mapboxMap = mapView.getMapboxMap();
        plugin = mapView.getPlugin(Plugin.MAPBOX_CAMERA_PLUGIN_ID);

        MapSettings();

        //地图标记插件
        annotationPlugin = mapView.getPlugin(Plugin.MAPBOX_ANNOTATION_PLUGIN_ID);
        // 注册点标记管理器
        pointAnnotationManager = (PointAnnotationManager)
                annotationPlugin .createAnnotationManager(AnnotationType.PointAnnotation, null);

            setDfMap(mapboxMap);


    }

    public void setDfMap(MapboxMap mapboxMap){


        //地图初始位置
        mapboxMap.setCamera(new CameraOptions.Builder()
                .center(Point.fromLngLat(104.065948, 30.536823))
                //.center(Point.fromLngLat(DEFAULT_LOCATION_LONGITUDE, DEFAULT_LOCATION_LATITUDE))
                .zoom(sf)
                .build()
        );
        mapboxMap.loadStyleUri(Categories.DEFAULT_MAP_STYLE[0], style -> {
        });


    }

    public void MapSettings(){

        LocationComponentPlugin locationPlugin = mapView.getPlugin(Plugin.MAPBOX_LOCATION_COMPONENT_PLUGIN_ID);

        locationPlugin.updateSettings(locationComponentSettings -> {
            locationComponentSettings.setEnabled(true);
            locationComponentSettings.setPulsingEnabled(true);  // 脉冲效果
            return null;
        });

        //地图默认初始化位置
        mapboxMap.setCamera(new CameraOptions.Builder()
                .center(Point.fromLngLat(104.065948, 30.536823))
                //.center(Point.fromLngLat(DEFAULT_LOCATION_LONGITUDE, DEFAULT_LOCATION_LATITUDE))
                .zoom(sf)
                .build()
        );
        mapboxMap.loadStyleUri(Categories.DEFAULT_MAP_STYLE[0], style -> {
        });

        locationEngineRequest = new LocationEngineRequest.Builder(DEFAULT_INTERVAL)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setDisplacement(DEFAULT_DISPLACEMENT)
                .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME)
                .setFastestInterval(DEFAULT_FASTEST_INTERVAL)
                .build();
    }

    public void initListener(){
        My_position.setOnClickListener(this);
        menu_map.setOnClickListener(this);
        bar.setOnClickListener(this);

    }

    @SuppressLint("MissingInflatedId")
    private void showPopupWindow(View anchorView) {
        if (popupWindow!=null){

            AnimationSet animationSet = fadeAnimation(popupWindow, anchorView);
            contentView.startAnimation(animationSet);
            popupWindow = null;
        }else {
            menu_map.setImageResource(R.mipmap.btn_common_map_type_selecting);
            popupWindow = new PopupWindow(context);
            popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
            popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            contentView = LayoutInflater.from(context).inflate(R.layout.popup_item, null);
            ImageView option1 = contentView.findViewById(R.id.option1);
            ImageView option2 = contentView.findViewById(R.id.option2);
            ImageView option3 = contentView.findViewById(R.id.option3);

            option1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 处理点击事件
                 //   option1.setBackgroundResource(R.mipmap.btn_dialog_gen_map_type_positive);
                    AnimationSet animationSet = fadeAnimation(popupWindow, anchorView);
                    contentView.startAnimation(animationSet);
                    mapboxMap.loadStyleUri(Categories.DEFAULT_MAP_STYLE[0], style -> {
                    });
                    popupWindow = null;
                }
            });

            option2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 处理点击事件
                    AnimationSet animationSet = fadeAnimation(popupWindow, anchorView);
                    contentView.startAnimation(animationSet);
                    mapboxMap.loadStyleUri(Categories.DEFAULT_MAP_STYLE[3], style -> {
                    });
                    popupWindow = null;
                }
            });

            option3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 处理点击事件
                    AnimationSet animationSet = fadeAnimation(popupWindow, anchorView);
                    contentView.startAnimation(animationSet);
                    mapboxMap.loadStyleUri(Categories.DEFAULT_MAP_STYLE[6], style -> {
                    });
                    popupWindow = null;
                }
            });


// 测量popup_item的高度
            contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            int popupItemHeight = contentView.findViewById(R.id.linear1).getMeasuredHeight();
            int popupItemWidth = contentView.findViewById(R.id.linear1).getMeasuredWidth();


            popupWindow.setContentView(contentView);

            int[] anchorLocation = new int[2];
            anchorView.getLocationOnScreen(anchorLocation);
            System.out.println(anchorLocation[1] + "****" + popupItemHeight);

            int pivotX = anchorLocation[0];
            int pivotY = anchorLocation[1] - popupItemHeight;

            TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, anchorView.getHeight(), 0);
            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(translateAnimation);
            animationSet.addAnimation(alphaAnimation);
            animationSet.setDuration(500);
            contentView.startAnimation(animationSet);

            System.out.println("--" + anchorView.getWidth() / 2);

            popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, pivotX + (anchorView.getWidth() / 2) - (popupItemWidth / 2), pivotY);
        }
    }



    public AnimationSet fadeAnimation(PopupWindow popupWindow,View anchorView){
        menu_map.setImageResource(R.mipmap.btn_common_map_type_pic);
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, anchorView.getHeight());
        AlphaAnimation alphaAnimation = new AlphaAnimation( 1.0f, 0.0f);
        alphaAnimation.setFillAfter(true);
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(translateAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setDuration(500);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                popupWindow.dismiss();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        return animationSet;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.menu_map:
                showPopupWindow(v);
                break;
            case R.id.My_position:
                moveCameraTo(Point.fromLngLat(longitude,latitude),16.0,1000, plugin, mapboxMap, mapView);
                break;
            case R.id.bar:
                startActivity(new Intent(context,SearchAct.class));

                break;
        }
    }



    @Override
    public void onSuccess(LocationEngineResult result) {
        System.out.println("请求位置成功");
        ArrayList<Double> list = setCurrentLocation(markLongitude,result, plugin, mapboxMap, mapView);
        latitude = list.get(0);
        longitude = list.get(1);
    }

    @Override
    public void onFailure(@NonNull Exception exception) {

    }


    @Override
    public void onResume() {
        super.onResume();

        locationEngine = LocationEngineProvider.getBestLocationEngine(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            return;
        }
        System.out.println("请求位置");
        locationEngine.requestLocationUpdates(locationEngineRequest,  this, Looper.getMainLooper());

        markLongitude = MMKV.defaultMMKV().decodeString("longitude");
        if (MMKV.defaultMMKV().decodeString("longitude")!=null&&MMKV.defaultMMKV().decodeString("latitude")!=null){
            double longitude = Double.parseDouble(MMKV.defaultMMKV().decodeString("longitude"));
            double latitude = Double.parseDouble(MMKV.defaultMMKV().decodeString("latitude"));
            addPointAnnotationInMap(context,annotationPlugin,pointAnnotationManager,longitude,latitude);
            moveCameraTo(Point.fromLngLat(longitude,latitude),16.0,1000, plugin, mapboxMap, mapView);

            MMKV.defaultMMKV().removeValueForKey("longitude");
            MMKV.defaultMMKV().removeValueForKey("latitude");
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates(this);
        }
    }
}
