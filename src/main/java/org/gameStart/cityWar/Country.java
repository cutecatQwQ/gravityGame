package org.gameStart.cityWar;

import org.mainFrame.PaintService;

import java.awt.*;
import java.util.Random;

public class Country {
    Random random = new Random();
    Population mainCity = new Population(100,100,10,1000, Color.BLACK){
        @Override
        public void numberChange(){
            super.numberChange();
            number+= (int) (-0.00000001*Math.pow((number-50000),2)+100)+(int)((random.nextInt(11)-5)*number*0.0005);
//            number+= (int) ((Math.sqrt(number)-0.000001*number*number+10000)/30)+(int)((random.nextInt(11)-5)*number*0.0005);
        }
    };
    public Country(){
//        PaintService.paintSet.add(mainCity);
    }
}
