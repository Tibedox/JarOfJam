package ru.myitschool.jarofjam;

import static ru.myitschool.jarofjam.JarOfJam.CAVE;
import static ru.myitschool.jarofjam.JarOfJam.FIELD;
import static ru.myitschool.jarofjam.JarOfJam.HONEY;
import static ru.myitschool.jarofjam.JarOfJam.HOUSE;
import static ru.myitschool.jarofjam.JarOfJam.JAROFJAM;
import static ru.myitschool.jarofjam.JarOfJam.KX;
import static ru.myitschool.jarofjam.JarOfJam.KY;
import static ru.myitschool.jarofjam.JarOfJam.RASPBERRY;
import static ru.myitschool.jarofjam.JarOfJam.SCR_HEIGHT;
import static ru.myitschool.jarofjam.JarOfJam.SCR_WIDTH;
import static ru.myitschool.jarofjam.JarOfJam.SWAMP;
import static ru.myitschool.jarofjam.JarOfJam.TREE5;
import static ru.myitschool.jarofjam.JarOfJam.current_SCREEN;
import static ru.myitschool.jarofjam.JarOfJam.quest_JAM;
import static ru.myitschool.jarofjam.JarOfJam.quest_RIDERS;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class ScreenCave implements Screen {
    final JarOfJam j;
    JojButton btnDown, btnKattle;
    Texture imgBG, imgLeshiy, imgKattle;

    ScreenCave(JarOfJam j) {
        this.j = j;

        imgBG = new Texture("screens/cave.jpg");
        imgLeshiy = new Texture("leshiy.png");
        imgKattle = new Texture("kattle.png");

        // кнопка стрелка вниз
        btnDown = new JojButton(950*KX, 0*KY, 300*KX, 100*KY, 1000*KX, j.imgArrowDown);
        // кнопка котёл
        btnKattle = new JojButton(600*KX, 200*KY, 326*KX, 363*KY, 750*KX);
        // создаём артефакты, которые будут на этом уровне
    }

    @Override
    public void show() {
        current_SCREEN = CAVE;
    }

    @Override
    public void render(float delta) {
        // обработка касаний
        if(Gdx.input.justTouched()){
            j.touch.set((float)Gdx.input.getX(), (float)Gdx.input.getY(), 0);
            j.camera.unproject(j.touch);

            if (btnDown.hit(j.touch.x, j.touch.y)) j.girl.goToPlace(btnDown.girlWannaPlaceX);
            if (btnKattle.hit(j.touch.x, j.touch.y)) j.girl.goToPlace(btnKattle.girlWannaPlaceX);
        }

        // игровые события
        j.girl.move();
        // идём на экран RIDERS
        if(quest_RIDERS && j.girl.wannaPlaceX == btnKattle.girlWannaPlaceX && j.girl.came(j.girl.wannaPlaceX)) {
            j.girl.setX(SCR_WIDTH/2f);
            j.setScreen(j.screenRiders);
        }
        // идём на экран SWAMP
        if(j.girl.wannaPlaceX == btnDown.girlWannaPlaceX && j.girl.came(j.girl.wannaPlaceX)) {
            j.girl.setX(SCR_WIDTH/2f);
            j.setScreen(j.screenSwamp);
        }


        // если девочка дошла до места, куда положить артефакт, то он пропадает из корзины
        if(j.artefacts[JAROFJAM].hitFinish(j.girl.x) && j.girl.artefact == j.artefacts[JAROFJAM] &&
                j.artefacts[JAROFJAM].inBasket && quest_JAM) {
            j.basket.removeArtefact(j.artefacts[JAROFJAM]);
            j.girl.artefact = null;
            quest_RIDERS = true;
        }

        // отрисовка
        j.camera.update();
        j.batch.setProjectionMatrix(j.camera.combined);
        j.batch.begin();
        j.batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        j.batch.draw(imgLeshiy, 1200*KX, 200*KY, 345*KX, 628*KY);
        j.batch.draw(imgKattle, 600*KX, 200*KY, 326*KX, 363*KY);

        // артефакты не в корзине
        for (Artefact a : j.artefacts)
            if (a != null && !a.inBasket && (a.startScreen == current_SCREEN && !a.isReleased || a.finishScreen == current_SCREEN && a.isReleased))
                j.batch.draw(j.imgArt[a.name], a.x, a.y, a.width, a.height);

        // девочка
        j.batch.draw(j.imgGirl[j.girl.faza], j.girl.x - j.girl.width / 2, j.girl.y, j.girl.width / 2, 0, j.girl.width, j.girl.height, j.girl.goLeft ? 1 : -1, 1, 0);

        // корзина
        if (j.basket.isOpen) {
            j.batch.draw(j.imgPanel, 50 * KX, 20 * KY, SCR_WIDTH - 70 * KX, 100 * KY);
            for (int i = 0; i < j.basket.artefacts.size(); i++)
                j.batch.draw(j.imgArt[j.basket.artefacts.get(i).name], j.basket.artefacts.get(i).x, j.basket.artefacts.get(i).y, j.basket.artefacts.get(i).width, j.basket.artefacts.get(i).height);
        }
        j.batch.draw(j.imgBasket, j.basket.x, j.basket.y, j.basket.width, j.basket.height); // сама корзинка
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
