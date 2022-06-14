package ru.myitschool.jarofjam;

import static ru.myitschool.jarofjam.JarOfJam.*;

import com.badlogic.gdx.InputProcessor;

public class JarTouch implements InputProcessor {
    static JarOfJam j;

    JarTouch(JarOfJam jarOfJam){
        j = jarOfJam;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override // если подняли палец
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        j.touch.set(screenX, screenY, 0);
        j.camera.unproject(j.touch);

        // если нажали кнопку выхода в меню
        if(current_SCREEN != MENU && j.btnGoMenu.hit(j.touch.x, j.touch.y)) {
            previous_SCREEN = current_SCREEN;
            j.setScreen(j.screenMenu);
        }

        for(int i=0; i<j.artefacts.length; i++){ // смотрим все артефакты
            if(j.artefacts[i]!= null) {
                if(j.artefacts[i].isMove){ // и какой-то артефакт тащили
                    // если попали, то девочка хочет его отнести
                    if(j.artefacts[i].hitFinish(j.touch.x, j.touch.y) && j.artefacts[i].finishScreen == current_SCREEN){
                        j.girl.goToArtefact(j.artefacts[i], j.touch.x);
                    }
                    // возвращаем артефакт в корзину
                    j.basket.setArtefactsXY();
                    j.artefacts[i].isMove = false;

                    return false;
                }
            }
        }

        // если нажали на корзину, она раскрывается/закрывается
        if(j.basket.hit(j.touch.x, j.touch.y)) {
            j.basket.isOpen = !j.basket.isOpen;
            return false;
        }

        for(int i=0; i<j.artefacts.length; i++) {
            if (j.artefacts[i]!= null) {
                if (j.artefacts[i].startScreen == current_SCREEN) { // если нажали на артефакт в зависимости от экрана
                    if (j.artefacts[i].hit(j.touch.x, j.touch.y)) {
                        // если арт не в корзине и не использован, то девочка идёт за ним
                        if (!j.artefacts[i].inBasket && !j.artefacts[i].isReleased) {
                            j.girl.goToArtefact(j.artefacts[i], j.touch.x);
                            return false;
                        }
                    }
                }
            }
        }

        // иначе девочка просто идёт в эту точку
        j.girl.setTarget(j.touch.x);

        System.out.println("Koordinates x = "+j.touch.x+" y = "+j.touch.y);
        return false;
    }

    @Override // если двигаем палец
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        j.touch.set(screenX, screenY, 0);
        j.camera.unproject(j.touch);

        for(int i=0; i<j.artefacts.length; i++){ // смотрим все артефакты
            if(j.artefacts[i]!= null) {
                if(j.artefacts[i].isMove){ // и какой-то артефакт тащим
                    j.artefacts[i].drag(j.touch.x, j.touch.y); // ставим его в место касания
                    break; // и кроме этого ничего больше нажать не можем
                }
                else if(j.artefacts[i].inBasket && j.artefacts[i].hit(j.touch.x, j.touch.y)){
                    j.artefacts[i].isMove = true;
                    j.artefacts[i].drag(j.touch.x, j.touch.y); // ставим его в место касания
                    break;
                }
            }
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
