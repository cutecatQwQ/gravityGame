package org.mouseAndKeyLister;

import org.Tool.Tool;
import org.model.Model;

import java.awt.event.KeyEvent;

public class KeyActionLister extends MouseAndKeyLister {
    //此Lister是用于实现键盘监听功能的
    Model model;
    boolean w,a,s,d;

    public KeyActionLister(Model model) {
        this.model = model;
        new Thread(() -> {
            while(true){
                model.xAdd(a?-10:0);
                model.xAdd(d?10:0);
                model.yAdd(w?-10:0);
                model.yAdd(s?10:0);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case 87:w = true;break;
            case 65:a = true;break;
            case 83:s = true;break;
            case 68:d = true;break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case 87:w = false;break;
            case 65:a = false;break;
            case 83:s = false;break;
            case 68:d = false;break;
        }
    }
}
