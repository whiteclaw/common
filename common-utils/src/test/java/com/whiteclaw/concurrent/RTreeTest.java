package com.whiteclaw.concurrent;

import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.Rectangle;
import com.whiteclaw.gps.BaiduMapTrans;
import com.whiteclaw.gps.polygon.Point;
import com.whiteclaw.gps.polygon.Polygon;
import rx.Observable;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author whiteclaw
 * created on 2019-01-21
 */
public class RTreeTest {
    public static void main(String[] args) throws Exception {
        URL resource = RTreeTest.class.getClassLoader().getResource("poi.csv");
        if (resource == null) {
            return;
        }
        HashMap<String, Polygon> polygonHashMap = new HashMap<>();
        RTree<String, Geometry> rTree = RTree.create();
        Path path = Paths.get(resource.toURI());
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path.toFile())));
        String line;
        while ((line = br.readLine()) != null) {
            String[] cols = line.split("\t");
            String name = cols[0];
            String[] lngLats = cols[1].split("\\|");
            String[] recs = lngLats[1].split(";");
            List<Point> points = Arrays.stream(recs).map(r -> {
                String[] lngLat = r.split(",");
                return BaiduMapTrans.trans(Double.parseDouble(lngLat[0]), Double.parseDouble(lngLat[1]));
            }).collect(Collectors.toList());
            Rectangle rectangle = Geometries.rectangleGeographic(points.get(0).lng, points.get(0).lat,
                    points.get(1).lng, points.get(1).lat);
            rTree = rTree.add(name, rectangle);
            String polylons = lngLats[2];
            if (';' == polylons.charAt(polylons.length() - 1)) {
                polylons = polylons.substring(0, polylons.length() - 2);
            }
            List<Double> pts = Arrays.stream(polylons.split("-")[1].split(","))
                    .map(Double::parseDouble)
                    .collect(Collectors.toList());
            Polygon.Builder builder = Polygon.Builder();
            for (int i = 0; i < (pts.size() >> 1); i++) {
                Point trans = BaiduMapTrans.trans(pts.get(2 * i), pts.get(2 * i + 1));
                builder.addVertex(trans);
            }
            polygonHashMap.put(name, builder.build());
        }
        br.close();

        Observable<Entry<String, Geometry>> search = rTree.search(Geometries.point(121.593886,31.260394));
        Iterator<Entry<String, Geometry>> iterator = search.toBlocking().getIterator();
        while (iterator.hasNext()) {
            Entry<String, Geometry> next = iterator.next();
            String name = next.value();
            System.out.println("rTree contains");
            Polygon polygon = polygonHashMap.get(name);
            boolean contains = polygon.contains(121.593886,31.260394);
            System.out.println(contains);
        }
    }
}
