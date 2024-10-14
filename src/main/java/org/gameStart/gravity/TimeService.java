package org.gameStart.gravity;

import org.Tool.Tool;

import java.util.TreeSet;

public class TimeService {
    //需要时间的集合
    private static final TreeSet<CollideModel> timeSet = new TreeSet<>();
    //待添加集合
    private static final TreeSet<CollideModel> addTimeSet = new TreeSet<>();
    //待移除集合
    private static final TreeSet<CollideModel> removeTimeSet = new TreeSet<>();
    //添加锁
    private static final Object addLock = new Object();
    //移除锁
    private static final Object removeLock = new Object();
    private static int time = 0;

    private static Integer speedOfTime = 1;

    //添加model
    public static void timeSetAdd(CollideModel collideModel) {
        synchronized (addLock) {
            addTimeSet.add(collideModel);
        }
    }

    //移除model
    public static void timeSetRemove(CollideModel collideModel) {
        synchronized (removeLock) {
            removeTimeSet.add(collideModel);
        }
    }

    public static void timePasses() {
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
        Tool.debug(++time + "");
    }

    public static int getTime() {
        return time;
    }

    public static void setTime(int time) {
        TimeService.time = time;
    }

    public static int getSpeedOfTime() {
        return speedOfTime;
    }

    public static void setSpeedOfTime(int speedOfTime) {
        TimeService.speedOfTime = speedOfTime;
    }
}
