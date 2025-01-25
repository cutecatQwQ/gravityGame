package org.gameStart.multiDimensional.objects;

import org.gameStart.multiDimensional.Camera;
import org.mainFrame.Service.Observable;

public class Object extends Observable implements Comparable<Object> {
    //维度
    private final int dimension;
    //投影出来的n-1维物体
    private Object lowDimensionalObject;

    public Object(int dimension) {
        this.dimension = dimension;
    }

    public int getDimension() {
        return dimension;
    }

    public Object getLowDimensionalObject() {
        return lowDimensionalObject;
    }

    public void setLowDimensionalObject(Object lowDimensionalObject) {
        this.lowDimensionalObject = lowDimensionalObject;
    }

    //生成n-1维物体
    public Object revise(Camera camera) {
        return null;
    }

    //移动
    public void move(MoveVector moveVector) {
    }

    //旋转
    public void revolve(RevolveVector revolveVector) {
    }


    @Override
    public String toString() {
        return "Object{" +
                "dimension=" + dimension +
                ", lowDimensionalObject=" + lowDimensionalObject +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        return this.toString().compareTo(o.toString());
    }
}
