package org.gameStart.multiDimensional;

import org.Tool.MathUtil;
import org.gameStart.multiDimensional.objects.Point;
import org.mainFrame.model.Model;

import java.util.HashMap;

public class Camera extends Model {
    //位置
    Point location;
    //基坐标矩阵
    Matrix baseCoordinates;

    public Camera(Point location, Matrix baseCoordinates) {
        this.location = location;
        this.baseCoordinates = baseCoordinates;
        //对基坐标的每一个向量规格化
        baseCoordinates.traverse(MathUtil::normalization);
    }

    //获取基向量矩阵
    public Matrix getProjectionBaseCoordinates() {
//        Matrix matrix = new Matrix(baseCoordinates.getRow(), baseCoordinates.getColumn());
//        matrix.set(0,new Vector(0,0,0,0));
//        matrix.set(1,baseCoordinates.get(1));
//        matrix.set(2,baseCoordinates.get(2));
//        matrix.set(3,new Vector(0,0,0,0));
        Matrix matrix = new Matrix(2, baseCoordinates.getColumn());
        for (int i = 0; i < baseCoordinates.getColumn(); i++) {
            matrix.set(0, i, baseCoordinates.get(i, 1));
            matrix.set(1, i, baseCoordinates.get(i, 2));
        }
        return matrix;
    }

    //空间换时间
    static HashMap<Double,Matrix>[][] matrixHashMap = new HashMap[4][4];

    static {
        for (int i = 0; i < matrixHashMap.length; i++) {
            matrixHashMap[i] = new HashMap[4];
            for (int j = 0; j < matrixHashMap[i].length; j++) {
                matrixHashMap[i][j] = new HashMap<>();
            }
        }
    }

    //0<=i<j<=3
    public void revolve(int i, int j, double a) {
        Matrix matrix = matrixHashMap[i][j].get(a);
        if (matrix == null) {
            matrix = new Matrix(baseCoordinates.getRow(), baseCoordinates.getColumn());
            for (int k = 0; k < matrix.getColumn(); k++) {
                for (int l = 0; l < matrix.getRow(); l++) {
                    matrix.set(k, l, k == l ? 1 : 0);
                }
            }
            matrix.set(i, i, Math.cos(Math.toRadians(a)));
            matrix.set(i, j, Math.sin(Math.toRadians(a)));
            matrix.set(j, i, -Math.sin(Math.toRadians(a)));
            matrix.set(j, j, Math.cos(Math.toRadians(a)));
            matrixHashMap[i][j].put(a,matrix);
        }
//        System.out.println(matrix);
        baseCoordinates = MathUtil.matrixMultiplication(matrix, baseCoordinates);
    }
}
