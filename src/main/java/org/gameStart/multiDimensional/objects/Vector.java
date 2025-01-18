package org.gameStart.multiDimensional.objects;

import org.gameStart.multiDimensional.objects.Point;

public class Vector extends Point {
    public Vector(double... coordinates) {
        super(coordinates);
    }

    public Vector(int dimension) {
        super(dimension);
    }

    public Vector(Point point) {
        super(point.getCoordinates());
    }
}
