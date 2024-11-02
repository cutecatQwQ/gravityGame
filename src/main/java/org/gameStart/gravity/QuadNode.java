package org.gameStart.gravity;

import java.awt.*;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.Vector;
import java.util.function.Consumer;

public class QuadNode {
    //当前节点的坐标
    double x, y, l;
    //容错大小
    private static final double r = 10;
    //当前节点的数量
    ArrayList<CollideModel> tList;
    //当前节点最大容纳的大小
    private static final int size = 10;
    //子节点
    QuadNode[] quadChild;
    //父节点
    QuadNode quadParent;
    //当前节点的碰撞箱
    RectangleModel rectangleModel;

    public QuadNode(double x, double y, double l) {
        this.x = x;
        this.y = y;
        this.l = l;
        this.tList = new ArrayList<>();
        Vector<Double> vector = new Vector<>();
        vector.add(0.0);
        vector.add(0.0);
        this.rectangleModel = new RectangleModel(x - r, y - r, l + r * 2, l + r * 2, new Color(0, 0, 0, 0), vector, vector, false, 0);
    }


    //批量添加节点
    public void addAll(TreeSet<CollideModel> treeSet) {
        treeSet.forEach(this::add);
    }

    //添加节点
    public QuadNode add(CollideModel t) {
        //是叶子节点
        if (quadChild == null) {
            //节点存储的数据未满，添加
            if (tList.size() < size) {
                tList.add(t);
            }
            //满了，分裂成新的4个子节点
            else {
                quadChild = new QuadNode[4];
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 2; j++) {
                        quadChild[2 * i + j] = new QuadNode(x + j * l / 2, y + i * l / 2, l / 2);
                        quadChild[2 * i + j].quadParent = this;
                    }
                }
                //向子节点中添加新的
                chileAdd(t);
                //向子节点中添加当前节点的
                tList.forEach(this::chileAdd);
                //销毁当前节点存储的数组
                tList.clear();
                tList = null;
            }
        }
        //有子结点，当前节点不是叶子结点
        else {
            //向子节点中添加新的
            chileAdd(t);
        }
        return this;
    }

    //根据碰撞检测判断应该向哪个子节点中添加
    public void chileAdd(CollideModel t) {
        for (int i = 0; i < 4; i++) {
            if (quadChild[i].rectangleModel.collisionDetection(t)) {
                quadChild[i].add(t);
            }
        }
    }

    //批量移除节点
    public void removeAll(TreeSet<CollideModel> treeSet) {
        treeSet.forEach(this::remove);
    }

    //移除节点
    public QuadNode remove(CollideModel t) {
        //是叶子节点,移除
        if (quadChild == null) {
            //节点存储的数据未满，添加
            tList.remove(t);
            //判断是否父节点可以合并
            if (quadParent.size() <= size) {
                quadParent.tList = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    quadParent.tList.addAll(quadParent.quadChild[i].tList);
                }
                quadParent.quadChild = null;
            }
        }
        //有子结点，当前节点不是叶子结点
        else {
            //让子节点移除
            chileRemove(t);
        }
        return this;
    }

    //根据碰撞检测判断应该向哪个子节点中移除
    public void chileRemove(CollideModel t) {
        for (int i = 0; i < 4; i++) {
            if (quadChild[i].rectangleModel.collisionDetection(t)) {
                quadChild[i].remove(t);
            }
        }
    }

    //遍历 函数式接口
    public void traverse(Consumer<ArrayList<CollideModel>> operation){
        //是叶子节点,运行
        if (quadChild == null) {
            operation.accept(tList);
        }
        //不是叶子结点，让叶子结点运行
        else {
            for (QuadNode quadNode : quadChild) {
                quadNode.traverse(operation);
            }
        }
    }

    //当前节点中包括的所有元素
    public int size() {
        //是叶子节点,直接返回
        if (quadChild == null) {
            return tList.size();
        }
        //不是叶子结点，算出子结点的总和
        else {
            int childSize = 0;
            for (QuadNode quadNode : quadChild) {
                childSize += quadNode.size();
            }
            return childSize;
        }
    }

    public void update(CollideModel t){
        //是叶子节点,进行更新
        if (quadChild == null) {
            tList.remove(t);
            QuadNode quadNode = this;
            while(!quadNode.rectangleModel.collisionDetection(t)){
                quadNode = quadNode.quadParent;
                if(quadNode == null) return;
            }
            quadNode.add(t);
        }
        //不是叶子结点，让子节点更新
        else {
            for (int i = 0; i < 4; i++) {
                if (quadChild[i].rectangleModel.collisionDetection(t)) {
                    quadChild[i].update(t);
                }
            }
        }
    }
}