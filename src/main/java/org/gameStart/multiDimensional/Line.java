package org.gameStart.multiDimensional;

import org.mainFrame.model.Model;

import java.awt.*;
import java.util.Arrays;
import java.util.function.Consumer;

public class Line extends Object {
    Point[] points;

    public Line(Point... points) {
        this.points = points;
        setColor(Color.BLACK);
    }

    @Override
    public void paint() {
        Graphics2D graphics2D = getNewGraphics2D();
//        if (getW() <= 3 || getH() <= 3)
//        graphics2D.setStroke(new BasicStroke(2));
        graphics2D.setColor(getColor());
        //直线投影完事\样的
//        if ((points[0].getXDouble() < points[1].getXDouble() &&
//                points[0].getYDouble() < points[1].getYDouble()) ||
//                (points[1].getXDouble() < points[0].getXDouble() &&
//                        points[1].getYDouble() < points[0].getYDouble()))
//            graphics2D.drawLine(add, add, getW()-add, getH()-add);
//            //直线投影式/样的
//        else graphics2D.drawLine(getW()-add, add, add, getH()-add);
        int[] xPoint = new int[points.length];
        int[] yPoint = new int[points.length];
        for (int i = 0; i < points.length; i++) {
            xPoint[i] = (int) (points[i].getXDouble()-getXDouble());
            yPoint[i] = (int) (points[i].getYDouble()-getYDouble());
        }
        graphics2D.drawLine(xPoint[0],yPoint[0],xPoint[1],yPoint[1]);
    }

    public Point get(int i) {
        return points[i];
    }

    public void set(int i, Point point) {
        points[i] = point;
    }

    @Override
    public void traverse(Consumer<Point> consumer) {
        //初始化每个点
        for (Point point : points) {
            consumer.accept(point);
        }
        setX(Math.min(points[0].getXDouble(), points[1].getXDouble())-add);
        setY(Math.min(points[0].getYDouble(), points[1].getYDouble())-add);
        setW(Math.max(points[0].getXDouble(), points[1].getXDouble()) - getXDouble()+add*2);
        setH(Math.max(points[0].getYDouble(), points[1].getYDouble()) - getYDouble()+add*2);
        if (getW() < 1) setW(1);
        if (getH() < 1) setH(1);
        paint();
    }

    @Override
    public String toString() {
        return "Line{" +
                "points=" + Arrays.toString(points) +
                '}';
    }
}
