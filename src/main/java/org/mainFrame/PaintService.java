package org.mainFrame;

import org.model.Model;

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
    //渲染图片长宽和整个屏幕的比值
    public static Integer index;

    //添加model
    public static void PaintSetAdd(Model model) {
        synchronized (PaintService.class) {
            addPaintSet.add(model);
        }
    }

    //移除model
    public static void PaintSetRemove(Model model) {
        synchronized (ListerService.class) {
            removePaintSet.add(model);
        }
    }

    //画
    public static void paint(Graphics g) {
        if (!addPaintSet.isEmpty())
            synchronized (PaintService.class) {
                paintSet.addAll(addPaintSet);
                addPaintSet.clear();
            }
        if (!removePaintSet.isEmpty())
            synchronized (ListerService.class) {
                paintSet.removeAll(removePaintSet);
                removePaintSet.clear();
            }
        //倒序迭代器，进行倒序遍历，最后画的在最上面，优先级最高
        Iterator<Model> descendingIterator = paintSet.descendingIterator();
        Model model;
        while (descendingIterator.hasNext()) {
            model = descendingIterator.next();
            g.drawImage(model.getImage(), index * model.getX(), index * model.getY(), index * model.getW(), index * model.getH(), null);
        }
    }
}