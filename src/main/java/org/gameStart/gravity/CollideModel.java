package org.gameStart.gravity;

import org.model.Model;

import java.util.Vector;

//碰撞实体类
public class CollideModel extends Model {
    //维度
    private Integer n = 2;
    //速度
    private Vector<Double> v = new Vector<>(n);
    //加速度
    private Vector<Double> a = new Vector<>(n);

    public CollideModel() {
        v.add((double) 0);
        v.add((double) 0);
        a.add((double) 0);
        a.add((double) 0);
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
}
