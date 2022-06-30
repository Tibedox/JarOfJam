package ru.myitschool.jarofjam;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector3;

public class JarOfJam extends Game {
    public static final int SCR_WIDTH = 1920;
    public static final int SCR_HEIGHT = 1080;
    // коэффициенты на случай, если сменим разрешение экрана
    public static final float KX = SCR_WIDTH/1920f;
    public static final float KY = SCR_HEIGHT/1080f;
    // артефакты
    public static final int HONEY = 0, TREE1 = 1, TREE2 = 2, TREE3 = 3, TREE4 = 4,TREE5 = 5, ROPE = 6;
    public static final int KEY = 7, JUGWATER = 8, SUGAR = 9, FRAGMENT1 = 10, FRAGMENT2 = 11, FRAGMENT3 = 12, FRAGMENT4 = 13;
    public static final int JAROFJAM = 14, POT = 15, MATCHES = 16, STRAWBERRY = 17, RASPBERRY = 18;
    public static final int ASTRAZENECA = 19, CORONAVAC = 20, PFIZER = 21, SPUTNIKV = 22, PLANTAIN = 23;

    public static final int N_ARTEFACTS = 24;
    // экраны
    public static final int MENU = 0, HOUSE = 1, FIELD = 2, FORREST = 3, SWAMP = 4, CHULAN = 5;
    public static final int INTRO = 6, GARDEN = 7, CAVE = 8, RIDERS = 9, END = 10;
    public static int current_SCREEN, previous_SCREEN = HOUSE; // активный экран
    // задания
    public static boolean quest_JAM, quest_ROPE, quest_FRAGMENT, quest_STONE, quest_CORONA, quest_RIDERS, quest_GAMEOVER;

    SpriteBatch batch;
    OrthographicCamera camera;
    Vector3 touch;

    BitmapFont fontMenu, fontGame, fontIntro;

    // объекты экранов
    ScreenMenu screenMenu;
    ScreenIntro screenIntro;
    ScreenChulan screenChulan;
    ScreenHouse screenHouse;
    ScreenGarden screenGarden;
    ScreenField screenField;
    ScreenForrest screenForrest;
    ScreenSwamp screenSwamp;
    ScreenCave screenCave;
    ScreenRiders screenRiders;
    ScreenEnd screenEnd;

    Texture imgBasket;
    Texture imgAtlasGirl;
    TextureRegion[] imgGirl = new TextureRegion[8];
    Texture[] imgArt = new Texture[N_ARTEFACTS];
    Texture imgPanel;
    Texture imgCross;
    Texture imgArrowLeft, imgArrowRight, imgArrowUp, imgArrowDown;

    Girl girl;
    Basket basket;
    Artefact[] artefacts = new Artefact[N_ARTEFACTS];
    JojButton btnGoMenu; // кнопка выхода в главное меню

    public void create() {
        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, SCR_WIDTH, SCR_HEIGHT);
        this.touch = new Vector3();

        generateFonts();

        imgBasket = new Texture("basket.png");
        imgCross = new Texture("sun.png");
        imgArrowLeft = new Texture("arrowleft.png");
        imgArrowRight = new Texture("arrowright.png");
        imgArrowUp = new Texture("arrowup.png");
        imgArrowDown = new Texture("arrowdown.png");

        // картинки артефактов
        imgArt[HONEY] = new Texture("artefacts/honey.png");
        for (int i = TREE1, j=1; i <= TREE5; i++, j++) imgArt[i] = new Texture("artefacts/tree"+j+".png");
        imgArt[ROPE] = new Texture("artefacts/rope.png");
        imgArt[KEY] = new Texture("artefacts/key.png");
        imgArt[MATCHES] = new Texture("artefacts/matches.png");
        imgArt[JUGWATER] = new Texture("artefacts/jugwater.png");
        imgArt[STRAWBERRY] = new Texture("artefacts/strawberry.png");
        imgArt[RASPBERRY] = new Texture("artefacts/raspberry.png");
        imgArt[SUGAR] = new Texture("artefacts/sugar.png");
        for (int i = FRAGMENT1, j=1; i <= FRAGMENT4; i++, j++) imgArt[i] = new Texture("artefacts/fragment"+j+".png");
        imgArt[JAROFJAM] = new Texture("artefacts/emptyjar.png");
        imgArt[POT] = new Texture("artefacts/pot.png");
        imgArt[ASTRAZENECA] = new Texture("artefacts/astrazeneca.png");
        imgArt[CORONAVAC] = new Texture("artefacts/coronavac.png");
        imgArt[PFIZER] = new Texture("artefacts/pfizer.png");
        imgArt[SPUTNIKV] = new Texture("artefacts/sputnikv.png");
        imgArt[PLANTAIN] = new Texture("artefacts/plantain.png");

        imgPanel = new Texture("inventory.png");

        basket = new Basket(10*KX, 10*KY, 180*KX, 180*KY);

        imgAtlasGirl = new Texture("atlasgirl.png");
        for (int i = 0; i < 8; ++i) imgGirl[i] = new TextureRegion(imgAtlasGirl, i * 250, 0, 250, 300);
        girl = new Girl(960*KX, 108*KY);

        Gdx.input.setInputProcessor(new JarTouch(this));

        this.screenMenu = new ScreenMenu(this);
        this.screenIntro = new ScreenIntro(this);
        this.screenChulan = new ScreenChulan(this);
        this.screenHouse = new ScreenHouse(this);
        this.screenGarden = new ScreenGarden(this);
        this.screenField = new ScreenField(this);
        this.screenForrest = new ScreenForrest(this);
        this.screenSwamp = new ScreenSwamp(this);
        this.screenCave = new ScreenCave(this);
        this.screenRiders = new ScreenRiders(this);
        this.screenEnd = new ScreenEnd(this);
        btnGoMenu = new JojButton(SCR_WIDTH-60*KX, SCR_HEIGHT-60*KY, 50*KX, 50*KY, 0); // кнопка выход в менею

