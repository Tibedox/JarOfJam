package ru.myitschool.jarofjam;

import static ru.myitschool.jarofjam.JarOfJam.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

public class ScreenForrest implements Screen {
    final JarOfJam j;
    JojButton btnRight, btnRemoveStone, btnGoSwamp;
    JojButton btnTalkBear;
    Texture imgBG;
    Texture imgRock, imgBear, imgPlantains;

    // диалоги
    boolean isDialogBear1, isDialogBear2;
    int nDial;
    ArrayList<Dialog> dialogBear1 = new ArrayList<>();
    ArrayList<Dialog> dialogBear2 = new ArrayList<>();

    ScreenForrest(JarOfJam j) {
        this.j = j;

        imgBG = new Texture("screens/forrest.jpg");
        imgBear = new Texture("bear.png");
        imgRock = new Texture("rock.png");
        imgPlantains = new Texture("plantains.png");

        // загрузка диалогов
        FileHandle file = Gdx.files.internal("text/dialogBear1.txt");
        String[] s = file.readString("UTF-8").split("#");
        for (int i = 0, k=0; i < s.length/3; i++) dialogBear1.add(new Dialog(s[k++], Integer.parseInt(s[k++]), Integer.parseInt(s[k++])));
        file = Gdx.files.internal("text/dialogBear2.txt");
        s = file.readString("UTF-8").split("#");
        for (int i = 0, k=0; i < s.length/3; i++) dialogBear2.add(new Dialog(s[k++], Integer.parseInt(s[k++]), Integer.parseInt(s[k++])));

        // кнопка переход в FIELD
        // кнопки стрелки
        //btnLeft = new JojButton(0, 200*KY, 100*KX, 300*KY, j.girl.width/2, j.imgArrowLeft);
        btnRight = new JojButton(SCR_WIDTH-100*KX, 200*KY, 100*KX, 300*KY, SCR_WIDTH-j.girl.width/2, j.imgArrowRight);
        btnRemoveStone = new JojButton(255 * KX, 315 * KY, 150 * KX, 145 * KY, 255 * KX);
        btnGoSwamp = new JojButton(160 * KX, 270 * KY, 280 * KX, 400 * KY, 200*KX);

        // кнопка разговор с медведом
        btnTalkBear = new JojButton(428 * KX, 230 * KY, 470 * KX, 310 * KY, SCR_WIDTH/2f);

        // создаём артефакты, которые будут на этом уровне
        j.artefacts[PLANTAIN] = new Artefact(PLANTAIN, 1450*KX, 278*KY, 97*KX, 106*KY, FORREST, 1000*KX, 660*KY, 500*KX, 176*KY, HOUSE, -1660*KX, 0*KY, j);
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

            if(isDialogBear1){
                if(++nDial == dialogBear1.size()) isDialogBear1 = false;
                return;
            }
            if(isDialogBear2){
                if(++nDial == dialogBear2.size()) {
                    isDialogBear2 = false;
                    quest_BEAR = true;
                }
                return;
            }
            if (btnTalkBear.hit(j.touch.x, j.touch.y) && !j.artefacts[HONEY].isReleased) {
                j.girl.goToPlace(SCR_WIDTH/2f);
                isDialogBear1 = true;
                nDial = 0;
            }
            if (btnTalkBear.hit(j.touch.x, j.touch.y) && j.artefacts[HONEY].isReleased && !quest_BEAR) {
                j.girl.goToPlace(SCR_WIDTH/2f);
                isDialogBear2 = true;
                nDial = 0;
            }

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
                j.girl.x = SCR_WIDTH / 2f;
                j.girl.goToPlace(SCR_WIDTH / 2f);
                j.setScreen(j.screenSwamp);
            }
        }

        // если девочка дошла до артефакта, то он попадает в корзину
        if(quest_CORONA)
            if(j.artefacts[PLANTAIN].hit(j.girl.x) && j.girl.artefact == j.artefacts[PLANTAIN] && !j.artefacts[PLANTAIN].inBasket) {
                j.basket.addArtefact(j.artefacts[PLANTAIN]);
                j.girl.artefact = null;
            }

        // если девочка дошла до места, куда положить артефакт, то он пропадает из корзины
        if(j.artefacts[HONEY].isReleased) {
            for (int i = FRAGMENT1; i <= FRAGMENT4; i++) {
                if (j.artefacts[i].hitFinish(j.girl.x) && j.girl.artefact == j.artefacts[i] && j.artefacts[i].inBasket) {
                    j.basket.removeArtefact(j.artefacts[i]);
                    j.girl.artefact = null;
                    quest_FRAGMENT = true;
                    for (int k = FRAGMENT1; k <= FRAGMENT4; k++) {
                        if (!j.artefacts[k].isReleased) {
                            quest_FRAGMENT = false;
                            break;
                        }
                    }
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
        j.batch.draw(imgPlantains, 1425*KX, 235*KY, 208*KX, 120*KY);
        if(!quest_STONE) j.batch.draw(imgRock, 200*KX, 232*KY, 253*KX, 341*KY);
        if(!quest_BEAR) j.batch.draw(imgBear, 400*KX, 180*KY, 522*KX, 384*KY);

        // артефакты не в корзине
        for (Artefact a : j.artefacts)
            if (a != null && !a.inBasket && (a.startScreen == current_SCREEN && !a.isReleased || a.finishScreen == current_SCREEN && a.isReleased))
                j.batch.draw(j.imgArt[a.name], a.x, a.y, a.width, a.height);

        // девочка
        j.batch.draw(j.imgGirl[j.girl.faza], j.girl.x - j.girl.width / 2, j.girl.y, j.girl.width / 2, 0, j.girl.width, j.girl.height, j.girl.goLeft ? 1 : -1, 1, 0);

        // диалоги
        if(isDialogBear1) j.fontGame.draw(j.batch, dialogBear1.get(nDial).words, dialogBear1.get(nDial).x, dialogBear1.get(nDial).y);
        if(isDialogBear2) j.fontGame.draw(j.batch, dialogBear2.get(nDial).words, dialogBear2.get(nDial).x, dialogBear2.get(nDial).y);

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
