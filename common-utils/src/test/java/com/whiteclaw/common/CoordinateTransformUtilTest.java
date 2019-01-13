package com.whiteclaw.common;

import com.whiteclaw.CoordinateTransformUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author whiteclaw
 * created on 2018-12-14
 */
class CoordinateTransformUtilTest {
    private double[] BD09 = new double[]{18.26101537329914, 109.4974007831044};
    private double[] WGS84 = new double[]{18.254770497679118, 109.48868774344767};

    @Test
    void bd09ToWGS84() {
        double[] result = CoordinateTransformUtil.bd09ToWGS84(BD09[0], BD09[1]);
        assertEquals(result[0], WGS84[0]);
        assertEquals(result[1], WGS84[1]);
    }

    @Test
    void wgs84ToBD09() {
    }

    @Test
    void gcj02ToBD09() {
    }

    @Test
    void bd09ToGCJ02() {
    }

    @Test
    void wgs84ToGCJ02() {
    }

    @Test
    void gcj02ToWGS84() {
    }
}
