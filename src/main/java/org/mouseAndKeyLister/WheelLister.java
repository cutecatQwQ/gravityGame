package org.mouseAndKeyLister;

import org.model.BoxAndTextModel;
import org.model.Model;

import java.awt.event.MouseWheelEvent;

public class WheelLister extends MouseAndKeyLister {
    //此Lister是专门监听鼠标滚轮的
    Model model;

    public WheelLister(Model model) {
        this.model = model;
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        int a = e.getWheelRotation();
        model.wAdd(2 * a);
        model.hAdd(2 * a);
        model.xAdd(-a);
        model.yAdd(-a);
        model.paint();
    }
}
