package org.gameStart.multiDimensional;

import org.Tool.Tool;
import org.jfree.chart.plot.PlotOrientation;
import org.mainFrame.MainJFrame;
import org.mainFrame.model.BoxAndTextModel;
import org.mainFrame.model.ImageModel;
import org.mainFrame.model.Model;
import org.mainFrame.mouseAndKeyLister.DraggableLister;
import org.mainFrame.mouseAndKeyLister.MouseAndKeyLister;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class MultiDimensionalInit {
    public static int N = 4;

    public static TreeSet<Object> objectSet = new TreeSet<>();


    static final int[] a = {1};//0 坐标 1 朝向
    static final int[] b = {0};
    static String[][] strings = new String[2][];
    static BoxAndTextModel[] boxAndTextModels = new BoxAndTextModel[6];

    static {
        strings[0] = new String[4];
        strings[1] = new String[6];
        strings[0][0] = "X";
        strings[0][1] = "Y";
        strings[0][2] = "Z";
        strings[0][3] = "W";
        strings[1][0] = "XY";
        strings[1][1] = "XZ";
        strings[1][2] = "YZ";
        strings[1][3] = "XW";
        strings[1][4] = "YW";
        strings[1][5] = "ZW";

    }

    public static void multiDimensionalInit(MainJFrame mainJFrame) {
        System.out.println("start！");


        Camera camera = new Camera(new Point(0, 0, 0, 0),
                new Matrix(new Vector(1, 0, 0, 0),
                        new Vector(0, -1, 0, 0),
                        new Vector(0, 0, 1, 0),
                        new Vector(0, 0, 0, 1)));


        for (int i = 0; i < boxAndTextModels.length; i++) {
            boxAndTextModels[i] = new BoxAndTextModel(0, i * 30, 600, 30, Color.WHITE, Color.BLACK, 0.5, "");
        }
        for (int i = 0; i < boxAndTextModels.length; i++) {
            mainJFrame.getPaintService().paintSetAdd(boxAndTextModels[i]);
        }

        camera.addLister(new MouseAndKeyLister() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyChar()) {
                    case 'W':
                        if (a[0] == 0) {
                            camera.location.add(b[0], 1);
                        } else {
                            switch (b[0]) {
                                case 0:
                                    camera.revolve(0, 1, 1);
                                    break;
                                case 1:
                                    camera.revolve(0, 2, 1);
                                    break;
                                case 2:
                                    camera.revolve(1, 2, 1);
                                    break;
                                case 3:
                                    camera.revolve(0, 3, 1);
                                    break;
                                case 4:
                                    camera.revolve(1, 3, 1);
                                    break;
                                case 5:
                                    camera.revolve(2, 3, 1);
                                    break;
                            }
                        }
                        break;
                    case 'S':
                        if (a[0] == 0) {
                            camera.location.add(b[0], -1);
                        } else {
                            switch (b[0]) {
                                case 0:
                                    camera.revolve(0, 1, -1);
                                    break;
                                case 1:
                                    camera.revolve(0, 2, -1);
                                    break;
                                case 2:
                                    camera.revolve(1, 2, -1);
                                    break;
                                case 3:
                                    camera.revolve(0, 3, -1);
                                    break;
                                case 4:
                                    camera.revolve(1, 3, -1);
                                    break;
                                case 5:
                                    camera.revolve(2, 3, -1);
                                    break;
                            }
                        }
                        break;
                    case '-':
                        if (a[0] == 0) a[0] = 1;
                        else a[0] = 0;
                        b[0] = 0;
                        break;
                    case '=':
                        if (a[0] == 0) a[0] = 1;
                        else a[0] = 0;
                        b[0] = 0;
                        break;
                    case '[':
                        if (b[0] == 0) b[0] = a[0] == 0 ? 3 : 5;
                        else b[0]--;
                        break;
                    case ']':
                        if (a[0] == 0 && b[0] == 3 || a[0] == 1 && b[0] == 5) b[0] = 0;
                        else b[0]++;
                        break;
                }
                change(camera);
            }

        });
        camera.addLister(new CameraController(camera, mainJFrame));
        mainJFrame.getListenService().keySetAdd(camera);

        //坐标轴
        Line line0 = new Line(new Point(0, 0, 0, 0), new Point(300, 0, 0, 0));
        line0.setColor(Color.red);
        Line line1 = new Line(new Point(0, 0, 0, 0), new Point(0, 300, 0, 0));
        line1.setColor(Color.BLUE);
        Line line2 = new Line(new Point(0, 0, 0, 0), new Point(0, 0, 300, 0));
        line2.setColor(Color.GREEN);
        Line line3 = new Line(new Point(0, 0, 0, 0), new Point(0, 0, 0, 300));
        line3.setColor(Color.BLACK);

        objectSet.add(line0);
        objectSet.add(line1);
        objectSet.add(line2);
        objectSet.add(line3);


        // Generate all vertices of the hypercube
        Point[] vertices = generateVertices();

        // Get all edges (lines) of the hypercube
        ArrayList<Line> edges = getAllEdges(vertices);

        ArrayList<Face> faces = getAllFaces(vertices);
        Random random = new Random();
        faces.forEach(face -> face.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255), 31)));
        // Print all faces
        Collections.addAll(objectSet, vertices);
        objectSet.addAll(edges);
        objectSet.addAll(faces);
