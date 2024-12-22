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
    //当前节点最大容纳的大小
    private static final int size = 1000;
    //小叶子节点大小
    private static final int minL = 20;
    //当前节点的数量
    ArrayList<CollideModel> tList;
    //子节点
    QuadNode[] quadChild;
    //父节点
    QuadNode quadParent;
    //邻居
    QuadNode[] quadNeighbor;
    //当前节点的碰撞箱
    RectangleModel rectangleModel;

    MainJFrame mainJFrame;

    HashSet<CollideModel> hashSet;

    static HashSet<QuadNode> trashCan;

    {
        trashCan = new HashSet<>();
//        Tool.afterAndContinue(1000,1000,()->trashCan = new HashSet<>());
    }

    public static QuadNode getQuadParent(double x, double y, double l, MainJFrame mainJFrame) {
        for (QuadNode quadNode : trashCan) {
            if (quadNode.x == x && quadNode.y == y && quadNode.l == l) {
                return quadNode;
            }
        }
        return new QuadNode(x, y, l, mainJFrame);
    }

    public QuadNode(double x, double y, double l, MainJFrame mainJFrame) {
        this.x = x;
        this.y = y;
        this.l = l;
        this.tList = new ArrayList<>();
        this.quadNeighbor = new QuadNode[4]; //上右下左0123
        Vector<Double> vector = new Vector<>();
        vector.add(0.0);
        vector.add(0.0);

        this.mainJFrame = mainJFrame;
        this.hashSet = new HashSet<>();
        Random random = new Random();
        this.rectangleModel = new RectangleModel(x - r, y - r, l + r * 2, l + r * 2, new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255), 50), vector, vector, false, 0) {
            @Override
            public void paint() {
//                Graphics2D g = getNewGraphics2D();
//                g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0));
//                g.fillRect(getX(), getY(), getW(), getH());
//                g.setComposite(AlphaComposite.SrcOver);
//                g.setColor(getColor());
//                g.fillRect(getX(), getY(), getW(), getH());
//                g.setColor(Color.BLACK);
//                g.drawRect((int) (getX() + r) + 2, (int) (getY() + r) + 2, (int) (getW() - 2 * r) - 4, (int) (getH() - 2 * r) - 4);
//                Tool.writeString((Graphics) g, "" + size(), "宋体", 0.66, Color.BLACK, (int) (getXDouble()/2-200), (int) (getYDouble()/2-100), 200, 100);
            }
        };
        mainJFrame.getPaintService().paintSetAdd(rectangleModel);
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
            if (tList.size() < size || l <= minL) {
                if (!tList.contains(t)) tList.add(t);
            }
            //满了，分裂成新的4个子节点
            else {
                quadChild = new QuadNode[4];
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 2; j++) {
                        quadChild[2 * i + j] = getQuadParent(x + j * l / 2, y + i * l / 2, l / 2, mainJFrame);
                        quadChild[2 * i + j].quadParent = this;
                    }
                }

                //左上
                quadChild[0].quadNeighbor[0] = quadNeighbor[0];
                quadChild[0].quadNeighbor[1] = quadChild[1];
                quadChild[0].quadNeighbor[2] = quadChild[2];
                quadChild[0].quadNeighbor[3] = quadNeighbor[3];
                //右上
                quadChild[1].quadNeighbor[0] = quadNeighbor[0];
                quadChild[1].quadNeighbor[1] = quadNeighbor[1];
                quadChild[1].quadNeighbor[2] = quadChild[3];
                quadChild[1].quadNeighbor[3] = quadChild[0];
                //左下
                quadChild[2].quadNeighbor[0] = quadChild[0];
                quadChild[2].quadNeighbor[1] = quadChild[3];
                quadChild[2].quadNeighbor[2] = quadNeighbor[2];
                quadChild[2].quadNeighbor[3] = quadNeighbor[3];
                //右下
                quadChild[3].quadNeighbor[0] = quadChild[1];
                quadChild[3].quadNeighbor[1] = quadNeighbor[1];
                quadChild[3].quadNeighbor[2] = quadNeighbor[2];
                quadChild[3].quadNeighbor[3] = quadChild[2];

                //向子节点中添加新的
                childAdd(t);
                //向子节点中添加当前节点的
                tList.forEach(this::childAdd);
                //销毁当前节点存储的数组
                tList.clear();
            }
        }
        //有子结点，当前节点不是叶子结点
        else {
            //向子节点中添加新的
            childAdd(t);
        }
        return this;
    }

    //根据碰撞检测判断应该向哪个子节点中添加
    public void childAdd(CollideModel t) {
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
            //节点存储的数据未满，移除
            tList.remove(t);
            //判断是否父节点可以合并
            QuadNode quadNode = this;
            while (quadNode.quadParent != null && quadNode.quadParent.size() <= size) {
                for (int i = 0; i < 4; i++) {
                    hashSet.addAll(quadNode.quadParent.quadChild[i].tList);
                    trashCan.add(quadNode.quadParent.quadChild[i]);
                    mainJFrame.getPaintService().paintSetRemove(quadNode.quadParent.quadChild[i].rectangleModel);
                }
                quadNode.quadParent.tList.addAll(hashSet);
                hashSet.clear();
                quadNode.quadParent.quadChild = null;
                quadNode = quadNode.quadParent;
            }
        }
        //有子结点，当前节点不是叶子结点
        else {
            //让子节点移除
            childRemove(t);
        }
        return this;
    }

    //根据碰撞检测判断应该向哪个子节点中移除
    public void childRemove(CollideModel t) {
        for (int i = 0; i < 4; i++) {
            if (quadChild[i].rectangleModel.collisionDetection(t)) {
                quadChild[i].remove(t);
                if (quadChild == null) break;
            }
        }
    }

    //深度优先遍历 函数式接口
    public void traverseDFS(Consumer<QuadNode> operation) {
        //是叶子节点,运行
        if (quadChild == null) {
            operation.accept(this);
        }
        //不是叶子结点，让叶子结点运行
        else {
            for (QuadNode quadNode : quadChild) {
                quadNode.traverseDFS(operation);
                if (quadChild == null) {
                    break;
                }
            }
        }
    }

    //广度优先遍历 函数式接口
    public void traverseBFS(Consumer<QuadNode> operation) {
        Queue<QuadNode> queue = new ArrayDeque<>();
        queue.add(this);
        QuadNode quadNode;
        while (!queue.isEmpty()) {
            quadNode = queue.poll();
            //是叶子节点,运行
            if (quadNode.quadChild == null) {
                operation.accept(quadNode);
            }
            //不是叶子结点，把叶子结点添加到队列
            else {
                Collections.addAll(queue, quadNode.quadChild);
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
            traverseDFS(quadNode -> {
                hashSet.addAll(quadNode.tList);
            });
            int size = hashSet.size();
            hashSet.clear();
            return size;
        }
    }

    public void update(CollideModel t) {
        //是叶子节点,进行更新
//        if (quadChild == null) {
//            if (!rectangleModel.collisionDetection(t)) remove(t);
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
//                if (quadChild[i].tList.contains(t)) {
//                    quadChild[i].update(t);
//                    if (quadChild == null) break;
//                }
//            }
//        }
        traverseDFS(quadNode -> {
            if (quadNode.tList.contains(t)) {
                if (!quadNode.rectangleModel.collisionDetection(t)) remove(t);
                for (int i = 0; i < quadNode.quadNeighbor.length; i++) {
                    if (quadNode.quadNeighbor[i] != null && quadNode.quadNeighbor[i].rectangleModel.collisionDetection(t)) {
                        quadNode.quadNeighbor[i].add(t);
                    }
                }
            }
        });
//        remove(t);
//        add(t);

    }

    @Override
    public String toString() {
        return "QuadNode{" +
                "x=" + x +
                ", y=" + y +
                ", l=" + l +
                ", tList=" + tList +
                ", quadChild=" + Arrays.toString(quadChild) +
                '}';
    }
}