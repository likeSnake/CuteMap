package net.ncie.cutemap.bean;

import com.mapbox.maps.Style;

public class Categories {
    public static final String[] DEFAULT_MAP_STYLE = {Style.MAPBOX_STREETS,Style.OUTDOORS,Style.SATELLITE,Style.SATELLITE_STREETS
            ,Style.LIGHT,Style.DARK,Style.TRAFFIC_DAY,Style.TRAFFIC_NIGHT
    };


    public static final float DEFAULT_DISPLACEMENT = 3.0f;

    public static final long DEFAULT_MAX_WAIT_TIME = 5000L;

    public static final long DEFAULT_FASTEST_INTERVAL = 1000L;

    public static final long DEFAULT_INTERVAL = 5000L;
}
