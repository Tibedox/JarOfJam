package ru.myitschool.jarofjam;

import static ru.myitschool.jarofjam.JarOfJam.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

public class ScreenEnd implements Screen {
    final JarOfJam j;
    JojButton btnDown;
    JojButton btnTalkEnd;
    Texture imgBG;
    boolean talk = true;
    // диалоги
    boolean isDialogEnd;
    int nDial;
    ArrayList<Dialog> dialogEnd = new ArrayList<>();

    ScreenEnd(JarOfJam j) {
        this.j = j;

        imgBG = new Texture("screens/happyend.jpg");

        // загрузка диалогов
        FileHandle file = Gdx.files.internal("text/dialogEnd.txt");
        String[] s = file.readString("UTF-8").split("#");
        for (int i = 0, k=0; i < s.length/3; i++) dialogEnd.add(new Dialog(s[k++], Integer.parseInt(s[k++]), Integer.parseInt(s[k++])));

        // кнопка стрелка вниз
        btnDown = new JojButton(850*KX, 0*KY, 300*KX, 100*KY, 1000*KX, j.imgArrowDown);
        // кнопка разговор
        btnTalkEnd = new JojButton(0, 200*KY, 1920*KX, 880*KY, SCR_WIDTH/2f);

    }

    @Override
    public void show() {
        current_SCREEN = END;
    }

    @Override
    public void render(float delta) {
        // обработка касаний
        if(Gdx.input.justTouched()){
            j.touch.set((float)Gdx.input.getX(), (float)Gdx.input.getY(), 0);
            j.camera.unproject(j.touch);

            if(isDialogEnd){
                if(++nDial == dialogEnd.size()) {
                    isDialogEnd = false;
                    talk = false;
                }
                return;
            }
            if (btnTalkEnd.hit(j.touch.x, j.touch.y) && talk) {
                isDialogEnd = true;
                nDial = 0;
            }

            if (btnDown.hit(j.touch.x, j.touch.y) && !talk) j.girl.goToPlace(btnDown.girlWannaPlaceX);
        }

        // игровые события
        j.girl.move();
        // идём на экран CHULAN
        if(j.girl.wannaPlaceX == btnDown.girlWannaPlaceX && j.girl.x == j.girl.wannaPlaceX) {
            j.girl.setX(j.girl.width/2);
            j.setScreen(j.screenMenu);
        }

        // отрисовка
        j.camera.update();
        j.batch.setProjectionMatrix(j.camera.combined);
        j.batch.begin();
        j.batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);

        // диалоги
        if(isDialogEnd) j.fontGame.draw(j.batch, dialogEnd.get(nDial).words, dialogEnd.get(nDial).x, dialogEnd.get(nDial).y);

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
