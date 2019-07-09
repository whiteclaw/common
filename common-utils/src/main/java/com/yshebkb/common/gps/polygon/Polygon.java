package com.yshebkb.common.gps.polygon;

import java.util.ArrayList;
import java.util.List;

/**
 * The 2D polygon. <br>
 *
 * @author Roman Kushnarenko (sromku@gmail.com)
 * @see {@link Builder}
 */
public class Polygon {

    private final BoundingBox boundingBox;
    private final List<Line> sides;

    private Polygon(List<Line> sides, BoundingBox boundingBox) {
        this.sides = sides;
        this.boundingBox = boundingBox;
    }

    /**
     * Get the builder of the polygon
     *
     * @return The builder
     */
    public static Builder Builder() {
        return new Builder();
    }

    /**
     * Builder of the polygon
     *
     * @author Roman Kushnarenko (sromku@gmail.com)
     */
    public static class Builder {
        private List<Point> vertexes = new ArrayList<>();
        private List<Line> sides = new ArrayList<>();
        private BoundingBox boundingBox = null;

        private boolean firstPoint = true;
        private boolean isClosed = false;

        /**
         * Add vertex points of the polygon.<br>
         * It is very important to add the vertexes by order, like you were drawing them one by one.
         *
         * @param point The vertex point
         * @return The builder
         */
        public Builder addVertex(Point point) {
            if (isClosed) {
                // each hole we start with the new array of vertex points
                vertexes = new ArrayList<Point>();
                isClosed = false;
            }

            updateBoundingBox(point);
            vertexes.add(point);

            // add line (edge) to the polygon
            if (vertexes.size() > 1) {
                Line line = new Line(vertexes.get(vertexes.size() - 2), point);
                sides.add(line);
            }

            return this;
        }

        public Builder addVertex(double x, double y) {
            return this.addVertex(new Point(x, y));
        }

        /**
         * Close the polygon shape. This will create a new side (edge) from the <b>last</b> vertex point to the <b>first</b> vertex point.
         *
         * @return The builder
         */
        public Builder close() {
            validate();

            // add last Line
            sides.add(new Line(vertexes.get(vertexes.size() - 1), vertexes.get(0)));
            isClosed = true;

            return this;
        }

        /**
         * Build the instance of the polygon shape.
         *
         * @return The polygon
         */
        public Polygon build() {
            validate();

            // in case you forgot to close
            if (!isClosed) {
                // add last Line
                sides.add(new Line(vertexes.get(vertexes.size() - 1), vertexes.get(0)));
            }

            return new Polygon(sides, boundingBox);
        }

        /**
         * Update bounding box with a new point.<br>
         *
         * @param point New point
         */
        private void updateBoundingBox(Point point) {
            if (firstPoint) {
                boundingBox = new BoundingBox();
                boundingBox.xMax = point.lng;
                boundingBox.xMin = point.lng;
                boundingBox.yMax = point.lat;
                boundingBox.yMin = point.lat;

                firstPoint = false;
            } else {
                // set bounding box
                if (point.lng > boundingBox.xMax) {
                    boundingBox.xMax = point.lng;
                } else if (point.lng < boundingBox.xMin) {
                    boundingBox.xMin = point.lng;
                }
                if (point.lat > boundingBox.yMax) {
                    boundingBox.yMax = point.lat;
                } else if (point.lat < boundingBox.yMin) {
                    boundingBox.yMin = point.lat;
                }
            }
        }

        private void validate() {
            if (vertexes.size() < 3) {
                throw new RuntimeException("Polygon must have at least 3 points");
            }
        }
    }

    /**
     * Check if the the given point is inside of the polygon.<br>
     *
     * @param point The point to check
     * @return <code>True</code> if the point is inside the polygon, otherwise return <code>False</code>
     */
    public boolean contains(Point point) {
        if (inBoundingBox(point)) {
            Line ray = createRay(point);
            int intersection = 0;
            for (Line side : sides) {
                if (intersect(ray, side)) {
                    intersection++;
                }
            }

            /*
             * If the number of intersections is odd, then the point is inside the polygon
             */
            return intersection % 2 != 0;
        }
        return false;
    }

    public boolean contains(double x, double y) {
        return this.contains(new Point(x, y));
    }

    public List<Line> getSides() {
        return sides;
    }

    /**
     * By given ray and one side of the polygon, check if both lines intersect.
     *
     * @return <code>True</code> if both lines intersect, otherwise return <code>False</code>
     */
    private boolean intersect(Line ray, Line side) {
        Point intersectPoint = null;

        // if both vectors aren't from the kind of x=1 lines then go into
        if (!ray.isVertical() && !side.isVertical()) {
            // check if both vectors are parallel. If they are parallel then no intersection point will exist
            if (ray.getA() - side.getA() == 0) {
                return false;
            }

            double x = ((side.getB() - ray.getB()) / (ray.getA() - side.getA())); // x = (b2-b1)/(a1-a2)
            double y = side.getA() * x + side.getB(); // y = a2*x+b2
            intersectPoint = new Point(x, y);
        } else if (ray.isVertical() && !side.isVertical()) {
            double x = ray.getStart().lng;
            double y = side.getA() * x + side.getB();
            intersectPoint = new Point(x, y);
        } else if (!ray.isVertical() && side.isVertical()) {
            double x = side.getStart().lng;
            double y = ray.getA() * x + ray.getB();
            intersectPoint = new Point(x, y);
        } else {
            return false;
        }

        if (side.isInside(intersectPoint) && ray.isInside(intersectPoint)) {
            return true;
        }

        return false;
    }

    /**
     * Create a ray. The ray will be created by given point and on point outside of the polygon.<br>
     * The outside point is calculated automatically.
     */
    private Line createRay(Point point) {
        // create outside point
        double epsilon = (boundingBox.xMax - boundingBox.xMin) / 10e6;
        Point outsidePoint = new Point(boundingBox.xMin - epsilon, boundingBox.yMin);

        Line vector = new Line(outsidePoint, point);
        return vector;
    }

    /**
     * Check if the given point is in bounding box
     *
     * @return <code>True</code> if the point in bounding box, otherwise return <code>False</code>
     */
    private boolean inBoundingBox(Point point) {
        if (point.lng < boundingBox.xMin || point.lng > boundingBox.xMax || point.lat < boundingBox.yMin || point.lat > boundingBox.yMax) {
            return false;
        }
        return true;
    }

    private static class BoundingBox {
        public double xMax = Double.NEGATIVE_INFINITY;
        public double xMin = Double.NEGATIVE_INFINITY;
        public double yMax = Double.NEGATIVE_INFINITY;
        public double yMin = Double.NEGATIVE_INFINITY;
    }
}
