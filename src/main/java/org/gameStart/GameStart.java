package org.gameStart;

import org.Tool.Tool;
import org.gameStart.cityWar.Buttons;
import org.gameStart.cityWar.Country;
import org.gameStart.cityWar.Population;
import org.gameStart.gravity.Time;
import org.gameStart.testOne.Graph;
import org.gameStart.testOne.Line;
import org.mainFrame.ListenService;
import org.mainFrame.MainJFrame;
import org.mainFrame.PaintService;
import org.model.BoxAndTextModel;
import org.model.ImageModel;
import org.model.Model;
import org.mouseAndKeyLister.*;
import org.gameStart.testOne.Point;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

public class GameStart {
    public GameStart(MainJFrame mainJFrame) {
        GameInit.init(mainJFrame);

//        test0(mainJFrame);
//        test1(mainJFrame);
//        test2(mainJFrame);
//        test3(mainJFrame);
//        test4(mainJFrame);
//        test5(mainJFrame);
        test6(mainJFrame);
    }

    private void test6(MainJFrame mainJFrame) {
        Time.timeStart();
    }

    private void test5(MainJFrame mainJFrame) {
        Country country = new Country();

    }

    private void test4(MainJFrame mainJFrame) {
        Random random = new Random();
        final BoxAndTextModel[] boxAndTextModel = new BoxAndTextModel[1];
        Tool.afterAndContinue(0, 1000, () -> {
            boxAndTextModel[0] = new BoxAndTextModel(100, 100, 1400, 500, new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)), new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)), 0.7, "新年快乐");
            PaintService.PaintSetAdd(boxAndTextModel[0]);
        });
        Tool.afterAndContinue(999, 1000, () -> {
            PaintService.PaintSetRemove(boxAndTextModel[0]);
        });
    }

    public static Point background = new Point(0, 0, 20, Color.red, 0) {
        @Override
        public void paint() {
            super.paint();
            Graphics2D g = ((BufferedImage) getImage()).createGraphics();
            // 设置抗锯齿以提高绘制质量
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            //绘制透明色
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0));
            g.fillOval(4, 4, 32, 32);
        }
    };

    private void test3(MainJFrame mainJFrame) {
        Population population = new Population(100, 100, 10, 100, Color.black) {
            @Override
            public void numberChange() {
                super.numberChange();
                number--;
            }
        };
        Buttons buttons = new Buttons(0, 0, 50, 25, Color.white, Color.black, 0.7);
        Random random = new Random();
        buttons.add("添加", () -> {
            buttons.add(random.nextInt(100) * 10 + "", () -> {
            });
        });
        buttons.add("1", () -> {
            buttons.remove("添加");
        });
        buttons.add("2", () -> {
            buttons.add("添加", () -> {
                buttons.add(random.nextInt(100) * 10 + "", () -> {
                });
            });
        });
        buttons.add("关闭", () -> {
            buttons.setBoolean(false);
        });
        buttons.setBoolean(false);
        population.point.addLister(new MouseAndKeyLister() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    buttons.setXAndY(e.getX(), e.getY());
                    buttons.setBoolean(true);
                }

            }
        });
        Model model = new ImageModel(0, 0, mainJFrame.getWidth(), mainJFrame.getHeight(), 10, "");
        model.addLister(new MouseAndKeyLister() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() != MouseEvent.BUTTON3) buttons.setBoolean(false);
                else {
                    buttons.setXAndY(e.getX(), e.getY());
                    buttons.setBoolean(true);
                }
            }
        });
        ListenService.mouseSetAdd(model);
    }

    private void test2(MainJFrame mainJFrame) {
        Random random = new Random();
        Graph graph = new Graph();
        Point[] points = new Point[16];
        for (int i = 0; i < points.length; i++) {
//            points[i] = new Point(10, 10, 5,Color.red,0);
            points[i] = new Point(random.nextInt(1500) + 10, random.nextInt(800) + 10, 10, Color.red, 0);
            graph.addPoint(points[i]);
        }

        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points.length; j++) {
                if (random.nextBoolean() && random.nextBoolean() && random.nextBoolean() && random.nextBoolean())
                    graph.addLine(points[i], points[j]);
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
                Graphics2D g = ((BufferedImage) getImage()).createGraphics();
                // 设置抗锯齿以提高绘制质量
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
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
                PaintService.PaintSetAdd(model);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                PaintService.PaintSetRemove(model);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                model.setX(point.getXDouble() - 5);
                model.setY(point.getYDouble() - 5);
                line.paint();
            }
        });
        PaintService.PaintSetAdd(point);
        PaintService.PaintSetAdd(line);
        ListenService.mouseSetAdd(point);

        point1.addLister(new DraggableLister(point1));
        point1.addLister(new MouseAndKeyLister() {
            @Override
            public void mouseEntered(MouseEvent e) {
                model.setX(point1.getXDouble() - 5);
                model.setY(point1.getYDouble() - 5);
                PaintService.PaintSetAdd(model);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                PaintService.PaintSetRemove(model);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                model.setX(point1.getXDouble() - 5);
                model.setY(point1.getYDouble() - 5);
                line.paint();
            }
        });
        PaintService.PaintSetAdd(point1);
        ListenService.mouseSetAdd(point1);
    }

    private void test0(MainJFrame mainJFrame) {
        ImageModel imageModel = new ImageModel(500, 500, 250, 200, "photo/头像.png");
        imageModel.addLister(new DraggableLister(imageModel));
        PaintService.PaintSetAdd(imageModel);
        ListenService.mouseSetAdd(imageModel);

        ImageModel Gao = new ImageModel(0, 0, 100, 100, -1, "photo/高晓蕊.jpg");
        Gao.addLister(new DraggableLister(Gao));
        Gao.addLister(new KeyActionLister(Gao));
        PaintService.PaintSetAdd(Gao);
        ListenService.mouseSetAdd(Gao);
        ListenService.keySetAdd(Gao);

        BoxAndTextModel boxAndTextModel = new BoxAndTextModel(0, 0, 200, 50, new Color(0, 0, 0, 0), Color.blue, 0.66, "点击开始");
        boxAndTextModel.addLister(new ButtonLister(boxAndTextModel));
        PaintService.PaintSetAdd(boxAndTextModel);
        ListenService.mouseSetAdd(boxAndTextModel);

//        Tool.after(3000, "", "addLister", "");

        BoxAndTextModel boxAndTextModel1 = new BoxAndTextModel(0, 50, 1960 * 4 / 50, 1080 * 4 / 50, Color.WHITE, Color.BLACK, 0.33, "原神");
        boxAndTextModel1.addLister(new WheelLister(boxAndTextModel1));
        PaintService.PaintSetAdd(boxAndTextModel1);
        ListenService.mouseSetAdd(boxAndTextModel1);

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
