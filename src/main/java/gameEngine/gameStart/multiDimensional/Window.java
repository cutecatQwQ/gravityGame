package gameEngine.gameStart.multiDimensional;

import gameEngine.gameStart.multiDimensional.objects.LinearGeometry;
import gameEngine.gameStart.multiDimensional.objects.Object;
import gameEngine.gameStart.multiDimensional.objects.Point;
import gameEngine.mainFrame.model.Model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.Arrays;
import java.util.TreeSet;

public class Window extends Model {
    private BufferedImage bufferedImage;
    private Camera camera;
    //深度缓冲
    double[][] height;
    //颜色处理
    Color[][] colors;
    //最小像素大小
    int n = 1;

    // 使用JDK9+的VarHandle实现 作为多线程使用height数组的一个类似锁的方法，可以在多线程中实现处理冲突
    private static final VarHandle HEIGHT_ELEMENT_HANDLE;

    static {
        try {
            // 获取一维数组元素的VarHandle
            HEIGHT_ELEMENT_HANDLE = MethodHandles.arrayElementVarHandle(double[].class);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    public Window(double x, double y, double w, double h) {
        setX(x);
        setY(y);
        setW(w);
        setH(h);
        setImage(new BufferedImage(getW(), getH(), BufferedImage.TYPE_INT_RGB));
        bufferedImage = new BufferedImage(getW(), getH(), BufferedImage.TYPE_INT_RGB);
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void paint(TreeSet<Object> renderingSet) {

        //对深度数组和颜色数组进行初始化
        final int rows = (int) Math.ceil(getWDouble() / n);
        final int cols = (int) Math.ceil(getHDouble() / n);
        height = new double[rows][cols];
        colors = new Color[rows][cols];

        // 并行初始化（JDK16+推荐使用显式分块）
        Arrays.parallelSetAll(height, i -> {
            double[] row = new double[cols];
            Arrays.fill(row, Double.POSITIVE_INFINITY);
            return row;
        });

        //清空缓冲图片，之后在缓冲图片上画
        Graphics2D g = bufferedImage.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getW(), getH());

        //parallelStream返回一个可并行操作的流，peek和foreach不同在于peek会返回调用它的流，可以用流再次操作。这里是为了确保所有的表达式都执行完
        renderingSet.parallelStream().forEach(rendering -> {
            //点
            for (int i = 0; i < ((LinearGeometry) rendering).getLinearGeometry(0).length; i++) {
                Point point = (Point) ((LinearGeometry) rendering).getLinearGeometry(0)[i];
                g.setColor(Color.black);
                //转换坐标系并旋转到摄像头方向然后投影
                double[] coordinates = camera.projection(camera.convertToCameraCoordinateSystem(point.getCoordinates()));

                //转到窗口坐标系
                coordinates[0] = getWDouble() / 2 - coordinates[0];
                coordinates[1] = getHDouble() / 2 - coordinates[1];

                //将投影出来的点的坐标记录下来
                if (point.getLowDimensionalObject() == null)
                    point.setLowDimensionalObject(new Point(point.size() - 1));
                ((Point) point.getLowDimensionalObject()).setCoordinates(coordinates);

                //画点
//                double a = 4;
//                g.fillOval((int) (coordinates[0]-getIndex()/2*a), (int) (coordinates[1]-getIndex()/2*a), (int) (getIndex().intValue()*a), (int) (getIndex().intValue()*a));
            }

            //线 目前没有一个场景被多个摄像头看的办法
            //当前是比线维度更高的存在
            if (rendering.getDimension() > 1) {
                for (LinearGeometry linearGeometry : ((LinearGeometry) rendering).getLinearGeometry(1)) {
                    paintLine(linearGeometry, bufferedImage);
                }
            }
            //当前的就是一个线
            else if (rendering.getDimension() == 1) {
                paintLine((LinearGeometry) rendering, bufferedImage);
            }

            //面
            //当前是比面维度更高的存在
            if (rendering.getDimension() > 2) {
                for (LinearGeometry linearGeometry : ((LinearGeometry) rendering).getLinearGeometry(2)) {
                    paintFace(linearGeometry);
                }
            }
            //当前的就是一个面
            else if (rendering.getDimension() == 2) {
                paintFace((LinearGeometry) rendering);
            }
        });

        //将存储的事物画到缓冲图片上
        for (int i = 0; i < colors.length; i++) {
            for (int j = 0; j < colors[i].length; j++) {
                if (colors[i][j] != null) {
                    g.setColor(colors[i][j]);
                    g.fillRect(i * n, j * n, n, n);
                }
            }
        }
        //两张图片交替实现缓冲
        BufferedImage image = (BufferedImage) getImage();
        setImage(bufferedImage);
        bufferedImage = image;
    }

    private void paintLine(LinearGeometry linearGeometry, BufferedImage bufferedImage) {
        if (linearGeometry.getColoration() == null) return;

        //获取面的点
        double[][] points = new double[linearGeometry.getLinearGeometry(0).length][3];
        for (int j = 0; j < linearGeometry.getLinearGeometry(0).length; j++) {
            points[j] = ((Point) linearGeometry.getLinearGeometry(0, j).getLowDimensionalObject()).getCoordinates();
        }

        //分量方向
        double dx = points[1][0] - points[0][0];
        double dy = points[1][1] - points[0][1];
        //线段总长
        double length = Math.sqrt(dx * dx + dy * dy);
        //将向量单位化
        dx /= length;
        dy /= length;
        //步长
        double stepLength = n * Math.max(Math.abs(dx), Math.abs(dy));

        double x = points[0][0];
        double y = points[0][1];
        //绘制点直到到达终点
        while (true) {
            //如果当前位置在屏幕内
            if (x >= 0 && x <= bufferedImage.getWidth() &&
                    y >= 0 && y <= bufferedImage.getHeight()) {
                //计算深度
                double h;
                //斜率为无穷(横坐标相等)单独处理 用y获取z z = z0+(y-y0)/(y1-y0) * (z1-z0)
                if (points[0][0] == points[1][0]) {
                    h = points[0][2] + (y - points[0][1]) / (points[1][1] - points[0][1]) * (points[1][2] - points[0][2]);
                }
                //用x获取z z = z0+(x-x0)/(x1-x0) * (z1-z0)
                else
                    h = points[0][2] + (x - points[0][0]) / (points[1][0] - points[0][0]) * (points[1][2] - points[0][2]);
                rendering((int) (x / n), (int) (y / n), h, linearGeometry.getColoration().getColor());
            }
            //出循环条件
            if (Math.abs(x - points[1][0]) < 1e-6 && Math.abs(y - points[1][1]) < 1e-6)
                break;
            //更新当前点坐标
            x += dx * stepLength;
            y += dy * stepLength;

            //检查坐标确保不超过终点
            if ((dx > 0 && x > points[1][0]) || (dx < 0 && x < points[1][0])) x = points[1][0];
            if ((dy > 0 && y > points[1][1]) || (dy < 0 && y < points[1][1])) y = points[1][1];
        }
    }

    public void paintFace(LinearGeometry linearGeometry) {
        if (linearGeometry.getLinearGeometry(0).length < 3)
            throw new IllegalArgumentException("组成当前面的点的个数小于3");
        //对每一个面的点进行遍历 射线法判断当前位置是否在多边形内，把点带入面的方程即可求出深度

        //获取面的点
        double[][] points = new double[linearGeometry.getLinearGeometry(0).length][3];
        for (int j = 0; j < linearGeometry.getLinearGeometry(0).length; j++) {
            points[j] = ((Point) linearGeometry.getLinearGeometry(0, j).getLowDimensionalObject()).getCoordinates();
        }

        //获取包围盒
        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        for (int j = 0; j < linearGeometry.getLinearGeometry(0).length; j++) {
            if (minX > points[j][0]) minX = points[j][0];
            if (minY > points[j][1]) minY = points[j][1];
            if (maxX < points[j][0]) maxX = points[j][0];
            if (maxY < points[j][1]) maxY = points[j][1];
        }

        //计算面的方程 ax+by+cz = ax1+by1+cz1
        double a = (points[1][1] - points[0][1]) * (points[2][2] - points[0][2]) - (points[1][2] - points[0][2]) * (points[2][1] - points[0][1]);
        double b = -(points[1][0] - points[0][0]) * (points[2][2] - points[0][2]) + (points[1][2] - points[0][2]) * (points[2][0] - points[0][0]);
        double c = (points[1][0] - points[0][0]) * (points[2][1] - points[0][1]) - (points[1][1] - points[0][1]) * (points[2][0] - points[0][0]);

        //计算每个边的斜率
        double[] k = new double[linearGeometry.getLinearGeometry(0).length];
        for (int j = 0; j < k.length; j++) {
            if (points[j][0] != points[(j + 1) % k.length][0]) {
                k[j] = (points[j][1] - points[(j + 1) % k.length][1]) / (points[j][0] - points[(j + 1) % k.length][0]);
            } else {
                k[j] = Double.POSITIVE_INFINITY;
            }
        }

        //遍历每个点 (x,y)为第几块 (x*n+n/2,y*n+n/2)为这块中心点的坐标
        for (int x = Math.max((int) Math.floor(minX / n), 0); x < Math.min((int) Math.ceil(maxX / n), getW() / n); x++) {
            for (int y = Math.max((int) Math.floor(minY / n), 0); y < Math.min((int) Math.ceil(maxY / n), getH() / n); y++) {
                // 计算当前块的中心坐标
                double xCenter = x * n + n / 2.0;
                double yCenter = y * n + n / 2.0;
                //交点个数
                int index = 0;
                //对点进行遍历求斜率进行判断是否穿过 j和(j+1)%points.length为一个线段的两个点
                for (int j = 0; j < points.length; j++) {
                    //先过滤不可能有交点的情况，过(x,y)的水平直线和线段没有交点
                    if ((yCenter > points[j][1] && yCenter > points[(j + 1) % points.length][1]) ||
                            (yCenter < points[j][1] && yCenter < points[(j + 1) % points.length][1])) {
                        continue;
                    }

                    //算出的与线段交点x坐标x0
                    double x0;

                    //斜率无穷大时说明是竖直的，直接取端点的x坐标即可
                    if (k[j] == Double.POSITIVE_INFINITY) {
                        x0 = points[j][0];
                    }
                    //边和x轴水平 当和边有重叠时，不管是从外部射出还是从内部射出还是从线段上射出都不需要处理，当没有重叠时更不需要处理
                    else if (k[j] == 0) {
                        continue;
                    }
                    //计算当前直线纵坐标为y0=y时x的值 (y1-y0)/(x1-x0) = k
                    else {
                        x0 = points[j][0] - (points[j][1] - yCenter) / k[j];
                    }

                    //因为是向x轴正方向射出的，所以大于的话算做穿过线段
                    if (x0 > xCenter) {
                        index++;
                    }
                }
                //当前点是奇数的情况下计算深度处理颜色
                if ((index & 1) == 1) {
                    double h = (a * (points[0][0] - xCenter) + b * (points[0][1] - yCenter)) / c + points[0][2];
                    rendering(x, y, h, linearGeometry.getColoration().getColor());
                }
            }
        }
    }

    //这里x,y的单位是像素块，h是深度 意思是更新这个像素块的渲染
    private void rendering(int x, int y, double h, Color color) {
        while (true) {
            //用并发处理获取数组内数据
            double current = (double) HEIGHT_ELEMENT_HANDLE.getVolatile(height[x], y);
            if (h < 0 || current < h) break;

            //如果其他线程已经修改了height[x][y]的值，则重复循环直到成功更新为止
            if ((boolean) HEIGHT_ELEMENT_HANDLE.compareAndSet(height[x], y, current, h)) {
                colors[x][y] = color; // 非原子操作但安全
                break;
            }
        }
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
        notifyAllObservers();
    }
}
