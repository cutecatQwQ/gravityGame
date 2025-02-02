package gameEngine.gameStart.multiDimensional;

import gameEngine.gameStart.multiDimensional.objects.Point;
import gameEngine.gameStart.multiDimensional.objects.RevolveVector;
import gameEngine.gameStart.multiDimensional.objects.Vector;
import gameEngine.Tool.MathUtil;
import gameEngine.gameStart.multiDimensional.objects.Object;

//摄像头，就是三维场景中的摄像头
public class Camera extends Object {
    //位置
    Point location;
    //方向 即z轴
    Vector direction;
    //焦距
    double focalLength;
    //模式 0正交投影 1透视投影
    int mode;
    //灵敏度 0.1是正常
    double sensitivity;
    public Camera(Point location, Vector direction) {
        this(location,direction,1000);
    }

    //透视投影摄像头
    public Camera(Point location, Vector direction, double focalLength) {
        super(3);
        this.location = location;
        this.direction = direction;
        this.sensitivity = 0.1;
        //对基坐标的向量规格化
        MathUtil.normalization(direction);
        this.focalLength = focalLength;
        this.mode = 1;
    }

    /**
     * 获取基向量矩阵 k根据叉乘计算 z是面朝的方向，x是左边的方向(右手坐标系)，y是头顶的方向
     * x = [-d2, d1, 0]
     * y = [-d1*d3, -d2*d3, d1^2+d2^2]
     * z = [d1, d2, d3]
     */
    public Vector getBaseVector(int i){
        Vector vector;
        switch (i) {
            case 0: vector =  new Vector(-direction.get(1), direction.get(0), 0); break;
            case 1: vector =  new Vector(
                    -direction.get(0) * direction.get(2),
                    -direction.get(1) * direction.get(2),
                    Math.pow(direction.get(0), 2) + Math.pow(direction.get(1), 2)); break;
            case 2: vector =  direction; break;
            default: throw new IllegalArgumentException("摄像机没有"+i+"维度的向量");
        }
        MathUtil.normalization(vector);
        return vector;
    }

    /**
     * 获取世界坐标系中移动方向
     * x = [d1, d2, 0]
     * y = [-d2, d1, 0]
     * z = [0,0,1]
     */
    public Vector getWorldBaseVector(int i){
        Vector vector;
        switch (i) {
            case 0: vector =  new Vector(direction.get(0), direction.get(1), 0); break;
            case 1: vector =  new Vector(-direction.get(1), direction.get(0), 0); break;
            case 2: vector =  new Vector(0,0,1); break;
            default: throw new IllegalArgumentException("摄像机没有"+i+"维度的向量");
        }
        MathUtil.normalization(vector);
        return vector;
    }


    public Matrix getBaseCoordinates() {
        Matrix matrix = new Matrix(getDimension(), getDimension());
        for (int i = 0; i < matrix.getColumn(); i++) {
            matrix.set(i,getBaseVector(i));
        }
        //正交化每个向量
        matrix.traverse(MathUtil::normalization);
        return matrix;
    }

    //旋转，按照世界坐标系的xyz轴进行旋转
    public void revolve(RevolveVector revolveVector) {
        if (revolveVector.get(0) != 3) {
            throw new IllegalArgumentException("旋转向量不是3维的");
        }
        if (revolveVector.get(1) >= 3 || revolveVector.get(2) >= 3) {
            throw new IllegalArgumentException("对摄像机旋转超过三个维度");
        }
        double a = Math.toRadians(revolveVector.get(3));
        int i = (int) revolveVector.get(1);
        int j = (int) revolveVector.get(2);

        if (a == 0) return;

        direction.setCoordinates(MathUtil.matrixVectorMultiplication(Matrix.getRevolveMatrix(getDimension(),i,j,a), direction.getCoordinates()));
    }

    //旋转，按照自身坐标系的xoz轴进行旋转
    public void revolveYOZ(double a) {
        direction.setCoordinates(MathUtil.revolve3D(direction.getCoordinates(),new Point(0,0,0),getBaseVector(0),a));
    }

    //转换到摄像头坐标系 先平移，移动-1倍的摄像头原点即可，再旋转
    public double[] convertToCameraCoordinateSystem(double[] point) {
        return MathUtil.revolve(MathUtil.move(point, -1,location.getCoordinates()),getBaseCoordinates());
    }

    //投影
    public double[] projection(double[] point) {
        return mode == 0?orthographicProjection(point):perspectiveProjection(point);
    }

    //透视投影 f/(f+z) = x2/x3  -->  x2 = x3*f/(f+z) z为深度
    private double[] perspectiveProjection(double[] point) {
        return new double[]{point[0] * focalLength / (focalLength + point[2]), point[1] * focalLength / (focalLength + point[1]),point[2]};
    }

    //正交投影 什么也不做，z轴为深度
    private double[] orthographicProjection(double[] point) {
        return point;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
        notifyAllObservers();
    }

    public double getFocalLength() {
        return focalLength;
    }

    public void setFocalLength(double focalLength) {
        this.focalLength = focalLength;
        notifyAllObservers();
    }

    public double getSensitivity() {
        return sensitivity;
    }

    public void setSensitivity(double sensitivity) {
        this.sensitivity = sensitivity;
    }
}
