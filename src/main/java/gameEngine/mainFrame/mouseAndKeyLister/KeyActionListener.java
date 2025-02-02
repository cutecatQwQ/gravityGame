package gameEngine.mainFrame.mouseAndKeyLister;

import gameEngine.mainFrame.model.Model;

import java.awt.event.KeyEvent;

public class KeyActionListener extends MouseAndKeyListener {
    //此Lister是用于实现键盘监听功能的
    Model model;
    boolean w,a,s,d;

    public KeyActionListener(Model model) {
        this.model = model;
        new Thread(() -> {
            while(true){
                model.addX(a?-10:0);
                model.addX(d?10:0);
                model.addY(w?-10:0);
                model.addY(s?10:0);
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
