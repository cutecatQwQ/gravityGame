package org.mainFrame.model;

import org.Tool.Tool;

import java.awt.*;

public class BoxAndTextModel extends Model {
    private Color fontColor;
    private double per;
    private String text;
    public BoxAndTextModel(double x,double y, double w, double h, Color color, Color fontColor,double per, String text) {
        setX(x);
        setY(y);
        setW(w);
        setH(h);
        setPriority(0);
        setColor(color);
        setFontColor(fontColor);
        setText(text);
        setPer(per);
        paint();
    }
    @Override
    public void paint(){
        Graphics2D g = getNewGraphics2D();
        g.setColor(getColor());
        g.fillRect(0,0,this.getW(),this.getH());
        g.setColor(fontColor);
        Tool.writeString(g,text,"微软雅黑",per,fontColor,0,0,this.getW(),this.getH());
        g.dispose();
    }

    public void setText(String text) {
        this.text = text;
        paint();
    }

    public Color getFontColor() {
        return fontColor;
    }

    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }

    public double getPer() {
        return per;
    }

    public void setPer(double per) {
        this.per = per;
    }

    public String getText() {
        return text;
    }
}
