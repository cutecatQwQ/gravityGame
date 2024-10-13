package org.model;

import org.Tool.Tool;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BoxAndTextModel extends Model {
    Color fontColor;
    double per;
    String text;
    BufferedImage image;
    Graphics2D g;
    public BoxAndTextModel(int x, int y, int w, int h, Color color, Color fontColor,double per, String text) {
        this.setX(x);
        this.setY(y);
        this.setW(w);
        this.setH(h);
        this.setPriority(0);
        setColor(color);
        this.fontColor = fontColor;
        this.text = text;
        this.per = per;
        paint();
    }
    @Override
    public void paint(){
        image = new BufferedImage(this.getW(),this.getH(),BufferedImage.TYPE_INT_ARGB);
        g = image.createGraphics();
        // 设置抗锯齿以提高绘制质量
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(getColor());
        g.fillRect(0,0,this.getW(),this.getH());
        g.setColor(fontColor);
        Tool.writeString(g,text,"微软雅黑",per,fontColor,0,0,this.getW(),this.getH());
        this.setImage(image);
        g.dispose();
    }

    public void setText(String text) {
        this.text = text;
        paint();
    }
}
