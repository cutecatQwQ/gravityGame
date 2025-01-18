package org.gameStart.multiDimensional.objects;

import org.gameStart.multiDimensional.Camera;

import java.awt.*;
import java.util.function.Consumer;
import java.util.function.DoubleUnaryOperator;

public class Point extends LinearGeometry {
    //实际坐标
    private double[] coordinates;
    //投影坐标就是Model的XY坐标

    public Point(double... coordinates) {
        super(coordinates.length);
        this.coordinates = coordinates;
        setW(1);
        setH(1);
        paint();
    }

    public Point(int dimension) {
        super(dimension);
        this.coordinates = new double[dimension];
        setW(1);
        setH(1);
        paint();
    }

    @Override
    public void paint() {
        Graphics2D graphics2D = getNewGraphics2D();
        graphics2D.fillRect(0, 0, 1, 1);
    }

    public double get(int i) {
        return coordinates[i];
    }

    public void set(int i, double coordinate) {
        coordinates[i] = coordinate;
    }

    public void add(int i, double coordinate) {
        coordinates[i] += coordinate;
    }

    public int size() {
        return coordinates.length;
    }

    //遍历
    public void traverse(DoubleUnaryOperator operation) {
        for (int i = 0; i < coordinates.length; i++) {
            coordinates[i] = operation.applyAsDouble(coordinates[i]);
        }
    }

    public double[] getCoordinates() {
        return coordinates;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < coordinates.length; i++) {
            sb.append(String.format("%.10f", coordinates[i]));
            if (i < coordinates.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    //每个坐标进行遍历
    public void traverseCoordinates(Consumer<Double> consumer) {
        for (double coordinate : coordinates) {
            consumer.accept(coordinate);
        }
    }
}