//        faces.forEach(face -> Collections.addAll(objectSet, face.lines));

//        Face face0 = new Face(new Point(100, -100, 0, 0),
//                new Point(100, 100, 0, 0),
//                new Point(-100, 100, 0, 0),
//                new Point(-100, -100, 0, 0));
//        face0.setColor(new Color(255, 0, 255, 127));
//        objectSet.add(face0);
//        objectSet.addAll(Arrays.asList(face0.lines));
//        Face face1 = new Face(new Point(100, 0, -100, 0),
//                new Point(100, 0, 100, 0),
//                new Point(-100, 0, 100, 0),
//                new Point(-100, 0, -100, 0));
//        face1.setColor(new Color(255, 255, 0, 127));
//        objectSet.add(face1);
//        objectSet.addAll(Arrays.asList(face1.lines));
//        Face face2 = new Face(new Point(0, 100, -100, 0),
//                new Point(0, 100, 100, 0),
//                new Point(0, -100, 100, 0),
//                new Point(0, -100, -100, 0));
//        face2.setColor(new Color(0, 255, 255, 127));
//        objectSet.add(face2);
//        objectSet.addAll(Arrays.asList(face2.lines));
//        Face face3 = new Face(new Point(100, 0, 0, -100),
//                new Point(100, 0, 0, 100),
//                new Point(-100, 0, 0, 100),
//                new Point(-100, 0, 0, -100));
//        face3.setColor(new Color(255, 0, 0, 127));
//        objectSet.add(face3);
//        objectSet.addAll(Arrays.asList(face3.lines));
//        Face face4 = new Face(new Point(0, 100, 0, -100),
//                new Point(0, 100, 0, 100),
//                new Point(0, -100, 0, 100),
//                new Point(0, -100, 0, -100));
//        face4.setColor(new Color(0, 0, 255, 127));
//        objectSet.add(face4);
//        objectSet.addAll(Arrays.asList(face4.lines));
//        Face face5 = new Face(new Point(0, 0, 100, -100),
//                new Point(0, 0, 100, 100),
//                new Point(0, 0, -100, 100),
//                new Point(0, 0, -100, -100));
//        face5.setColor(new Color(0, 255, 0, 127));
//        objectSet.add(face5);
//        objectSet.addAll(Arrays.asList(face5.lines));


        change(camera);
        //把所有事物集里面的事物添加到画服务中
        objectSet.forEach(e -> mainJFrame.getPaintService().paintSetAdd((Model) e));

        //折线图
//        Tool.newLineChart("Line Chart","x","y",mainJFrame);
//        Tool.afterAndContinue(1000,1000, Tool::updateLineChart);

