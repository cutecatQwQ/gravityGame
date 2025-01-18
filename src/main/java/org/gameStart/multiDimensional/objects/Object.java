package org.gameStart.multiDimensional.objects;

import org.gameStart.multiDimensional.Camera;
import org.mainFrame.model.Model;

public class Object extends Model {
    //因为画图的时候开了抗锯齿，这个线在图片特别细的时候会画不清楚，所以设置一个描边宽度(图片外围)为了让线画的清楚
    public static int add = 1;
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
                ", model=" + super.toString() +
                '}';
    }
}
