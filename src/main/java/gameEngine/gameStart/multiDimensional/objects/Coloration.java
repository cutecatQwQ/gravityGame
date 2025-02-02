package gameEngine.gameStart.multiDimensional.objects;

import java.awt.*;

//着色方法
public class Coloration {
    //着色方法 0唯一颜色 1渐变 2图片填充
    private int mode;
    private Color[] colors;

    public Coloration(int mode, Color color) {
        this.mode = mode;
        colors = new Color[1];
        colors[0] = color;
    }
    //默认mode = 0
    public void setColor(Color color){
        colors[0] = color;
    }
    //默认mode = 0
    public Color getColor(){
        return colors[0];
    }
}
