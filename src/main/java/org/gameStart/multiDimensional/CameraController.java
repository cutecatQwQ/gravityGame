package org.gameStart.multiDimensional;

import org.Tool.MathUtil;
import org.Tool.Tool;
import org.gameStart.multiDimensional.objects.RevolveVector;
import org.mainFrame.mouseAndKeyLister.MouseAndKeyListener;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class CameraController extends MouseAndKeyListener {
    //摄像头
    Camera camera;
    //按键
    boolean w,a,s,d,e,q;

    //旧的鼠标位置
    int oldEX, oldEY;

    //是否被按下
    boolean isPress = false;

    //模式 0世界坐标系 1相机坐标系
    int mode;

    public CameraController(Camera camera) {
        this.camera = camera;
        this.w = false;
        this.a = false;
        this.s = false;
        this.d = false;
        this.e = false;
        this.q = false;
        this.mode = 0;
        Tool.afterAndContinue(10, 50, () -> {
            if(w||s||a||d||e||q) {
//                System.out.println(""+w+s+a+d+e+q);
            }
            //世界坐标系
            if(mode == 0) {
                if (w) camera.location.setCoordinates(MathUtil.move(camera.location.getCoordinates(),10,camera.getWorldBaseVector(0).getCoordinates()));
                if (s) camera.location.setCoordinates(MathUtil.move(camera.location.getCoordinates(),-10,camera.getWorldBaseVector(0).getCoordinates()));
                if (a) camera.location.setCoordinates(MathUtil.move(camera.location.getCoordinates(),-10,camera.getWorldBaseVector(1).getCoordinates()));
                if (d) camera.location.setCoordinates(MathUtil.move(camera.location.getCoordinates(),10,camera.getWorldBaseVector(1).getCoordinates()));
                if (e) camera.location.setCoordinates(MathUtil.move(camera.location.getCoordinates(),10,camera.getWorldBaseVector(2).getCoordinates()));
                if (q) camera.location.setCoordinates(MathUtil.move(camera.location.getCoordinates(),-10,camera.getWorldBaseVector(2).getCoordinates()));
            }
            //相机坐标系
            else {
            if (w) camera.location.setCoordinates(MathUtil.move(camera.location.getCoordinates(),10,camera.getBaseVector(2).getCoordinates()));
            if (s) camera.location.setCoordinates(MathUtil.move(camera.location.getCoordinates(),-10,camera.getBaseVector(2).getCoordinates()));
            if (a) camera.location.setCoordinates(MathUtil.move(camera.location.getCoordinates(),-10,camera.getBaseVector(0).getCoordinates()));
            if (d) camera.location.setCoordinates(MathUtil.move(camera.location.getCoordinates(),10,camera.getBaseVector(0).getCoordinates()));
            if (e) camera.location.setCoordinates(MathUtil.move(camera.location.getCoordinates(),10,camera.getBaseVector(1).getCoordinates()));
            if (q) camera.location.setCoordinates(MathUtil.move(camera.location.getCoordinates(),-10,camera.getBaseVector(1).getCoordinates()));
        }});
    }

    //拖拽功能的实现
    @Override
    public void mousePressed(MouseEvent e) {
        isPress = true;
        oldEX = e.getX();
        oldEY = e.getY();
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        isPress = false;
    }
        @Override
    public void mouseDragged(MouseEvent e) {
        if (isPress) {
            //绕世界坐标系的xoy平面旋转
            camera.revolve(new RevolveVector(3,0, 1, (e.getX() - oldEX) * camera.sensitivity));
            //抬头/低头
            camera.revolveYOZ(-(e.getY() - oldEY) * camera.sensitivity);
            oldEX = e.getX();
            oldEY = e.getY();
        }
    }
    //键盘监听
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyChar()) {
            case 'W':
                w = true;
                break;
            case 'S':
                s = true;
                break;
            case 'A':
                a = true;
                break;
            case 'D':
                d = true;
                break;
            case 'E':
                this.e = true;
                break;
            case 'Q':
                q = true;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyChar()) {
            case 'W':
                w = false;
                break;
            case 'S':
                s = false;
                break;
            case 'A':
                a = false;
                break;
            case 'D':
                d = false;
                break;
            case 'E':
                this.e = false;
                break;
            case 'Q':
                q = false;
                break;
        }
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
