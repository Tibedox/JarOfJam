package ru.myitschool.jarofjam;

import static ru.myitschool.jarofjam.JarOfJam.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

public class ScreenSwamp implements Screen {
    final JarOfJam j;
    JojButton btnDown, btnGoCave;
    JojButton btnTalkFrog;
    Texture imgBG, imgFrog;

    // диалоги
    boolean isDialogFrog1, isDialogFrog2;
    int nDial;
    ArrayList<Dialog> dialogFrog1 = new ArrayList<>();
    ArrayList<Dialog> dialogFrog2 = new ArrayList<>();

    ScreenSwamp(JarOfJam j) {
        this.j = j;

        imgBG = new Texture("screens/swamp.jpg");
        imgFrog = new Texture("frog.png");

        // загрузка диалогов
        FileHandle file = Gdx.files.internal("text/dialogFrog1.txt");
        String[] s = file.readString("UTF-8").split("#");
        for (int i = 0, k=0; i < s.length/3; i++) dialogFrog1.add(new Dialog(s[k++], Integer.parseInt(s[k++]), Integer.parseInt(s[k++])));
        file = Gdx.files.internal("text/dialogFrog2.txt");
        s = file.readString("UTF-8").split("#");
        for (int i = 0, k=0; i < s.length/3; i++) dialogFrog2.add(new Dialog(s[k++], Integer.parseInt(s[k++]), Integer.parseInt(s[k++])));

        // кнопки переход в лес
        //btnGoForrestR = new JojButton(SCR_WIDTH-j.girl.width/2, 200*KY, SCR_WIDTH-j.girl.width/2-100*KX, 300*KY, SCR_WIDTH-j.girl.width/2);
        //btnGoForrestL = new JojButton(0, 200*KY, 100*KX, 300*KY, j.girl.width/2);
        btnDown = new JojButton(700*KX, 0*KY, 300*KX, 100*KY, 750*KX, j.imgArrowDown);
        btnGoCave = new JojButton(860 * KX, 600 * KY, 280 * KX, 400 * KY, 900*KX);

        // кнопка разговор с водяным
        btnTalkFrog = new JojButton(1322*KX, 474*KY, 338*KX, 245*KY, SCR_WIDTH/2f);

        // создаём артефакты, которые будут на этом уровне
        j.artefacts[TREE1] = new Artefact(TREE1, 1363*KX, 123*KY, 270*KX, 105*KY, SWAMP, 800*KX, 322*KY, 300*KX, 340*KY, SWAMP, 800*KX, 310*KY, j);
        j.artefacts[TREE2] = new Artefact(TREE2, 200*KX, 183*KY, 254*KX, 94*KY, SWAMP, 800*KX, 322*KY, 300*KX, 340*KY, SWAMP, 818*KX, 410*KY, j);
        j.artefacts[TREE3] = new Artefact(TREE3, 800*KX, 183*KY, 233*KX, 72*KY, SWAMP, 800*KX, 322*KY, 300*KX, 340*KY, SWAMP, 835*KX, 499*KY, j);
        j.artefacts[TREE4] = new Artefact(TREE4, 663*KX, 120*KY, 197*KX, 57*KY, SWAMP, 800*KX, 322*KY, 300*KX, 340*KY, SWAMP, 860*KX, 567*KY, j);

        j.artefacts[FLY0] = new Artefact(FLY0, 133*KX, 423*KY, 120*KX, 120*KY, SWAMP, 1322*KX, 474*KY, 338*KX, 245*KY, SWAMP, -800*KX, 310*KY, j);
        j.artefacts[FLY1] = new Artefact(FLY1, 200*KX, 383*KY, 181*KX, 150*KY, SWAMP, 1322*KX, 474*KY, 338*KX, 245*KY, SWAMP, -818*KX, 410*KY, j);
        j.artefacts[FLY2] = new Artefact(FLY2, 800*KX, 283*KY, 100*KX, 100*KY, SWAMP, 1322*KX, 474*KY, 338*KX, 245*KY, SWAMP, -835*KX, 499*KY, j);
        j.artefacts[FLY3] = new Artefact(FLY3, 663*KX, 420*KY, 121*KX, 100*KY, SWAMP, 1322*KX, 474*KY, 338*KX, 245*KY, SWAMP, -860*KX, 567*KY, j);
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

            if(isDialogFrog1){
                if(++nDial == dialogFrog1.size()) isDialogFrog1 = false;
                return;
            }
            if(isDialogFrog2){
                if(++nDial == dialogFrog2.size()) isDialogFrog2 = false;
                j.artefacts[TREE5].x = 930*KX;
                return;
            }
            if (btnTalkFrog.hit(j.touch.x, j.touch.y) && !quest_FLY) {
                j.girl.goToPlace(SCR_WIDTH/2f);
                isDialogFrog1 = true;
                nDial = 0;
            }
            if (btnTalkFrog.hit(j.touch.x, j.touch.y) && quest_FLY) {
                j.girl.goToPlace(SCR_WIDTH/2f);
                isDialogFrog2 = true;
                nDial = 0;
            }

            if(j.artefacts[ROPE].isReleased) if (btnGoCave.hit(j.touch.x, j.touch.y)) j.girl.goToPlace(btnGoCave.girlWannaPlaceX);
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

        // идём на экран CAVE
        if(j.artefacts[ROPE].isReleased) {
            if (j.girl.wannaPlaceX == btnGoCave.girlWannaPlaceX && j.girl.came(j.girl.wannaPlaceX)) {
                j.girl.setX(SCR_WIDTH/2f);
                j.setScreen(j.screenCave);
            }
        }

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
        for (int i = FLY0; i <= FLY3; i++) {
            if(j.artefacts[i].hit(j.girl.x) && j.girl.artefact == j.artefacts[i] && !j.artefacts[i].inBasket) {
                j.basket.addArtefact(j.artefacts[i]);
                j.girl.artefact = null;
            }
        }
        for (int i = FLY0; i <= FLY3; i++) if(!(j.artefacts[i].inBasket || j.artefacts[i].isReleased)) j.artefacts[i].fly();
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
                j.imgArt[ROPE] = new Texture("artefacts/rope2.png");
                j.artefacts[ROPE].width = 244*KX;
                j.artefacts[ROPE].height = 361*KY;
            }
        }
        for (int i = FLY0; i <= FLY3; i++) {
            if (j.artefacts[i].hitFinish(j.girl.x) && j.girl.artefact == j.artefacts[i] && j.artefacts[i].inBasket) {
                j.basket.removeArtefact(j.artefacts[i]);
                j.girl.artefact = null;
            }
        }
        quest_FLY = true;
        for (int i = FLY0; i <= FLY3; i++) if(!j.artefacts[i].isReleased) {
            quest_FLY = false;
            break;
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

        // диалоги
        if(isDialogFrog1) j.fontGame.draw(j.batch, dialogFrog1.get(nDial).words, dialogFrog1.get(nDial).x, dialogFrog1.get(nDial).y);
        if(isDialogFrog2) j.fontGame.draw(j.batch, dialogFrog2.get(nDial).words, dialogFrog2.get(nDial).x, dialogFrog2.get(nDial).y);

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
