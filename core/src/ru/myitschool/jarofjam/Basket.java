package ru.myitschool.jarofjam;

import static ru.myitschool.jarofjam.JarOfJam.*;

import java.util.ArrayList;

public class Basket {
    ArrayList<Artefact> artefacts = new ArrayList<>();
    float x, y, width, height;
    float openX, openY, openWidth, openHeight; // параметры раскрытой панели
    boolean isOpen;

    public Basket(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        openX = 50*KX;
        openY = 20*KY;
        openWidth = SCR_WIDTH-70*KX;
        openHeight = 100 * KY;
    }

    void addArtefact(Artefact artefact){
        artefact.goToBasket();
        artefacts.add(artefact);
        setArtefactsXY();
    }

    void removeArtefact(Artefact artefact){
        for(int i=0; i<artefacts.size(); i++)
            if(artefact == artefacts.get(i)) {
                artefact.goOutBasket();
                artefacts.remove(i);
            }
    }

    void setArtefactsXY(){
        for(int i=0; i<artefacts.size(); i++) {
            artefacts.get(i).goToBasket();
            artefacts.get(i).x = 200 * KX + i * (artefacts.get(i).basketWidth+10*KX);
            artefacts.get(i).y = 35 * KY;
        }
    }

    boolean hit(float tx, float ty) {
        return tx > x && tx < x+width && ty > y && ty < y+height;
    }

    boolean hitInOpenBasket(float tx, float ty){
        return tx>x && tx<x+openWidth && ty>y && ty<y+openHeight;
    }
}
