package ru.myitschool.jarofjam;

import static ru.myitschool.jarofjam.JarOfJam.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class ScreenField implements Screen {
    final JarOfJam j;
    JojButton btnGoGarden, btnGoForrest;
    Texture imgBG, imgBush;

    ScreenField(JarOfJam j) {
        this.j = j;

        imgBG = new Texture("screens/field.jpg");
        imgBush = new Texture("bush.png");

        // кнопка переход в сад и в лес
        btnGoGarden = new JojButton(SCR_WIDTH-j.girl.width/2, 200*KY, SCR_WIDTH-j.girl.width/2-100*KX, 300*KY, SCR_WIDTH-j.girl.width/2);
        btnGoForrest = new JojButton(0, 200*KY, 100*KX, 300*KY, j.girl.width/2);

        // создаём артефакты, которые будут на этом уровне
        j.artefacts[RASPBERRY] = new Artefact(RASPBERRY, 568*KX, 176*KY, 200*KX, 150*KY, FIELD, 1660*KX, 625*KY, 150*KX, 115*KY, HOUSE, -1660*KX, 0*KY, j);
    }

    @Override
    public void show() {
        current_SCREEN = FIELD;
    }

    @Override
    public void render(float delta) {
        // обработка касаний
        if(Gdx.input.justTouched()){
            j.touch.set((float)Gdx.input.getX(), (float)Gdx.input.getY(), 0);
            j.camera.unproject(j.touch);

            if (btnGoGarden.hit(j.touch.x, j.touch.y)) j.girl.goToPlace(btnGoGarden.girlWannaPlaceX);
            if (btnGoForrest.hit(j.touch.x, j.touch.y)) j.girl.goToPlace(btnGoForrest.girlWannaPlaceX);
        }

        // игровые события
        j.girl.move();
        // идём на экран HOME
        if(j.girl.wannaPlaceX == btnGoGarden.girlWannaPlaceX && j.girl.x == j.girl.wannaPlaceX) {
            j.girl.setX(j.girl.width/2);
            j.setScreen(j.screenGarden);
        }
        // идём на экран FORREST
        if(j.girl.wannaPlaceX == btnGoForrest.girlWannaPlaceX && j.girl.x == j.girl.wannaPlaceX) {
            j.girl.setX(SCR_WIDTH-j.girl.width/2);
            j.setScreen(j.screenForrest);
        }

        // если девочка дошла до артефакта, то он попадает в корзину
        if(j.artefacts[RASPBERRY].hit(j.girl.x) && j.girl.artefact == j.artefacts[RASPBERRY] && !j.artefacts[RASPBERRY].inBasket) {
            j.basket.addArtefact(j.artefacts[RASPBERRY]);
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

        j.batch.draw(imgBush, 0, 0, SCR_WIDTH, 200*KY);

        // корзина
        if (j.basket.isOpen) {
            j.batch.draw(j.imgPanel, 50 * KX, 20 * KY, SCR_WIDTH - 70 * KX, 100 * KY);
            for (int i = 0; i < j.basket.artefacts.size(); i++)
                j.batch.draw(j.imgArt[j.basket.artefacts.get(i).name], j.basket.artefacts.get(i).x, j.basket.artefacts.get(i).y, j.basket.artefacts.get(i).width, j.basket.artefacts.get(i).height);
        }
        j.batch.draw(j.imgBasket, j.basket.x, j.basket.y, j.basket.width, j.basket.height); // сама корзинка
        j.batch.draw(j.imgCross, j.btnGoMenu.x, j.btnGoMenu.y, j.btnGoMenu.width, j.btnGoMenu.height); // выход в главное меню

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

