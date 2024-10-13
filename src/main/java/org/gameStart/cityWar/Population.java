package org.gameStart.cityWar;

import org.Tool.Tool;
import org.mainFrame.ListenService;
import org.mainFrame.PaintService;
import org.model.BoxAndTextModel;
import org.mouseAndKeyLister.MouseAndKeyLister;

import java.awt.*;
import java.awt.event.MouseEvent;

import static org.gameStart.GameStart.background;

public class Population {
    public BoxAndTextModel boxAndTextModel;
    public Point point;
    int x, y;
    public int number;
    public Population(int x, int y,int r,int number, Color color){
        this.x = x;
        this.y = y;
        this.number = number;
        point = new Point(x,y,r,color,0);
        boxAndTextModel = new BoxAndTextModel(x-6-r,y-2*r,4*r,3*r/2,new Color(0,0,0,0),color,0.8,number+"");
        point.addLister(new MouseAndKeyLister() {
            @Override
            public void mouseEntered(MouseEvent e) {
                background.setX(point.getXDouble()- point.r);
                background.setY(point.getYDouble()- point.r);
                PaintService.PaintSetAdd(background);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                PaintService.PaintSetRemove(background);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                background.setX(point.getXDouble()-point.r);
                background.setY(point.getYDouble()- point.r);
            }
        });
        ListenService.mouseSetAdd(point);
        PaintService.PaintSetAdd(point);
        PaintService.PaintSetAdd(boxAndTextModel);
        Tool.afterAndContinue(0, 1000, this::numberChange);
    }
    public void numberChange(){
        boxAndTextModel.setText(number+"");
        boxAndTextModel.paint();
    }

    public void setXAndY(int x,int y) {
        this.x = x;
        this.y = y;
        point.setXAndY(x,y);
        point.paint();
    }
}
