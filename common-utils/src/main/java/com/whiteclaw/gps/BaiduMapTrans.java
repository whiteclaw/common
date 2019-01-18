package com.whiteclaw.gps;

import java.text.DecimalFormat;

/**
 * 将百度地图编码过的坐标转化为标准BD09坐标
 *
 * @author heys
 * created on 2019-01-18
 */
public class BaiduMapTrans {

    private final static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.000000");
    private final static double EARTHRADIUS = 6370996.81;
    private final static double[] MCBAND = new double[]{1.289059486e7, 8362377.87, 5591021, 3481989.83, 1678043.12, 0};
    private final static int[] LLBAND = new int[]{75, 60, 45, 30, 15, 0};
    private final static double[][] MC2LL = new double[][]{
            {1.410526172116255e-8, 8.98305509648872e-6, -1.9939833816331, 200.9824383106796, -187.2403703815547, 91.6087516669843, -23.38765649603339, 2.57121317296198, -0.03801003308653, 1.73379812e7},
            {-7.435856389565537e-9, 8.983055097726239e-6, -0.78625201886289, 96.32687599759846, -1.85204757529826, -59.36935905485877, 47.40033549296737, -16.50741931063887, 2.28786674699375, 1.026014486e7},
            {-3.030883460898826e-8, 8.98305509983578e-6, 0.30071316287616, 59.74293618442277, 7.357984074871, -25.38371002664745, 13.45380521110908, -3.29883767235584, 0.32710905363475, 6856817.37},
            {-1.981981304930552e-8, 8.983055099779535e-6, 0.03278182852591, 40.31678527705744, 0.65659298677277, -4.44255534477492, 0.85341911805263, 0.12923347998204, -0.04625736007561, 4482777.06},
            {3.09191371068437e-9, 8.983055096812155e-6, 6.995724062e-5, 23.10934304144901, -2.3663490511e-4, -0.6321817810242, -0.00663494467273, 0.03430082397953, -0.00466043876332, 2555164.4},
            {2.890871144776878e-9, 8.983055095805407e-6, -3.068298e-8, 7.47137025468032, -3.53937994e-6, -0.02145144861037, -1.234426596e-5, 1.0322952773e-4, -3.23890364e-6, 826088.5}
    };

    private final static double[][] LL2MC = new double[][]{
            {-0.0015702102444, 111320.7020616939, 1.704480524535203e15, -1.0338987376042340e16, 2.611266785660388e16, -3.51496691766537e16, 2.659570071840392e16, -1.072501245418824e16, 1.800819912950474e15, 82.5},
            {8.277824516172526e-4, 111320.7020463578, 6.477955746671607e8, -4.082003173641316e9, 1.077490566351142e10, -1.517187553151559e10, 1.205306533862167e10, -5.124939663577472e9, 9.133119359512032e8, 67.5},
            {0.00337398766765, 111320.7020202162, 4481351.045890365, -2.339375119931662e7, 7.968221547186455e7, -1.159649932797253e8, 9.723671115602145e7, -4.366194633752821e7, 8477230.501135234, 52.5},
            {0.00220636496208, 111320.7020209128, 51751.86112841131, 3796837.749470245, 992013.7397791013, -1221952.21711287, 1340652.697009075, -620943.6990984312, 144416.9293806241, 37.5},
            {-3.441963504368392e-4, 111320.7020576856, 278.2353980772752, 2485758.690035394, 6070.750963243378, 54821.18345352118, 9540.606633304236, -2710.55326746645, 1405.483844121726, 22.5},
            {-3.218135878613132e-4, 111320.7020701615, 0.00369383431289, 823725.6402795718, 0.46104986909093, 2351.343141331292, 1.58060784298199, 8.77738589078284, 0.37238884252424, 7.45}
    };

    /**
     * 百度转码后的经纬度转化为bd09的经纬度
     *
     * @param lng 纬度
     * @param lat 经度
     * @return 转化后的经纬度
     */
    public static Point trans(double lng, double lat) {
        double[] factor = null;
        for (int i = 0; i < MCBAND.length; i++) {
            if (lat >= MCBAND[i]) {
                factor = MC2LL[i];
                break;
            }
        }
        return convert(new Point(lng, lat), factor);
    }

    private static Point convert(Point fromPoint, double[] factor) {
        if (fromPoint == null || factor == null) {
            return null;
        }

        double x = factor[0] + factor[1] * Math.abs(fromPoint.lng);

        double temp = Math.abs(fromPoint.lat) / factor[9];
        double y = factor[2] + factor[3] * temp +
                factor[4] * temp * temp +
                factor[5] * temp * temp * temp +
                factor[6] * temp * temp * temp * temp +
                factor[7] * temp * temp * temp * temp * temp +
                factor[8] * temp * temp * temp * temp * temp * temp;
        x *= fromPoint.lng < 0 ? -1 : 1;
        y *= fromPoint.lat < 0 ? -1 : 1;
        return new Point(Double.parseDouble(DECIMAL_FORMAT.format(x)), Double.parseDouble(DECIMAL_FORMAT.format(y)));
    }

    public static class Point {
        /**
         * 纬度
         */
        private double lng;
        /**
         * 经度
         */
        private double lat;

        public Point() {
        }

        public Point(double lng, double lat) {
            this.lng = lng;
            this.lat = lat;
        }

        public double getLng() {
            return this.lng;
        }

        public double getLat() {
            return this.lat;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (!(o instanceof BaiduMapTrans.Point)) {
                return false;
            } else {
                BaiduMapTrans.Point other = (BaiduMapTrans.Point) o;
                if (!other.canEqual(this)) {
                    return false;
                } else if (Double.compare(this.getLng(), other.getLng()) != 0) {
                    return false;
                } else {
                    return Double.compare(this.getLat(), other.getLat()) == 0;
                }
            }
        }

        private boolean canEqual(Object other) {
            return other instanceof BaiduMapTrans.Point;
        }

        @Override
        public int hashCode() {
            int result = 1;
            long longitude = Double.doubleToLongBits(this.getLng());
            result = result * 59 + (int) (longitude >>> 32 ^ longitude);
            long latitude = Double.doubleToLongBits(this.getLat());
            result = result * 59 + (int) (latitude >>> 32 ^ latitude);
            return result;
        }

        @Override
        public String toString() {
            return "BaiduMapTrans.Point(lng=" + this.getLng() + ", lat=" + this.getLat() + ")";
        }
    }
}
