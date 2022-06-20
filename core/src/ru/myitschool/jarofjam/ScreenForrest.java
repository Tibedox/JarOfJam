package ru.myitschool.jarofjam;

import static ru.myitschool.jarofjam.JarOfJam.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class ScreenForrest implements Screen {
    final JarOfJam j;
    JojButton btnRight, btnRemoveStone, btnGoSwamp;
    Texture imgBG;
    Texture imgRock, imgBear;

    ScreenForrest(JarOfJam j) {
        this.j = j;

        imgBG = new Texture("screens/forrest.jpg");
        imgBear = new Texture("bear.png");
        imgRock = new Texture("rock.png");

        // кнопка переход в FIELD
        // кнопки стрелки
        //btnLeft = new JojButton(0, 200*KY, 100*KX, 300*KY, j.girl.width/2, j.imgArrowLeft);
        btnRight = new JojButton(SCR_WIDTH-100*KX, 200*KY, 100*KX, 300*KY, SCR_WIDTH-j.girl.width/2, j.imgArrowRight);
        btnRemoveStone = new JojButton(255 * KX, 315 * KY, 150 * KX, 145 * KY, 255 * KX);
        btnGoSwamp = new JojButton(160 * KX, 270 * KY, 280 * KX, 400 * KY, 200*KX);

        // создаём артефакты, которые будут на этом уровне
  /*  j.artefacts[STRAWBERRY] = new Artefact(STRAWBERRY, 863*KX, 183*KY, 150*KX, 150*KY, FIELD,
            1660*KX, 625*KY, 150*KX, 115*KY, HOUSE, 1660*KX, 600*KY, j.imgStrawberry, j.basket);*/
    }

    @Override
    public void show() {
        current_SCREEN = FORREST;
    }

    @Override
    public void render(float delta) {
        // обработка касаний
        if(Gdx.input.justTouched()){
            j.touch.set((float)Gdx.input.getX(), (float)Gdx.input.getY(), 0);
            j.camera.unproject(j.touch);

            //if (btnLeft.hit(j.touch.x, j.touch.y)) j.girl.goToPlace(btnLeft.girlWannaPlaceX);
            if (btnRight.hit(j.touch.x, j.touch.y)) j.girl.goToPlace(btnRight.girlWannaPlaceX);
            if(quest_STONE) if (btnGoSwamp.hit(j.touch.x, j.touch.y)) j.girl.goToPlace(btnGoSwamp.girlWannaPlaceX);
            if (quest_FRAGMENT && btnRemoveStone.hit(j.touch.x, j.touch.y)) {
                quest_STONE = true;
                for (int i = FRAGMENT1; i <= FRAGMENT4; i++) j.artefacts[i].x = -2000;
            }
        }

        // игровые события
        j.girl.move();
        // идём на экран HOME
        if(j.girl.wannaPlaceX == btnRight.girlWannaPlaceX && j.girl.x == j.girl.wannaPlaceX) {
            j.girl.setX(j.girl.width/2);
            j.setScreen(j.screenField);
        }
        // идём на экран SWAMP
        if(quest_STONE) {
            if (j.girl.wannaPlaceX == btnGoSwamp.girlWannaPlaceX && j.girl.came(j.girl.wannaPlaceX)) {
                j.girl.x = SCR_WIDTH / 2;
                j.girl.goToPlace(SCR_WIDTH / 2);
                j.setScreen(j.screenSwamp);
            }
        }

        // если девочка дошла до места, куда положить артефакт, то он пропадает из корзины
        if(j.artefacts[HONEY].isReleased) {
            for (int i = FRAGMENT1; i <= FRAGMENT4; i++) {
                if (j.artefacts[i].hitFinish(j.girl.x) && j.girl.artefact == j.artefacts[i] && j.artefacts[i].inBasket) {
                    j.basket.removeArtefact(j.artefacts[i]);
                    j.girl.artefact = null;
                    quest_FRAGMENT = true;
                    for (int k = FRAGMENT1; k <= FRAGMENT4; k++)
                        if(!j.artefacts[k].isReleased) quest_FRAGMENT = false;

                }
            }
        }

        if(j.artefacts[HONEY].hitFinish(j.girl.x) && j.girl.artefact == j.artefacts[HONEY] && j.artefacts[HONEY].inBasket) {
            j.basket.removeArtefact(j.artefacts[HONEY]);
            j.girl.artefact = null;
        }
        // отрисовка
        j.camera.update();
        j.batch.setProjectionMatrix(j.camera.combined);
        j.batch.begin();
        j.batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);

        if(!quest_STONE) j.batch.draw(imgRock, 200*KX, 232*KY, 253*KX, 341*KY);
        if(!j.artefacts[HONEY].isReleased) j.batch.draw(imgBear, 400*KX, 180*KY, 522*KX, 384*KY);

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
        //j.batch.draw(btnLeft.img, btnLeft.x, btnLeft.y, btnLeft.width, btnLeft.height); // стрелка влево
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
