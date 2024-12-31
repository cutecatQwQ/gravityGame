package org.gameStart.multiDimensional;

import org.mainFrame.model.Model;

import java.awt.*;
import java.util.Arrays;
import java.util.function.Consumer;

//凸四边形
public class Face extends Model implements Object {
    Point[] points;
    Line[] lines;

    public Face(Point... points) {
        this.points = points;
        lines = new Line[points.length];
        for (int i = 0; i < points.length; i++) {
            lines[i] = new Line(points[i],points[(i+1)%points.length]);
        }
        setColor(Color.RED);
    }

    @Override
    public void paint() {
        Graphics2D graphics2D = getNewGraphics2D();
        graphics2D.setColor(getColor());
        int[] xPoint = new int[points.length];
        int[] yPoint = new int[points.length];
        for (int i = 0; i < points.length; i++) {
            xPoint[i] = (int) (points[i].getXDouble()-getXDouble());
            yPoint[i] = (int) (points[i].getYDouble()-getYDouble());
        }
        graphics2D.fillPolygon(xPoint, yPoint, points.length);
    }

    public Point get(int i) {
        return points[i];
    }

    public void set(int i, Point point) {
        points[i] = point;
    }

    @Override
    public void traverse(Consumer<Point> consumer) {
        //初始化每个边，让边去初始化点
//        for (Line line : lines) {
//            line.traverse(consumer);
//        }
        //初始化点
        for (Point point : points) {
            consumer.accept(point);
        }
        double minX = points[0].getXDouble();
        double minY = points[0].getYDouble();
        double maxX = points[0].getXDouble();
        double maxY = points[0].getYDouble();
        for (int i = 1; i < points.length; i++) {
            if (points[i].getXDouble() < minX) minX = points[i].getXDouble();
            if (points[i].getYDouble() < minY) minY = points[i].getYDouble();
            if (points[i].getXDouble() > maxX) maxX = points[i].getXDouble();
            if (points[i].getYDouble() > maxY) maxY = points[i].getYDouble();
        }
        setX(minX-add);
        setY(minY-add);
        setW(maxX - minX+add*2);
        setH(maxY - minY+add*2);
        if (getW() > 2&&getH() > 2) paint();
    }

    @Override
    public String toString() {
        return "Face{" +
                "points=" + Arrays.toString(points) +
                '}';
    }
}
