package org.mainFrame.Service;

import java.util.ArrayList;

public class Observable {
    private ArrayList<Observer> observers = new ArrayList<>();
    //添加观察者
    public void attach(Observer observer){
        observers.add(observer);
    }
    //注意到变量发生变化，通知观察者
    public void notifyAllObservers(){
        for (Observer observer : observers) {
            observer.update();
        }
    }
}
