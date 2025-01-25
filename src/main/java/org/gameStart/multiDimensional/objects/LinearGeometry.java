package org.gameStart.multiDimensional.objects;

import org.Tool.MathUtil;
import org.gameStart.multiDimensional.Matrix;

import java.util.Arrays;
import java.util.function.Consumer;

//线性几何体
public class LinearGeometry extends Object {
    //0到n-1维线性几何体子结构的集合
    private final LinearGeometry[][] lowDimensionalLinearGeometry;

    public LinearGeometry(int dimension) {
        super(dimension);
        lowDimensionalLinearGeometry = new LinearGeometry[dimension][];
    }

    //获取子结构的方法
    public LinearGeometry getLinearGeometry(int i,int j){
        return lowDimensionalLinearGeometry[i][j];
    }
    public LinearGeometry[] getLinearGeometry(int i){
        return lowDimensionalLinearGeometry[i];
    }
    //设置低维子结构的方法
    public void setLinearGeometry(int i,int j,LinearGeometry linearGeometry){
        lowDimensionalLinearGeometry[i][j] = linearGeometry;
    }
    public void setLinearGeometry(int i,LinearGeometry[] linearGeometries){
        lowDimensionalLinearGeometry[i] = linearGeometries;
    }

    //生成0到n-1维线性几何体子结构的方法
    public void generateLowDimensionalLinearGeometry(){}
    //某维线性几何体子结构的遍历方法
    public void traverse(int i,Consumer<LinearGeometry> consumer){
        for (LinearGeometry linearGeometry : lowDimensionalLinearGeometry[i]) {
            consumer.accept(linearGeometry);
        }
    }

    public LinearGeometry[][] getLowDimensionalLinearGeometry() {
        return lowDimensionalLinearGeometry;
    }

    @Override
    public void move(MoveVector moveVector) {
        if(moveVector.size() != getDimension()) throw new IllegalArgumentException("两个维度不同，无法移动");
        for (LinearGeometry linearGeometry : lowDimensionalLinearGeometry[0]) {
            ((Point) linearGeometry).setCoordinates(MathUtil.move(((Point) linearGeometry).getCoordinates(),1,moveVector.getCoordinates()));
        }
        notifyAllObservers();
    }

    @Override
    public void revolve(RevolveVector revolveVector) {
        Point point;
        for (int i = 0; i < lowDimensionalLinearGeometry[0].length; i++) {
            point = (Point) lowDimensionalLinearGeometry[0][i];
            point.setCoordinates(
                    MathUtil.matrixVectorMultiplication(
                            Matrix.getRevolveMatrix(
                                    (int) revolveVector.get(0),
                                    (int) revolveVector.get(1),
                                    (int) revolveVector.get(2),
                                    revolveVector.get(3)
                            ), point.getCoordinates()
                    )
            );
        }
        notifyAllObservers();
    }

    @Override
    public String toString() {
        return "LinearGeometry{" +
                "lowDimensionalLinearGeometry=" + Arrays.toString(lowDimensionalLinearGeometry) +
                ", object=" +super.toString() +
                '}';
    }
}
