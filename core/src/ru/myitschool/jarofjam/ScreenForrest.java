package ru.myitschool.jarofjam;

import static ru.myitschool.jarofjam.JarOfJam.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class ScreenForrest implements Screen {
    final JarOfJam j;
    JojButton btnGoField;
    Texture imgBG;

    ScreenForrest(JarOfJam j) {
        this.j = j;

        imgBG = new Texture("forrest.jpg");

        // кнопка переход в FIELD
        btnGoField = new JojButton(SCR_WIDTH-j.girl.width/2, 200*KY, SCR_WIDTH-j.girl.width/2-100*KX, 300*KY, SCR_WIDTH-j.girl.width/2);


        // создаём артефакты, которые будут на этом уровне
  /*  j.artefacts[STRAWBERRY] = new Artefact(STRAWBERRY, 863*KX, 183*KY, 150*KX, 150*KY, FIELD,
            1660*KX, 625*KY, 150*KX, 115*KY, HOUSE, 1660*KX, 600*KY, j.imgStrawberry, j.basket);*/
    }

    @Override
    public void show() {
        current_SCREEN = FORREST;
        j.saveGame();
    }

    @Override
    public void render(float delta) {
        // обработка касаний
        if(Gdx.input.justTouched()){
            j.touch.set((float)Gdx.input.getX(), (float)Gdx.input.getY(), 0);
            j.camera.unproject(j.touch);

            if (btnGoField.hit(j.touch.x, j.touch.y)) {
                j.girl.goToPlace(btnGoField.girlWannaPlaceX);
            }

        }

        // игровые события
        j.girl.move();
        // идём на экран HOME
        if(j.girl.wannaPlaceX == btnGoField.girlWannaPlaceX && j.girl.x == j.girl.wannaPlaceX) {
            j.girl.came(j.girl.width/2);
            j.setScreen(j.screenField);
        }

        // если девочка дошла до места, куда положить артефакт, то он пропадает из корзины
        if(j.artefacts[ROPE].hitFinish(j.girl.x) && j.girl.artefact == j.artefacts[ROPE] && j.artefacts[ROPE].inBasket) {
            quest_ROPE = true;
            j.basket.removeArtefact(j.artefacts[ROPE]);
            j.girl.artefact = null;
        }


        // отрисовка
        j.camera.update();
        j.batch.setProjectionMatrix(j.camera.combined);
        j.batch.begin();
        j.batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);

        // артефакты не в корзине
        for(Artefact a: j.artefacts)
            if(a != null && !a.inBasket && a.startScreen == FORREST)
                j.batch.draw(a.img, a.x, a.y, a.width, a.height);

        // девочка
        j.batch.draw(j.imgGirl[j.girl.faza], j.girl.x-j.girl.width/2, j.girl.y, j.girl.width/2, 0, j.girl.width, j.girl.height, j.girl.goLeft?1:-1, 1, 0);

        // корзина
        if(j.basket.isOpen) {
            j.batch.draw(j.imgPanel, 50*KX, 20*KY, SCR_WIDTH-70*KX, 100 * KY);
            for (int i = 0; i < j.basket.artefacts.size(); i++)
                j.batch.draw(j.basket.artefacts.get(i).img, j.basket.artefacts.get(i).x, j.basket.artefacts.get(i).y, j.basket.artefacts.get(i).width, j.basket.artefacts.get(i).height);
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
