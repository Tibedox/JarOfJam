package ru.myitschool.jarofjam;

import static ru.myitschool.jarofjam.JarOfJam.GARDEN;
import static ru.myitschool.jarofjam.JarOfJam.HOUSE;
import static ru.myitschool.jarofjam.JarOfJam.KX;
import static ru.myitschool.jarofjam.JarOfJam.KY;
import static ru.myitschool.jarofjam.JarOfJam.SCR_HEIGHT;
import static ru.myitschool.jarofjam.JarOfJam.SCR_WIDTH;
import static ru.myitschool.jarofjam.JarOfJam.STRAWBERRY;
import static ru.myitschool.jarofjam.JarOfJam.current_SCREEN;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class ScreenGarden implements Screen {
    final JarOfJam j;
    JojButton btnLeft, btnRight;
    Texture imgBG;

    ScreenGarden(JarOfJam j) {
        this.j = j;

        imgBG = new Texture("screens/garden.jpg");

        // кнопки перехода в дом и на поле
        btnLeft = new JojButton(0, 200*KY, 100*KX, 300*KY, j.girl.width/2, j.imgArrowLeft);
        btnRight = new JojButton(SCR_WIDTH-100*KX, 200*KY, 100*KX, 300*KY, SCR_WIDTH-j.girl.width/2, j.imgArrowRight);

        // создаём артефакты, которые будут на этом уровне
        j.artefacts[STRAWBERRY] = new Artefact(STRAWBERRY, 887*KX, 400*KY, 85*KX, 89*KY, GARDEN, 1660*KX, 625*KY, 150*KX, 115*KY, HOUSE, -1660*KX, 0*KY, j);
    }

    @Override
    public void show() {
        current_SCREEN = GARDEN;
    }

    @Override
    public void render(float delta) {
        // обработка касаний
        if(Gdx.input.justTouched()){
            j.touch.set((float)Gdx.input.getX(), (float)Gdx.input.getY(), 0);
            j.camera.unproject(j.touch);

            if (btnLeft.hit(j.touch.x, j.touch.y)) j.girl.goToPlace(btnLeft.girlWannaPlaceX);
            if (btnRight.hit(j.touch.x, j.touch.y)) j.girl.goToPlace(btnRight.girlWannaPlaceX);
        }

        // игровые события
        j.girl.move();
        // идём на экран FIELD
        if(j.girl.wannaPlaceX == btnLeft.girlWannaPlaceX && j.girl.x == j.girl.wannaPlaceX) {
            j.girl.setX(SCR_WIDTH-j.girl.width/2);
            j.setScreen(j.screenField);
        }
        // идём на экран HOUSE
        if(j.girl.wannaPlaceX == btnRight.girlWannaPlaceX && j.girl.x == j.girl.wannaPlaceX) {
            j.girl.setX(j.girl.width/2);
            j.setScreen(j.screenHouse);
        }
        // если девочка дошла до артефакта, то он попадает в корзину
        if(j.artefacts[STRAWBERRY].hit(j.girl.x) && j.girl.artefact == j.artefacts[STRAWBERRY] && !j.artefacts[STRAWBERRY].inBasket) {
            j.basket.addArtefact(j.artefacts[STRAWBERRY]);
            j.girl.artefact = null;
        }

        // отрисовка
        j.camera.update();
        j.batch.setProjectionMatrix(j.camera.combined);
        j.batch.begin();
        j.batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);

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
        j.batch.draw(btnLeft.img, btnLeft.x, btnLeft.y, btnLeft.width, btnLeft.height); // стрелка влево
        j.batch.draw(btnRight.img, btnRight.x, btnRight.y, btnRight.width, btnRight.height); // стрелка вправо

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
