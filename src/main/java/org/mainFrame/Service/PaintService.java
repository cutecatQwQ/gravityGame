package org.mainFrame.Service;

import org.mainFrame.model.Model;

import java.awt.*;
import java.util.Iterator;
import java.util.TreeSet;

public class PaintService {
    //所画的集合
    private static final TreeSet<Model> paintSet = new TreeSet<>();
    //待添加集合
    private static final TreeSet<Model> addPaintSet = new TreeSet<>();
    //待移除集合
    private static final TreeSet<Model> removePaintSet = new TreeSet<>();
    //添加锁
    private static final Object addLock = new Object();
    //移除锁
    private static final Object removeLock = new Object();
    //渲染图片长宽和整个屏幕的比值
    public static Double index;

    //添加model
    public static void paintSetAdd(Model model) {
        synchronized (addLock) {
            addPaintSet.add(model);
        }
    }

    //移除model
    public static void paintSetRemove(Model model) {
        synchronized (removeLock) {
            removePaintSet.add(model);
        }
    }

    //画
    public static void paint(Graphics g) {
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
            g.drawImage(model.getImage(), (int)(index * model.getXDouble()), (int)(index * model.getYDouble()), (int)(index * model.getWDouble()), (int)(index * model.getHDouble()), null);
        }
    }
}