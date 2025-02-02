package gameEngine.gameStart.gravity;

import gameEngine.Tool.Tool;
import gameEngine.mainFrame.MainJFrame;

import java.util.Random;
import java.util.TreeSet;

public class TimeService {
    //需要时间的集合
    private final TreeSet<CollideModel> timeSet = new TreeSet<>();
    //待添加集合
    private final TreeSet<CollideModel> addTimeSet = new TreeSet<>();
    //待移除集合
    private final TreeSet<CollideModel> removeTimeSet = new TreeSet<>();
    //添加锁
    private final Object addLock = new Object();
    //移除锁
    private final Object removeLock = new Object();
    //当前时间
    private int time = 0;

    //运行时间倍速
    private Integer speedOfTime;

    private MainJFrame mainJFrame;

    //四叉树
    private QuadNode quadNode;

    public TimeService(Integer speedOfTime, MainJFrame mainJFrame) {
        this.speedOfTime = speedOfTime;
        this.mainJFrame = mainJFrame;
        this.quadNode = new QuadNode(0, 0, MainJFrame.dimension.width,mainJFrame);
    }

    //添加model
    public void timeSetAdd(CollideModel collideModel) {
        synchronized (addLock) {
            addTimeSet.add(collideModel);
        }
    }

    //移除model
    public void timeSetRemove(CollideModel collideModel) {
        synchronized (removeLock) {
            removeTimeSet.add(collideModel);
        }
    }

    public void timePasses() {
        if (!addTimeSet.isEmpty())
            synchronized (addLock) {
                timeSet.addAll(addTimeSet);
                quadNode.addAll(addTimeSet);
                addTimeSet.clear();
            }
        if (!removeTimeSet.isEmpty())
            synchronized (removeLock) {
                timeSet.removeAll(removeTimeSet);
                quadNode.removeAll(removeTimeSet);
                removeTimeSet.clear();
            }
        for (CollideModel leaf : timeSet) {
            leaf.timeAdd(speedOfTime);
        }

        //碰撞检测，每次时间流逝的时候执行一次
        collisionDetection();

        Tool.debug(++time + "", mainJFrame);
    }

    Random random = new Random();

    //碰撞检测
    private void collisionDetection() {
        try {
//            System.out.print(quadNode.size()+":");
//            AtomicInteger a = new AtomicInteger();
//            quadNode.traverseDFS(quadNode-> a.getAndIncrement());
//            System.out.print(a+":");
//            a.set(0);
//            quadNode.traverseDFS(quadNode-> a.addAndGet(quadNode.tList.size()));
//            System.out.println(a);
            timeSet.forEach(e -> quadNode.update(e));
            quadNode.traverseDFS(quadNode -> {
//                quadNode.rectangleModel.paint();
                // 使用嵌套循环对ArrayList中的元素两两使用一次
                for (int i = 0; i < quadNode.tList.size(); i++) {
                    for (int j = i + 1; j < quadNode.tList.size(); j++) {
                        //碰撞检测
                        if (quadNode.tList.get(i).collisionDetection(quadNode.tList.get(j))) {
                            quadNode.tList.get(i).elasticCollision(quadNode.tList.get(j), 1);
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getSpeedOfTime() {
        return speedOfTime;
    }

    public void setSpeedOfTime(int speedOfTime) {
        this.speedOfTime = speedOfTime;
    }
}
