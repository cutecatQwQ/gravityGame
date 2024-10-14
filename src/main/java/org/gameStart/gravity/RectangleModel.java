package org.gameStart.gravity;

import java.awt.*;
import java.util.Vector;

public class RectangleModel extends CollideModel{
    public RectangleModel(double x, double y, double w, double h, Color color, Vector<Double> v, Vector<Double> a,boolean stable,double m) {
        super(x, y, w, h, color, v, a,stable,m);
        paint();
    }

    @Override
    public void paint() {
        Graphics2D g = getNewGraphics2D();
        g.setColor(getColor());
        g.fillRect(0,0,getW(),getH());
    }
}
