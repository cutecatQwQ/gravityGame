package org.gameStart.cityWar;

import org.mainFrame.ListerService;
import org.mainFrame.PaintService;
import org.model.BoxAndTextModel;
import org.mouseAndKeyLister.ButtonLister;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class Buttons {
    int x, y, w, h;
    Color color, fontColor;
    double per;
    Map<String, BoxAndTextModel> boxAndTextModelHashMap = new HashMap<>();

    public Buttons(int x, int y, int w, int h, Color color, Color fontColor, double per) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.color = color;
        this.fontColor = fontColor;
        this.per = per;
    }

    public void setXAndY(int x, int y) {
        this.x = x;
        this.y = y;
        int i = 0;
        for (String key : boxAndTextModelHashMap.keySet()) {
            boxAndTextModelHashMap.get(key).setX(x);
            boxAndTextModelHashMap.get(key).setY(y + i * h);
            i++;
        }
    }

    public Buttons add(String s, Runnable runnable) {
        BoxAndTextModel boxAndTextModel = new BoxAndTextModel(x, y + boxAndTextModelHashMap.size() * h, w, h, color, fontColor, per, s);
        boxAndTextModel.add(new ButtonLister(boxAndTextModel) {
                                @Override
                                public void mousePressed(MouseEvent e) {
                                }

                                @Override
                                public void mouseReleased(MouseEvent e) {
                                }

                                @Override
                                public void mouseClicked(MouseEvent e) {
                                    runnable.run();
                                }
                            }
        );
        boxAndTextModelHashMap.put(s, boxAndTextModel);
        ListerService.mouse.add(boxAndTextModel);
        PaintService.PaintSetAdd(boxAndTextModel);
        return this;
    }

    public Buttons remove(String s) {
        BoxAndTextModel boxAndTextModel = boxAndTextModelHashMap.get(s);
        if(boxAndTextModel == null) return this;
        ListerService.mouse.remove(boxAndTextModel);
        PaintService.PaintSetRemove(boxAndTextModel);
        boxAndTextModelHashMap.remove(s);
        int i = 0;
        for (String key : boxAndTextModelHashMap.keySet()) {
            boxAndTextModelHashMap.get(key).setY(y + i * h);
            i++;
        }
        return this;
    }

    public void setBoolean(boolean b) {
        if (b) {
            for (String key : boxAndTextModelHashMap.keySet()) {
                boxAndTextModelHashMap.get(key).paint();
                ListerService.mouse.add(boxAndTextModelHashMap.get(key));
                PaintService.PaintSetAdd(boxAndTextModelHashMap.get(key));
            }

        } else {
            for (String key : boxAndTextModelHashMap.keySet()) {
                ListerService.mouse.remove(boxAndTextModelHashMap.get(key));
                PaintService.PaintSetRemove(boxAndTextModelHashMap.get(key));
            }
        }
    }
}
