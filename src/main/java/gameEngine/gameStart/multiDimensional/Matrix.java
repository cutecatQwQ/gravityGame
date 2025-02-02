package gameEngine.gameStart.multiDimensional;

import gameEngine.gameStart.multiDimensional.objects.Vector;

import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Consumer;

public class Matrix {
    //列向量集合
    Vector[] vectors;

    //空间换时间，存储旋转矩阵 第一个参数是0则为三维旋转矩阵，第一个参数为1则为四维旋转矩阵
    static HashMap<Double, Matrix>[][][] matrixHashMap = new HashMap[2][][];

    static {
        matrixHashMap[0] = new HashMap[3][3];
        matrixHashMap[1] = new HashMap[4][4];
        for (int i = 0; i < matrixHashMap[0].length; i++) {
            matrixHashMap[0][i] = new HashMap[3];
            for (int j = 0; j < matrixHashMap[0][i].length; j++) {
                matrixHashMap[0][i][j] = new HashMap<>();
            }
        }
        for (int i = 0; i < matrixHashMap[1].length; i++) {
            matrixHashMap[1][i] = new HashMap[4];
            for (int j = 0; j < matrixHashMap[1][i].length; j++) {
                matrixHashMap[1][i][j] = new HashMap<>();
            }
        }
    }

    //获取旋转矩阵，如果之前没用过就新建
    public static Matrix getRevolveMatrix(int dimension, int i, int j, double a) {
        Matrix matrix = matrixHashMap[dimension - 3][i][j].get(a);
        if (matrix == null) {
            matrix = new Matrix(dimension, dimension);
            for (int k = 0; k < matrix.getColumn(); k++) {
                for (int l = 0; l < matrix.getRow(); l++) {
                    matrix.set(k, l, k == l ? 1 : 0);
                }
            }
            matrix.set(i, i, Math.cos(a));
            matrix.set(i, j, Math.sin(a));
            matrix.set(j, i, -Math.sin(a));
            matrix.set(j, j, Math.cos(a));
            matrixHashMap[dimension - 3][i][j].put(a, matrix);
        }
        return matrix;
    }

    public Matrix(Vector... vectors) {
        this.vectors = vectors;
    }

    //n行m列
    public Matrix(int n, int m) {
        this.vectors = new Vector[m];
        for (int i = 0; i < vectors.length; i++) {
            vectors[i] = new Vector(n);
        }
    }

    //第几列
    public Vector get(int i) {
        return vectors[i];
    }

    //i行j列
    public double get(int i, int j) {
        return vectors[j].get(i);
    }

    public void set(int i, Vector vector) {
        vectors[i] = vector;
    }

    //i行j列
    public void set(int i, int j, double coordinate) {
        vectors[j].set(i, coordinate);
    }

    //遍历
    public void traverse(Consumer<Vector> operation) {
        for (Vector vector : vectors) {
            operation.accept(vector);
        }
    }

    public int getRow() {
        return vectors[0].size();
    }

    public int getColumn() {
        return vectors.length;
    }

    @Override
    public String toString() {
        return "Matrix{" +
                Arrays.toString(vectors) +
                '}';
    }
}
