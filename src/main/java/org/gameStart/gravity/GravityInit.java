package org.gameStart.gravity;

import org.Tool.Tool;
import org.mainFrame.Service.PaintService;

import java.awt.*;
import java.util.Vector;

public class GravityInit {
    public static void timeStart() {
        Vector<Double> v = new Vector<>(2);
        v.add(1.0);
        v.add(0.0);
        Vector<Double> a = new Vector<>(2);
        a.add(0.0);
        a.add(-0.01);
        RectangleModel rectangleModel = new RectangleModel(500, 500, 50, 50, Color.black, v, a,false,1);
        v.set(0, -1.0);
        CircleModel circleModel = new CircleModel(1000, 300, 50, Color.black, v, a,false,1);
        PaintService.paintSetAdd(rectangleModel);
        PaintService.paintSetAdd(circleModel);
        TimeService.timeSetAdd(rectangleModel);
        TimeService.timeSetAdd(circleModel);
        //间隔10毫秒时间流逝，time++，处理运动的物体
        Tool.afterAndContinue(0, 10, TimeService::timePasses);

    }
}
