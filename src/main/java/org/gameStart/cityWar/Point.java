package org.gameStart.cityWar;

import org.model.Model;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Point extends Model {
    Color color;
    int r;
    public Point(int x, int y, int r ,Color color,int priority) {
        this.setX(x - r / 2);
        this.setY(y - r / 2);
        this.setW(r * 2);
        this.setH(r * 2);
        this.setPriority(priority);
        this.color = color;
        this.r = r;
        paint();
    }

    @Override
    public void paint() {
        setImage(new BufferedImage(getW(), getH(), BufferedImage.TYPE_INT_ARGB));
        Graphics2D g = ((BufferedImage) getImage()).createGraphics();
        // 设置抗锯齿以提高绘制质量
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(0, 0, 0, 0));
        g.fillRect(0, 0, getW(), getH());
        g.setColor(color);
        g.fillOval(0, 0, getW(), getH());
        g.dispose();
    }
    public Point setXAndY(int x,int y){
        this.setX(x - r / 2);
        this.setY(y - r / 2);
        return this;
    }
}
