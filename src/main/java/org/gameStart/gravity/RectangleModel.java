package org.gameStart.gravity;

import java.awt.*;
import java.util.Vector;

public class RectangleModel extends CollideModel{
    public RectangleModel(double x, double y, double w, double h, Color color, Vector<Double> v, Vector<Double> a,boolean stable,double m) {
        super(x, y, w, h, color, v, a, m);
        paint();
    }

    //碰撞检测
    @Override
    public boolean collisionDetection(CollideModel collideModel) {
        //矩形和矩形，按照x来说，矩形1的右边在矩形2的左边的右边 或者 矩形1的左边在矩形2的右边的左边 就不会碰撞
        if(collideModel instanceof RectangleModel){
            return !(this.getXDouble() >= collideModel.getXDouble() + collideModel.getWDouble() ||
                    this.getXDouble() + this.getWDouble() <= collideModel.getXDouble() ||
                    this.getYDouble() >= collideModel.getYDouble() + collideModel.getHDouble() ||
                    this.getYDouble() + this.getHDouble() <= collideModel.getYDouble());
        }
        return false;
    }

    @Override
    public void paint() {
        Graphics2D g = getNewGraphics2D();
        g.setColor(getColor());
        g.fillRect(0,0,getW(),getH());
    }
}
