package org.gameStart.testOne;

import org.mainFrame.model.Model;

import java.awt.*;
import java.util.ArrayList;

public class Point extends Model {
    Color color;
    ArrayList<Line> lines = new ArrayList<>();
    double r;
    public Point(double x, double y, double r ,Color color,int priority) {
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
        Graphics2D g = getNewGraphics2D();
        g.setColor(new Color(0, 0, 0, 0));
        g.fillRect(0, 0, getW(), getH());
        g.setColor(color);
        g.fillOval(0, 0, getW(), getH());
        g.dispose();
    }

    public void addLine(Line line){
        lines.add(line);
    }
}
