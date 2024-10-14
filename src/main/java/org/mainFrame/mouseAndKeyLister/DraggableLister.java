package org.mainFrame.mouseAndKeyLister;

import org.mainFrame.model.Model;

import java.awt.event.MouseEvent;

public class DraggableLister extends MouseAndKeyLister {
    //此Lister是用于实现拖拽功能的
    Model model;
    //旧的鼠标位置
    int oldEX, oldEY;
    //旧的model位置
    int oldX, oldY;
    //是否被按下
    boolean isPress = false;

    public DraggableLister(Model model) {
        this.model = model;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        isPress = true;
        oldEX = e.getX();
        oldEY = e.getY();
        oldX = model.getX();
        oldY = model.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        isPress = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (isPress) {
            model.setX(oldX + e.getX() - oldEX);
            model.setY(oldY + e.getY() - oldEY);
        }
    }
}
