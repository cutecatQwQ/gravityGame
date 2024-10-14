package org.mainFrame.model;

import org.Tool.Tool;

//有图片
public class ImageModel extends Model {
    //优先级默认为0的构造方法
    public ImageModel(double x,double y, double w, double h,String path) {
        setX(x);
        setY(y);
        setW(w);
        setH(h);
        setPriority(0);
        setImage(Tool.getImage(path));
    }
    //设定优先级的构造方法
    public ImageModel(double x,double y, double w, double h, int priority,String path) {
        setX(x);
        setY(y);
        setW(w);
        setH(h);
        setPriority(priority);
        setImage(Tool.getImage(path));
    }
}
