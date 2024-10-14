package org.mainFrame.mouseAndKeyLister;

import org.mainFrame.model.Model;

import java.awt.*;
import java.awt.event.MouseEvent;

public class ButtonLister extends MouseAndKeyLister {
    //此Lister是专门监听按钮的，鼠标进入会变色，按下会变小
    Model model;
    //正常时候的位置宽高
    double x, y, w, h;

    public ButtonLister(Model model) {
        this.model = model;
        this.x = model.getXDouble();
        this.y = model.getYDouble();
        this.w = model.getWDouble();
        this.h = model.getHDouble();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        model.setX(0.025 * w + x);
        model.setY(0.025 * h + y);
        model.setW(0.95 * w);
        model.setH(0.95 * h);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        model.setW(w);
        model.setH(h);
        model.setX(x);
        model.setY(y);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        Graphics g = model.getGraphics2D();
        g.setColor(new Color(0, 0, 0, 50));
        g.fillRect(0, 0, model.getW(), model.getH());
    }

    @Override
    public void mouseExited(MouseEvent e) {
        model.paint();
    }
}
