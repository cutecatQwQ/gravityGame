package gameEngine.Tool;

import gameEngine.gameStart.GameStart;
import gameEngine.mainFrame.MainJFrame;
import gameEngine.mainFrame.model.ImageModel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import gameEngine.mainFrame.mouseAndKeyLister.WheelListener;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import gameEngine.mainFrame.model.BoxAndTextModel;
import gameEngine.mainFrame.mouseAndKeyLister.DraggableListener;
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
    public static boolean inBox(double x, double y, double w, double h, int ex, int ey) {
        return ex > x && ex < x + w && ey > y && ey < y + h;
    }

    //绘制字符串 在宽为w高为h的一个框里面居中绘制,per是字体高度占据w的比例，一般在(0,1)之间
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

    //debug用的
    private final static BoxAndTextModel debug = new BoxAndTextModel(0, 0, 300, 50, new Color(225, 225, 225), Color.black, 0.66, "");
    private static boolean isAdd = false;

    public static void debug(String string, MainJFrame mainJFrame) {
        if (!isAdd) {
            isAdd = true;
            debug.setPriority(-10);
            debug.addListener(new DraggableListener(debug));
            mainJFrame.getListenService().mouseSetAdd(debug);
            mainJFrame.getPaintService().paintSetAdd(debug);
        }
        debug.setText(string);
    }

    /**
     * 图表工具类，使用jFreeChart依赖
     * 新建折线图newLineChart()后向数据集中添加数据addDate(x,y)，
     * 再结合afterAndContinue()定时更新updateLineChart()
     */
    private static ImageModel lineChartImage = new ImageModel(MainJFrame.dimension.width - 500, 0, 500, 500, "");
    private static XYSeries series = new XYSeries("a");
    private static String x, y, title;
    //索引计数
    private static int index;
    static {
        //初始化图片Model
        lineChartImage.addListener(new DraggableListener(lineChartImage));
        lineChartImage.addListener(new WheelListener(lineChartImage));
        lineChartImage.setPriority(-10);
        series.setMaximumItemCount(100);
    }

    //name折线的名字
    public static void newLineChart(String title, String x, String y, MainJFrame mainJFrame) {
        //数据集
        Tool.x = x;
        Tool.y = y;
        Tool.title = title;
        series.clear();
        index = 0;
        mainJFrame.getListenService().mouseSetAdd(lineChartImage);
        mainJFrame.getPaintService().paintSetAdd(lineChartImage);
        updateLineChart();
    }

    //索引为x轴添加数据
    public static void addDate(double y) {
        //没有新建图表直接返回
        if (series == null) return;
        series.add(index++,y);
    }

    //添加数据
    public static void addDate(double x, double y) {
        //没有新建图表直接返回
        if (series == null) return;
        series.add(x, y);
    }

    //更新折线图
    public static void updateLineChart() {
        //没有新建图表直接返回
        if (series == null) return;

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        JFreeChart chart = ChartFactory.createXYLineChart(
                title, // 图表标题
                Tool.x, // x轴标签
                Tool.y, // y轴标签
                dataset, // 数据集
                PlotOrientation.VERTICAL, // 绘图方向
                true,                 // 是否显示图例
                true,                 // 是否生成工具提示
                false                 // 是否生成URL链接
        );
        // 设置字体
        Font font = new Font("SimSun", Font.PLAIN, 12); // 使用宋体
        chart.getTitle().setFont(font); // 设置标题字体
        chart.getLegend().setItemFont(font); // 设置图例字体
        // 获取 XYPlot 并设置其属性
        XYPlot plot = chart.getXYPlot();
        plot.getDomainAxis().setLabelFont(font); // 设置x轴标签字体
        plot.getRangeAxis().setLabelFont(font); // 设置y轴标签字体

        // 获取y轴并设置为自适应最小值
        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setAutoRangeIncludesZero(false); // 不包含零

        int width = 500;
        int height = 500;
        // 创建图表并转换为BufferedImage
        BufferedImage image = chart.createBufferedImage(width, height);

        if (image != null) {
//            System.out.println("图表已成功转换为BufferedImage！");
            // 这里可以进一步处理或显示这个BufferedImage
            lineChartImage.setImage(image);
        } else {
            log.error("转换失败！");
        }
    }

    //delay毫秒后执行方法,使用ScheduledFuture的cancel()方法并传递true即可打断
    public static ScheduledFuture<?> after(long delay, Runnable runnable) {
        //几毫秒秒后执行方法 TimeUnit.MILLISECONDS表示单位是毫秒
        return scheduledExecutorService.schedule(runnable, delay, TimeUnit.MILLISECONDS);
    }

    //delay毫秒后执行方法,并且以period毫秒为周期的频率重复,使用ScheduledFuture的cancel()方法并传递true即可打断
    public static ScheduledFuture<?> afterAndContinue(long delay, long period, Runnable runnable) {
        try {
            return scheduledExecutorService.scheduleAtFixedRate(runnable, delay, period, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //delay毫秒后执行方法,使用ScheduledFuture的cancel()方法并传递true即可打断
    public static ScheduledFuture<?> after(long delay, Object object, String methodName, Object... param) {
        try {
            //几毫秒秒后执行方法
            return scheduledExecutorService.schedule(() -> run(object, methodName, param), delay, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //delay毫秒后执行方法,并且以period毫秒为周期的频率重复,使用ScheduledFuture的cancel()方法并传递true即可打断
    public static ScheduledFuture<?> afterAndContinue(long delay, long period, Object object, String methodName, Object... param) {
        try {
            return scheduledExecutorService.scheduleAtFixedRate(() -> run(object, methodName, param), delay, period, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
                    } catch (Exception e) {
                        log.error("exception message", e);
                    }
                }
            }
        }
    }
}
