package gameEngine.mainFrame.Service;

import gameEngine.mainFrame.model.Model;

import java.awt.*;
import java.util.Iterator;
import java.util.TreeSet;

public class PaintService {
    //所画的集合
    private final TreeSet<Model> paintSet = new TreeSet<>();
    //待添加集合
    private final TreeSet<Model> addPaintSet = new TreeSet<>();
    //待移除集合
    private final TreeSet<Model> removePaintSet = new TreeSet<>();
    //添加锁
    private final Object addLock = new Object();
    //移除锁
    private final Object removeLock = new Object();
    //渲染图片长宽和整个屏幕的比值
    private  Double index;

    public PaintService(Double index) {
        setIndex(index);
    }

    public void setIndex(Double index) {
        this.index = index;
    }

    //添加model
    public void paintSetAdd(Model model) {
        synchronized (addLock) {
            addPaintSet.add(model);
        }
    }

    //移除model
    public void paintSetRemove(Model model) {
        synchronized (removeLock) {
            removePaintSet.add(model);
        }
    }

    //画
    public void paint(Graphics g) {
        if (!addPaintSet.isEmpty())
            synchronized (addLock) {
                paintSet.addAll(addPaintSet);
                addPaintSet.clear();
            }
        if (!removePaintSet.isEmpty())
            synchronized (removeLock) {
                paintSet.removeAll(removePaintSet);
                removePaintSet.clear();
            }
        //倒序迭代器，进行倒序遍历，最后画的在最上面，优先级最高
        Iterator<Model> descendingIterator = paintSet.descendingIterator();
        Model model;
        while (descendingIterator.hasNext()) {
            model = descendingIterator.next();
            //下面注释是坐标变换的尝试，问题有鼠标位置需要改变    尝试设置整个坐标系的变换，可以通过拖动看到超出屏幕的事物（未完成）
            g.drawImage(model.getImage(),
                    (int)(index * model.getXDouble()),
                    (int)(/*MainJFrame.dimension.getHeight()*MainJFrame.index-*/index * model.getYDouble()),
                    (int)(index * model.getWDouble()),
                    (int)(/*-*/index * model.getHDouble()), null);
        }
    }
}