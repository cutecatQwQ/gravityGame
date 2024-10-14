package org.gameStart;

import org.mainFrame.Service.ListenService;
import org.mainFrame.MainJFrame;
import org.mainFrame.model.Model;
import org.mainFrame.mouseAndKeyLister.MouseAndKeyLister;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class GameInit {
    public static void init(MainJFrame mainJFrame){
        //最小化方法
        Model esc = new Model() {
        };
        esc.addLister(new MouseAndKeyLister() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case 27:
                        mainJFrame.getGraphicsConfiguration().getDevice().setFullScreenWindow(null);
                        mainJFrame.setExtendedState(JFrame.ICONIFIED);
                        break;
                }
            }
        });
        ListenService.keySetAdd(esc);
    }
}