//        Tool.newLineChart("Line Chart", "time", "time(ms)", mainJFrame);
//        Tool.afterAndContinue(1000,1000, Tool::updateLineChart);

        System.out.println("over");
    }

    //摄像头移动了，苏姚对所有点重新初始化
    public synchronized static void change(Camera camera) {
        try {
            objectSet.forEach(e -> e.traverse(point -> init(camera, point)));
            boxAndTextModels[0].setText("坐标" + camera.location);
            boxAndTextModels[1].setText("基坐标x" + camera.baseCoordinates.get(0));
            boxAndTextModels[2].setText("基坐标y" + camera.baseCoordinates.get(1));
            boxAndTextModels[3].setText("基坐标z" + camera.baseCoordinates.get(2));
            boxAndTextModels[4].setText("基坐标w" + camera.baseCoordinates.get(3));
            boxAndTextModels[5].setText("当前操作:" + (a[0] == 0 ? "坐标" : "方向") + strings[a[0]][b[0]]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //初始化
    public static void init(Camera camera, Point point) {
        //平移后的点
        Point point1 = MatrixUtil.translate(point, camera.location);
        //旋转后的点
        point1 = MatrixUtil.revolve(point1, camera.baseCoordinates);
        //投影矩阵
//        Matrix matrix = MatrixUtil.projection(camera.getProjectionBaseCoordinates());
//        投影后的点
//        point1 = MatrixUtil.matrixVectorMultiplication(matrix,new Vector(point1));
        point.setX(point1.get(1) + (double) MainJFrame.dimension.width / 2);
        point.setY((double) MainJFrame.dimension.height / 2 - point1.get(2));
//        System.out.println(point+"->"+point1);
    }


    private static final int DIMENSIONS = 4;
    private static final int VERTICES_COUNT = (int) Math.pow(2, DIMENSIONS);

    private static Point[] generateVertices() {
        Point[] vertices = new Point[VERTICES_COUNT];
        int index = 0;
        for (int i = 0; i < VERTICES_COUNT; i++) {
            double[] coordinates = new double[DIMENSIONS];
            for (int j = 0; j < DIMENSIONS; j++) {
                coordinates[j] = (i & (1 << j)) != 0 ? 100 : -100;
            }
            vertices[index++] = new Point(coordinates);
        }
        return vertices;
    }

    private static ArrayList<Line> getAllEdges(Point[] vertices) {
        ArrayList<Line> edges = new ArrayList<>();
        for (int i = 0; i < vertices.length; i++) {
            for (int j = i + 1; j < vertices.length; j++) {
                if (areAdjacent(vertices[i], vertices[j])) {
                    edges.add(new Line(vertices[i], vertices[j]));
                }
            }
        }
        return edges;
    }

    private static boolean areAdjacent(Point p1, Point p2) {
        int diffCount = 0;
        for (int i = 0; i < DIMENSIONS; i++) {
            if (p1.get(i) != p2.get(i)) {
                diffCount++;
            }
        }
        return diffCount == 1;
    }

    private static ArrayList<Face> getAllFaces(Point[] vertices) {
        ArrayList<Face> faces = new ArrayList<>();

        // 选择两个维度进行固定
        for (int i = 0; i < DIMENSIONS; i++) {
            for (int j = i + 1; j < DIMENSIONS; j++) {
                // 对于每对固定的维度值
                for (int fixed1 = 0; fixed1 <= 1; fixed1++) {
                    for (int fixed2 = 0; fixed2 <= 1; fixed2++) {
                        ArrayList<Point> faceVertices = new ArrayList<>();
                        // 收集符合条件的顶点
                        for (Point vertex : vertices) {
                            if (vertex.get(i) == (fixed1 == 0 ? -100 : 100) &&
                                    vertex.get(j) == (fixed2 == 0 ? -100 : 100)) {
                                faceVertices.add(vertex); // 添加顶点
                            }
                        }
                        // 每个面应包含四个顶点
                        if (faceVertices.size() == 4) {
                            faces.add(new Face(faceVertices.get(0), faceVertices.get(1), faceVertices.get(3), faceVertices.get(2))); // 创建面
                        }
                    }
                }
            }
        }
        return faces;
    }

    // 创建数据集
//    public static XYSeries series = new XYSeries("Random Data");
//    public static void setChartImage(ImageModel imageModel){
//        XYSeriesCollection dataset = new XYSeriesCollection();
//        dataset.addSeries(series);
//
//        // 创建图表并转换为BufferedImage
//        BufferedImage image = createChartAsBufferedImage(dataset);
//        if (image != null) {
//            System.out.println("图表已成功转换为BufferedImage！");
//            // 这里可以进一步处理或显示这个BufferedImage
//            imageModel.setImage(image);
//        } else {
//            System.out.println("转换失败！");
//        }
//    }
//    public static BufferedImage createChartAsBufferedImage(XYSeriesCollection dataset) {
//        JFreeChart chart = ChartFactory.createXYLineChart(
//                "Line chart", // 图表标题
//                "index", // x轴标签
//                "time(ms)", // y轴标签
//                dataset, // 数据集
//                PlotOrientation.VERTICAL, // 绘图方向
//                true,                 // 是否显示图例
//                true,                 // 是否生成工具提示
//                false                 // 是否生成URL链接
//        );
//
//        int width = 500;
//        int height = 500;
//        return chart.createBufferedImage(width, height);
//    }
}
