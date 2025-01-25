package org.mainFrame.Service;

import java.util.function.Consumer;

public class Observer<T> {
    T t;
    Runnable runnable;
    Consumer<T> consumer;
    public void update(){
        if(runnable != null)runnable.run();
        if(consumer != null)consumer.accept(t);
    }

    public Observer(T t, Consumer<T> consumer) {
        this.t = t;
        this.consumer = consumer;
    }

    public Observer(Runnable runnable) {
        this.runnable = runnable;
    }
}
