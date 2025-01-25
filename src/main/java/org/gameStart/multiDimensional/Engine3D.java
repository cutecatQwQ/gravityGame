package org.gameStart.multiDimensional;

import org.Tool.Tool;
import org.gameStart.multiDimensional.objects.Point;
import org.gameStart.multiDimensional.objects.Vector;
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
import java.util.LinkedList;

public class Engine3D {
    //窗口
    private Window window;
    //场景
    private Scene scene;
    //摄像机
    private Camera camera;
    //相机监听
    private CameraController cameraController;
    //UI组件
    private ArrayList<Model> models;

    //当前帧是否渲染
    boolean render;

    public Engine3D() {
        this.window = new Window(0, 0, MainJFrame.dimension.width, MainJFrame.dimension.height);
        this.scene = new Scene();
        this.camera = new Camera(new Point(0, 0, 0), new Vector(-1, -1, -1), 1000);
        this.cameraController = new CameraController(camera);
        this.models = new ArrayList<>();
        this.render = true;
    }

    public void init(MainJFrame mainJFrame) {
        System.out.println("3D engine init start！");

        window.setCamera(camera);
        window.addListener(cameraController);
        mainJFrame.getPaintService().paintSetAdd(window);
        mainJFrame.getListenService().mouseSetAdd(window);
        mainJFrame.getListenService().keySetAdd(window);

        initUI(mainJFrame);

        //添加相机变动渲染观察者
        camera.location.attach(new Observer(this::rendering));
        camera.direction.attach(new Observer(this::rendering));
        camera.attach(new Observer(this::rendering));

        //检查当前帧是否需要渲染，如果需要进行渲染
        Tool.afterAndContinue(100,10,()->{
            if(render) {
                scene.rendering(window);
                render = false;
            }
        });

        System.out.println("3D engine init over");
    }

    public void initUI(MainJFrame mainJFrame) {
        BoxAndTextModel boxAndTextModel;

        //0 投影按钮
        boxAndTextModel = new BoxAndTextModel(0, 0, 100, 30, new Color(225, 225, 225), new Color(64, 64, 64), 0.6, window.getCamera().getMode() == 0 ? "正交投影" : "透视投影");
        models.add(boxAndTextModel);
        boxAndTextModel.setPriority(-1);
        boxAndTextModel.addListener(new ButtonListener(boxAndTextModel, mainJFrame) {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (window.getCamera().getMode() == 0) {
                    window.getCamera().setMode(1);
                    ((BoxAndTextModel) getModel()).setText("透视投影");
                } else {
                    window.getCamera().setMode(0);
                    ((BoxAndTextModel) getModel()).setText("正交投影");
                }
                //渲染一下
                rendering();
            }
        });

        //1 移动按钮
        boxAndTextModel = new BoxAndTextModel(110, 0, 120, 30, new Color(225, 225, 225), new Color(64, 64, 64), 0.6, "沿世界系移动");
        models.add(boxAndTextModel);
        boxAndTextModel.setPriority(-1);
        boxAndTextModel.addListener(new ButtonListener(boxAndTextModel, mainJFrame) {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (cameraController.getMode() == 0) {
                    cameraController.setMode(1);
                    ((BoxAndTextModel) getModel()).setText("沿相机系移动");
                } else {
                    cameraController.setMode(0);
                    ((BoxAndTextModel) getModel()).setText("沿世界系移动");
                }
            }
        });

        //2 显示焦距
        boxAndTextModel = new BoxAndTextModel(0, 35, 60, 30, Color.white, Color.black, 0.4, "焦距");
        models.add(boxAndTextModel);
        boxAndTextModel.setPriority(-1);
        //345 焦距
        ScrollBar scrollBar0 = new ScrollBar(60, 35, 200, 30, 60, 0.4, 500, 5000, new Color(192, 192, 192), Color.white, Color.black, Color.gray);
        models.add(scrollBar0);
        models.add(scrollBar0.getTextModel());
        models.add(scrollBar0.getButtonModel());
        scrollBar0.setPriority(-1);
        scrollBar0.setContent(1000);
        scrollBar0.getButtonModel().addListener(new MouseAndKeyListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                camera.setFocalLength(scrollBar0.getContent());
            }
        });


        //6 显示灵敏度
        boxAndTextModel = new BoxAndTextModel(0, 70, 60, 30, Color.white, Color.black, 0.4, "灵敏度");
        models.add(boxAndTextModel);
        boxAndTextModel.setPriority(-1);
        //789 鼠标灵敏度
        ScrollBar scrollBar1 = new ScrollBar(60, 70, 200, 30, 60, 0.4, 0.01, 1, new Color(192, 192, 192), Color.white, Color.black, Color.gray);
        models.add(scrollBar1);
        models.add(scrollBar1.getTextModel());
        models.add(scrollBar1.getButtonModel());
        scrollBar1.setPriority(-1);
        scrollBar1.setContent(0.1);
        scrollBar1.getButtonModel().addListener(new MouseAndKeyListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                camera.setSensitivity(scrollBar1.getContent());
            }
        });


        //10 显示坐标
        boxAndTextModel = new BoxAndTextModel(0, 100, 500, 30, Color.white, Color.black, 0.4, "坐标: " + Arrays.toString(camera.location.getCoordinates()));
        models.add(boxAndTextModel);
        boxAndTextModel.setPriority(-1);
        //添加观察者
        camera.location.attach(new Observer(() -> {
            ((BoxAndTextModel) models.get(10)).setText("坐标: " + Arrays.toString(camera.location.getCoordinates()));
        }));

        //11 显示朝向
        boxAndTextModel = new BoxAndTextModel(0, 130, 500, 30, Color.white, Color.black, 0.4, "朝向: " + Arrays.toString(camera.direction.getCoordinates()));
        models.add(boxAndTextModel);
        boxAndTextModel.setPriority(-1);
        //添加观察者
        camera.direction.attach(new Observer(() -> {
            ((BoxAndTextModel) models.get(11)).setText("朝向: " + Arrays.toString(camera.direction.getCoordinates()));
        }));

        //将ui添加进去
        models.forEach(model -> mainJFrame.getPaintService().paintSetAdd(model));
        models.forEach(model -> mainJFrame.getListenService().mouseSetAdd(model));
    }

    public Window getWindow() {
        return window;
    }

    public void setWindow(Window window) {
        this.window = window;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public ArrayList<Model> getModels() {
        return models;
    }

    public void setModels(ArrayList<Model> models) {
        this.models = models;
    }

    public void rendering() {
        render = true;
    }
}
