package org.gameStart.gravity;

import org.Tool.Tool;
import org.mainFrame.MainJFrame;

import java.awt.*;
import java.util.*;
import java.util.function.Consumer;

public class QuadNode {
    //当前节点的坐标
    double x, y, l;
    //容错大小
    private static final double r = 0;
    //当前节点的数量
    HashSet<CollideModel> tSet;
    //当前节点最大容纳的大小
    private static final int size = 10;
    //子节点
    QuadNode[] quadChild;
    //父节点
    QuadNode quadParent;
    //当前节点的碰撞箱
    RectangleModel rectangleModel;

    private MainJFrame mainJFrame;

    public QuadNode(double x, double y, double l, MainJFrame mainJFrame) {
        this.x = x;
        this.y = y;
        this.l = l;
        this.tSet = new HashSet<>();
        this.mainJFrame = mainJFrame;
        Vector<Double> vector = new Vector<>();
        vector.add(0.0);
        vector.add(0.0);
        Random random = new Random();
//        this.rectangleModel = new RectangleModel(x - r, y - r, l + r * 2, l + r * 2, new Color(0, 255, 0), vector, vector, false, 0) {
        this.rectangleModel = new RectangleModel(x - r, y - r, l + r * 2, l + r * 2, new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)), vector, vector, false, 0) {
            @Override
            public void paint() {
                Graphics2D g = getNewGraphics2D();
                g.setColor(getColor());
//                g.drawRect((int) (0 + r), (int) (0 + r), (int) (getW() - 2 * r), (int) (getH() - 2 * r));
                g.drawRect(0, 0, getW(), getH());
                //绘制透明色
//                g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0));
//                g.fillOval(1, 1, getW()-1, getH()-1);
                Tool.writeString(g, size() + " ", "宋体", 1, getColor(), (int) r, (int) r, 50, 50);
            }
        };
        mainJFrame.getPaintService().paintSetAdd(rectangleModel);
    }


    //批量添加节点
    public void addAll(TreeSet<CollideModel> treeSet) {
        treeSet.forEach(this::add);
    }

    //添加节点
    public void add(CollideModel t) {
        //是叶子节点
        if (quadChild == null) {
            //节点存储的数据未满，添加
            if (tSet.size() < size) {
                tSet.add(t);
            }
            //满了，分裂成新的4个子节点
            else {
                quadChild = new QuadNode[4];
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 2; j++) {
                        quadChild[2 * i + j] = new QuadNode(x + j * l / 2, y + i * l / 2, l / 2, mainJFrame);
                        quadChild[2 * i + j].quadParent = this;
                    }
                }
                //向子节点中添加新的
                chileAdd(t);
                //向子节点中添加当前节点的
                tSet.forEach(this::chileAdd);
                //销毁当前节点存储的数组
                tSet.clear();
                tSet = null;
            }
        }
        //有子结点，当前节点不是叶子结点
        else {
            //向子节点中添加新的
            chileAdd(t);
        }
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

    HashSet<CollideModel> hashSet = new HashSet<>();

    //移除节点
    public void remove(CollideModel t) {
        //是叶子节点,移除
        if (quadChild == null) {
            tSet.remove(t);
            //判断是否父节点可以合并
            hashSetAdd(hashSet);
            System.out.println(hashSet.size() + ":" + size);
//            if (quadParent != null && quadParent.size() <= size) {
            if (quadParent != null && hashSet.size() <= size) {
                quadParent.tSet = new HashSet<>();
                for (int i = 0; i < 4; i++) {
                    hashSetAdd(quadParent.tSet);
                }
                quadParent.quadChild = null;
            }
            hashSet.clear();
        }
        //有子结点，当前节点不是叶子结点
        else {
            //让子节点移除
            chileRemove(t);
        }
    }

    //根据碰撞检测判断应该向哪个子节点中移除
    public void chileRemove(CollideModel t) {
        for (int i = 0; i < 4; i++) {
            if (!quadChild[i].rectangleModel.collisionDetection(t)) {
                quadChild[i].remove(t);
            }
        }
    }

    //遍历 函数式接口
    public void traverse(Consumer<HashSet<CollideModel>> operation) {
        //是叶子节点,运行
        if (quadChild == null) {
            operation.accept(tSet);
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
            return tSet.size();
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

    //当前节点的个数
    public int count() {
        //是叶子节点,直接返回
        if (quadChild == null) {
            return 1;
        }
        //不是叶子结点，算出子结点的总和
        else {
            int childSize = 0;
            for (QuadNode quadNode : quadChild) {
                childSize += quadNode.count();
            }
            return childSize;
        }
    }

    public void hashSetAdd(HashSet<CollideModel> hashSet) {
        //是叶子节点,直接返回
        if (quadChild == null) {
            hashSet.addAll(tSet);
        }
        //不是叶子结点，算出子结点的总和
        else {
            for (QuadNode quadNode : quadChild) {
                quadNode.hashSetAdd(hashSet);
            }
        }
    }

//        public void update(CollideModel t) {
//        //是叶子节点,进行更新
//        if (quadChild == null) {
//            if (tSet.contains(t)) {
//                remove(t);
//                rectangleModel.paint();
//            }
//            QuadNode quadNode = this;
//            while (!quadNode.rectangleModel.collisionDetection(t)) {
//                quadNode = quadNode.quadParent;
//                if (quadNode == null) return;
//            }
//            quadNode.add(t);
//        }
//        //不是叶子结点，让子节点更新
//        else {
//            for (int i = 0; i < 4; i++) {
//                if (quadChild != null && quadChild[i].rectangleModel.collisionDetection(t)) {
//                    quadChild[i].update(t);
//                }
//            }
//        }
//    }
    public void update(CollideModel t) {
        //是叶子节点,进行更新
        if (quadChild == null) {
            if (tSet.contains(t)) {
                remove(t);
                rectangleModel.paint();
            }
            QuadNode quadNode = this;
            while (quadNode.quadParent != null) {
                quadNode = quadNode.quadParent;
            }
            quadNode.add(t);
        }
        //不是叶子结点，让子节点更新
        else {
            for (int i = 0; i < 4; i++) {
                if (quadChild != null && quadChild[i].rectangleModel.collisionDetection(t)) {
                    quadChild[i].update(t);
                }
            }
        }
    }
}