package org.gameStart.gravity;

import java.awt.*;
import java.util.Vector;

public class CircleModel extends CollideModel {
    //半径
    private double r;

    public CircleModel(double x, double y, double r, Color color, Vector<Double> v, Vector<Double> a,boolean stable,double m) {
        super(x-r, y-r, r, r, color, v, a,stable,m);
        setIndex(2.0);
        paint();
    }

    @Override
    public void paint() {
        Graphics2D g = getNewGraphics2D();
        g.setColor(getColor());
        g.fillOval(0, 0, getW(), getH());
    }

    public int getR() {
        return (int) Math.round(r);
    }

    public int getRDouble() {
        return (int) Math.round(r);
    }

    public void setR(double r) {
        this.r = r;
        setH(r);
        setW(r);
    }
}
