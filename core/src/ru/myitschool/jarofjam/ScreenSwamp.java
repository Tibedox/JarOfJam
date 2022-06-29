package ru.myitschool.jarofjam;

import static ru.myitschool.jarofjam.JarOfJam.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class ScreenSwamp implements Screen {
    final JarOfJam j;
    //JojButton btnGoForrestL, btnGoForrestR;
    JojButton btnDown, btnGoCave;
    Texture imgBG, imgFrog;

    ScreenSwamp(JarOfJam j) {
        this.j = j;

        imgBG = new Texture("screens/swamp.jpg");
        imgFrog = new Texture("frog.png");

        // кнопки переход в лес
        //btnGoForrestR = new JojButton(SCR_WIDTH-j.girl.width/2, 200*KY, SCR_WIDTH-j.girl.width/2-100*KX, 300*KY, SCR_WIDTH-j.girl.width/2);
        //btnGoForrestL = new JojButton(0, 200*KY, 100*KX, 300*KY, j.girl.width/2);
        btnDown = new JojButton(700*KX, 0*KY, 300*KX, 100*KY, 750*KX, j.imgArrowDown);
        btnGoCave = new JojButton(860 * KX, 600 * KY, 280 * KX, 400 * KY, 900*KX);

        // создаём артефакты, которые будут на этом уровне
        j.artefacts[TREE1] = new Artefact(TREE1, 1363*KX, 123*KY, 270*KX, 105*KY, SWAMP, 800*KX, 322*KY, 300*KX, 340*KY, SWAMP, 800*KX, 310*KY, j);
        j.artefacts[TREE2] = new Artefact(TREE2, 200*KX, 183*KY, 254*KX, 94*KY, SWAMP, 800*KX, 322*KY, 300*KX, 340*KY, SWAMP, 818*KX, 410*KY, j);
        j.artefacts[TREE3] = new Artefact(TREE3, 800*KX, 183*KY, 233*KX, 72*KY, SWAMP, 800*KX, 322*KY, 300*KX, 340*KY, SWAMP, 835*KX, 499*KY, j);
        j.artefacts[TREE4] = new Artefact(TREE4, 663*KX, 120*KY, 197*KX, 57*KY, SWAMP, 800*KX, 322*KY, 300*KX, 340*KY, SWAMP, 860*KX, 567*KY, j);
    }

    @Override
    public void show() {
        current_SCREEN = SWAMP;
    }

    @Override
    public void render(float delta) {
        // обработка касаний
        if(Gdx.input.justTouched()){
            j.touch.set((float)Gdx.input.getX(), (float)Gdx.input.getY(), 0);
            j.camera.unproject(j.touch);

            //if (btnGoForrestL.hit(j.touch.x, j.touch.y)) j.girl.goToPlace(btnGoForrestL.girlWannaPlaceX);
            //if (btnGoForrestR.hit(j.touch.x, j.touch.y)) j.girl.goToPlace(btnGoForrestR.girlWannaPlaceX);
            if(quest_BRIDGE) if (btnGoCave.hit(j.touch.x, j.touch.y)) j.girl.goToPlace(btnGoCave.girlWannaPlaceX);
            boolean flag = true;
            for (int i = 0; i < j.basket.artefacts.size(); i++){
                if(j.basket.artefacts.get(i).isMove) {
                    flag = false;
                    break;
                }
            }
            if (flag && btnDown.hit(j.touch.x, j.touch.y)) j.girl.goToPlace(btnDown.girlWannaPlaceX);
        }

        // игровые события
        j.girl.move();
        // идём на экран FORREST
        /*if(j.girl.wannaPlaceX == btnGoForrestL.girlWannaPlaceX && j.girl.x == j.girl.wannaPlaceX ||
                j.girl.wannaPlaceX == btnGoForrestR.girlWannaPlaceX && j.girl.x == j.girl.wannaPlaceX) {
            j.girl.setX(SCR_WIDTH/2);
            j.setScreen(j.screenForrest);
        }*/
        if(j.girl.wannaPlaceX == btnDown.girlWannaPlaceX && j.girl.came(j.girl.wannaPlaceX)) {
            j.girl.setX(SCR_WIDTH/2f);
            j.setScreen(j.screenForrest);
        }

        // если девочка дошла до артефакта, то он попадает в корзину
        for (int i = TREE1; i <= TREE4; i++) {
            if(j.artefacts[i].hit(j.girl.x) && j.girl.artefact == j.artefacts[i] && !j.artefacts[i].inBasket) {
                j.basket.addArtefact(j.artefacts[i]);
                j.girl.artefact = null;
            }
        }
        // если девочка дошла до места, куда положить артефакт, то он пропадает из корзины
        for (int i = TREE1; i <= TREE5; i++) {
            if (j.artefacts[i].hitFinish(j.girl.x) && j.girl.artefact == j.artefacts[i] && j.artefacts[i].inBasket) {
                j.basket.removeArtefact(j.artefacts[i]);
                j.girl.artefact = null;
            }
        }
        boolean bridge = true;
        for (int i = TREE1; i <= TREE5; i++) if(!j.artefacts[i].isReleased) {
            bridge = false;
            break;
        }
        if(bridge) {
            if (j.artefacts[ROPE].hitFinish(j.girl.x) && j.girl.artefact == j.artefacts[ROPE] && j.artefacts[ROPE].inBasket) {
                j.basket.removeArtefact(j.artefacts[ROPE]);
                j.girl.artefact = null;
                j.imgArt[ROPE] = j.imgArt[ROPE2];
                j.artefacts[ROPE].width = 244;
                j.artefacts[ROPE].height = 361;
            }
        }

        // отрисовка
        j.camera.update();
        j.batch.setProjectionMatrix(j.camera.combined);
        j.batch.begin();
        j.batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        j.batch.draw(imgFrog, 1322*KX, 474*KY, 338*KX, 245*KY);

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
