package net.ncie.cutemap.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.core.content.ContextCompat;

import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.MapboxMap;
import com.mapbox.maps.plugin.animation.CameraAnimationsPlugin;
import com.mapbox.maps.plugin.animation.MapAnimationOptions;
import com.mapbox.maps.plugin.annotation.AnnotationPlugin;
import com.mapbox.maps.plugin.annotation.AnnotationType;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;

import net.ncie.cutemap.R;
import net.ncie.cutemap.bean.Categories;


import java.util.ArrayList;

public class MapUtil {

    /*public static void showPopupMenu(MapboxMap mapboxMap,View view, Context context) {
        final PopupMenu popupMenu = new PopupMenu(context,view);
        //menu 布局
        popupMenu.getMenuInflater().inflate(R.menu.map_type,popupMenu.getMenu());
        //点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.map_1:
                        mapboxMap.loadStyleUri(Categories.DEFAULT_MAP_STYLE[0], style -> {});
                        break;
                    case R.id.map_2:
                        mapboxMap.loadStyleUri(Categories.DEFAULT_MAP_STYLE[1], style -> {});
                        break;
                    case R.id.map_3:
                        mapboxMap.loadStyleUri(Categories.DEFAULT_MAP_STYLE[4], style -> {});
                        break;
                    case R.id.map_4:
                        mapboxMap.loadStyleUri(Categories.DEFAULT_MAP_STYLE[5], style -> {});
                        break;
                    case R.id.map_5:
                        mapboxMap.loadStyleUri(Categories.DEFAULT_MAP_STYLE[2], style -> {});
                        break;
                    case R.id.map_6:
                        mapboxMap.loadStyleUri(Categories.DEFAULT_MAP_STYLE[3], style -> {});
                        break;
                    case R.id.map_7:
                        mapboxMap.loadStyleUri(Categories.DEFAULT_MAP_STYLE[6], style -> {});
                        break;
                    case R.id.map_8:
                        mapboxMap.loadStyleUri(Categories.DEFAULT_MAP_STYLE[7], style -> {});
                        break;
                }
                return false;
            }
        });
        //关闭事件
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
            }
        });
        //显示菜单，不要少了这一步
        popupMenu.show();
    }
*/
    public static void moveCameraTo(Point point, double zoom, int duration, CameraAnimationsPlugin cameraAnimationsPlugin, MapboxMap mapboxMap, MapView mapView) {
        if (mapView == null) {
            return;
        }
        if (duration != 0 && cameraAnimationsPlugin != null) {
            cameraAnimationsPlugin.flyTo(new CameraOptions.Builder()
                            .center(point)
                            .zoom(zoom)
                            .build(),
                    new MapAnimationOptions.Builder().duration(duration).build());
        } else {
            mapboxMap.setCamera(new CameraOptions.Builder()
                    .center(point)
                    .zoom(zoom)
                    .build());
        }
    }
    public static  ArrayList<Double> setCurrentLocation(String markLongitude,LocationEngineResult result,CameraAnimationsPlugin cameraAnimationsPlugin,MapboxMap mapboxMap,MapView mapView){
        Location lastLocation = result.getLastLocation();
        ArrayList<Double> list = new ArrayList<>();
        if (lastLocation != null) {
            double latitude = lastLocation.getLatitude();
            double longitude = lastLocation.getLongitude();
            list.add(latitude);
            list.add(longitude);
            if (markLongitude==null) {
                moveCameraTo(Point.fromLngLat(longitude, latitude), 16.0, 1000, cameraAnimationsPlugin, mapboxMap, mapView);
            }
        }
        return list;
    }

    /**
     * 在地图中添加Point类型标记
     * @param longitude 添加坐标X
     * @param latitude  添加坐标Y
     */
    public static void addPointAnnotationInMap(Context context,AnnotationPlugin annotationPlugin,PointAnnotationManager pointAnnotationManager,double longitude, double latitude) {
        if(annotationPlugin == null){
            return;
        }
        if (pointAnnotationManager == null) {
            pointAnnotationManager = (PointAnnotationManager)
                    annotationPlugin .createAnnotationManager(AnnotationType.PointAnnotation, null);
        }
        Resources res = context.getResources();
        Bitmap bmp = decodeBitmapFromResource(context, R.drawable.location);


        System.out.println(bmp);
        PointAnnotationOptions pointAnnotationOptions = new PointAnnotationOptions()
                .withPoint(Point.fromLngLat(longitude, latitude))
                .withIconImage(bmp);
        pointAnnotationManager .create(pointAnnotationOptions);
    }

    public static Bitmap decodeBitmapFromResource(Context context, int drawableId) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            return BitmapFactory.decodeResource(context.getResources(), drawableId);
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }


}
