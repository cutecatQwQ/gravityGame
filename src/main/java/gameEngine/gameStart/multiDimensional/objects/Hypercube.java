package gameEngine.gameStart.multiDimensional.objects;

import gameEngine.Tool.MathUtil;

import java.util.Arrays;

//超方形
public class Hypercube extends LinearGeometry {
    Hypercube fatherHypercube;

    public Hypercube(int dimension, Point... points) {
        super(dimension);
        //初始化每个数组 长度C(n,k) × 2^(n − k)
        for (int i = 0; i < getLowDimensionalLinearGeometry().length; i++) {
            setLinearGeometry(i, new LinearGeometry[(int) (MathUtil.C(getDimension(), i) * Math.pow(2, getDimension() - i))]);
        }
        //填充点
        for (int i = 0; i < getLinearGeometry(0).length; i++) {
            setLinearGeometry(0, i, points[i]);
        }

        //填充低维流形数组
//        generateLowDimensionalLinearGeometry();
    }

    //有父流形的
    public Hypercube(Hypercube fatherHypercube, Point... points) {
        this(fatherHypercube.getDimension() - 1, points);
        this.fatherHypercube = fatherHypercube;
        generateLowDimensionalLinearGeometry();
    }

    //根据一个点向其他方向扩展的 (不是很好写，咕咕)
    public static Hypercube getHypercube(int dimension, Point point,double... edgeLengths) {
        if(dimension != point.size() || dimension != edgeLengths.length)
            throw new IllegalArgumentException("维度不同");
        Point[] points = new Point[(int) Math.pow(2,dimension)];

        for (int i = 0; i < points.length; i++) {
            double[] coordinates = new double[dimension];
            for (int j = 0; j < coordinates.length; j++) {
                coordinates[j] = (((i>>j&1) == 0)?point.get(j):point.get(j)+edgeLengths[j]);
            }
            points[i] = new Point(coordinates);
        }
        return new Hypercube(dimension,points);
    }

    /**
     * 两个对立的流形会有以下特征
     * 它们所有相互对应点中只有一个坐标不同，即一个流形可以通过一个方向的平移变成另一个流形
     * 那么此刻只要对每一个坐标轴进行判定(即第一个i循环)即可找到两个对立的流形lowDimensionalPoints0和lowDimensionalPoints1
     * <p>
     * 一个流形的点可以用索引表示，把索引转换成n位二进制数，其中某一位不同的点即可判定当前点位于哪个流形
     * 即j / (2^i) % 2,j / (2^i)是获取转换后的二进制数的第i位,%2判断是0还是1
     */
    //填充低维流形数组
    @Override
    public void generateLowDimensionalLinearGeometry() {
        //低维流形的索引
        int lowDimensionalIndex = 0;
        //坐标轴循环
        for (int i = 0; i < getDimension(); i++) {
            //相对于当前超方形的流形，它的下一个流形和它的对立流形
            Point[] lowDimensionalPoints0 = new Point[getLinearGeometry(0).length / 2];
            Point[] lowDimensionalPoints1 = new Point[getLinearGeometry(0).length / 2];
            //一个低维流形对应的点的索引
            int lowDimensionalPointsIndex0 = 0;
            int lowDimensionalPointsIndex1 = 0;
            //对点遍历
            for (int j = 0; j < getLinearGeometry(0).length && lowDimensionalPointsIndex0 + lowDimensionalPointsIndex1
                    < getLinearGeometry(0).length; j++) {
                //判断这些点是属于第一个流形还是第二个流形
                if ((j / (int) Math.pow(2, i)) % 2 == 0)
                    lowDimensionalPoints0[lowDimensionalPointsIndex0++] = (Point) getLinearGeometry(0, j);
                else
                    lowDimensionalPoints1[lowDimensionalPointsIndex1++] = (Point) getLinearGeometry(0, j);
            }
            if (getDimension() > 1) {
                getLinearGeometry(getDimension() - 1)[lowDimensionalIndex++] = findLinearGeometry(lowDimensionalPoints0);
                getLinearGeometry(getDimension() - 1)[lowDimensionalIndex++] = findLinearGeometry(lowDimensionalPoints1);
            }
        }

        //对于二维面处理一下点的顺序
        if(getDimension() == 2){
            LinearGeometry linearGeometry = getLinearGeometry(0,2);
            setLinearGeometry(0,2,getLinearGeometry(0,3));
            setLinearGeometry(0,3,linearGeometry);
        }
    }

    //从父流形里找已经存在的低维流形，去重
    private LinearGeometry findLinearGeometry(Point[] lowDimensionalPoints) {
        //获取根节点流形
        Hypercube firstHypercube = this;
        while (firstHypercube.fatherHypercube != null) {
            firstHypercube = firstHypercube.fatherHypercube;
        }
        int index = 0;
        for (; index < firstHypercube.getLinearGeometry(getDimension() - 1).length; index++) {
            if (firstHypercube.getLinearGeometry(getDimension() - 1)[index] == null) break;
            if (Arrays.equals(firstHypercube.getLinearGeometry(getDimension() - 1)[index].getLinearGeometry(0), lowDimensionalPoints)) {
                return firstHypercube.getLinearGeometry(getDimension() - 1)[index];
            }
        }
        //发现没有之后向里面填充
        LinearGeometry linearGeometry = new Hypercube(this, lowDimensionalPoints);
        firstHypercube.getLinearGeometry(getDimension() - 1)[index] = linearGeometry;
        return linearGeometry;
    }

    //让某一个边变长a
    public void add(int n,double a){
        if(n>getDimension()) throw new IllegalArgumentException("维度超出上限");
        for (int i = 0; i < getLinearGeometry(0).length; i++) {
            if((i>>n&1) == 1) ((Point)getLinearGeometry(0,i)).add(n,a);
        }
    }
}
