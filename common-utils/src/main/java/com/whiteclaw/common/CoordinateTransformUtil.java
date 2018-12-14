package com.whiteclaw.common;

/**
 * 百度坐标（BD09）、国测局坐标（火星坐标，GCJ02）、和WGS84坐标系之间的转换的工具
 *
 * 原始地址: https://github.com/geosmart/coordtransform/blob/master/src/main/java/me/demo/util/geo/CoordinateTransformUtil.java
 *
 * @author heys
 * created on 2018/11/23.
 */
public class CoordinateTransformUtil {
    private static final double X_PI = 3.14159265358979324 * 3000.0 / 180.0;
    /**
     * 长半轴
     */
    private static final double A = 6378245.0;
    /**
     * 扁率
     */
    private static final double EE = 0.00669342162296594323;

    /**
     * 百度坐标系(BD-09)转WGS坐标
     *
     * @param lon 百度坐标纬度
     * @param lat 百度坐标经度
     * @return WGS84坐标数组
     */
    public static double[] bd09ToWGS84(double lat, double lon) {
        double[] gcj = bd09ToGCJ02(lat, lon);
        return gcj02ToWGS84(gcj[0], gcj[1]);
    }

    /**
     * WGS坐标转百度坐标系(BD-09)
     *
     * @param lon WGS84坐标系的经度
     * @param lat WGS84坐标系的纬度
     * @return 百度坐标数组
     */
    public static double[] wgs84ToBD09(double lat, double lon) {
        double[] gcj = wgs84ToGCJ02(lat, lon);
        return gcj02ToBD09(gcj[0], gcj[1]);
    }

    /**
     * 火星坐标系(GCJ-02)转百度坐标系(BD-09)
     * 谷歌、高德——>百度
     *
     * @param lon 火星坐标经度
     * @param lat 火星坐标纬度
     * @return 百度坐标数组
     */
    public static double[] gcj02ToBD09(double lat, double lon) {
        double z = Math.sqrt(lon * lon + lat * lat) + 0.00002 * Math.sin(lat * X_PI);
        double theta = Math.atan2(lat, lon) + 0.000003 * Math.cos(lon * X_PI);
        double bdLon = z * Math.cos(theta) + 0.0065;
        double bdLat = z * Math.sin(theta) + 0.006;
        return new double[]{bdLat, bdLon};
    }

    /**
     * 百度坐标系(BD-09)转火星坐标系(GCJ-02)
     * 百度——>谷歌、高德
     *
     * @param lon 百度坐标纬度
     * @param lat 百度坐标经度
     * @return 火星坐标数组
     */
    public static double[] bd09ToGCJ02(double lat, double lon) {
        double x = lon - 0.0065;
        double y = lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * X_PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * X_PI);
        double googleLon = z * Math.cos(theta);
        double googleLat = z * Math.sin(theta);
        return new double[]{googleLat, googleLon};
    }

    /**
     * WGS84转GCJ02(火星坐标系)
     *
     * @param lon WGS84坐标系的经度
     * @param lat WGS84坐标系的纬度
     * @return 火星坐标数组
     */
    public static double[] wgs84ToGCJ02(double lat, double lon) {
        if (outOfChina(lat, lon)) {
            return new double[]{lat, lon};
        }
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radlat = lat / 180.0 * Math.PI;
        double magic = Math.sin(radlat);
        magic = 1 - EE * magic * magic;
        double sqrtmagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((A * (1 - EE)) / (magic * sqrtmagic) * Math.PI);
        dLon = (dLon * 180.0) / (A / sqrtmagic * Math.cos(radlat) * Math.PI);
        double mglat = lat + dLat;
        double mgLon = lon + dLon;
        return new double[]{mglat, mgLon};
    }

    /**
     * GCJ02(火星坐标系)转GPS84
     *
     * @param lon 火星坐标系的经度
     * @param lat 火星坐标系纬度
     * @return WGS84坐标数组
     */
    public static double[] gcj02ToWGS84(double lat, double lon) {
        if (outOfChina(lat, lon)) {
            return new double[]{lat, lon};
        }
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radlat = lat / 180.0 * Math.PI;
        double magic = Math.sin(radlat);
        magic = 1 - EE * magic * magic;
        double sqrtmagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((A * (1 - EE)) / (magic * sqrtmagic) * Math.PI);
        dLon = (dLon * 180.0) / (A / sqrtmagic * Math.cos(radlat) * Math.PI);
        double mglat = lat + dLat;
        double mgLon = lon + dLon;
        return new double[]{lat * 2 - mglat, lon * 2 - mgLon};
    }

    /**
     * 纬度转换
     */
    public static double transformLat(double lat, double lon) {
        double ret = -100.0 + 2.0 * lon + 3.0 * lat + 0.2 * lat * lat + 0.1 * lon * lat + 0.2 * Math.sqrt(Math.abs(lon));
        ret += (20.0 * Math.sin(6.0 * lon * Math.PI) + 20.0 * Math.sin(2.0 * lon * Math.PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lat * Math.PI) + 40.0 * Math.sin(lat / 3.0 * Math.PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(lat / 12.0 * Math.PI) + 320 * Math.sin(lat * Math.PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    /**
     * 经度转换
     */
    public static double transformLon(double lat, double lon) {
        double ret = 300.0 + lon + 2.0 * lat + 0.1 * lon * lon + 0.1 * lon * lat + 0.1 * Math.sqrt(Math.abs(lon));
        ret += (20.0 * Math.sin(6.0 * lon * Math.PI) + 20.0 * Math.sin(2.0 * lon * Math.PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lon * Math.PI) + 40.0 * Math.sin(lon / 3.0 * Math.PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(lon / 12.0 * Math.PI) + 300.0 * Math.sin(lon / 30.0 * Math.PI)) * 2.0 / 3.0;
        return ret;
    }

    /**
     * 判断是否在国内，不在国内不做偏移
     */
    public static boolean outOfChina(double lat, double lon) {
        if (lon < 72.004 || lon > 137.8347) {
            return true;
        } else if (lat < 0.8293 || lat > 55.8271) {
            return true;
        }
        return false;
    }
}