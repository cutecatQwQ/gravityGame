package org.gameStart.multiDimensional;

import org.mainFrame.MainJFrame;
import org.mainFrame.model.ImageModel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MultiDimensionalInit3D {
    private static Rendering3DService rendering3DService;

    public static void multiDimensionalInit(MainJFrame mainJFrame) {
        System.out.println("start！");

        ImageModel imageModel = new ImageModel(0, 0, MainJFrame.dimension.width, MainJFrame.dimension.height, "");
        Image image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = (Graphics2D) image.getGraphics();
        graphics2D.setColor(new Color(0, 0, 0, 31));
        graphics2D.fillRect(0, 0, MainJFrame.dimension.width, MainJFrame.dimension.height);
        imageModel.setImage(image);
        mainJFrame.getPaintService().paintSetAdd(imageModel);

        Camera3D camera3D = new Camera3D(new Point(0, 0, 0),
                new Matrix(new Vector(1, 0, 0),
                        new Vector(0, 1, 0),
                        new Vector(0, 0, 1)));
        rendering3DService = new Rendering3DService(mainJFrame, camera3D);

        camera3D.addLister(new Camera3DController(camera3D, rendering3DService));
        mainJFrame.getListenService().mouseSetAdd(camera3D);

        //坐标轴
        Line line0 = new Line(new Point(0, 0, 0), new Point(300, 0, 0));
        line0.setColor(Color.red);
        Line line1 = new Line(new Point(0, 0, 0), new Point(0, 300, 0));
        line1.setColor(Color.BLUE);
        Line line2 = new Line(new Point(0, 0, 0), new Point(0, 0, 300));
        line2.setColor(Color.GREEN);

        for (int i = -100; i <= 100; i += 20) {
            Line line = new Line(new Point(i, -120, 0), new Point(i, 120, 0));
            line.setColor(new Color(0, 0, 0, 63));
            rendering3DService.rendering3DSetAdd(line);
        }
        for (int i = -100; i <= 100; i += 20) {
            Line line = new Line(new Point(-120, i, 0), new Point(120, i, 0));
            line.setColor(new Color(0, 0, 0, 63));
            rendering3DService.rendering3DSetAdd(line);
        }

        Point[] points = new Point[8];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    points[i + j * 2 + k * 4] = new Point(i * 20, j * 20, k * 20);
                }
            }
        }
        Line[] lines = new Line[12];
        int index = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = i + 1; j < 8; j++) {
                if ((points[i].get(0) == points[j].get(0) && points[i].get(1) == points[j].get(1) && points[i].get(2) != points[j].get(2)) ||
                        (points[i].get(0) == points[j].get(0) && points[i].get(1) != points[j].get(1) && points[i].get(2) == points[j].get(2)) ||
                        (points[i].get(0) != points[j].get(0) && points[i].get(1) == points[j].get(1) && points[i].get(2) == points[j].get(2))) {
                    lines[index++] = new Line(points[i], points[j]);
                }
            }
        }
        for (Line line : lines) {
            rendering3DService.rendering3DSetAdd(line);
        }
        Face[] faces = new Face[6];
        try{int out = 0;
        faces[0] = new Face(points[0], points[1], points[3], points[2]);
        for (Point point : faces[0].getPoints()) {
            point.add(2, -out);
        }
        faces[0].setColor(new Color(255, 255, 255));
        faces[1] = new Face(points[0], points[1], points[5], points[4]);
        for (Point point : faces[1].getPoints()) {
            point.add(1, -out);
        }
        faces[1].setColor(new Color(255, 0, 0));
        faces[2] = new Face(points[0], points[2], points[6], points[4]);
        for (Point point : faces[2].getPoints()) {
            point.add(0, -out);
        }
        faces[2].setColor(new Color(0, 0, 255));
        faces[3] = new Face(points[1], points[3], points[7], points[5]);
        for (Point point : faces[3].getPoints()) {
            point.add(0, out);
        }
        faces[3].setColor(new Color(0, 255, 0));
        faces[4] = new Face(points[2], points[3], points[7], points[6]);
        for (Point point : faces[4].getPoints()) {
            point.add(1, out);
        }
        faces[4].setColor(new Color(250, 128, 10));
        faces[5] = new Face(points[4], points[5], points[7], points[6]);
        for (Point point : faces[5].getPoints()) {
            point.add(2, out);
        }
        faces[5].setColor(new Color(255, 255, 0));
    } catch (Exception e){
            e.printStackTrace();
        }
        for (Face face : faces) {
            rendering3DService.rendering3DSetAdd(face);
        }

        rendering3DService.rendering3DSetAdd(line0);
        rendering3DService.rendering3DSetAdd(line1);
        rendering3DService.rendering3DSetAdd(line2);

        rendering3DService.rendering3D();

        System.out.println("over");
    }
}
