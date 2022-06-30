package ru.myitschool.jarofjam;

import static ru.myitschool.jarofjam.JarOfJam.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class ScreenEnd implements Screen {
    final JarOfJam j;
    JojButton btnDown;
    Texture imgBG;

    ScreenEnd(JarOfJam j) {
        this.j = j;

        imgBG = new Texture("screens/happyend.jpg");

        // кнопка стрелка вниз
        btnDown = new JojButton(850*KX, 0*KY, 300*KX, 100*KY, 1000*KX, j.imgArrowDown);

    }

    @Override
    public void show() {
        current_SCREEN = END;
    }

    @Override
    public void render(float delta) {
        // обработка касаний
        if(Gdx.input.justTouched()){
            j.touch.set((float)Gdx.input.getX(), (float)Gdx.input.getY(), 0);
            j.camera.unproject(j.touch);

            if (btnDown.hit(j.touch.x, j.touch.y)) j.girl.goToPlace(btnDown.girlWannaPlaceX);
        }

        // игровые события
        j.girl.move();
        // идём на экран CHULAN
        if(j.girl.wannaPlaceX == btnDown.girlWannaPlaceX && j.girl.x == j.girl.wannaPlaceX) {
            j.girl.setX(j.girl.width/2);
            j.setScreen(j.screenMenu);
        }

        // отрисовка
        j.camera.update();
        j.batch.setProjectionMatrix(j.camera.combined);
        j.batch.begin();
        j.batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);

        j.batch.draw(j.imgCross, j.btnGoMenu.x, j.btnGoMenu.y, j.btnGoMenu.width, j.btnGoMenu.height); // выход в главное меню
        j.batch.draw(btnDown.img, btnDown.x, btnDown.y, btnDown.width, btnDown.height); // стрелка вниз

        j.batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
