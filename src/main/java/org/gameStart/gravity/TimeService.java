package org.gameStart.gravity;

import org.Tool.Tool;
import org.mainFrame.MainJFrame;

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

    public TimeService(Integer speedOfTime, MainJFrame mainJFrame) {
        this.speedOfTime = speedOfTime;
        this.mainJFrame = mainJFrame;
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
                addTimeSet.clear();
            }
        if (!removeTimeSet.isEmpty())
            synchronized (removeLock) {
                timeSet.removeAll(removeTimeSet);
                removeTimeSet.clear();
            }
        for (CollideModel leaf : timeSet) {
            leaf.timeAdd(speedOfTime);
        }
        Tool.debug(++time + "",mainJFrame);
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
