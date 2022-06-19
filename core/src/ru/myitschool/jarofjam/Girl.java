package ru.myitschool.jarofjam;

import static ru.myitschool.jarofjam.JarOfJam.*;

import com.badlogic.gdx.utils.TimeUtils;

public class Girl {
    float x, y;
    float step;
    float width, height;
    float targetX;
    boolean goLeft;
    boolean isGo;
    int faza;
    int nFaz = 7;
    long timeLastDo;
    long timeInterval = 50L;
    Artefact artefact;
    float wannaPlaceX;

    public Girl(float x, float y) {
        this.x = x;
        this.y = y;
        targetX = x;
        width = 400*KX;
        height = 400*KY;
        step = 40*KX;
    }

    void setTarget(float x){
        targetX = x;
        if(targetX<width/2) targetX=width/2;
        if(targetX>SCR_WIDTH-width/2) targetX=SCR_WIDTH-width/2;
    }

    void goToArtefact(Artefact artefact, float x){
        this.artefact = artefact;
        targetX = x;
    }

    void goToPlace(float x){
        wannaPlaceX = x;
        targetX = x;
    }

    void setX(float x){
        this.x = x;
        setTarget(x);
        wannaPlaceX = 0;
    }

    boolean came(float x){
        return this.x>x-width/2 && this.x<x+width/2;
    }

    void move() {
        if(x != targetX) isGo = true;
        else isGo = false;

        if (timeLastDo + timeInterval < TimeUtils.millis()) {
            timeLastDo = TimeUtils.millis();
            if(Math.abs(x-targetX)< step) x = targetX;
            if(x<targetX){
                x += step;
                goLeft = false;
            }
            if(x>targetX){
                x -= step;
                goLeft = true;
            }

            if(isGo) {
                if (++faza == nFaz) faza = 1;
            }
            else faza = 0;
        }
    }
}