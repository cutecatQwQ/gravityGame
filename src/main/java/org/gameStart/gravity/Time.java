package org.gameStart.gravity;

import org.Tool.Tool;
import org.mainFrame.PaintService;
import org.model.BoxAndTextModel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Vector;

public class Time {
    private static int time;
    public static void timeStart(){
        CollideModel collideModel = new CollideModel(){
            @Override
            public void paint() {
                setImage(new BufferedImage(getW(), getH(), BufferedImage.TYPE_INT_ARGB));
                Graphics2D g = ((BufferedImage) getImage()).createGraphics();
                // 设置抗锯齿以提高绘制质量
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                //绘制透明色
                g.setColor(getColor());
                g.fillRect(0,0,getW(),getH());
            }
        };
        collideModel.setX(500);
        collideModel.setY(500);
        collideModel.setW(50);
        collideModel.setH(50);
        collideModel.setColor(Color.BLACK);
        collideModel.paint();
        Vector<Double> v = new Vector<>(2);
        v.add(1.0);
        v.add(0.0);
        collideModel.setV(v);
        Vector<Double> a = new Vector<>(2);
        a.add(0.0);
        a.add(-0.001);
        collideModel.setA(a);
        PaintService.PaintSetAdd(collideModel);
        time = 0;
        Tool.afterAndContinue(0,10,()->{
            collideModel.timeAdd(1);
            Tool.debug(++time+"");
        });
    }
}
