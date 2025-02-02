package gameEngine.gameStart.gravity;

import gameEngine.Tool.Tool;
import gameEngine.mainFrame.MainJFrame;
import gameEngine.mainFrame.model.BoxAndTextModel;
import gameEngine.mainFrame.model.Model;
import gameEngine.mainFrame.mouseAndKeyLister.ButtonListener;
import gameEngine.mainFrame.mouseAndKeyLister.DraggableListener;
import gameEngine.mainFrame.mouseAndKeyLister.MouseAndKeyListener;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ScheduledFuture;

public class GravityInit {
    //组件
    public static void timeInit(MainJFrame mainJFrame, TimeService timeService){
        //间隔10毫秒时间流逝，time++，处理运动的物体
        final ScheduledFuture[] p = {null};
        final boolean[] pa = {true};

        //暂停功能
        BoxAndTextModel button = new BoxAndTextModel(1400, 0, 1960 * 4 / 50.0, 1080 * 4 / 50.0, new Color(225,225,225), Color.BLACK, 0.33, "暂停");
        button.addListener(new ButtonListener(button,mainJFrame){
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
        button.addListener(new DraggableListener(button));
        mainJFrame.getPaintService().paintSetAdd(button);
        mainJFrame.getListenService().mouseSetAdd(button);
        Model paused = new Model() {};
        paused.addListener(new MouseAndKeyListener() {
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

        //步进
        BoxAndTextModel button1 = new BoxAndTextModel(1400, 1080 * 4 / 50.0, 1960 * 4 / 50.0, 1080 * 4 / 50.0, new Color(225,225,225), Color.BLACK, 0.33, "步进");
        button1.addListener(new ButtonListener(button1,mainJFrame){
            @Override
            public void mouseClicked(MouseEvent e) {
                timeService.timePasses();
            }
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                for (int i = 0; i < e.getWheelRotation(); i++) {
                    timeService.timePasses();
                }
            }
        });
        button1.addListener(new DraggableListener(button1));
        mainJFrame.getPaintService().paintSetAdd(button1);
        mainJFrame.getListenService().mouseSetAdd(button1);
    }
    public static void timeStart(MainJFrame mainJFrame) {
        TimeService timeService = new TimeService(1,mainJFrame);

        timeInit(mainJFrame,timeService);


        Vector<Double> v = new Vector<>(2);
        v.add(1.0);
        v.add(0.0);
        Vector<Double> a = new Vector<>(2);
        a.add(0.0);
        a.add(-0.05);
//        RectangleModel rectangleModel = new RectangleModel(700, 500, 50, 50, Color.black, v, a,false,10);
//        v.set(0, -1.0);
//        CircleModel circleModel = new CircleModel(1000, 300, 50, Color.black, v, a,false,1);
//        mainJFrame.getPaintService().paintSetAdd(rectangleModel);
//        mainJFrame.getPaintService().paintSetAdd(circleModel);
//        timeService.timeSetAdd(rectangleModel);
//        rectangleModel.addListener(new DraggableListener(rectangleModel));
//        mainJFrame.getListenService().mouseSetAdd(rectangleModel);

//        timeService.timeSetAdd(circleModel);

        v.set(0,0.0);
        v.set(1,0.0);
        a.set(0,0.0);
        a.set(1,0.0);
        double infinity = 100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000.0;
        RectangleModel wall1 = new RectangleModel(-40, 0, 50, MainJFrame.dimension.height-10, Color.black, v, a,false,infinity);
        RectangleModel wall2 = new RectangleModel(10, -40,  MainJFrame.dimension.width,50, Color.black, v, a,false,infinity);
        RectangleModel wall3 = new RectangleModel(MainJFrame.dimension.width-10, 10, 50, MainJFrame.dimension.height-10, Color.black, v, a,false,infinity);
        RectangleModel wall4 = new RectangleModel(0, MainJFrame.dimension.height-10,  MainJFrame.dimension.width-10,50, Color.black, v, a,false,infinity);

        wall1.addListener(new DraggableListener(wall1));
        wall2.addListener(new DraggableListener(wall2));
        wall3.addListener(new DraggableListener(wall3));
        wall4.addListener(new DraggableListener(wall4));

        timeService.timeSetAdd(wall1);
        timeService.timeSetAdd(wall2);
        timeService.timeSetAdd(wall3);
        timeService.timeSetAdd(wall4);

        mainJFrame.getPaintService().paintSetAdd(wall1);
        mainJFrame.getPaintService().paintSetAdd(wall2);
        mainJFrame.getPaintService().paintSetAdd(wall3);
        mainJFrame.getPaintService().paintSetAdd(wall4);

        mainJFrame.getListenService().mouseSetAdd(wall1);
        mainJFrame.getListenService().mouseSetAdd(wall2);
        mainJFrame.getListenService().mouseSetAdd(wall3);
        mainJFrame.getListenService().mouseSetAdd(wall4);





        RectangleModel[] rectangleModels = new RectangleModel[1000];
        Random random = new Random();
        for (int i = 0; i < rectangleModels.length; i++) {
            rectangleModels[i] = new RectangleModel(20+i%100*15, 100+i/100*10, 10, 10, new Color(random.nextInt(256),random.nextInt(256),random.nextInt(256)), v, a,false,1);
            v.set(0,(2*random.nextDouble()-1));
            v.set(1,(2*random.nextDouble()-1));
//            a.set(1,-0.1);
//            a.set(0,(2*random.nextDouble()-1)/100);
//            a.set(1,(2*random.nextDouble()-1)/100);
            rectangleModels[i].setA(a);
            rectangleModels[i].setV(v);
            rectangleModels[i].addListener(new DraggableListener(rectangleModels[i]));
            mainJFrame.getPaintService().paintSetAdd(rectangleModels[i]);
            timeService.timeSetAdd(rectangleModels[i]);
            mainJFrame.getListenService().mouseSetAdd(rectangleModels[i]);
        }

        Tool.afterAndContinue(0,1000,()->{
            int aa = 0;
            for (int i = 0; i < rectangleModels.length; i++) {
                if(rectangleModels[i].getX()<0||rectangleModels[i].getX()>mainJFrame.dimension.width||
                        rectangleModels[i].getY()<0||rectangleModels[i].getY()>mainJFrame.dimension.height) {
                    System.out.println(aa+++":"+rectangleModels[i]);
                    System.out.println(rectangleModels[i].getV());
                    Vector<Double> vv = new Vector<>();
                    vv.add(0.0);
                    vv.add(0.0);
                    rectangleModels[i].setV(vv);
                }
            }
        });

//        RectangleModel[] rectangleModelss = new RectangleModel[1000];
//        for (int i = 0; i < rectangleModelss.length; i++) {
//            rectangleModelss[i] = new RectangleModel(500+Math.cos(2.0*Math.PI*i/rectangleModelss.length)*100, 500+Math.sin(2.0*Math.PI*i/rectangleModelss.length)*100, 10, 10, Color.black, v, a,false,1);
//            v.set(0,(2*random.nextDouble()-1));
//            v.set(1,(2*random.nextDouble()-1));
////            a.set(1,0.0);
//            a.set(0,0.0);
//            a.set(1,0.0);
//            rectangleModelss[i].setA(a);
//            rectangleModelss[i].setV(v);
//            rectangleModelss[i].addListener(new DraggableListener(rectangleModelss[i]));
//            mainJFrame.getPaintService().paintSetAdd(rectangleModelss[i]);
//            timeService.timeSetAdd(rectangleModelss[i]);
//            mainJFrame.getListenService().mouseSetAdd(rectangleModelss[i]);
//        }
    }
}
