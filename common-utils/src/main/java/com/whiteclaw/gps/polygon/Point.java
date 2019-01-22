package com.whiteclaw.gps.polygon;

/**
 * @author whiteclaw
 * created on 2019-01-21
 */
public class Point {
    /**
     * 经度
     */
    public final double lng;
    /**
     * 纬度
     */
    public final double lat;

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

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Point)) {
            return false;
        } else {
            Point other = (Point) o;
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
        return other instanceof Point;
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
        return "Point(lng=" + this.getLng() + ", lat=" + this.getLat() + ")";
    }
}
