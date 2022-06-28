package ru.myitschool.jarofjam;

import static ru.myitschool.jarofjam.JarOfJam.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class ScreenMenu implements Screen {
    final JarOfJam j;

    JojButton btnIntro, btnPlay, btnLoad, btnSave, btnExit;
    Texture imgBG;

    ScreenMenu(JarOfJam j) {
        this.j = j;
        imgBG = new Texture("screens/menu.jpg");

        btnIntro = new JojButton("Вступление", j.fontMenu, 960*KX, (128+5*160)*KY);
        btnPlay = new JojButton("Играть", j.fontMenu, 960*KX, (128+4*160)*KY);
        btnLoad = new JojButton("Загрузить", j.fontMenu, 960*KX, (128+3*160)*KY);
        btnSave = new JojButton("Сохранить", j.fontMenu, 960*KX, (128+2*160)*KY);
        btnExit = new JojButton("Выход", j.fontMenu, 960*KX, (128+1*160)*KY);
    }

    public void show() {
        current_SCREEN = MENU;
    }

    public void render(float delta) {
        if (Gdx.input.justTouched()) {
            j.touch.set((float)Gdx.input.getX(), (float)Gdx.input.getY(), 0);
            j.camera.unproject(j.touch);

            if (btnIntro.hit(j.touch.x, j.touch.y)) {
                j.setScreen(getScreen(INTRO));
            }

            if (btnPlay.hit(j.touch.x, j.touch.y)) {
                j.setScreen(getScreen(previous_SCREEN));
            }

            if (btnLoad.hit(j.touch.x, j.touch.y)) {
                j.loadGame();
                j.setScreen(getScreen(previous_SCREEN));
            }

            if (btnSave.hit(j.touch.x, j.touch.y)) {
                j.saveGame();
                j.setScreen(getScreen(previous_SCREEN));
            }

            if (btnExit.hit(j.touch.x, j.touch.y)) {
                Gdx.app.exit();
            }
        }

        j.camera.update();
        j.batch.setProjectionMatrix(j.camera.combined);
        j.batch.begin();
        j.batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);

        j.fontMenu.draw(j.batch, btnIntro.text, btnIntro.x, btnIntro.y);
        j.fontMenu.draw(j.batch, btnPlay.text, btnPlay.x, btnPlay.y);
        j.fontMenu.draw(j.batch, btnLoad.text, btnLoad.x, btnLoad.y);
        j.fontMenu.draw(j.batch, btnSave.text, btnSave.x, btnSave.y);
        j.fontMenu.draw(j.batch, btnExit.text, btnExit.x, btnExit.y);

        j.batch.end();
    }

    Screen getScreen(int screen){
        switch(screen){
            case HOUSE: return j.screenHouse;
            case INTRO: return j.screenIntro;
            case CHULAN: return j.screenChulan;
            case GARDEN: return j.screenGarden;
            case FIELD: return j.screenField;
            case FORREST: return j.screenForrest;
            case SWAMP: return j.screenSwamp;
            case CAVE: return j.screenCave;
            case RIDERS: return j.screenRiders;
            default: return j.screenMenu;
        }
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