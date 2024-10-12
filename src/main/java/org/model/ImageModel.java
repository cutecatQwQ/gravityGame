package org.model;

import org.Tool.Tool;

//有图片
public class ImageModel extends Model {
    //优先级默认为0的构造方法
    public ImageModel(int x,int y, int w, int h,String path) {
        this.setX(x);
        this.setY(y);
        this.setW(w);
        this.setH(h);
        this.setPriority(0);
        this.setImage(Tool.getImage(path));
    }
    //设定优先级的构造方法
    public ImageModel(int x,int y, int w, int h, int priority,String path) {
        this.setX(x);
        this.setY(y);
        this.setW(w);
        this.setH(h);
        this.setPriority(priority);
        this.setImage(Tool.getImage(path));
    }
}