        this.setScreen(this.screenHouse);
    }

    void generateFonts(){
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/text.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.characters = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяabcdefghijklmnopqrstuvwxyzАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<>";
        parameter.size = (int)(100*KX);
        parameter.color = Color.DARK_GRAY;
        parameter.borderColor = Color.CORAL;
        parameter.borderWidth = 2;
        fontMenu = generator.generateFont(parameter);

        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/ubuntu.ttf"));
        parameter.size = (int)(50*KX);
        parameter.borderWidth = 4;
        parameter.color = Color.WHITE;
        parameter.borderColor = Color.DARK_GRAY;
        fontIntro = generator.generateFont(parameter);

        parameter.size = (int)(40*KX);
        parameter.borderWidth = 2;
        parameter.color = Color.GOLD;
        parameter.borderColor = Color.DARK_GRAY;
        fontGame = generator.generateFont(parameter);
        generator.dispose();
    }

    void saveGame(){
        Preferences prefs = Gdx.app.getPreferences("jar_of_jam_preferences"); // заводим preferences
        prefs.putInteger("current_SCREEN", current_SCREEN); // сохраняем число по ключу
        prefs.putInteger("previous_SCREEN", previous_SCREEN);
        prefs.putBoolean("quest_JAM", quest_JAM);
        prefs.putBoolean("quest_ROPE", quest_ROPE);
        prefs.putBoolean("quest_FRAGMENT", quest_FRAGMENT);
        prefs.putBoolean("quest_STONE", quest_STONE);
        prefs.putBoolean("quest_CORONA", quest_CORONA);
        prefs.putBoolean("quest_RIDERS", quest_RIDERS);
        prefs.putFloat("girl.x", girl.x);
        prefs.putFloat("girl.y", girl.y);
        for (int i = 0; i < artefacts.length; i++) {
            if(artefacts[i] != null) {
                prefs.putInteger("art" + i + "name", artefacts[i].name);
                prefs.putFloat("art" + i + "x", artefacts[i].x);
                prefs.putFloat("art" + i + "y", artefacts[i].y);
                prefs.putFloat("art" + i + "width", artefacts[i].width);
                prefs.putFloat("art" + i + "height", artefacts[i].height);
                prefs.putBoolean("art" + i + "inBasket", artefacts[i].inBasket);
                prefs.putBoolean("art" + i + "isReleased", artefacts[i].isReleased);
            }
        }
        prefs.flush();
    }

    void loadGame(){
        Preferences prefs = Gdx.app.getPreferences("jar_of_jam_preferences"); // заводим preferences
        if(prefs.contains("current_SCREEN")) current_SCREEN = prefs.getInteger("current_SCREEN", current_SCREEN);
        if(prefs.contains("previous_SCREEN")) previous_SCREEN = prefs.getInteger("previous_SCREEN", previous_SCREEN);
        if(prefs.contains("quest_JAM")) quest_JAM = prefs.getBoolean("quest_JAM", quest_JAM);
        if(prefs.contains("quest_ROPE")) quest_ROPE = prefs.getBoolean("quest_ROPE", quest_ROPE);
        if(prefs.contains("quest_FRAGMENT")) quest_FRAGMENT = prefs.getBoolean("quest_FRAGMENT", quest_FRAGMENT);
        if(prefs.contains("quest_STONE")) quest_STONE = prefs.getBoolean("quest_STONE", quest_STONE);
        if(prefs.contains("quest_CORONA")) quest_CORONA = prefs.getBoolean("quest_BRIDGE", quest_CORONA);
        if(prefs.contains("quest_RIDERS")) quest_RIDERS = prefs.getBoolean("quest_RIDERS", quest_RIDERS);
        if(prefs.contains("girl.x")) girl.x = prefs.getFloat("girl.x", girl.x);
        if(prefs.contains("girl.y")) girl.y = prefs.getFloat("girl.y", girl.y);

        basket.artefacts.clear(); // очищаем корзину от артефактов
        for (int i = 0; i < artefacts.length; i++) {
            if(prefs.contains("art"+i+"name")) artefacts[i].name = prefs.getInteger("art"+i+"name", artefacts[i].name);
            else continue;
            if(prefs.contains("art"+i+"x")) artefacts[i].x = prefs.getFloat("art"+i+"x", artefacts[i].x);
            if(prefs.contains("art"+i+"y")) artefacts[i].y = prefs.getFloat("art"+i+"y", artefacts[i].y);
            if(prefs.contains("art"+i+"width")) artefacts[i].width = prefs.getFloat("art"+i+"width", artefacts[i].width);
            if(prefs.contains("art"+i+"height")) artefacts[i].height = prefs.getFloat("art"+i+"height", artefacts[i].height);
            if(prefs.contains("art"+i+"inBasket")) {
                artefacts[i].inBasket = prefs.getBoolean("art"+i+"inBasket", artefacts[i].inBasket);
                if(artefacts[i].inBasket) basket.addArtefact(artefacts[artefacts[i].name]); // складываем артефакт в корзину
            }
            if(prefs.contains("art"+i+"isReleased")) artefacts[i].isReleased = prefs.getBoolean("art"+i+"isReleased", artefacts[i].isReleased);
            if(artefacts[ROPE].isReleased) imgArt[ROPE] = new Texture("artefacts/rope2.png");
            if(quest_JAM) imgArt[JAROFJAM] = new Texture("artefacts/jarofjam.png");
        }
    }

    public void dispose() {
        super.dispose();
        batch.dispose();
    }
}