package gameEngine.mainFrame.model;

import gameEngine.mainFrame.MainJFrame;
import gameEngine.mainFrame.mouseAndKeyLister.ButtonListener;
import gameEngine.mainFrame.mouseAndKeyLister.MouseAndKeyListener;

import java.awt.*;
import java.awt.event.MouseEvent;

//滚动条
public class ScrollBar extends BoxAndTextModel{
    //数值类型
    double content;
    //最大数值和最小数值
    double min, max;
    //文字框
    BoxAndTextModel textModel;
    //按钮
    BoxAndTextModel buttonModel;
    //x y w h 文字宽 文字大小 最小值 最大值 背景颜色 字体颜色 按钮颜色
    public ScrollBar(double x, double y, double w, double h, int size, double per, double min, double max, Color color, Color textColor , Color fontColor, Color buttonColor, MainJFrame mainJFrame) {
        super(x+size, y+h*2/5, w-size, h/5,color,fontColor,0,"");
        this.min = min;
        this.max = max;
        textModel = new BoxAndTextModel(x,y,size,h,textColor,fontColor,per,""+min);
        buttonModel = new BoxAndTextModel(x+size,y,h,h,buttonColor,fontColor,per,"");
        buttonModel.addListener(new ButtonListener(buttonModel,mainJFrame));
        buttonModel.addListener(new MouseAndKeyListener() {
            //旧的鼠标位置
            int oldEX;
            //旧的model位置
            int oldX;
            //是否被按下
            boolean isPress = false;

            @Override
            public void mousePressed(MouseEvent e) {
                isPress = true;
                oldEX = e.getX();
                oldX = buttonModel.getX();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isPress = false;
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (isPress) {
                    int newX = oldX + e.getX() - oldEX;
                    if(getX()> newX){
                        newX = getX();
                    } else if(newX +getHDouble()*5>getXDouble()+getWDouble()) {
                        newX = (int) (getXDouble()+getWDouble()-getHDouble()*5);
                    }
                    buttonModel.setX(newX);
                    content = (max-min)*(newX - getX())/(getWDouble()-getHDouble()*5)+min;
                    textModel.setText(String.format("%.2f", content));
                }
            }});
    }

    @Override
    public void setPriority(int priority) {
        super.setPriority(priority);
        if(textModel == null) return;
        textModel.setPriority(priority-1);
        buttonModel.setPriority(priority-1);
    }

    public BoxAndTextModel getTextModel() {
        return textModel;
    }

    public BoxAndTextModel getButtonModel() {
        return buttonModel;
    }

    public double getContent() {
        return content;
    }

    //设置的时候把里面的组件model也设置一下
    public void setContent(double content) {
        this.content = content;
        textModel.setText(String.format("%.2f", content));
        buttonModel.setX((content-min)/(max-min) *(getWDouble()-getHDouble()*5)+ getX());
    }
}
