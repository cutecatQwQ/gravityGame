package org.gameStart.multiDimensional;

import java.util.function.Consumer;

public interface Object {
    //因为画图的时候开了抗锯齿，这个线在图片特别细的时候会画不清楚，所以设置一个描边宽度(图片外围)为了让线画的清楚
    public static int add = 1;
    public void traverse(Consumer<Point> consumer);
}
