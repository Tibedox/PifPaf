package ru.pifpaf;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class Main extends Game {
    public static final float SCR_WIDTH = 900, SCR_HEIGHT = 1600;
    public static final boolean RIGHT = true, LEFT = false;
    public static final int SCREEN = 0, JOYSTICK = 1, ACCELEROMETER = 2;
    public static int controls = SCREEN;
    public static boolean isSound = true;

    public SpriteBatch batch;
    public OrthographicCamera camera;
    public Vector3 touch;
    public BitmapFont font120White;
    public BitmapFont font80White;
    public BitmapFont font80Yellow;
    public BitmapFont font80Gray;
    public BitmapFont font50Yellow;
    public Joystick joystick;

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
        font120White = new BitmapFont(Gdx.files.internal("fonts/crystal120white.fnt"));
        font80White = new BitmapFont(Gdx.files.internal("fonts/crystal80white.fnt"));
        font80Yellow = new BitmapFont(Gdx.files.internal("fonts/crystal80yellow.fnt"));
        font80Gray = new BitmapFont(Gdx.files.internal("fonts/crystal80gray.fnt"));
        font50Yellow = new BitmapFont(Gdx.files.internal("fonts/crystal50yellow.fnt"));
        joystick = new Joystick(360, RIGHT);

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
        font80White.dispose();
        font80Yellow.dispose();
        font80Gray.dispose();
        font50Yellow.dispose();
    }
}
