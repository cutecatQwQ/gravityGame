package gameEngine.gameStart;

import gameEngine.mainFrame.MainJFrame;
import gameEngine.mainFrame.model.Model;
import gameEngine.mainFrame.mouseAndKeyLister.MouseAndKeyListener;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class GameInit {
    public static void init(MainJFrame mainJFrame){
        //最小化方法
        Model esc = new Model() {
        };
        esc.addListener(new MouseAndKeyListener() {
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
        mainJFrame.getListenService().keySetAdd(esc);
    }
}
