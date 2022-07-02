package ru.myitschool.jarofjam;

import static ru.myitschool.jarofjam.JarOfJam.*;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

public class Artefact {
    final JarOfJam j;
    float x, y, width, height; // параметры артефакта
    float vx, vy;
    float fx, fy, fwidth, fheight; // параметры места, куда его надо приспособить
    float newX, newY; // координаты арта, когда его приспособили
    float outWidth, outHeight; // размеры артефакта вне корзины
    float basketWidth, basketHeight; // размеры артефакта в корзине
    boolean inBasket; // в корзине ли артефакт
    boolean isMove; // перемещаем ли артефакт
    boolean isReleased; // использован ли артефакт
    int name;
    int startScreen, finishScreen; // на каком экране находится артефакт
    //Texture img;
    //Basket basket; // корзина нужна, чтобы посмотреть, попадает ли по размерам арт в корзину

    public Artefact(int name, float x, float y, float width, float height, int startScreen,
                    float fx, float fy, float fwidth, float fheight, int finishScreen,
                    float newX, float newY, JarOfJam joj) {
        j = joj;
        this.x = x;
        this.y = y;
        this.outWidth = width;
        this.outHeight = height;
        setCurrentWidthHeight(width, height);
        this.fx = fx;
        this.fy = fy;
        this.fwidth = fwidth;
        this.fheight = fheight;
        this.newX = newX;
        this.newY = newY;
        this.name = name;
        this.startScreen = startScreen;
        this.finishScreen = finishScreen;
        float k = width > height ? 60 * KX/width : 60*KY/height;
        basketWidth = width*k;
        basketHeight = height*k;
        vx = MathUtils.random(2, 10);
        vy = MathUtils.random(2, 10);
    }

    void drag(float x, float y){
        // если артефакт выполнил работу
        if(isReleased) return;

        // двигаем артефакт вслед за мышью
        if(isMove) setXY(x, y);

        // меняем размеры в зависимости от того, в корзине мы, или нет
        if(j.basket.isOpen && j.basket.hitInOpenBasket(x, y)) setCurrentWidthHeight(basketWidth, basketHeight);
        else setCurrentWidthHeight(outWidth, outHeight);
    }

    // помещаем артефакт в корзину
    void goToBasket(){
        setCurrentWidthHeight(basketWidth, basketHeight);
        inBasket = true;
    }

    // убираем артефакт из корзины
    void goOutBasket(){
        inBasket = false;
        isReleased = true;
        x = newX;
        y = newY;
        setCurrentWidthHeight(outWidth, outHeight);
        startScreen = finishScreen; // это нужно, чтобы он рисовался на новом экпане
    }

    // устанавливаем текущие высоту и ширину
    void setCurrentWidthHeight(float width, float height){
        this.width = width;
        this.height = height;
    }

    // устанавливаем текущие координаты
    void setXY(float x, float y){
        this.x = x-width/2;
        this.y = y-height/2;
    }

    boolean hit(float tx, float ty) {
        return tx > x && tx < x+width && ty > y && ty < y+height;
    }

    boolean hit(float tx) {
        return tx > x && tx < x+width;
    }

    boolean hitFinish(float tx, float ty) {
        return tx > fx && tx < fx+fwidth && ty > fy && ty < fy+fheight;
    }
    
    boolean hitFinish(float tx) {
        return tx > fx && tx < fx+fwidth;
    }

    void fly(){
        x+=vx;
        y+=vy;
        if(x<width || x>SCR_WIDTH-width) vx = -vx;
        if(y<height*2 || y>SCR_HEIGHT-height) vy = -vy;
    }
}
