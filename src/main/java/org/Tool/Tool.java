package org.Tool;

import org.gameStart.GameStart;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//工具类
public class Tool {
    //日志框架
    private static final Logger log = LoggerFactory.getLogger(Tool.class);
    //定时任务服务
    static ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    //根据URL获取图片
    public static Image getImage(String s) {
        try {
            //根据class文件获取输入流
            InputStream inputStream = GameStart.class.getClassLoader().getResourceAsStream(s);
            Image image = ImageIO.read(inputStream);
            inputStream.close();
            return image;
        } catch (IOException e) {
            log.error("exception message", e);
        }
        return null;
    }

    //鼠标是否在盒子内
    public static boolean inBox(int x, int y, int w, int h, int ex, int ey) {
        return ex > x && ex < x + w && ey > y && ey < y + h;
    }

    //绘制字符串 在宽为w高为h的一个框里面居中绘制
    public static void writeString(Graphics g, String text, String font, double per, Color color, int x, int y, int w, int h) {
        Font oldFont = g.getFont();
        Color oldColor = g.getColor();
        Font f = new Font(font, Font.PLAIN, (int) (per * h));
        g.setFont(f);
        g.setColor(color);
        // 获取字体的属性
        FontMetrics metrics = g.getFontMetrics();

        // 计算字符串的宽度和高度
        int textWidth = metrics.stringWidth(text);
        int textHeight = metrics.getHeight();

        // 计算字符串的起始位置
        int fx = (w - textWidth) / 2;
        int fy = (h - textHeight) / 2 + metrics.getAscent();

        // 绘制字符串
        g.drawString(text, fx + x, fy + y);
        g.setColor(oldColor);
        g.setFont(oldFont);
    }

    //delay毫秒后执行方法,使用ScheduledFuture的cancel()方法并传递true即可打断
    public static ScheduledFuture<?> after(long delay, Runnable runnable) {
        //几毫秒秒后执行方法 TimeUnit.MILLISECONDS表示单位是毫秒
        return scheduledExecutorService.schedule(runnable, delay, TimeUnit.MILLISECONDS);
    }
    //delay毫秒后执行方法,并且以period毫秒为周期的频率重复,使用ScheduledFuture的cancel()方法并传递true即可打断
    public static ScheduledFuture<?> afterAndContinue(long delay, long period, Runnable runnable) {
        return scheduledExecutorService.scheduleAtFixedRate(runnable, delay, period, TimeUnit.MILLISECONDS);
    }




    //delay毫秒后执行方法,使用ScheduledFuture的cancel()方法并传递true即可打断
    public static ScheduledFuture<?> after(long delay, Object object, String methodName, Object... param) {
        //几毫秒秒后执行方法
        return scheduledExecutorService.schedule(() -> run(object, methodName, param), delay, TimeUnit.MILLISECONDS);
    }
    //delay毫秒后执行方法,并且以period毫秒为周期的频率重复,使用ScheduledFuture的cancel()方法并传递true即可打断
    public static ScheduledFuture<?> afterAndContinue(long delay, long period, Object object, String methodName, Object... param) {
        return scheduledExecutorService.scheduleAtFixedRate(() -> run(object, methodName, param), delay, period, TimeUnit.MILLISECONDS);
    }
    //反射调用方法，参数分别是：使用方法的对象，方法名，传递的参数
    private static void run(Object object, String methodName, Object... param) {
        //获取参数的类型数组
        Class<?>[] parames = new Class[param.length];
        for (int i = 0; i < param.length; i++) {
            parames[i] = param[i].getClass();
        }
        //获取对象的所有方法并遍历
        Method[] methods = object.getClass().getMethods();
        for (Method method : methods) {
            //找到相同名字的方法
            if (method.getName().equals(methodName)) {
                //获取此方法的参数列表，如果与传入的参数不符则重新寻找
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length != parames.length) continue;
                boolean match = true;
                for (int i = 0; i < parameterTypes.length; i++) {
                    if (!parameterTypes[i].isAssignableFrom(parames[i])) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    //找到想要的方法
                    try {
                        // 调用方法，并传递参数
                        method.invoke(object, param);
                        break;
                    }catch (Exception e) {
                        log.error("exception message", e);
                    }
                }
            }
        }
    }
}
