package org.gameStart.multiDimensional;

import org.Tool.Tool;
import org.gameStart.multiDimensional.objects.*;
import org.gameStart.multiDimensional.objects.Point;
import org.mainFrame.MainJFrame;
import org.mainFrame.Service.Observer;
import org.mainFrame.model.BoxAndTextModel;
import org.mainFrame.model.Model;
import org.mainFrame.model.ScrollBar;
import org.mainFrame.mouseAndKeyLister.ButtonListener;
import org.mainFrame.mouseAndKeyLister.MouseAndKeyListener;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

public class MultiDimensionalInit {
    public static void multiDimensionalInit(MainJFrame mainJFrame) {

        Engine3D engine3D = new Engine3D();
        engine3D.init(mainJFrame);

        //立方体
        double egad = 100;
        Point[] points = new Point[8];
        int index = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    points[index++] = new Point(i * egad, j * egad, k * egad);
                }
            }
        }
        Hypercube hypercube = new Hypercube(3, points);
        hypercube.generateLowDimensionalLinearGeometry();
        engine3D.getScene().objectSetAdd(hypercube);


        LinearGeometry linearGeometry = new LinearGeometry(1);
        linearGeometry.setLinearGeometry(0, new Point[]{new Point(0, 0, 0), new Point(1000, 0, 0)});
        engine3D.getScene().objectSetAdd(linearGeometry);

        linearGeometry = new LinearGeometry(1);
        linearGeometry.setLinearGeometry(0, new Point[]{new Point(0, 0, 0), new Point(0, 500, 0)});
        engine3D.getScene().objectSetAdd(linearGeometry);

        linearGeometry = new LinearGeometry(1);
        linearGeometry.setLinearGeometry(0, new Point[]{new Point(0, 0, 0), new Point(0, 0, 200)});
        engine3D.getScene().objectSetAdd(linearGeometry);

        RevolveVector revolveVector1 = new RevolveVector(3, 0, 1, Math.toRadians(1));
        RevolveVector revolveVector2 = new RevolveVector(3, 0, 2, Math.toRadians(1));
        RevolveVector revolveVector3 = new RevolveVector(3, 1, 2, Math.toRadians(1));

        Tool.afterAndContinue(0, 10, () -> {
            hypercube.revolve(revolveVector1);
            hypercube.revolve(revolveVector2);
            hypercube.revolve(revolveVector3);
        });

        hypercube.attach(new Observer(engine3D::rendering));

    }
}
