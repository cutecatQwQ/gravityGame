package gameEngine.gameStart.multiDimensional;

import gameEngine.gameStart.multiDimensional.objects.*;
import gameEngine.mainFrame.MainJFrame;
import gameEngine.Tool.Tool;
import gameEngine.gameStart.multiDimensional.objects.Point;
import gameEngine.mainFrame.Service.Observer;

import java.awt.*;
import java.util.Arrays;
import java.util.Random;

public class MultiDimensionalInit {
    public static void multiDimensionalInit(MainJFrame mainJFrame) {

        Engine3D engine3D = new Engine3D();
        engine3D.init(mainJFrame);

        //立方体
        double egad = 100;
        gameEngine.gameStart.multiDimensional.objects.Point[] points = new gameEngine.gameStart.multiDimensional.objects.Point[8];
        int index = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    points[index++] = new gameEngine.gameStart.multiDimensional.objects.Point(i * egad, j * egad, k * egad);
                }
            }
        }
        Hypercube hypercube = new Hypercube(3, points);
        hypercube.generateLowDimensionalLinearGeometry();
        engine3D.getScene().objectSetAdd(hypercube);
        //对面设置着色方法
        Random random = new Random();
        hypercube.traverse(2, linearGeometry -> {
            linearGeometry.setColoration(new Coloration(0, new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256))));
            System.out.println(Arrays.toString(linearGeometry.getLinearGeometry(0)));
            System.out.println(linearGeometry.getColoration().getColor());
        });

        Hypercube hypercube1 = Hypercube.getHypercube(3, new gameEngine.gameStart.multiDimensional.objects.Point(-100, -100, -100), 200, 200, 200);
        hypercube1.generateLowDimensionalLinearGeometry();
        engine3D.getScene().objectSetAdd(hypercube1);
        //对面设置着色方法
        hypercube1.traverse(2, linearGeometry -> {
            linearGeometry.setColoration(new Coloration(0, new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256))));
            System.out.println(Arrays.toString(linearGeometry.getLinearGeometry(0)));
            System.out.println(linearGeometry.getColoration().getColor());
        });

        Hypercube[] hypercubes = new Hypercube[10];
        for (int i = 0; i < hypercubes.length; i++) {
            hypercubes[i] = Hypercube.getHypercube(3,new Point(100*i,0,0),100,100,100);
            hypercubes[i].generateLowDimensionalLinearGeometry();
            engine3D.getScene().objectSetAdd(hypercubes[i]);
            //对面设置着色方法
            hypercubes[i].traverse(2, linearGeometry -> {
                linearGeometry.setColoration(new Coloration(0, new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256))));
                System.out.println(Arrays.toString(linearGeometry.getLinearGeometry(0)));
                System.out.println(linearGeometry.getColoration().getColor());
            });
        }

        LinearGeometry linearGeometry = new LinearGeometry(1);
        linearGeometry.setLinearGeometry(0, new gameEngine.gameStart.multiDimensional.objects.Point[]{new gameEngine.gameStart.multiDimensional.objects.Point(0, 0, 0), new gameEngine.gameStart.multiDimensional.objects.Point(1000, 0, 0)});
        engine3D.getScene().objectSetAdd(linearGeometry);
        linearGeometry.setColoration(new Coloration(0, Color.red));

        linearGeometry = new LinearGeometry(1);
        linearGeometry.setLinearGeometry(0, new gameEngine.gameStart.multiDimensional.objects.Point[]{new gameEngine.gameStart.multiDimensional.objects.Point(0, 0, 0), new gameEngine.gameStart.multiDimensional.objects.Point(0, 500, 0)});
        engine3D.getScene().objectSetAdd(linearGeometry);
        linearGeometry.setColoration(new Coloration(0, Color.green));


        linearGeometry = new LinearGeometry(1);
        linearGeometry.setLinearGeometry(0, new gameEngine.gameStart.multiDimensional.objects.Point[]{new gameEngine.gameStart.multiDimensional.objects.Point(0, 0, 0), new Point(0, 0, 200)});
        engine3D.getScene().objectSetAdd(linearGeometry);
        linearGeometry.setColoration(new Coloration(0, Color.blue));

        LinearGeometry[] linearGeometries1 = new LinearGeometry[10];
        LinearGeometry[] linearGeometries2 = new LinearGeometry[10];
        for (int i = 0; i < linearGeometries1.length; i++) {
            linearGeometries1[i] = new LinearGeometry(1);
            linearGeometries2[i] = new LinearGeometry(1);
            linearGeometries1[i].setLinearGeometry(0, new Point[]{new Point(i * 30, 0, 0), new Point(i * 30, 300, 0)});
            linearGeometries2[i].setLinearGeometry(0, new Point[]{new Point(0, i * 30, 0), new Point(300, i * 30, 0)});
            engine3D.getScene().objectSetAdd(linearGeometries1[i]);
            engine3D.getScene().objectSetAdd(linearGeometries2[i]);
            linearGeometries1[i].setColoration(new Coloration(0, new Color(127,127,127)));
            linearGeometries2[i].setColoration(new Coloration(0, new Color(127,127,127)));
        }


        RevolveVector revolveVector1 = new RevolveVector(3, 0, 1, Math.toRadians(1));
        RevolveVector revolveVector2 = new RevolveVector(3, 0, 2, -Math.toRadians(1));
        RevolveVector revolveVector3 = new RevolveVector(3, 1, 2, Math.toRadians(1));
        RevolveVector revolveVector4 = new RevolveVector(3, 0, 1, -Math.toRadians(1));
        RevolveVector revolveVector5 = new RevolveVector(3, 0, 2, Math.toRadians(1));
        RevolveVector revolveVector6 = new RevolveVector(3, 1, 2, -Math.toRadians(1));

        Tool.afterAndContinue(0, 40, () -> {
            hypercube.revolve(revolveVector1);
            hypercube.revolve(revolveVector2);
            hypercube.revolve(revolveVector3);
            hypercube1.revolve(revolveVector4);
            hypercube1.revolve(revolveVector5);
            hypercube1.revolve(revolveVector6);
        });

        hypercube.attach(new Observer(engine3D::rendering));

    }
}
