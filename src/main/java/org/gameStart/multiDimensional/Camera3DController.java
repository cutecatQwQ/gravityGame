package org.gameStart.multiDimensional;

import org.Tool.Tool;
import org.mainFrame.MainJFrame;
import org.mainFrame.mouseAndKeyLister.MouseAndKeyLister;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * 此Controller是用于实现鼠标拖拽控制摄像头功能的，而且仅仅是为了方便建立4D摄像头，
 * 所以没有任何额外的移动功能，而且只能在一个球体上运动，
 * 而让摄像头在原点处也可以获得这个效果，但是这个时候会抛弃一个前后概念，后面的也会出现在屏幕上，
 * 所以只能实现一个摄像头的视觉效果，本身的存在并不严谨。
 */

public class Camera3DController extends MouseAndKeyLister {
    Camera3D camera3D;
    Rendering3DService rendering3DService;
    //旧的鼠标位置
    int oldEX, oldEY;

    //是否被按下
    boolean isPress = false;

    public Camera3DController(Camera3D camera3D, Rendering3DService rendering3DService) {
        this.camera3D = camera3D;
        this.rendering3DService = rendering3DService;
        camera3D.setX(0);
        camera3D.setY(0);
        camera3D.setW(MainJFrame.dimension.width);
        camera3D.setH(MainJFrame.dimension.height);
    }

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
            //绕xoy平面旋转
            camera3D.revolve(0, 1, (e.getX() - oldEX) * camera3D.sensitivity);
            camera3D.revolve(1, 2, -compensateForRotation(camera3D.baseCoordinates.get(1)));
            System.out.println(camera3D.baseCoordinates.get(1));
            //绕xoz平面旋转
            camera3D.revolve(0, 2, (e.getY() - oldEY) * camera3D.sensitivity);
            System.out.println(camera3D.baseCoordinates.get(1));

            oldEX = e.getX();
            oldEY = e.getY();
            rendering3DService.rendering3D();
        }
    }

    /**
     * 补偿旋转，让摄像机的y向量始终在绝对xoy平面内
     * y'是y在绝对xoy平面的投影
     * 角度a的余弦值cos(a) = cos((y·y')/(|y|*|y'|)) = cos((x^2+y^2+z*0)/(1*sqrt(x^2+y^2))) = cos(sqrt(x^2+y^2))
     * 最后转换为角度制
     */
    private double compensateForRotation(Vector y) {
        if (y.get(2) == 0) return 0;
        System.out.print(y);
        double yx = y.get(0);
        double yy = y.get(1);
        double yz = y.get(2);

        // 计算 y 的模长
        double lengthY = Math.sqrt(yx * yx + yy * yy + yz * yz);

        // 投影向量 y'
        double lengthYPrime = Math.sqrt(yx * yx + yy * yy);

        // 点积
        double dotProduct = (yx * yx + yy * yy);

        // 计算余弦值
        double cosA = dotProduct / (lengthY * lengthYPrime);
        if (cosA > 1 || cosA < -1) return 0;
        // 返回角度
        return (yz>0?-1:1)*Math.toDegrees(Math.acos(cosA));
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        int a = e.getWheelRotation();
        camera3D.sensitivity += camera3D.sensitivity * a * 0.01;
        if (camera3D.sensitivity < 0) camera3D.sensitivity = 0;
    }
}
