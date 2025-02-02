package gameEngine.gameStart.multiDimensional.objects;

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
