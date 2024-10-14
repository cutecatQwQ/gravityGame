package org.mainFrame.Service;

import org.Tool.Tool;
import org.mainFrame.model.Model;

import java.awt.event.*;
import java.util.TreeSet;

public class ListenService implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {
    //当前焦点所在的model
    private static Model focus;
    //鼠标所在的model
    private static Model hover;

    //鼠标事件的model集合
    private static final TreeSet<Model> mouse = new TreeSet<>();
    //待添加集合
    private static final TreeSet<Model> addMouseSet = new TreeSet<>();
    //待移除集合
    private static final TreeSet<Model> removeMouseSet = new TreeSet<>();
    //鼠标集合添加锁
    private static final Object mouseAddLock = new Object();
    //鼠标集合移除锁
    private static final Object mouseRemoveLock = new Object();

    //键盘事件的model集合
    private static final TreeSet<Model> key = new TreeSet<>();
    //待添加集合
    private static final TreeSet<Model> addKeySet = new TreeSet<>();
    //待移除集合
    private static final TreeSet<Model> removeKeySet = new TreeSet<>();
    //键盘集合添加锁
    private static final Object keyAddLock = new Object();
    //键盘集合移除锁
    private static final Object keyRemoveLock = new Object();

    //添加mouse model
    public static void mouseSetAdd(Model model) {
        synchronized (mouseAddLock) {
            addMouseSet.add(model);
        }
    }
    //移除mouse model
    public static void mouseSetRemove(Model model) {
        synchronized (mouseRemoveLock) {
            removeMouseSet.add(model);
        }
    }

    //添加key model
    public static void keySetAdd(Model model) {
        synchronized (keyAddLock) {
            addKeySet.add(model);
        }
    }
    //移除key model
    public static void keySetRemove(Model model) {
        synchronized (keyRemoveLock) {
            removeKeySet.add(model);
        }
    }

    //鼠标集合处理
    private static void mouseAddAndRemove(){
        if (!addMouseSet.isEmpty())
            synchronized (mouseAddLock) {
                mouse.addAll(addMouseSet);
                addMouseSet.clear();
            }
        if (!removeMouseSet.isEmpty())
            synchronized (mouseRemoveLock) {
                mouse.removeAll(removeMouseSet);
                removeMouseSet.clear();
            }
    }

    //鼠标点击事件
    @Override
    public void mouseClicked(MouseEvent e) {
        if (hover != null) hover.mouseAndKeyLister.forEach(m -> m.mouseClicked(e));
    }

    //鼠标按下事件
    @Override
    public void mousePressed(MouseEvent e) {
        focus = hover;
        if (focus != null) focus.mouseAndKeyLister.forEach(m -> m.mousePressed(e));
    }

    //鼠标释放事件
    @Override
    public void mouseReleased(MouseEvent e) {
        if (focus != null) focus.mouseAndKeyLister.forEach(m -> m.mouseReleased(e));
        focus = null;
    }

    //监听的是整个窗口，所以鼠标进入进出暂时没有作用
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    //鼠标拖拽事件
    @Override
    public void mouseDragged(MouseEvent e) {
        if (focus != null) focus.mouseAndKeyLister.forEach(m -> m.mouseDragged(e));
    }

    //鼠标移动事件
    @Override
    public void mouseMoved(MouseEvent e) {
        mouseAddAndRemove();
        for (Model leaf : mouse) {
            if (Tool.inBox(leaf.getXDouble(), leaf.getYDouble(), leaf.getWDouble(), leaf.getHDouble(), e.getX(), e.getY())) {
                leaf.mouseAndKeyLister.forEach(m -> m.mouseMoved(e));
                //如果移动时当前所处的位置不等于新的model，则判定当前鼠标所在model发生改变，产生鼠标进入进出事件
                if (hover != leaf) {
                    leaf.mouseAndKeyLister.forEach(m -> m.mouseEntered(e));
                    if (hover != null) hover.mouseAndKeyLister.forEach(m -> m.mouseExited(e));
                    hover = leaf;
                }
                return;
            }
        }
        //运行到此行代码时说明鼠标没有在任何一个model里，则是从一个model来到了空地，产生鼠标进入进出事件
        if (hover != null) {
            hover.mouseAndKeyLister.forEach(m -> m.mouseExited(e));
            hover = null;
        }
    }

    //鼠标滚轮事件
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        mouseAddAndRemove();
        for (Model leaf : mouse) {
            if (Tool.inBox(leaf.getXDouble(), leaf.getYDouble(), leaf.getWDouble(), leaf.getHDouble(), e.getX(), e.getY())) {
                leaf.mouseAndKeyLister.forEach(m -> m.mouseWheelMoved(e));
                return;
            }
        }
    }

    //键盘集合处理
    private static void keyAddAndRemove(){
        if (!addKeySet.isEmpty())
            synchronized (keyAddLock) {
                key.addAll(addKeySet);
                addKeySet.clear();
            }
        if (!removeKeySet.isEmpty())
            synchronized (keyRemoveLock) {
                key.removeAll(removeKeySet);
                removeKeySet.clear();
            }
    }

    //键盘按下时事件
    @Override
    public void keyTyped(KeyEvent e) {
        keyAddAndRemove();
        for (Model leaf : key) {
            leaf.mouseAndKeyLister.forEach(m -> m.keyTyped(e));
        }
    }

    //键盘按下事件
    @Override
    public void keyPressed(KeyEvent e) {
        keyAddAndRemove();
        for (Model leaf : key) {
            leaf.mouseAndKeyLister.forEach(m -> m.keyPressed(e));
        }
    }

    //键盘抬起事件
    @Override
    public void keyReleased(KeyEvent e) {
        keyAddAndRemove();
        for (Model leaf : key) {
            leaf.mouseAndKeyLister.forEach(m -> m.keyReleased(e));
        }
    }
}
