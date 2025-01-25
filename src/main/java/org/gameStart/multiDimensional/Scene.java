package org.gameStart.multiDimensional;

import org.gameStart.multiDimensional.objects.Object;

import java.util.TreeSet;

public class Scene {
    //所画的集合
    private final TreeSet<Object> objectSet = new TreeSet<>();
    //待添加集合
    private final TreeSet<Object> addObjectSet = new TreeSet<>();
    //待移除集合
    private final TreeSet<Object> removeObjectSet = new TreeSet<>();
    //添加锁
    private final java.lang.Object addLock = new java.lang.Object();
    //移除锁
    private final java.lang.Object removeLock = new java.lang.Object();

    Window window;

    public Scene() {
    }

    //添加model
    public void objectSetAdd(Object object) {
        synchronized (addLock) {
            addObjectSet.add(object);
        }
    }

    //移除model
    public void objectSetRemove(Object object) {
        synchronized (removeLock) {
            removeObjectSet.add(object);
        }
    }

    //画
    public synchronized void rendering(Window window) {
        if (!addObjectSet.isEmpty())
            synchronized (addLock) {
                objectSet.addAll(addObjectSet);
                addObjectSet.clear();
            }
        if (!removeObjectSet.isEmpty())
            synchronized (removeLock) {
                objectSet.removeAll(removeObjectSet);
                removeObjectSet.clear();
            }

        //画
        window.paint(objectSet);

    }
}
