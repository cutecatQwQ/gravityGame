package org.mainFrame;

import org.Tool.Tool;

import javax.swing.*;
import java.awt.*;

public class MainJFrame extends JFrame {
    //初始化屏幕
    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    //双缓冲的图片
    Image img;
    //双缓冲的图片的画笔
    Graphics g;
    //渲染图片长宽和整个屏幕的比值
    Integer index = 2;
    //鼠标键盘监听service
    ListerService listerService = new ListerService();

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

        PaintService.index = index;

        //添加监听事件
        addLister(this.listerService);
        // 启动所有线程
        start();
    }

    //添加鼠标键盘监听
    private void addLister(ListerService listerService) {
        addMouseListener(listerService);
        addMouseMotionListener(listerService);
        addMouseWheelListener(listerService);
        addKeyListener(listerService);
    }

    //画
    @Override
    public void paint(Graphics graphics) {
        //双缓冲
        if (img == null) {
            img = createImage(index*dimension.width, index*dimension.height);
            g = img.getGraphics();
        }
        //绘制白板
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, index*dimension.width, index*dimension.height);
        //绘制主程序
        PaintService.paint(g);
        //绘制双缓冲
        graphics.drawImage(img, 0, 0, dimension.width, dimension.height, null);
    }

    //绘画线程
    private void paintRun() {
        while (true) {
            //重绘
            repaint();
            try {
                //延时10毫秒
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //启动所有线程
    private void start() {
        //启动绘画线程
        new Thread(this::paintRun).start();
    }
}
