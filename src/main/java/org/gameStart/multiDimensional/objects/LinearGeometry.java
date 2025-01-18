package org.gameStart.multiDimensional.objects;

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
    public String toString() {
        return "LinearGeometry{" +
                "lowDimensionalLinearGeometry=" + Arrays.toString(lowDimensionalLinearGeometry) +
                ", object=" +super.toString() +
                '}';
    }
}
