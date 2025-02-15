package gameEngine.gameStart.multiDimensional;

import gameEngine.gameStart.multiDimensional.objects.LinearGeometry;
import gameEngine.gameStart.multiDimensional.objects.Object;
import gameEngine.gameStart.multiDimensional.objects.Point;
import gameEngine.mainFrame.model.Model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.stream.IntStream;

public class Window extends Model {
    private BufferedImage bufferedImage;
    private Camera camera;
    //深度缓冲
    double[][] height;
    //颜色处理
    Color[][] colors;

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
        final int rows = (int) Math.ceil(getWDouble() / camera.getPixelEdge());
        final int cols = (int) Math.ceil(getHDouble() / camera.getPixelEdge());
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
                //转换坐标系并旋转到摄像头方向
                double[] coordinates = camera.convertToCameraCoordinateSystem(point.getCoordinates());
//                coordinates = projection(coordinates);
                //将转换后的点的坐标记录下来
                ((Point) point.getLowDimensionalObject()).setCoordinates(coordinates);
                coordinates = projection(coordinates);
                ((Point) (point.getLowDimensionalObject()).getLowDimensionalObject()).setCoordinates(coordinates);
            }

            //线 目前没有一个场景被多个摄像头看的办法
            //当前是比线维度更高的存在
            if (rendering.getDimension() > 1) {
                for (LinearGeometry linearGeometry : ((LinearGeometry) rendering).getLinearGeometry(1)) {
                    paintLine(linearGeometry);
                }
            }
            //当前的就是一个线
            else if (rendering.getDimension() == 1) {
                paintLine((LinearGeometry) rendering);
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

        /**
         * 代码解释
         * 像素数组访问：通过DataBufferInt直接获取像素数组pixels，允许高效地修改像素值。
         * 并行流处理：IntStream.range().parallel().forEach()将外层循环并行化，每个线程处理不同的i值。
         * 颜色块处理：对于每个颜色块，计算其对应的像素区域，并将颜色填充至该区域的所有像素。
         * 边界检查：使用Math.min确保填充区域不超过图像尺寸，避免数组越界。
         * 注意事项
         * 线程安全：由于每个颜色块处理独立的像素区域，对pixels数组的修改是线程安全的。
         * 性能优化：适用于颜色块数量较多或处理较耗时的情况。若颜色块过小（如n=1），可考虑合并任务以减少开销。
         * 颜色转换：确保Color.getRGB()正确转换为与BufferedImage匹配的格式（如ARGB）。
         * 这种方法通过并行处理独立颜色块，有效利用多核CPU，同时避免线程竞争，显著提升绘制效率。
         * */
        // 假设bufferedImage是已经创建的缓冲图片
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        int[] pixels = ((DataBufferInt) bufferedImage.getRaster().getDataBuffer()).getData();

        // 使用并行流处理外层循环
        IntStream.range(0, colors.length).parallel().forEach(i -> {
            for (int j = 0; j < colors[i].length; j++) {
                Color color = colors[i][j];
                if (color != null) {
                    int rgb = color.getRGB();
                    int xStart = i * camera.getPixelEdge();
                    int yStart = j * camera.getPixelEdge();
                    // 计算实际填充区域，防止越界
                    int xEnd = Math.min(xStart + camera.getPixelEdge(), width);
                    int yEnd = Math.min(yStart + camera.getPixelEdge(), height);
                    // 遍历该颜色块内的所有像素并设置颜色
                    for (int y = yStart; y < yEnd; y++) {
                        int rowStart = y * width;
                        for (int x = xStart; x < xEnd; x++) {
                            pixels[rowStart + x] = rgb;
                        }
                    }
                }
            }
        });

        //两张图片交替实现缓冲
        BufferedImage image = (BufferedImage) getImage();
        setImage(bufferedImage);
        bufferedImage = image;
    }

    private void paintLine(LinearGeometry linearGeometry) {
        if (linearGeometry.getColoration() == null) return;

        //渲染的两个点
        double[][] points = new double[linearGeometry.getLinearGeometry(0).length][3];

        //裁剪深度
        Point pp = ((Point) linearGeometry.getLinearGeometry(0, 0).getLowDimensionalObject());
        Point qq = ((Point) linearGeometry.getLinearGeometry(0, 1).getLowDimensionalObject());
        //两个点的深度都小于0，可以不渲染了
        if (pp.get(2) <= 0 && qq.get(2) <= 0) return;
            //其中一个点深度小于0，做裁剪
        else if (pp.get(2) <= 0 || qq.get(2) <= 0) {
            //两个点的z不可能相等的，所以斜率不可能为0，pz-qz != 0
            //(pz-0)/(pz-qz) = (px-x)/(px-qx) -->  x = px-(px-qx)*pz/(pz-qz)
            double[] point = new double[3];
            point[0] = pp.get(0) - (pp.get(0) - qq.get(0)) * pp.get(2) / (pp.get(2) - qq.get(2));
            point[1] = pp.get(1) - (pp.get(1) - qq.get(1)) * pp.get(2) / (pp.get(2) - qq.get(2));
            point[2] = 0;
            point = projection(point);
            points[0] = point;
            points[1] = ((Point) ((pp.get(2) > 0) ? pp : qq).getLowDimensionalObject()).getCoordinates();
        }
        //两个点的深度都大于0，不需要裁剪
        else {
            //获取线的点
            for (int j = 0; j < linearGeometry.getLinearGeometry(0).length; j++) {
                points[j] = ((Point) (linearGeometry.getLinearGeometry(0, j).getLowDimensionalObject()).getLowDimensionalObject()).getCoordinates();
            }
        }
        //裁剪视锥
        //分量方向
        double dx = points[1][0] - points[0][0];
        double dy = points[1][1] - points[0][1];
        //平行于屏幕且在屏幕边缘的线，直接不画了吧
        if ((dx == 0 && (points[0][0] == 0 || points[0][0] == getW())) ||
                (dy == 0 && (points[0][1] == 0 || points[0][1] == getH()))) {
            return;
        }
        //不画点
        else if (dx == 0 && dy == 0) {
            return;
        }
        //非特殊情况

        //计算和所有边的交点，范围在屏幕边上的即为正确交点 x = t*dx+p0 y = t*dy+p1
        //计算x = 0 x = W两个竖直交界处的交点
        double y0 = (0 - points[0][0]) / dx * dy + points[0][1];
        double yW = (getW() - camera.getPixelEdge() - points[0][0]) / dx * dy + points[0][1];
        //计算y = 0 y = H两个水平交界处的交点
        double x0 = (0 - points[0][1]) / dy * dx + points[0][0];
        double xH = (getH() - camera.getPixelEdge() - points[0][1]) / dy * dx + points[0][0];

        //存储四个有用的点,进行排序，中间两个作为要渲染的线段端点(除非这四个点的顺序是2个真点2个算出来的交点，这个时候直接不渲染)
        ArrayList<double[]> p = new ArrayList<>();
        //得出两个实际在屏幕边缘的点
        if (y0 > 0 && y0 < getH()) p.add(new double[]{0.0, y0});
        if (yW > 0 && yW < getH()) p.add(new double[]{getW() - camera.getPixelEdge(), yW});
        if (x0 > 0 && x0 < getW()) p.add(new double[]{x0, 0.0});
        if (xH > 0 && xH < getW()) p.add(new double[]{xH, getH() - camera.getPixelEdge()});
        //不满足条件，线段和长方形的交点在屏幕边缘的不到2个，不用渲染
        if (p.size() < 2) return;
        //将两个线段的端点添加进去
        p.add(points[0]);
        p.add(points[1]);
        //对p进行排序 默认是x，斜率为无穷时才用y
        if (dx != 0) p.sort(Comparator.comparingDouble(o -> o[0]));
        else p.sort(Comparator.comparingDouble(o -> o[1]));
        //四个点的顺序是2个真点2个算出来的交点 不需要渲染
        if (p.get(0).length == p.get(1).length) return;
        //设置初始点
        double x = p.get(1)[0];
        double y = p.get(1)[1];

        //更新一下分量方向
        dx = p.get(2)[0] - p.get(1)[0];
        dy = p.get(2)[1] - p.get(1)[1];
        //需要画的线段总长
        double length = Math.sqrt(dx * dx + dy * dy);
        //将向量单位化
        dx /= length;
        dy /= length;
        //步长 每步至少走一个像素的大小
        double stepLength = camera.getPixelEdge() * Math.max(Math.abs(dx), Math.abs(dy));

        //绘制点直到到达终点
        while (true) {
            //计算深度
            double h;
            //斜率为无穷(横坐标相等)单独处理 用y获取z z = z0+(y-y0)/(y1-y0) * (z1-z0)
            if (dx == 0) {
                h = points[0][2] + (y - points[0][1]) / (points[1][1] - points[0][1]) * (points[1][2] - points[0][2]);
            }
            //用x获取z z = z0+(x-x0)/(x1-x0) * (z1-z0)
            else
                h = points[0][2] + (x - points[0][0]) / (points[1][0] - points[0][0]) * (points[1][2] - points[0][2]);

            //渲染
            rendering((int) (x / camera.getPixelEdge()), (int) (y / camera.getPixelEdge()), h, linearGeometry.getColoration().getColor());

            //出循环条件
            if (Math.abs(x - p.get(2)[0]) < 1e-6 && Math.abs(y - p.get(2)[1]) < 1e-6)
                break;
            //更新当前点坐标
            x += dx * stepLength;
            y += dy * stepLength;

            //检查坐标确保不超过终点
            if ((dx > 0 && x > p.get(2)[0]) || (dx < 0 && x < p.get(2)[0])) x = p.get(2)[0];
            if ((dy > 0 && y > p.get(2)[1]) || (dy < 0 && y < p.get(2)[1])) y = p.get(2)[1];
        }
    }

    public void paintFace(LinearGeometry linearGeometry) {
        if (linearGeometry.getColoration() == null) return;


        if (linearGeometry.getLinearGeometry(0).length < 3)
            throw new IllegalArgumentException("组成当前面的点的个数小于3");
        //对每一个面的点进行遍历 射线法判断当前位置是否在多边形内，把点带入面的方程即可求出深度


        //获取面的点
        double[][] points = new double[linearGeometry.getLinearGeometry(0).length][3];
        ArrayList<double[]> coordinates = new ArrayList<>();
        //裁剪深度
        for (int j = 0; j < linearGeometry.getLinearGeometry(0).length; j++) {
            points[j] = ((Point) linearGeometry.getLinearGeometry(0, j).getLowDimensionalObject()).getCoordinates();
        }
        for (int i = 0; i < points.length; i++) {
            //这个点和下一个点的z值都>0，将它自身添加进去就行
            if (points[i][2] > 0 && points[(i + 1) % points.length][2] > 0) {
                coordinates.add(((Point) linearGeometry.getLinearGeometry(0, i).getLowDimensionalObject().getLowDimensionalObject()).getCoordinates());
            }
            //其中一个z值>0，另一个z值<=0
            else if (points[i][2] > 0 || points[(i + 1) % points.length][2] > 0) {
                //两个点的z不可能相等的，所以斜率不可能为0，pz-qz != 0
                //(pz-0)/(pz-qz) = (px-x)/(px-qx) -->  x = px-(px-qx)*pz/(pz-qz)
                double[] point = new double[3];
                point[0] = points[i][0] - (points[i][0] - points[(i + 1) % points.length][0]) * points[i][2] / (points[i][2] - points[(i + 1) % points.length][2]);
                point[1] = points[i][1] - (points[i][1] - points[(i + 1) % points.length][1]) * points[i][2] / (points[i][2] - points[(i + 1) % points.length][2]);
                point[2] = 0;
                //按顺序添加进去
                if (points[i][2] > 0) {
                    coordinates.add(((Point) linearGeometry.getLinearGeometry(0, i).getLowDimensionalObject().getLowDimensionalObject()).getCoordinates());
                }
                coordinates.add(projection(point));
            }
        }
        //不用画了
        if (coordinates.isEmpty()) return;
        points = new double[coordinates.size()][3];
        coordinates.toArray(points);
        //裁剪面
        //获取包围盒
        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        for (int j = 0; j < points.length; j++) {
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
        double[] k = new double[points.length];
        for (int j = 0; j < k.length; j++) {
            if (points[j][0] != points[(j + 1) % k.length][0]) {
                k[j] = (points[j][1] - points[(j + 1) % k.length][1]) / (points[j][0] - points[(j + 1) % k.length][0]);
            } else {
                k[j] = Double.POSITIVE_INFINITY;
            }
        }

        //遍历每个点 (x,y)为第几块 (x*pixelEdge+pixelEdge/2,y*pixelEdge+pixelEdge/2)为这块中心点的坐标
        for (int x = Math.max((int) Math.floor(minX / camera.getPixelEdge()), 0); x < Math.min((int) Math.ceil(maxX / camera.getPixelEdge()), getW() / camera.getPixelEdge()); x++) {
            for (int y = Math.max((int) Math.floor(minY / camera.getPixelEdge()), 0); y < Math.min((int) Math.ceil(maxY / camera.getPixelEdge()), getH() / camera.getPixelEdge()); y++) {
                // 计算当前块的中心坐标
                double xCenter = x * camera.getPixelEdge() + camera.getPixelEdge() / 2.0;
                double yCenter = y * camera.getPixelEdge() + camera.getPixelEdge() / 2.0;
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
        if (x < 0 || x >= getW() / camera.getPixelEdge() || y < 0 || y >= getH() / camera.getPixelEdge()) return;
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

    //投影
    private double[] projection(double[] coordinates) {
        //投影
        coordinates = camera.projection(coordinates);
        //转到窗口坐标系
        coordinates[0] = getWDouble() / 2 - coordinates[0] / camera.getScaleParameters();
        coordinates[1] = getHDouble() / 2 - coordinates[1] / camera.getScaleParameters();
        return coordinates;
    }
}
