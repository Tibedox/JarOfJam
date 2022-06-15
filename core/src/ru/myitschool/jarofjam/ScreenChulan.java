package ru.myitschool.jarofjam;

import static ru.myitschool.jarofjam.JarOfJam.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class ScreenChulan implements Screen {
    final JarOfJam j;

    Texture imgBG;
    Texture imgRecipe;

    JojButton btnGoHouse;
    JojButton btnRecipe;
    boolean isRecipeShow;

    ScreenChulan(JarOfJam j) {
        this.j = j;

        imgBG = new Texture("screens/chulan.jpg");
        imgRecipe = new Texture("recipe.jpg");

        // кнопка переход в дом
        //btnGoHouse = new JojButton(125 * KX, 240 * KY, 186 * KX, 728 * KY, j.girl.width/2);
        btnGoHouse = new JojButton(0, 200*KY, 100*KX, 300*KY, j.girl.width/2);
        // кнопка спотреть рецепт
        btnRecipe = new JojButton(932 * KX, 432 * KY, 110 * KX, 75 * KY, 400*KX);

        // создаём артефакты, которые будут на этом уровне
        j.artefacts[HONEY] = new Artefact(HONEY, 660 * KX, 560 * KY, 120 * KX, 120 * KY, CHULAN, 1660 * KX, 625 * KY, 150 * KX, 115 * KY, HOUSE, 1660 * KX, 620 * KY, j);
        j.artefacts[JAROGJAM] = new Artefact(JAROGJAM, 590 * KX, 700 * KY, 68 * KX, 96 * KY, CHULAN, 1660 * KX, 625 * KY, 150 * KX, 115 * KY, HOUSE, 1660 * KX, 620 * KY, j);
        j.artefacts[POT] = new Artefact(POT, 420 * KX, 694 * KY, 162 * KX, 111 * KY, CHULAN, 1660 * KX, 625 * KY, 150 * KX, 115 * KY, HOUSE, 1660 * KX, 620 * KY, j);
        j.artefacts[FRAGMENT2] = new Artefact(FRAGMENT2, 720 * KX, 698 * KY, 52 * KX, 50 * KY, CHULAN, 255 * KX, 315 * KY, 150 * KX, 145 * KY, FORREST, 350 * KX, 427 * KY, j);
        j.artefacts[FRAGMENT3] = new Artefact(FRAGMENT3, 1080 * KX, 450 * KY, 81 * KX, 58 * KY, CHULAN, 255 * KX, 315 * KY, 150 * KX, 145 * KY, FORREST, 280 * KX, 415 * KY, j);
    }

    public void show() {
        current_SCREEN = CHULAN;
        //j.saveGame();
    }

    public void render(float delta) {
        // обработка касаний
        if (Gdx.input.justTouched()) {
            j.touch.set((float) Gdx.input.getX(), (float) Gdx.input.getY(), 0);
            j.camera.unproject(j.touch);

            // смотрим рецепт
            if(isRecipeShow) isRecipeShow = false;
            else if (btnRecipe.hit(j.touch.x, j.touch.y)) isRecipeShow = true;

            if (btnGoHouse.hit(j.touch.x, j.touch.y)) j.girl.goToPlace(btnGoHouse.girlWannaPlaceX);
        }

        // игровые события
        j.girl.move();
        // идём на экран HOUSE
        if (j.girl.wannaPlaceX == btnGoHouse.girlWannaPlaceX && j.girl.x == j.girl.wannaPlaceX) {
            j.girl.came(SCR_WIDTH - j.girl.width / 2);
            j.setScreen(j.screenHouse);
        }

        // если девочка дошла до артефакта, то он попадает в корзину
        if (j.artefacts[HONEY].hit(j.girl.x) && j.girl.artefact == j.artefacts[HONEY] && !j.artefacts[HONEY].inBasket) {
            j.basket.addArtefact(j.artefacts[HONEY]);
            j.girl.artefact = null;
        }
        if (j.artefacts[JAROGJAM].hit(j.girl.x) && j.girl.artefact == j.artefacts[JAROGJAM] && !j.artefacts[JAROGJAM].inBasket) {
            j.basket.addArtefact(j.artefacts[JAROGJAM]);
            j.girl.artefact = null;
        }
        if (j.artefacts[POT].hit(j.girl.x) && j.girl.artefact == j.artefacts[POT] && !j.artefacts[POT].inBasket) {
            j.basket.addArtefact(j.artefacts[POT]);
            j.girl.artefact = null;
        }
        if (j.artefacts[FRAGMENT2].hit(j.girl.x) && j.girl.artefact == j.artefacts[FRAGMENT2] && !j.artefacts[FRAGMENT2].inBasket) {
            j.basket.addArtefact(j.artefacts[FRAGMENT2]);
            j.girl.artefact = null;
        }
        if (j.artefacts[FRAGMENT3].hit(j.girl.x) && j.girl.artefact == j.artefacts[FRAGMENT3] && !j.artefacts[FRAGMENT3].inBasket) {
            j.basket.addArtefact(j.artefacts[FRAGMENT3]);
            j.girl.artefact = null;
        }
        // если девочка дошла до места, куда положить артефакт, то он пропадает из корзины
        /*if(j.artefacts[SUGAR].hitFinish(j.girl.x) && j.girl.artefact == j.artefacts[SUGAR] && j.artefacts[SUGAR].inBasket) {
            j.basket.removeArtefact(j.artefacts[SUGAR]);
            j.girl.artefact = null;
        }
        if(j.artefacts[HONEY].hitFinish(j.girl.x) && j.girl.artefact == j.artefacts[HONEY] && j.artefacts[HONEY].inBasket) {
            quest_HONEY = true;
            j.basket.removeArtefact(j.artefacts[HONEY]);
            j.girl.artefact = null;
        }*/
       /* if(j.artefacts[STRAWBERRY].hitFinish(j.girl.x) && j.girl.artefact == j.artefacts[STRAWBERRY] && j.artefacts[STRAWBERRY].inBasket) {
            QUEST_STRAWBERRY = true;
            j.basket.removeArtefact(j.artefacts[STRAWBERRY]);
            j.girl.artefact = null;
        }*/

        //********************* отрисовка ************************************
        j.camera.update();
        j.batch.setProjectionMatrix(j.camera.combined);
        j.batch.begin();

        j.batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);

        // артефакты не в корзине
        for (Artefact a : j.artefacts)
            if (a != null && !a.inBasket && a.startScreen == CHULAN)
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

        if(isRecipeShow) j.batch.draw(imgRecipe, 0, 0, SCR_WIDTH, SCR_HEIGHT);

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
    }
}

