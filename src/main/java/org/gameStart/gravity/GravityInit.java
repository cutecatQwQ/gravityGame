package org.gameStart.gravity;

import org.Tool.Tool;
import org.mainFrame.MainJFrame;
import org.mainFrame.model.BoxAndTextModel;
import org.mainFrame.model.Model;
import org.mainFrame.mouseAndKeyLister.ButtonLister;
import org.mainFrame.mouseAndKeyLister.DraggableLister;
import org.mainFrame.mouseAndKeyLister.MouseAndKeyLister;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ScheduledFuture;

public class GravityInit {
    public static void timeStart(MainJFrame mainJFrame) {
        TimeService timeService = new TimeService(1,mainJFrame);

        //间隔10毫秒时间流逝，time++，处理运动的物体
        final ScheduledFuture[] p = {null};
        final boolean[] pa = {true};

        //暂停功能
        BoxAndTextModel button = new BoxAndTextModel(1400, 0, 1960 * 4 / 50.0, 1080 * 4 / 50.0, new Color(225,225,225), Color.BLACK, 0.33, "暂停");
        button.addLister(new ButtonLister(button,mainJFrame){
            @Override
            public void mouseClicked(MouseEvent e) {
                if(pa[0]){
                    p[0] = Tool.afterAndContinue(0, 10, timeService::timePasses);
                } else {
                    p[0].cancel(false);
                }
                pa[0] = !pa[0];
            }
        });
        button.addLister(new DraggableLister(button));
        mainJFrame.getPaintService().paintSetAdd(button);
        mainJFrame.getListenService().mouseSetAdd(button);
        Model paused = new Model() {};
        paused.addLister(new MouseAndKeyLister() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_P:
                        if(pa[0]){
                            p[0] = Tool.afterAndContinue(0, 10, timeService::timePasses);
                        } else {
                            p[0].cancel(false);
                        }
                        pa[0] = !pa[0];
                        break;
                }
            }
        });
        mainJFrame.getListenService().keySetAdd(paused);

        Vector<Double> v = new Vector<>(2);
        v.add(1.0);
        v.add(0.0);
        Vector<Double> a = new Vector<>(2);
        a.add(0.0);
        a.add(-0.01);
        RectangleModel rectangleModel = new RectangleModel(500, 500, 50, 50, Color.black, v, a,false,1);
        v.set(0, -1.0);
        CircleModel circleModel = new CircleModel(1000, 300, 50, Color.black, v, a,false,1);
        mainJFrame.getPaintService().paintSetAdd(rectangleModel);
        mainJFrame.getPaintService().paintSetAdd(circleModel);
        timeService.timeSetAdd(rectangleModel);
        timeService.timeSetAdd(circleModel);

        RectangleModel[] rectangleModels = new RectangleModel[10];
        Random random = new Random();
        for (int i = 0; i < rectangleModels.length; i++) {
            rectangleModels[i] = new RectangleModel(i%100*10, i/100*10, 10, 10, Color.black, v, a,false,1);
            v.set(0,(2*random.nextDouble()-1));
            v.set(1,(2*random.nextDouble()-1));
            a.set(1,0.0);
//            a.set(0,(2*random.nextDouble()-1)/100);
//            a.set(1,(2*random.nextDouble()-1)/100);
            rectangleModels[i].setA(a);
            rectangleModels[i].setV(v);
            rectangleModels[i].addLister(new DraggableLister(rectangleModels[i]));
            mainJFrame.getPaintService().paintSetAdd(rectangleModels[i]);
            timeService.timeSetAdd(rectangleModels[i]);
            mainJFrame.getListenService().mouseSetAdd(rectangleModels[i]);
        }
    }
}
