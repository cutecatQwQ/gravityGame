package org.gameStart.multiDimensional;

import org.mainFrame.MainJFrame;
import org.mainFrame.model.Model;

import java.util.TreeSet;

public class Rendering3DService {
    //所画的集合
    private final TreeSet<Object> rendering3DSet = new TreeSet<>();
    //待添加集合
    private final TreeSet<Object> addRendering3DSet = new TreeSet<>();
    //待移除集合
    private final TreeSet<Object> removeRendering3DSet = new TreeSet<>();
    //添加锁
    private final java.lang.Object addLock = new java.lang.Object();
    //移除锁
    private final java.lang.Object removeLock = new java.lang.Object();

    private Camera3D camera3D;

    private MainJFrame mainJFrame;
    public Rendering3DService(MainJFrame mainJFrame, Camera3D camera3D) {
        this.camera3D = camera3D;
        this.mainJFrame = mainJFrame;
    }

    //添加model
    public void rendering3DSetAdd(Object object) {
        synchronized (addLock) {
            addRendering3DSet.add(object);
        }
    }

    //移除model
    public void rendering3DSetRemove(Object object) {
        synchronized (removeLock) {
            removeRendering3DSet.add(object);
        }
    }

    //画
    public synchronized void rendering3D() {
        if (!addRendering3DSet.isEmpty())
            synchronized (addLock) {
                rendering3DSet.addAll(addRendering3DSet);
                addRendering3DSet.forEach(rendering3D->mainJFrame.getPaintService().paintSetAdd((Model) rendering3D));
                addRendering3DSet.clear();
            }
        if (!removeRendering3DSet.isEmpty())
            synchronized (removeLock) {
                rendering3DSet.removeAll(removeRendering3DSet);
                removeRendering3DSet.forEach(rendering3D->mainJFrame.getPaintService().paintSetRemove((Model) rendering3D));
                removeRendering3DSet.clear();
            }

        //对每个点进行初始化
        rendering3DSet.forEach(rendering3D-> rendering3D.traverse(camera3D::init));

    }
}
