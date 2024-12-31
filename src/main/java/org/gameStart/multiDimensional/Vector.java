package org.gameStart.multiDimensional;

public class Vector extends Point{
    public Vector(double... coordinates) {
        super(coordinates);
    }

    public Vector(int n) {
        super(n);
    }

    public Vector(Point point) {
        super(point.getCoordinates());
    }
}
