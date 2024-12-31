package org.gameStart.multiDimensional;

import org.Tool.Tool;
import org.mainFrame.MainJFrame;
import org.mainFrame.mouseAndKeyLister.MouseAndKeyLister;

import java.awt.event.KeyEvent;

public class CameraController extends MouseAndKeyLister {
    Camera camera;

    boolean u, i, o, j, k, l;

    public CameraController(Camera camera, MainJFrame mainJFrame) {
        this.camera = camera;
        this.u = false;
        this.i = false;
        this.o = false;
        this.j = false;
        this.k = false;
        this.l = false;
        final int[] index = {0};
        Tool.afterAndContinue(10, 50, () -> {
//            double startTime = 0;
//            if(u||i||o||j||k||l)startTime = System.currentTimeMillis();
            if (u) camera.revolve(0, 1, 1);
            if (i) camera.revolve(0, 2, 1);
            if (o) camera.revolve(1, 2, 1);
            if (j) camera.revolve(0, 3, 1);
            if (k) camera.revolve(1, 3, 1);
            if (l) camera.revolve(2, 3, 1);
            if(u||i||o||j||k||l) MultiDimensionalInit.change(camera);
            if(u||i||o||j||k||l) {
//                System.out.println(""+u+i+o+j+k+l);
//                Tool.addDate(index[0]++,System.currentTimeMillis()-startTime);
            }
        });
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.print(e.getKeyChar());
        switch (e.getKeyChar()) {
            case 'U':
                u = true;
                break;
            case 'I':
                i = true;
                break;
            case 'O':
                o = true;
                break;
            case 'J':
                j = true;
                break;
            case 'K':
                k = true;
                break;
            case 'L':
                l = true;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyChar()) {
            case 'U':
                u = false;
                break;
            case 'I':
                i = false;
                break;
            case 'O':
                o = false;
                break;
            case 'J':
                j = false;
                break;
            case 'K':
                k = false;
                break;
            case 'L':
                l = false;
                break;
        }
    }
}
