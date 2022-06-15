package ru.myitschool.jarofjam;

import static ru.myitschool.jarofjam.JarOfJam.*;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Align;

public class ScreenIntro implements Screen {
    final JarOfJam j;
    public static final int N = 5;
    Texture imgBG;
    String[] text = new String[N];
    int n; // счётчик нажатий

    ScreenIntro(JarOfJam j) {
        this.j = j;
        text[0] = "В одной из деревушек Омской области\n" +
                "жила себе да поживала девочка по имени Алёнушка.\n";
        text[1] = "И носила она чудный бордовый шарфик,\n" +
                "который связала ей её любимая бабушка.";
        text[2] = "Но в последнее время бабушка плохо себя чувствовала\n" +
                "и вскоре совсем слегла.";
        text[3] = "Никто не знал, что за недуг такой страшный её одолел,\n" +
                "но люди поговаривали, что виноват в том вирус был заморский!";
        text[4] = "Решила внучка бабуле помочь и отправилась на поиски лекарства.\n" +
                "А уж что ей испытать предстоит, мы с вами сейчас и узнаем...";
        imgBG = new Texture("intro/intro0.jpg");
    }

    public void show() {
        current_SCREEN = INTRO;
    }

    public void render(float delta) {
        if (Gdx.input.justTouched()) {
            j.touch.set((float)Gdx.input.getX(), (float)Gdx.input.getY(), 0);
            j.camera.unproject(j.touch);
            n++;
            if (n == N) {
                n = 0;
                j.setScreen(j.screenMenu);
            }
            imgBG = new Texture("intro/intro"+n+".jpg");
        }

        j.camera.update();
        j.batch.setProjectionMatrix(j.camera.combined);
        j.batch.begin();
        j.batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);

        j.fontIntro.draw(j.batch, text[n], SCR_WIDTH/10, 200*KY, SCR_WIDTH*4/5, Align.center, false);

        j.batch.end();
    }

    public void resize(int width, int height) {
    }

    public void pause() {
    }

    public void resume() {
    }

    public void hide() {
    }

    public void dispose() {
        imgBG.dispose();
    }
}
