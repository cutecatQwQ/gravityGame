package org.gameStart.multiDimensional;

import org.gameStart.multiDimensional.objects.Vector;

import java.util.Arrays;
import java.util.function.Consumer;

public class Matrix {
    //列向量集合
    Vector[] vectors;

    public Matrix(Vector... vectors) {
        this.vectors = vectors;
    }
    //n行m列
    public Matrix(int n,int m) {
        this.vectors = new Vector[m];
        for (int i = 0; i < vectors.length; i++) {
            vectors[i] = new Vector(n);
        }
    }
    //第几列
    public Vector get(int i){
        return vectors[i];
    }

    //i行j列
    public double get(int i,int j){
        return vectors[j].get(i);
    }

    public void set(int i,Vector vector){
        vectors[i] = vector;
    }

    //i行j列
    public void set(int i,int j,double coordinate){
        vectors[j].set(i,coordinate);
    }

    //遍历
    public void traverse(Consumer<Vector> operation) {
        for (Vector vector : vectors) {
            operation.accept(vector);
        }
    }

    public int getRow(){
        return vectors[0].size();
    }

    public int getColumn(){
        return vectors.length;
    }

    @Override
    public String toString() {
        return "Matrix{" +
                "vectors=" + Arrays.toString(vectors) +
                '}';
    }
}
