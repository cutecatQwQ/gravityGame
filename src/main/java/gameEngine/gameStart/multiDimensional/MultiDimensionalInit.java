package gameEngine.gameStart.multiDimensional;

import gameEngine.gameStart.multiDimensional.objects.*;
import gameEngine.mainFrame.MainJFrame;
import gameEngine.Tool.Tool;
import gameEngine.gameStart.multiDimensional.objects.Point;
import gameEngine.mainFrame.Service.Observer;

import java.awt.*;
import java.util.Random;

public class MultiDimensionalInit {
    public static void multiDimensionalInit(MainJFrame mainJFrame) {

        Engine3D engine3D = new Engine3D();
        engine3D.init(mainJFrame);
        Random random = new Random();

        //立方体
        Hypercube[][] hypercubes = new Hypercube[100][100];
        for (int i = 0; i < hypercubes.length; i++) {
            hypercubes[i] = new Hypercube[100];
            for (int j = 0; j < hypercubes[i].length; j++) {
//                hypercubes[i][j] = Hypercube.getHypercube(3, new Point(150 * i, 150 * j, 0), 100, 100, 100);
                hypercubes[i][j] = Hypercube.getHypercube(3, new Point(random.nextInt(1500),random.nextInt(1500), 0), random.nextInt(100), random.nextInt(100), random.nextInt(100));
                hypercubes[i][j].revolve(new RevolveVector(3, random.nextInt(3), random.nextInt(3), Math.toRadians(random.nextInt(180))));

                hypercubes[i][j].generateLowDimensionalLinearGeometry();
                engine3D.getScene().objectSetAdd(hypercubes[i][j]);
                //对面设置着色方法
                hypercubes[i][j].traverse(2, linearGeometry -> {
                    linearGeometry.setColoration(new Coloration(0, new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256))));
                });

                hypercubes[i][j].attach(new Observer(engine3D::rendering));

            }
        }


        LinearGeometry linearGeometry = new LinearGeometry(1);
        linearGeometry.setLinearGeometry(0, new Point[]{new Point(0, 0, 0), new Point(10000, 0, 0)});
        engine3D.getScene().objectSetAdd(linearGeometry);
        linearGeometry.setColoration(new Coloration(0, Color.red));

        linearGeometry = new LinearGeometry(1);
        linearGeometry.setLinearGeometry(0, new Point[]{new Point(0, 0, 0), new Point(0, 10000, 0)});
        engine3D.getScene().objectSetAdd(linearGeometry);
        linearGeometry.setColoration(new Coloration(0, Color.green));


        linearGeometry = new LinearGeometry(1);
        linearGeometry.setLinearGeometry(0, new Point[]{new Point(0, 0, 0), new Point(0, 0, 10000)});
        engine3D.getScene().objectSetAdd(linearGeometry);
        linearGeometry.setColoration(new Coloration(0, Color.blue));

        LinearGeometry[] linearGeometries1 = new LinearGeometry[100];
        LinearGeometry[] linearGeometries2 = new LinearGeometry[100];
        for (int i = -linearGeometries1.length / 2; i < linearGeometries1.length / 2; i++) {
            if (i == 0) continue;
            linearGeometries1[i + linearGeometries1.length / 2] = new LinearGeometry(1);
            linearGeometries2[i + linearGeometries1.length / 2] = new LinearGeometry(1);
            linearGeometries1[i + linearGeometries1.length / 2].setLinearGeometry(0, new Point[]{new Point(i * 300, -30000, 0), new Point(i * 300, 30000, 0)});
            linearGeometries2[i + linearGeometries1.length / 2].setLinearGeometry(0, new Point[]{new Point(-30000, i * 300, 0), new Point(30000, i * 300, 0)});
            engine3D.getScene().objectSetAdd(linearGeometries1[i + linearGeometries1.length / 2]);
            engine3D.getScene().objectSetAdd(linearGeometries2[i + linearGeometries1.length / 2]);
            linearGeometries1[i + linearGeometries1.length / 2].setColoration(new Coloration(0, new Color(196, 196, 196)));
            linearGeometries2[i + linearGeometries1.length / 2].setColoration(new Coloration(0, new Color(196, 196, 196)));
        }

        Tool.afterAndContinue(0, 50, () -> {

            for (int i = 0; i < hypercubes.length; i++) {
                for (int j = 0; j < hypercubes[i].length; j++) {
//                    hypercubes[i][j].move(new MoveVector(random.nextInt(5)-2, random.nextInt(5)-2, random.nextInt(5)-2));
//                    hypercubes[i][j].revolve(new RevolveVector(3,random.nextInt(3),random.nextInt(3),Math.toRadians(random.nextInt(5)-2)));
                    hypercubes[i][j].revolve(new RevolveVector(3,0,1,Math.toRadians(100.0/i/j)));
//                    hypercubes[i][j].add(random.nextInt(3),random.nextInt(10)-5);
//                    ((Point)hypercubes[i][j].getLinearGeometry(0,random.nextInt(hypercubes[i][j].getLinearGeometry(0).length))).add(random.nextInt(3),random.nextInt(3)-1);
                }
            }
        });
    }
}
