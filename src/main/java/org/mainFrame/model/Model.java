package org.mainFrame.model;

import org.mainFrame.MainJFrame;
import org.mainFrame.mouseAndKeyLister.MouseAndKeyLister;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


public abstract class Model implements Comparable<Model> {
    //宽,高
    private Double w, h;
    //左上角坐标
    private Double x, y;
    //优先级 越小越靠前
    private int priority;
    private Color color;

    //图片
    private Image image;
    //鼠标键盘监听
    public final ArrayList<MouseAndKeyLister> mouseAndKeyLister = new ArrayList<>();

    //渲染图片长宽和整个屏幕的比值,每个model都有单独的index让图片渲染的更清晰，默认是整个系统的比值
    private Double index = MainJFrame.index;

    //添加鼠标键盘监听事件
    public Model addLister(MouseAndKeyLister mouseAndKeyLister) {
        this.mouseAndKeyLister.add(mouseAndKeyLister);
        return this;
    }

    //移除鼠标键盘监听事件
    public Model removeLister(MouseAndKeyLister mouseAndKeyLister) {
        this.mouseAndKeyLister.remove(mouseAndKeyLister);
        return this;
    }

    //比较方法,优先级数小的在前面不被遮挡
    @Override
    public int compareTo(Model o) {
        if (this.priority != o.priority) return this.priority - o.priority;
        return this.toString().compareTo(o.toString());
    }

    public void paint() {
    }

    //返回图片画笔，如图片不存在创建一个
    public Graphics2D getNewGraphics2D() {
        setImage(new BufferedImage((int) (index * getWDouble()), (int) (index * getHDouble()), BufferedImage.TYPE_INT_ARGB));
        Graphics2D g = ((BufferedImage) getImage()).createGraphics();
        // 设置抗锯齿以提高绘制质量
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.scale(index, index);
        return g;
    }

    public Graphics2D getGraphics2D() {
        if (getImage() == null) {
            setImage(new BufferedImage((int) (index * getWDouble()), (int) (index * getHDouble()), BufferedImage.TYPE_INT_ARGB));
        }
        Graphics2D g = ((BufferedImage) getImage()).createGraphics();
        // 设置抗锯齿以提高绘制质量
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.scale(index, index);
        return g;
    }

    //位置++方法
    public void xAdd(double ax) {
        x += ax;
    }

    public void yAdd(double ay) {
        y += ay;
    }

    public void wAdd(double aw) {
        w += aw;
    }

    public void hAdd(double ah) {
        h += ah;
    }

    //获取精确值方法
    public double getXDouble() {
        return x;
    }

    public double getYDouble() {
        return y;
    }

    public double getWDouble() {
        return w;
    }

    public double getHDouble() {
        return h;
    }

    public int getW() {
        return (int) Math.round(w);
    }

    public void setW(double w) {
        this.w = w;
    }

    public int getH() {
        return (int) Math.round(h);
    }

    public void setH(double h) {
        this.h = h;
    }

    public int getX() {
        return (int) Math.round(x);
    }

    public void setX(double x) {
        this.x = x;
    }

    public int getY() {
        return (int) Math.round(y);
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Double getIndex() {
        return index;
    }

    public void setIndex(Double index) {
        this.index = index;
    }
}
