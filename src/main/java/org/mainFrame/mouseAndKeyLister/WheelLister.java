package org.mainFrame.mouseAndKeyLister;

import org.mainFrame.model.Model;

import java.awt.event.MouseWheelEvent;

public class WheelLister extends MouseAndKeyLister {
    //此Lister是专门监听鼠标滚轮的
    Model model;

    public WheelLister(Model model) {
        this.model = model;
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        int a = e.getWheelRotation();
        model.addW(4 * a);
        model.addH((model.getHDouble() * model.getWDouble()) /(model.getWDouble()-4*a) - model.getHDouble());
        model.addX(-2*a);
        model.addY(-((model.getHDouble() * model.getWDouble()) /(model.getWDouble()-4*a) - model.getHDouble())/2);
        model.paint();
    }
}
