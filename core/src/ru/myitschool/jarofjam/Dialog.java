package ru.myitschool.jarofjam;

import static ru.myitschool.jarofjam.JarOfJam.*;

public class Dialog {
    String words;
    float x, y;

    public Dialog(String words, float x, float y) {
        this.words = words;
        this.x = x*KX;
        this.y = y*KY;
    }
}
