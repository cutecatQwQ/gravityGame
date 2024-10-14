package org.gameStart.testOne;

import org.mainFrame.model.Model;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Line extends Model {
    Color color;
    Point point1,point2;
    public Line(Point point1,Point point2,Color color,int priority){
        this.point1 = point1;
        this.point2 = point2;
        this.color = color;
        setPriority(priority);
        paint();
    }

    @Override
    public void paint() {
        setX(Math.min(point1.getXDouble(), point2.getXDouble())+point1.r);
        setY(Math.min(point1.getYDouble(), point2.getYDouble())+point1.r);
        setW(Math.max(Math.abs(point1.getXDouble() - point2.getXDouble()), 2));
        setH(Math.max(Math.abs(point1.getYDouble() - point2.getYDouble()), 2));
        setImage(new BufferedImage(getW(), getH(), BufferedImage.TYPE_INT_ARGB));
        Graphics2D g = ((BufferedImage) getImage()).createGraphics();
        // 设置抗锯齿以提高绘制质量
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(color);
        g.drawLine(
                point1.getX()-getX()+point1.r,
                point1.getY()-getY()+point1.r,
                point2.getX()-getX()+point2.r,
                point2.getY()-getY()+point2.r
                );
    }
}
