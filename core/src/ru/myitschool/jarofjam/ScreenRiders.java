package ru.myitschool.jarofjam;

import static ru.myitschool.jarofjam.JarOfJam.ASTRAZENECA;
import static ru.myitschool.jarofjam.JarOfJam.CORONAVAC;
import static ru.myitschool.jarofjam.JarOfJam.FIELD;
import static ru.myitschool.jarofjam.JarOfJam.HOUSE;
import static ru.myitschool.jarofjam.JarOfJam.KX;
import static ru.myitschool.jarofjam.JarOfJam.KY;
import static ru.myitschool.jarofjam.JarOfJam.PFIZER;
import static ru.myitschool.jarofjam.JarOfJam.RASPBERRY;
import static ru.myitschool.jarofjam.JarOfJam.RIDERS;
import static ru.myitschool.jarofjam.JarOfJam.ROPE;
import static ru.myitschool.jarofjam.JarOfJam.SCR_HEIGHT;
import static ru.myitschool.jarofjam.JarOfJam.SCR_WIDTH;
import static ru.myitschool.jarofjam.JarOfJam.SPUTNIKV;
import static ru.myitschool.jarofjam.JarOfJam.SWAMP;
import static ru.myitschool.jarofjam.JarOfJam.TREE5;
import static ru.myitschool.jarofjam.JarOfJam.current_SCREEN;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

public class ScreenRiders implements Screen {
    final JarOfJam j;
    JojButton btnDown;
    JojButton btnTalkRiders;
    Texture imgBG;
    boolean talk = true;
    // диалоги
    boolean isDialogRiders;
    int nDial;
    ArrayList<Dialog> dialogRiders = new ArrayList<>();

    ScreenRiders (JarOfJam j) {
        this.j = j;

        imgBG = new Texture("screens/riders.jpg");

        // загрузка диалогов
        FileHandle file = Gdx.files.internal("text/dialogRiders.txt");
        String[] s = file.readString("UTF-8").split("#");
        for (int i = 0, k=0; i < s.length/3; i++) dialogRiders.add(new Dialog(s[k++], Integer.parseInt(s[k++]), Integer.parseInt(s[k++])));

        // кнопка стрелка вниз
        btnDown = new JojButton(850*KX, 0*KY, 300*KX, 100*KY, 1000*KX, j.imgArrowDown);

        // кнопка разговор с Всадниками
        btnTalkRiders = new JojButton(0, 200*KY, 1920*KX, 880*KY, SCR_WIDTH/2f);

        // создаём артефакты, которые будут на этом уровне
        j.artefacts[ASTRAZENECA] = new Artefact(ASTRAZENECA, -300*KX, 176*KY, 150*KX, 350*KY, RIDERS, 1000*KX, 660*KY, 500*KX, 176*KY, HOUSE, -1660*KX, 0*KY, j);
        j.artefacts[CORONAVAC] = new Artefact(CORONAVAC, -700*KX, 176*KY, 150*KX, 350*KY, RIDERS, 1000*KX, 660*KY, 500*KX, 176*KY, HOUSE, -1660*KX, 0*KY, j);
        j.artefacts[PFIZER] = new Artefact(PFIZER, -1200*KX, 176*KY, 150*KX, 350*KY, RIDERS, 1000*KX, 660*KY, 500*KX, 176*KY, HOUSE, -1660*KX, 0*KY, j);
        j.artefacts[SPUTNIKV] = new Artefact(SPUTNIKV, -1600*KX, 176*KY, 150*KX, 350*KY, RIDERS, 1000*KX, 660*KY, 500*KX, 176*KY, HOUSE, -1660*KX, 0*KY, j);
    }

    @Override
    public void show() {
        current_SCREEN = RIDERS;
    }

    @Override
    public void render(float delta) {
        // обработка касаний
        if(Gdx.input.justTouched()){
            j.touch.set((float)Gdx.input.getX(), (float)Gdx.input.getY(), 0);
            j.camera.unproject(j.touch);

            if(isDialogRiders){
                if(++nDial == dialogRiders.size()) {
                    isDialogRiders = false;
                    j.artefacts[ASTRAZENECA].x = 300*KX;
                    j.artefacts[CORONAVAC].x = 700*KX;
                    j.artefacts[PFIZER].x = 1200*KX;
                    j.artefacts[SPUTNIKV].x = 1600*KX;
                    talk = false;
                }
                return;
            }
            if (btnTalkRiders.hit(j.touch.x, j.touch.y) && talk) {
                j.girl.goToPlace(SCR_WIDTH/2f);
                isDialogRiders = true;
                nDial = 0;
            }

            if (btnDown.hit(j.touch.x, j.touch.y) && !talk) j.girl.goToPlace(btnDown.girlWannaPlaceX);
        }

        // игровые события
        j.girl.move();
        // идём на экран CHULAN
        if(j.girl.wannaPlaceX == btnDown.girlWannaPlaceX && j.girl.x == j.girl.wannaPlaceX) {
            j.girl.setX(j.girl.width/2);
            j.setScreen(j.screenChulan);
        }

        // если девочка дошла до артефакта, то он попадает в корзину
        if(j.artefacts[ASTRAZENECA].hit(j.girl.x) && j.girl.artefact == j.artefacts[ASTRAZENECA] && !j.artefacts[ASTRAZENECA].inBasket) {
            j.basket.addArtefact(j.artefacts[ASTRAZENECA]);
            j.girl.artefact = null;
        }
        if(j.artefacts[CORONAVAC].hit(j.girl.x) && j.girl.artefact == j.artefacts[CORONAVAC] && !j.artefacts[CORONAVAC].inBasket) {
            j.basket.addArtefact(j.artefacts[CORONAVAC]);
            j.girl.artefact = null;
        }
        if(j.artefacts[PFIZER].hit(j.girl.x) && j.girl.artefact == j.artefacts[PFIZER] && !j.artefacts[PFIZER].inBasket) {
            j.basket.addArtefact(j.artefacts[PFIZER]);
            j.girl.artefact = null;
        }
        if(j.artefacts[SPUTNIKV].hit(j.girl.x) && j.girl.artefact == j.artefacts[SPUTNIKV] && !j.artefacts[SPUTNIKV].inBasket) {
            j.basket.addArtefact(j.artefacts[SPUTNIKV]);
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

        // диалоги
        if(isDialogRiders) j.fontGame.draw(j.batch, dialogRiders.get(nDial).words, dialogRiders.get(nDial).x, dialogRiders.get(nDial).y);

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
