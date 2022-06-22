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
    Texture imgWindow;

    JojButton btnLeft, btnRight;
    JojButton btnWindow;
    JojButton btnTalkGrandma;

    boolean isDialog;
    int nDial;
    Dialog[] dialog = {new Dialog("- Кхе-Кхе", 900, 850),
            new Dialog("- Как ты бабушка?", 1000, 460),
            new Dialog("- Я немного приболела...\n Мне нужно поспать...", 900, 950),
            new Dialog("- Хорошо, поправляйся бабуля!", 1000, 460),
            new Dialog("- Я волнуюсь за бабушку, \nнужно найти ей какое-то лекарство.", 500, 300),
            new Dialog("- На столе ничего нет, я не нашла лекарство,\n но есть пара вещей, может они пригодятся?", 500, 300),
            new Dialog("- Хм, а что это там в окне?", 500, 500)
    };
    boolean isWindow;
    Dialog[] dialog2 = {new Dialog("- Уугу-уугу, здравствуй,\nменя зовут Алконис\nи я знаю где тебе найти лекарство\nдля бабушки-и-и...", 150, 970),
            new Dialog("- Это что человек с телом совы,\nили сова с головой человека?\nОткуда ты знаешь, что бабушка болеет?", 145, 300),
            new Dialog("- Я все знаю, я Алконост,\nприлетела, чтобы тебе подсказать,\n как вылечить твою бабушку-y-y...", 150, 970),
            new Dialog("- Отправляйся в дремучий лес,\nтам ты найдешь лешего...", 150, 950),
            new Dialog("- Если выполнишь его просьбу,\nон сделает тебе лекарство!", 150, 950),
            new Dialog("- Интересно, ей можно верить?\n Ну, выбора у меня не остается, другого лекарства нет.", 145, 220)
    };

    ScreenHouse(JarOfJam j) {
        this.j = j;

        imgTable = new Texture("table.png");
        imgPech = new Texture("peh.png");
        imgBG = new Texture("screens/house.jpg");
        imgBasket2 = new Texture("basket2.png");
        imgGrandma = new Texture("grandmother.png");
        imgSpider = new Texture("spider.png");
        imgWindow = new Texture("window.jpg");

        // кнопки стрелки
        btnLeft = new JojButton(0, 200*KY, 100*KX, 300*KY, j.girl.width/2, j.imgArrowLeft);
        btnRight = new JojButton(SCR_WIDTH-100*KX, 200*KY, 100*KX, 300*KY, SCR_WIDTH-j.girl.width/2, j.imgArrowRight);
        // кнопка окно
        btnWindow = new JojButton(167*KX, 658*KY, 463*KX, 316*KY, j.girl.width/2);
        // кнопка разговор с бабкой
        btnTalkGrandma = new JojButton(1000*KX, 660*KY, 500*KX, 176*KY, SCR_WIDTH/2f);

        // создаём артефакты, которые будут на этом уровне
        j.artefacts[SUGAR] = new Artefact(SUGAR, 500*KX, 420*KY, 72*KX, 54*KY, HOUSE, 1660*KX, 625*KY, 150*KX, 115*KY, HOUSE, -1660*KX, 0*KY, j);
        j.artefacts[ROPE] = new Artefact(ROPE, 1420*KX, 218*KY, 200*KX, 200*KY, HOUSE, 800*KX, 322*KY, 300*KX, 340*KY, SWAMP, 820*KX, 310*KY, j);
        j.artefacts[JUGWATER] = new Artefact(JUGWATER, 200*KX, 420*KY, 124*KX, 154*KY, HOUSE, 1660*KX, 625*KY, 150*KX, 115*KY, HOUSE, -1660*KX, 0*KY, j);
        j.artefacts[MATCHES] = new Artefact(MATCHES, 320*KX, 420*KY, 85*KX, 36*KY, HOUSE, 1740*KX, 430*KY, 75*KX, 123*KY, HOUSE, -1660*KX, 0*KY, j);
        j.artefacts[FRAGMENT1] = new Artefact(FRAGMENT1, 1500*KX, 675*KY, 50*KX, 54*KY, HOUSE, 255 * KX, 315 * KY, 150 * KX, 145 * KY, FORREST, 358 * KX, 385 * KY, j);
        j.artefacts[FRAGMENT4] = new Artefact(FRAGMENT4, 1684*KX, 190*KY, 80*KX, 60*KY, HOUSE, 255 * KX, 315 * KY, 150 * KX, 145 * KY, FORREST, 286 * KX, 372 * KY, j);
    }

    public void show () {
        current_SCREEN = HOUSE;
    }

    public void render(float delta) {
        // обработка касаний
        if(Gdx.input.justTouched()){
            j.touch.set((float)Gdx.input.getX(), (float)Gdx.input.getY(), 0);
            j.camera.unproject(j.touch);

            if(isDialog){
                if(++nDial == dialog.length) isDialog = false;
                return;
            }
            if(isWindow){
                if(++nDial == dialog2.length) isWindow = false;
                return;
            }

            if (btnLeft.hit(j.touch.x, j.touch.y)) j.girl.goToPlace(btnLeft.girlWannaPlaceX);
            if (btnRight.hit(j.touch.x, j.touch.y)) j.girl.goToPlace(btnRight.girlWannaPlaceX);
            if (btnWindow.hit(j.touch.x, j.touch.y)) {
                isWindow = true;
                nDial = 0;
            }
            if (btnTalkGrandma.hit(j.touch.x, j.touch.y)) {
                j.girl.goToPlace(SCR_WIDTH/2);
                isDialog = true;
                nDial = 0;
            }
        }

        // игровые события
        j.girl.move();
        // идём на экран Garden
        if(j.girl.wannaPlaceX == btnLeft.girlWannaPlaceX && j.girl.x == j.girl.wannaPlaceX) {
            j.girl.setX(SCR_WIDTH-j.girl.width/2);
            j.setScreen(j.screenGarden);
        }
        // идём на экран CHULAN
        if(j.girl.wannaPlaceX == btnRight.girlWannaPlaceX && j.girl.x == j.girl.wannaPlaceX) {
            j.girl.setX(j.girl.width/2);
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
        if(j.artefacts[MATCHES].hit(j.girl.x) && j.girl.artefact == j.artefacts[MATCHES] && !j.artefacts[MATCHES].inBasket) {
            j.basket.addArtefact(j.artefacts[MATCHES]);
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
        if(j.artefacts[FRAGMENT4].hit(j.girl.x) && j.girl.artefact == j.artefacts[FRAGMENT4] && !j.artefacts[FRAGMENT4].inBasket) {
            j.basket.addArtefact(j.artefacts[FRAGMENT4]);
            j.girl.artefact = null;
        }

        // если девочка дошла до места, куда положить артефакт, то он пропадает из корзины
        if(j.artefacts[POT].hitFinish(j.girl.x) && j.girl.artefact == j.artefacts[POT] && j.artefacts[POT].inBasket) {
            j.basket.removeArtefact(j.artefacts[POT]);
            j.girl.artefact = null;
        }
        if(j.artefacts[POT].isReleased && j.artefacts[SUGAR].isReleased && j.artefacts[JUGWATER].isReleased
                && j.artefacts[STRAWBERRY].isReleased && j.artefacts[RASPBERRY].isReleased && j.artefacts[JAROFJAM].inBasket) {
            if (j.artefacts[MATCHES].hitFinish(j.girl.x) && j.girl.artefact == j.artefacts[MATCHES] && j.artefacts[MATCHES].inBasket) {
                j.basket.removeArtefact(j.artefacts[MATCHES]);
                j.girl.artefact = null;
                j.imgArt[JAROFJAM] = new Texture("artefacts/jarofjam.png");
                quest_JAM = true;
            }
        }
        if(j.artefacts[POT].isReleased) {
            if (j.artefacts[SUGAR].hitFinish(j.girl.x) && j.girl.artefact == j.artefacts[SUGAR] && j.artefacts[SUGAR].inBasket) {
                j.basket.removeArtefact(j.artefacts[SUGAR]);
                j.girl.artefact = null;
            }
            if (j.artefacts[JUGWATER].hitFinish(j.girl.x) && j.girl.artefact == j.artefacts[JUGWATER] && j.artefacts[JUGWATER].inBasket) {
                j.basket.removeArtefact(j.artefacts[JUGWATER]);
                j.girl.artefact = null;
            }
            if (j.artefacts[RASPBERRY].hitFinish(j.girl.x) && j.girl.artefact == j.artefacts[RASPBERRY] && j.artefacts[RASPBERRY].inBasket) {
                j.basket.removeArtefact(j.artefacts[RASPBERRY]);
                j.girl.artefact = null;
            }
            if (j.artefacts[STRAWBERRY].hitFinish(j.girl.x) && j.girl.artefact == j.artefacts[STRAWBERRY] && j.artefacts[STRAWBERRY].inBasket) {
                j.basket.removeArtefact(j.artefacts[STRAWBERRY]);
                j.girl.artefact = null;
            }
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
            if (a != null && !a.inBasket && (a.startScreen == current_SCREEN && !a.isReleased || a.finishScreen == current_SCREEN && a.isReleased))
                j.batch.draw(j.imgArt[a.name], a.x, a.y, a.width, a.height);

        // девочка
        j.batch.draw(j.imgGirl[j.girl.faza], j.girl.x-j.girl.width/2, j.girl.y, j.girl.width/2, 0, j.girl.width, j.girl.height, j.girl.goLeft?1:-1, 1, 0);

        // диалоги
        if(isDialog) j.fontGame.draw(j.batch, dialog[nDial].words, dialog[nDial].x, dialog[nDial].y);

        // корзина
        if(j.basket.isOpen) {
            j.batch.draw(j.imgPanel, 50*KX, 20*KY, SCR_WIDTH-70*KX, 100 * KY);
            for (int i = 0; i < j.basket.artefacts.size(); i++)
                j.batch.draw(j.imgArt[j.basket.artefacts.get(i).name], j.basket.artefacts.get(i).x, j.basket.artefacts.get(i).y, j.basket.artefacts.get(i).width, j.basket.artefacts.get(i).height);
        }
        j.batch.draw(j.imgBasket, j.basket.x, j.basket.y, j.basket.width, j.basket.height); // сама корзинка
        j.batch.draw(j.imgCross, j.btnGoMenu.x, j.btnGoMenu.y, j.btnGoMenu.width, j.btnGoMenu.height); // выход в главное меню
        j.batch.draw(btnLeft.img, btnLeft.x, btnLeft.y, btnLeft.width, btnLeft.height); // стрелка влево
        j.batch.draw(btnRight.img, btnRight.x, btnRight.y, btnRight.width, btnRight.height); // стрелка вправо

        if(isWindow) {
            j.batch.draw(imgWindow, 0, 0, SCR_WIDTH, SCR_HEIGHT);
            // диалог
            j.fontGame.draw(j.batch, dialog2[nDial].words, dialog2[nDial].x, dialog2[nDial].y);
        }

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

