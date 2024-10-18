package org.gameStart;

import org.Tool.Tool;
import org.gameStart.gravity.GravityInit;
import org.gameStart.testOne.Graph;
import org.gameStart.testOne.Line;
import org.mainFrame.Service.ListenService;
import org.mainFrame.MainJFrame;
import org.mainFrame.Service.PaintService;
import org.mainFrame.model.BoxAndTextModel;
import org.mainFrame.model.ImageModel;
import org.mainFrame.model.Model;
import org.mainFrame.mouseAndKeyLister.*;
import org.gameStart.testOne.Point;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Random;

public class GameStart {
    public GameStart(MainJFrame mainJFrame) {
        GameInit.init(mainJFrame);

//        test0(mainJFrame);
//        test1(mainJFrame);
//        test2(mainJFrame);
//        test4(mainJFrame);
        test6(mainJFrame);
    }

    private void test6(MainJFrame mainJFrame) {
        GravityInit.timeStart(mainJFrame);
    }

    private void test4(MainJFrame mainJFrame) {
        Random random = new Random();
        final BoxAndTextModel[] boxAndTextModel = new BoxAndTextModel[1];
        Tool.afterAndContinue(0, 1000, () -> {
            boxAndTextModel[0] = new BoxAndTextModel(100, 100, 1400, 500, new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)), new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)), 0.7, "新年快乐");
            mainJFrame.getPaintService().paintSetAdd(boxAndTextModel[0]);
        });
        Tool.afterAndContinue(999, 1000, () -> {
            mainJFrame.getPaintService().paintSetRemove(boxAndTextModel[0]);
        });
    }

    public static Point background = new Point(0, 0, 20, Color.red, 0) {
        @Override
        public void paint() {
            super.paint();
            Graphics2D g = getNewGraphics2D();
            //绘制透明色
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0));
            g.fillOval(4, 4, 32, 32);
        }
    };

    private void test2(MainJFrame mainJFrame) {
        Random random = new Random();
        Graph graph = new Graph();
        Point[] points = new Point[16];
        for (int i = 0; i < points.length; i++) {
//            points[i] = new Point(10, 10, 5,Color.red,0);
            points[i] = new Point(random.nextInt(1500) + 10, random.nextInt(800) + 10, 10, Color.red, 0);
            graph.addPoint(points[i],mainJFrame);
        }

        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points.length; j++) {
                if (random.nextBoolean() && random.nextBoolean() && random.nextBoolean() && random.nextBoolean())
                    graph.addLine(points[i], points[j],mainJFrame);
            }
        }
//        for (int i = 0; i < points.length; i++) {
//            graph.addLine(points[i],points[(i+1)% points.length]);
//        }
//        for (int i = 0; i < 10; i++) {
//            graph.addLine(points[random.nextInt(points.length)],points[random.nextInt(points.length)]);
//        }
    }


    private void test1(MainJFrame mainJFrame) {
        Point model = new Point(1000, 100, 10, Color.black, -2) {
            @Override
            public void paint() {
                super.paint();
                Graphics2D g = getNewGraphics2D();
                //绘制透明色
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0));
                g.fillOval(2, 2, 16, 16);
            }
        };
        Point point1 = new Point(1000, 100, 5, Color.black, -1);

        Point point = new Point(1200, 200, 5, Color.black, -1);
        Line line = new Line(point1, point, Color.black, 0);
        line.addLister(new ButtonLister(line));
        point.addLister(new DraggableLister(point));
        point.addLister(new MouseAndKeyLister() {
            @Override
            public void mouseEntered(MouseEvent e) {
                model.setX(point.getXDouble() - 5);
                model.setY(point.getYDouble() - 5);
                mainJFrame.getPaintService().paintSetAdd(model);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                mainJFrame.getPaintService().paintSetRemove(model);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                model.setX(point.getXDouble() - 5);
                model.setY(point.getYDouble() - 5);
                line.paint();
            }
        });
        mainJFrame.getPaintService().paintSetAdd(point);
        mainJFrame.getPaintService().paintSetAdd(line);
        mainJFrame.getListenService().mouseSetAdd(point);

        point1.addLister(new DraggableLister(point1));
        point1.addLister(new MouseAndKeyLister() {
            @Override
            public void mouseEntered(MouseEvent e) {
                model.setX(point1.getXDouble() - 5);
                model.setY(point1.getYDouble() - 5);
                mainJFrame.getPaintService().paintSetAdd(model);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                mainJFrame.getPaintService().paintSetRemove(model);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                model.setX(point1.getXDouble() - 5);
                model.setY(point1.getYDouble() - 5);
                line.paint();
            }
        });
        mainJFrame.getPaintService().paintSetAdd(point1);
        mainJFrame.getListenService().mouseSetAdd(point1);
    }

    private void test0(MainJFrame mainJFrame) {
        ImageModel imageModel = new ImageModel(500, 500, 250, 200, "photo/头像.png");
        imageModel.addLister(new DraggableLister(imageModel));
        imageModel.addLister(new WheelLister(imageModel));
        mainJFrame.getPaintService().paintSetAdd(imageModel);
        mainJFrame.getListenService().mouseSetAdd(imageModel);

        ImageModel Gao = new ImageModel(0, 0, 100, 100, -1, "photo/高晓蕊.jpg");
        Gao.addLister(new DraggableLister(Gao));
        Gao.addLister(new KeyActionLister(Gao));
        mainJFrame.getPaintService().paintSetAdd(Gao);
        mainJFrame.getListenService().mouseSetAdd(Gao);
        mainJFrame.getListenService().keySetAdd(Gao);

        BoxAndTextModel boxAndTextModel = new BoxAndTextModel(0, 0, 200, 50, new Color(0, 0, 0, 0), Color.blue, 0.66, "点击开始");
        boxAndTextModel.addLister(new ButtonLister(boxAndTextModel));
        mainJFrame.getPaintService().paintSetAdd(boxAndTextModel);
        mainJFrame.getListenService().mouseSetAdd(boxAndTextModel);

//        Tool.after(3000, "", "addLister", "");

        BoxAndTextModel boxAndTextModel1 = new BoxAndTextModel(0, 50, 1960 * 4 / 50.0, 1080 * 4 / 50.0, new Color(225,225,225), Color.BLACK, 0.33, "原神");
        boxAndTextModel1.addLister(new WheelLister(boxAndTextModel1));
        mainJFrame.getPaintService().paintSetAdd(boxAndTextModel1);
        mainJFrame.getListenService().mouseSetAdd(boxAndTextModel1);

//        new Thread(() -> {
//            Random random = new Random();
//            while(true){
//                try {
//                    Thread.sleep(1);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//                imageModel.xAdd((random.nextInt(3)-1)*2);
//                imageModel.yAdd((random.nextInt(3)-1)*2);
//                imageModel.wAdd((random.nextInt(3)-1)*2);
//                imageModel.hAdd((random.nextInt(3)-1)*2);
//            }
//        }).start();
    }
}
