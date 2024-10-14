package org.gameStart.testOne;


import org.mainFrame.Service.ListenService;
import org.mainFrame.Service.PaintService;
import org.mainFrame.mouseAndKeyLister.DraggableLister;
import org.mainFrame.mouseAndKeyLister.MouseAndKeyLister;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Graph {
    ArrayList<Point> points = new ArrayList<>();
    ArrayList<Line> lines = new ArrayList<>();
    Point background = new Point(1000,100,20, Color.red,0) {
        @Override
        public void paint() {
            super.paint();
            Graphics2D g = ((BufferedImage) getImage()).createGraphics();
            // 设置抗锯齿以提高绘制质量
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            //绘制透明色
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0));
            g.fillOval(4,4,32,32);
        }
    };

    public void addPoint(Point point) {
        points.add(point);
        point.addLister(new DraggableLister(point));
        point.addLister(new MouseAndKeyLister() {
            @Override
            public void mouseEntered(MouseEvent e) {
                background.setX(point.getXDouble()- point.r);
                background.setY(point.getYDouble()- point.r);
                PaintService.paintSetAdd(background);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                PaintService.paintSetRemove(background);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                background.setX(point.getXDouble()-point.r);
                background.setY(point.getYDouble()- point.r);
                point.lines.forEach(Line::paint);
            }
        });
        ListenService.mouseSetAdd(point);
        PaintService.paintSetAdd(point);
    }
    public void addLine(Point point1,Point point2) {
        if(point1 == point2) return;
        Line line = new Line(point1,point2,Color.black,0);
        lines.add(line);
        PaintService.paintSetAdd(line);
        point1.addLine(line);
        point2.addLine(line);
    }
}
