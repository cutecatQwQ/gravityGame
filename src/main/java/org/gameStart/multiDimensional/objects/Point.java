package org.gameStart.multiDimensional.objects;

import org.gameStart.multiDimensional.Camera;

import java.awt.*;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.DoubleUnaryOperator;

public class Point extends LinearGeometry {
    //实际坐标
    private double[] coordinates;


    public Point(double... coordinates) {
        super(0);
        this.coordinates = coordinates;
    }

    public Point(int dimension) {
        super(0);
        this.coordinates = new double[dimension];
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

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
        notifyAllObservers();
    }


    @Override
    public String toString() {
        return "Point{" +
                "coordinates=" + Arrays.toString(coordinates) +
                ", LinearGeometry=" + super.toString() +
                '}';
    }

    //每个坐标进行遍历
    public void traverse(Consumer<Double> consumer) {
        for (double coordinate : coordinates) {
            consumer.accept(coordinate);
        }
    }
}
