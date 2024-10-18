package org.mainFrame;

import org.Tool.Tool;
import org.mainFrame.Service.ListenService;
import org.mainFrame.Service.PaintService;

import javax.swing.*;
import java.awt.*;

public class MainJFrame extends JFrame {
    //初始化屏幕
    private static final Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    //双缓冲的图片
    private Image img;
    //双缓冲的图片的画笔
    private Graphics g;
    //渲染图片长宽和整个屏幕的比值
    public static final Double index = 2.5;
    //鼠标键盘监听service
    private ListenService listenService;
    //绘画线程
    private PaintService paintService;

    public MainJFrame() {
        //取消边框
        setUndecorated(true);
        //设置大小
        setSize(dimension);
        // 设置窗口居中
        setLocationRelativeTo(null);
        //设置关闭方法
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //全屏
        getGraphicsConfiguration().getDevice().setFullScreenWindow(this);
        //设置窗口可视
        setVisible(true);

        // 初始化
        init();
        // 启动所有线程
        initAndStart();
    }

    //画
    @Override
    public void paint(Graphics graphics) {
        //双缓冲
        if (img == null) {
            img = createImage((int)(index*dimension.width), (int)(index*dimension.height));
            g = img.getGraphics();
        }
        //绘制白板
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, (int)(index*dimension.width), (int)(index*dimension.height));
        //绘制主程序
        paintService.paint(g);
        //绘制双缓冲
        graphics.drawImage(img, 0, 0, dimension.width, dimension.height, null);
    }

    //绘画线程
    private void paintRun() {
        Tool.afterAndContinue(0,10,this::repaint);
//        while (true) {
//            //重绘
//            repaint();
//            try {
//                //延时10毫秒
//                Thread.sleep(10);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
    }

    //添加鼠标键盘监听
    private void addLister(ListenService listenService) {
        addMouseListener(listenService);
        addMouseMotionListener(listenService);
        addMouseWheelListener(listenService);
        addKeyListener(listenService);
    }

    //初始化线程服务
    private void init() {
        //初始化监听服务
        listenService = new ListenService();
        //初始化绘画线程服务
        paintService = new PaintService(index);
    }

    //启动所有服务
    private void initAndStart() {
        //添加监听事件
        addLister(listenService);
        //启动绘画线程
        new Thread(this::paintRun).start();
    }

    /**
     * 设置鼠标样式
     * Cursor.HAND_CURSOR 可以按下的状态
    */
    public void setMouse(int type){
        getContentPane().setCursor(Cursor.getPredefinedCursor(type));
    }

    public ListenService getListenService() {
        return listenService;
    }

    public PaintService getPaintService() {
        return paintService;
    }
}
