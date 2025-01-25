package org.gameStart.multiDimensional.objects;

//旋转向量
public class RevolveVector extends Vector{
    //坐标里面的第一个是维度，之后两个是构成面的方向，第四个是角度
    public RevolveVector(int dimension,int i,int j,double a) {
        super(4);
        if(i == j) throw new IllegalArgumentException("旋转矩阵两个维度不能相同"+ i +"=="+j);
        set(0,dimension);
        set(1,i);
        set(2,j);
        set(3,a);
    }
}
