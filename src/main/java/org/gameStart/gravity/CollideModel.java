package org.gameStart.gravity;

import org.mainFrame.model.Model;

import java.awt.*;
import java.util.Vector;

//碰撞实体类
public abstract class CollideModel extends Model {
    //维度
    private final Integer n = 2;
    //速度
    private Vector<Double> v = new Vector<>(n);
    //加速度
    private Vector<Double> a = new Vector<>(n);
    //是否会被其他实体碰撞,稳定性
    private boolean stable;
    //质量
    private double m;

    public CollideModel(double x,double y, double w, double h, Color color,Vector<Double> v,Vector<Double> a,boolean stable,double m) {
        setX(x);
        setY(y);
        setW(w);
        setH(h);
        setPriority(0);
        setColor(color);
        this.v.add((double) 0);
        this.v.add((double) 0);
        setV(v);
        this.a.add((double) 0);
        this.a.add((double) 0);
        setA(a);
        setStable(stable);
        setM(m);
        paint();
    }

    //时间流逝
    public void timeAdd(Integer time) {
        for (int i = 0; i < time; i++) {
            xAdd(v.get(0));
            yAdd(-v.get(1));
            addV(a);
        }
    }
    //++方法
    public void addV(Vector<Double> av){
        this.v.set(0,this.v.get(0)+av.get(0));
        this.v.set(1,this.v.get(1)+av.get(1));
    }
    public void addA(Vector<Double> aa){
        this.a.set(0,this.a.get(0)+aa.get(0));
        this.a.set(1,this.a.get(1)+aa.get(1));
    }
    //set方法
    public void setV(Vector<Double> v){
        this.v.set(0,v.get(0));
        this.v.set(1,v.get(1));
    }
    public void setA(Vector<Double> a){
        this.a.set(0,a.get(0));
        this.a.set(1,a.get(1));
    }

    public boolean isStable() {
        return stable;
    }

    public void setStable(boolean stable) {
        this.stable = stable;
    }

    public double getM() {
        return m;
    }

    public void setM(double m) {
        this.m = m;
    }
}
