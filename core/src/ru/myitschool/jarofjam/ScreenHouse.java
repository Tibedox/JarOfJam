package ru.myitschool.jarofjam;

import static ru.myitschool.jarofjam.JarOfJam.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class ScreenHouse implements Screen {
    final JarOfJam j;

    Texture imgBG;
    Texture imgTable;
    Texture imgPech;
    Texture imgGrandma;
    Texture imgSpider;
    Texture imgBasket2;

    JojButton btnGoField, btnGoChulan;
    JojButton btnTalkGrandma;

    ScreenHouse(JarOfJam j) {
        this.j = j;

        imgTable = new Texture("table.png");
        imgPech = new Texture("peh.png");
        imgBG = new Texture("screens/house.jpg");
        imgBasket2 = new Texture("basket2.png");
        imgGrandma = new Texture("grandmother.png");
        imgSpider = new Texture("spider.png");

        // кнопка переход на поле
        btnGoField = new JojButton(0, 200*KY, 100*KX, 300*KY, j.girl.width/2);
        // кнопка переход в чулан
        btnGoChulan = new JojButton(SCR_WIDTH-j.girl.width/2, 200*KY, SCR_WIDTH-j.girl.width/2-100*KX, 300*KY, SCR_WIDTH-j.girl.width/2);
        // кнопка разговор с бабкой
        btnTalkGrandma = new JojButton(1000*KX, 650*KY, 539*KX, 176*KY, SCR_WIDTH/2);

        // создаём артефакты, которые будут на этом уровне
        j.artefacts[SUGAR] = new Artefact(SUGAR, 500*KX, 420*KY, 72*KX, 54*KY, HOUSE, 1660*KX, 625*KY, 150*KX, 115*KY, HOUSE, 1660*KX, 620*KY, j.imgArt[SUGAR], j.basket);
        j.artefacts[ROPE] = new Artefact(ROPE, 1420*KX, 218*KY, 200*KX, 200*KY, HOUSE, 145*KX, 368*KY, 377*KX, 492*KY, FORREST, 145*KX, 300*KY, j.imgArt[ROPE], j.basket);
        j.artefacts[JUGWATER] = new Artefact(JUGWATER, 200*KX, 420*KY, 124*KX, 154*KY, HOUSE, 1660*KX, 625*KY, 150*KX, 115*KY, HOUSE, 1660*KX, 620*KY, j.imgArt[JUGWATER], j.basket);
        j.artefacts[FRAGMENT1] = new Artefact(FRAGMENT1, 1700*KX, 625*KY, 50*KX, 54*KY, HOUSE, 1660*KX, 625*KY, 150*KX, 115*KY, HOUSE, 1660*KX, 620*KY, j.imgArt[FRAGMENT1], j.basket);
    }

    public void show () {
        current_SCREEN = HOUSE;
        j.saveGame();
    }

    public void render(float delta) {
        // обработка касаний
        if(Gdx.input.justTouched()){
            j.touch.set((float)Gdx.input.getX(), (float)Gdx.input.getY(), 0);
            j.camera.unproject(j.touch);

            if (btnGoField.hit(j.touch.x, j.touch.y)) j.girl.goToPlace(btnGoField.girlWannaPlaceX);
            if (btnGoChulan.hit(j.touch.x, j.touch.y)) j.girl.goToPlace(btnGoChulan.girlWannaPlaceX);
            if (btnTalkGrandma.hit(j.touch.x, j.touch.y)) j.girl.goToPlace(btnTalkGrandma.girlWannaPlaceX);
        }

        // игровые события
        j.girl.move();
        // идём на экран FIELD
        if(j.girl.wannaPlaceX == btnGoField.girlWannaPlaceX && j.girl.x == j.girl.wannaPlaceX) {
            j.girl.came(SCR_WIDTH-j.girl.width/2);
            j.setScreen(j.screenField);
        }
        // идём на экран CHULAN
        if(j.girl.wannaPlaceX == btnGoChulan.girlWannaPlaceX && j.girl.x == j.girl.wannaPlaceX) {
            j.girl.came(j.girl.width/2);
            j.setScreen(j.screenChulan);
        }
        // если девочка дошла до артефакта, то он попадает в корзину
        /*if(j.artefacts[HONEY].hit(j.girl.x) && j.girl.artefact == j.artefacts[HONEY] && !j.artefacts[HONEY].inBasket) {
            j.basket.addArtefact(j.artefacts[HONEY]);
            j.girl.artefact = null;
        }*/
        if(j.artefacts[SUGAR].hit(j.girl.x) && j.girl.artefact == j.artefacts[SUGAR] && !j.artefacts[SUGAR].inBasket) {
            j.basket.addArtefact(j.artefacts[SUGAR]);
            j.girl.artefact = null;
        }
        if(j.artefacts[ROPE].hit(j.girl.x) && j.girl.artefact == j.artefacts[ROPE] && !j.artefacts[ROPE].inBasket) {
            j.basket.addArtefact(j.artefacts[ROPE]);
            j.girl.artefact = null;
        }
        if(j.artefacts[JUGWATER].hit(j.girl.x) && j.girl.artefact == j.artefacts[JUGWATER] && !j.artefacts[JUGWATER].inBasket) {
            j.basket.addArtefact(j.artefacts[JUGWATER]);
            j.girl.artefact = null;
        }
        if(j.artefacts[FRAGMENT1].hit(j.girl.x) && j.girl.artefact == j.artefacts[FRAGMENT1] && !j.artefacts[FRAGMENT1].inBasket) {
            j.basket.addArtefact(j.artefacts[FRAGMENT1]);
            j.girl.artefact = null;
        }
        // если девочка дошла до места, куда положить артефакт, то он пропадает из корзины
        /*if(j.artefacts[SUGAR].hitFinish(j.girl.x) && j.girl.artefact == j.artefacts[SUGAR] && j.artefacts[SUGAR].inBasket) {
            j.basket.removeArtefact(j.artefacts[SUGAR]);
            j.girl.artefact = null;
        }*/
        if(j.artefacts[HONEY].hitFinish(j.girl.x) && j.girl.artefact == j.artefacts[HONEY] && j.artefacts[HONEY].inBasket) {
            quest_HONEY = true;
            j.basket.removeArtefact(j.artefacts[HONEY]);
            j.girl.artefact = null;
        }
       /* if(j.artefacts[STRAWBERRY].hitFinish(j.girl.x) && j.girl.artefact == j.artefacts[STRAWBERRY] && j.artefacts[STRAWBERRY].inBasket) {
            QUEST_STRAWBERRY = true;
            j.basket.removeArtefact(j.artefacts[STRAWBERRY]);
            j.girl.artefact = null;
        }*/

        // отрисовка
        j.camera.update();
        j.batch.setProjectionMatrix(j.camera.combined);
        j.batch.begin();

        j.batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        j.batch.draw(imgPech, 330*KX, 65*KY, 1570*KX, 1015*KY);
        j.batch.draw(imgTable, 50*KX, 200*KY, 846*KX, 447*KY);
        j.batch.draw(imgSpider, 0*KX, SCR_HEIGHT-150*KY, 200*KX, 150*KY);
        j.batch.draw(imgGrandma, 1000*KX, 650*KY, 539*KX, 176*KY);
        j.batch.draw(imgBasket2, 1380*KX, 160*KY, 319*KX, 321*KY);

        // артефакты не в корзине
        for(Artefact a: j.artefacts)
            if(a != null && !a.inBasket && a.startScreen == HOUSE)
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

    public void resize ( int width, int height){
    }

    public void pause () {
    }

    public void resume () {
    }

    public void hide () {
    }

    public void dispose () {
    }
}

