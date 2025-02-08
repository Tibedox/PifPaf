package ru.pifpaf;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class Main extends Game {
    public static final float SCR_WIDTH = 900, SCR_HEIGHT = 1600;
    public static final int SCREEN = 0, JOYSTICK_RIGHT = 1, JOYSTICK_LEFT = 2, ACCELEROMETER = 3;
    public static int controls = SCREEN;

    public SpriteBatch batch;
    public OrthographicCamera camera;
    public Vector3 touch;
    public BitmapFont fontWhite;
    public BitmapFont fontYellow;
    public BitmapFont fontGray;

    public ScreenMenu screenMenu;
    public ScreenGame screenGame;
    public ScreenSettings screenSettings;
    public ScreenRecords screenRecords;
    public ScreenAbout screenAbout;

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCR_WIDTH, SCR_HEIGHT);
        touch = new Vector3();
        fontWhite = new BitmapFont(Gdx.files.internal("gardens80white.fnt"));
        fontYellow = new BitmapFont(Gdx.files.internal("gardens80yellow.fnt"));
        fontGray = new BitmapFont(Gdx.files.internal("gardens80gray.fnt"));

        screenMenu = new ScreenMenu(this);
        screenGame = new ScreenGame(this);
        screenSettings = new ScreenSettings(this);
        screenRecords = new ScreenRecords(this);
        screenAbout = new ScreenAbout(this);
        setScreen(screenMenu);
    }

    @Override
    public void dispose() {
        batch.dispose();
        fontWhite.dispose();
        fontYellow.dispose();
        fontGray.dispose();
    }
}
