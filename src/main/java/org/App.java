package org;

import org.Tool.Tool;
import org.gameStart.GameStart;
import org.mainFrame.MainJFrame;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        //禁用硬件加速
        System.setProperty("sun.java2d.d3d", "false");
//        System.setProperty("sun.java2d.opengl", "false");
//        System.setProperty("sun.java2d.xrender", "false");

        //框架入口
        MainJFrame mainJFrame = new MainJFrame();
        //游戏入口
        GameStart gameStart = new GameStart(mainJFrame);

//        Tool.after(3000,()->{
//            //框架入口
//            MainJFrame mainJFrame1 = new MainJFrame();
//            //游戏入口
//            GameStart gameStart1 = new GameStart(mainJFrame1);
//        });
    }
}
