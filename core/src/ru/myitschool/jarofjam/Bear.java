package ru.myitschool.jarofjam;

import static ru.myitschool.jarofjam.JarOfJam.SCR_WIDTH;

import com.badlogic.gdx.utils.TimeUtils;

import java.util.concurrent.TimeUnit;

public class Bear {
    float x, y;
    float vx;
    float width, height;
    int faza, nFaz = 4;
    long time, timeInterval=100;

    Bear(){
        width = 714;
        height = 400;
        x = 428;
        y = 230;
        vx = 40;
    }

    void go(){
        if(time+timeInterval< TimeUtils.millis()) {
            time = TimeUtils.millis();
            x += vx;
            if (x > SCR_WIDTH) vx = 0;
            if (++faza == nFaz) faza = 0;
        }
    }
}
