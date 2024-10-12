package org.model;

import org.mouseAndKeyLister.MouseAndKeyLister;

import java.awt.*;
import java.util.ArrayList;
import java.util.TreeSet;


public abstract class Model implements Comparable<Model> {
    //宽,高
    private int w,h;
    //左上角坐标
    private int x,y;
    //优先级 越小越靠前
    private int priority;
    //图片
    private Image image;
    //鼠标键盘监听
    public final ArrayList<MouseAndKeyLister> mouseAndKeyLister = new ArrayList<>();

    //添加鼠标键盘监听事件
    public Model add(MouseAndKeyLister mouseAndKeyLister){
        this.mouseAndKeyLister.add(mouseAndKeyLister);
        return this;
    }
    //移除鼠标键盘监听事件
    public Model remove(MouseAndKeyLister mouseAndKeyLister){
        this.mouseAndKeyLister.remove(mouseAndKeyLister);
        return this;
    }
    //比较方法,优先级数小的在前面不被遮挡
    @Override
    public int compareTo(Model o){
        if(this.priority!=o.priority) return this.priority-o.priority;
        return this.toString().compareTo(o.toString());
    }
    public void paint(){}
    //位置++方法
    public void xAdd(int ax){x += ax;}
    public void yAdd(int ay){y += ay;}
    public void wAdd(int aw){w += aw;}
    public void hAdd(int ah){h += ah;}

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
