package org.gameStart.multiDimensional;

import org.gameStart.multiDimensional.objects.Line;
import org.gameStart.multiDimensional.objects.Object;
import org.gameStart.multiDimensional.objects.Point;

import java.awt.*;
import java.util.Arrays;
import java.util.function.Consumer;

//凸四边形
public class Face extends Object {
    org.gameStart.multiDimensional.objects.Point[] points;
    Line[] lines;

    public Face(Point... points) {
        super(0);
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

    public org.gameStart.multiDimensional.objects.Point[] getPoints() {
        return points;
    }

    public org.gameStart.multiDimensional.objects.Point get(int i) {
        return points[i];
    }

    public void set(int i, org.gameStart.multiDimensional.objects.Point point) {
        points[i] = point;
    }

//    @Override
    public void traverse(Consumer<org.gameStart.multiDimensional.objects.Point> consumer) {
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
