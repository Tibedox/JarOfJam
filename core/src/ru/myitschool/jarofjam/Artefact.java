package ru.myitschool.jarofjam;

import static ru.myitschool.jarofjam.JarOfJam.*;

import com.badlogic.gdx.graphics.Texture;

public class Artefact {
    float x, y, width, height; // параметры артефакта
    float fx, fy, fwidth, fheight; // параметры места, куда его надо приспособить
    float newX, newY; // координаты арта, когда его приспособили
    float outWidth, outHeight; // размеры артефакта вне корзины
    float basketWidth, basketHeight; // размеры артефакта в корзине
    boolean inBasket; // в корзине ли артефакт
    boolean isMove; // перемещаем ли артефакт
    boolean isReleased; // использован ли артефакт
    int name;
    int startScreen, finishScreen; // на каком экране находится артефакт
    Texture img;
    Basket basket; // корзина нужна, чтобы посмотреть, попадает ли по размерам арт в корзину


    public Artefact(int name, float x, float y, float width, float height, int startScreen,
                    float fx, float fy, float fwidth, float fheight, int finishScreen,
                    float newX, float newY, Texture t, Basket basket) {
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
        this.basket = basket;
        img = t;
        basketWidth = 70*KX;
        basketHeight = 70*KY;
    }

    void drag(float x, float y){
        // если артефакт выполнил работу
        if(isReleased) return;

        // двигаем артефакт вслед за мышью
        if(isMove) setXY(x, y);

        // меняем размеры в зависимости от того, в корзине мы, или нет
        if(basket.isOpen && basket.hitInOpenBasket(x, y)) setCurrentWidthHeight(basketWidth, basketHeight);
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
}
