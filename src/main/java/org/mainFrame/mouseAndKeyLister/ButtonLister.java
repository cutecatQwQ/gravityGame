package org.mainFrame.mouseAndKeyLister;

import org.mainFrame.MainJFrame;
import org.mainFrame.model.Model;

import java.awt.*;
import java.awt.event.MouseEvent;

public class ButtonLister extends MouseAndKeyLister {
    //此Lister是专门监听按钮的，鼠标进入会变色，按下会变小
    Model model;
    //需要监听的窗口
    MainJFrame mainJFrame;
    //正常时候的位置宽高
    double x, y, w, h;

    public ButtonLister(Model model, MainJFrame mainJFrame) {
        this.model = model;
        this.x = 0.025 *model.getWDouble();
        this.y = 0.025 *model.getHDouble();
        this.w = 0.05 *model.getWDouble();
        this.h = 0.05 *model.getHDouble();
        this.mainJFrame = mainJFrame;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        model.addX(x);
        model.addY(y);
        model.addW(-w);
        model.addH(-h);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        model.addX(-x);
        model.addY(-y);
        model.addW(w);
        model.addH(h);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        Graphics g = model.getGraphics2D();
        g.setColor(new Color(0, 0, 0, 50));
        g.fillRect(0, 0, model.getW(), model.getH());
        //鼠标状态设置为可按下
        mainJFrame.setMouse(Cursor.HAND_CURSOR);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        model.paint();
        //鼠标状态设置为默认
        mainJFrame.setMouse(Cursor.DEFAULT_CURSOR);
    }
}
