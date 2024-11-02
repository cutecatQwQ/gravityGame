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
    //质量
    private double m;

    public CollideModel(double x,double y, double w, double h, Color color,Vector<Double> v,Vector<Double> a,double m) {
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
        setM(m);
        paint();
    }

    //时间流逝
    public void timeAdd(Integer time) {
        for (int i = 0; i < time; i++) {
            addX(v.get(0));
            addY(-v.get(1));
            addV(a);
        }
    }

    //碰撞检测
    public abstract boolean collisionDetection(CollideModel collideModel);

    //弹性碰撞 e是恢复系数，e=1时为完全弹性碰撞 e=0时为完全非弹性碰撞
    public void elasticCollision(CollideModel collideModel, double e) {

//        if (Double.isInfinite(getM()) || Double.isInfinite(collideModel.getM())) {
//            if(Double.isInfinite(getM())){
//                if(Double.isInfinite(collideModel.getM())){
//                    return;//两个墙
//                }
//                else {
//
//                }
//            }
//        }

        //质量总和
        double m = getM()+collideModel.getM();
        //v1
        Vector<Double> v1 = new Vector<>();
        v1.add(((getM()-e*collideModel.getM())*this.v.get(0)+(1+e)*collideModel.getM()*collideModel.v.get(0))/m);
        v1.add(((getM()-e*collideModel.getM())*this.v.get(1)+(1+e)*collideModel.getM()*collideModel.v.get(1))/m);
        //v2
        Vector<Double> v2 = new Vector<>();
        v2.add(((collideModel.getM()-e*getM())*collideModel.v.get(0)+(1+e)*getM()*this.v.get(0))/m);
        v2.add(((collideModel.getM()-e*getM())*collideModel.v.get(1)+(1+e)*getM()*this.v.get(1))/m);

        setV(v1);
        collideModel.setV(v2);

        timeAdd(1);
    }

    //++方法
    public void addV(Vector<Double> av){
        for (int i = 0; i < n; i++) {
            this.v.set(i,this.v.get(i)+av.get(i));
        }
    }
    public void addA(Vector<Double> aa){
        for (int i = 0; i < n; i++) {
            this.a.set(i,this.a.get(i)+aa.get(i));
        }
    }
    //set方法
    public void setV(Vector<Double> v){
        for (int i = 0; i < n; i++) {
            this.v.set(i,v.get(i));
        }
    }
    public void setA(Vector<Double> a){
        for (int i = 0; i < n; i++) {
            this.a.set(i,a.get(i));
        }
    }

    public double getM() {
        return m;
    }

    public void setM(double m) {
        this.m = m;
    }
}
