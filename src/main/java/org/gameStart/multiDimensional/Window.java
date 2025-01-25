package org.gameStart.multiDimensional;

import org.gameStart.multiDimensional.objects.LinearGeometry;
import org.gameStart.multiDimensional.objects.Object;
import org.gameStart.multiDimensional.objects.Point;
import org.mainFrame.model.Model;

import java.awt.*;
import java.util.TreeSet;

public class Window extends Model {
    private Camera camera;
    //深度缓冲
    double[][] height;

    public Window(double x, double y, double w, double h) {
        setX(x);
        setY(y);
        setW(w);
        setH(h);
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void paint(TreeSet<Object> renderingSet) {
        Graphics2D g = getNewGraphics2D();
        renderingSet.forEach(rendering -> {
            //点
            for (int i = 0; i < ((LinearGeometry) rendering).getLinearGeometry(0).length; i++) {
                Point point = (Point) ((LinearGeometry) rendering).getLinearGeometry(0)[i];
//                if(point == null) {
//                    point = new Point(camera.getDimension()-1);
//                }
                g.setColor(Color.black);
                //转换坐标系并旋转到摄像头方向然后投影
                double[] coordinates = camera.projection(camera.convertToCameraCoordinateSystem(point.getCoordinates()));

                //转到窗口坐标系
                coordinates[0] += getWDouble() / 2;
                coordinates[1] = getHDouble() / 2 - coordinates[1];

                if (point.getLowDimensionalObject() == null)
                    point.setLowDimensionalObject(new Point(point.size() - 1));
                ((Point) point.getLowDimensionalObject()).setCoordinates(coordinates);

                //画点
//                double a = 4;
//                g.fillOval((int) (coordinates[0]-getIndex()/2*a), (int) (coordinates[1]-getIndex()/2*a), (int) (getIndex().intValue()*a), (int) (getIndex().intValue()*a));
            }
            //线 目前没有一个场景被多个摄像头看的办法
            if (rendering.getDimension() > 1) {
                for (int i = 0; i < ((LinearGeometry) rendering).getLinearGeometry(1).length; i++) {
                    LinearGeometry linearGeometry = ((LinearGeometry) rendering).getLinearGeometry(1)[i];
                    g.setColor(Color.black);
                    g.drawLine(
                            (int) ((Point) linearGeometry.getLinearGeometry(0, 0).getLowDimensionalObject()).get(0),
                            (int) ((Point) linearGeometry.getLinearGeometry(0, 0).getLowDimensionalObject()).get(1),
                            (int) ((Point) linearGeometry.getLinearGeometry(0, 1).getLowDimensionalObject()).get(0),
                            (int) ((Point) linearGeometry.getLinearGeometry(0, 1).getLowDimensionalObject()).get(1));
                }
            } else {
                g.setColor(Color.black);
                g.drawLine(
                        (int) ((Point) ((LinearGeometry)rendering).getLinearGeometry(0, 0).getLowDimensionalObject()).get(0),
                        (int) ((Point) ((LinearGeometry)rendering).getLinearGeometry(0, 0).getLowDimensionalObject()).get(1),
                        (int) ((Point) ((LinearGeometry)rendering).getLinearGeometry(0, 1).getLowDimensionalObject()).get(0),
                        (int) ((Point) ((LinearGeometry)rendering).getLinearGeometry(0, 1).getLowDimensionalObject()).get(1));
            }
        });
    }
}
