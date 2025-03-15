package ru.pifpaf;

import static ru.pifpaf.Main.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;

public class ScreenSettings implements Screen {
    SpriteBatch batch;
    OrthographicCamera camera;
    Vector3 touch;
    BitmapFont fontWhite, fontGray, font120;
    Main main;
    InputKeyboard keyboard;

    Texture imgBackGround;

    PifPafButton btnName;
    PifPafButton btnControl;
    PifPafButton btnScreen;
    PifPafButton btnJoystick;
    PifPafButton btnAccelerometer;
    PifPafButton btnSound;
    PifPafButton btnBack;

    ScreenSettings(Main main){
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;
        fontWhite = main.font80White;
        fontGray = main.font80Gray;
        font120 = main.font120White;
        this.main = main;
        keyboard = new InputKeyboard(fontWhite, SCR_WIDTH, SCR_HEIGHT/2, 8);

        imgBackGround = new Texture("space1.png");

        loadSettings();

        btnName = new PifPafButton("Name: "+main.screenGame.player.name, fontWhite, 100, 1300);
        btnControl = new PifPafButton("Control:", fontWhite, 100, 1150);
        btnScreen = new PifPafButton("Screen", isActiveFont(SCREEN), 200, 1050);
        btnJoystick = new PifPafButton(main.joystick.text, isActiveFont(JOYSTICK), 200, 950);
        btnAccelerometer = new PifPafButton("Accelerometer", isActiveFont(ACCELEROMETER), 200, 850);
        btnSound = new PifPafButton(isSound?"Sound On":"Sound Off", fontWhite, 100, 700);
        btnBack = new PifPafButton("Back", fontWhite, 200);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // касания
        if(Gdx.input.justTouched()){
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);

            if(keyboard.isKeyboardShow) {
                if (keyboard.touch(touch.x, touch.y)) {
                    main.screenGame.player.name = keyboard.getText();
                    btnName.setText("Name: "+main.screenGame.player.name);
                }
            } else {
                if (btnName.hit(touch)) {
                    keyboard.start();
                }
                if (btnScreen.hit(touch)) {
                    btnScreen.setFont(fontWhite);
                    btnJoystick.setFont(fontGray);
                    btnAccelerometer.setFont(fontGray);
                    controls = SCREEN;
                }
                if (btnJoystick.hit(touch)) {
                    btnScreen.setFont(fontGray);
                    btnJoystick.setFont(fontWhite);
                    btnAccelerometer.setFont(fontGray);
                    if (controls == JOYSTICK) {
                        main.joystick.setSide(!main.joystick.side);
                        btnJoystick.setText(main.joystick.text);
                    }
                    controls = JOYSTICK;
                }
                if (btnAccelerometer.hit(touch)) {
                    if (Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer)) {
                        btnScreen.setFont(fontGray);
                        btnJoystick.setFont(fontGray);
                        btnAccelerometer.setFont(fontWhite);
                        controls = ACCELEROMETER;
                    }
                }
                if (btnSound.hit(touch)) {
                    isSound = !isSound;
                    btnSound.setText(isSound ? "Sound On" : "Sound Off");
                }
                if (btnBack.hit(touch)) {
                    main.setScreen(main.screenMenu);
                }
            }
        }

        // события
        // отрисовка
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        fontWhite.draw(batch, "Settings", 0, 1500, SCR_WIDTH, Align.center, false);
        btnName.font.draw(batch, btnName.text, btnName.x, btnName.y);
        btnControl.font.draw(batch, btnControl.text, btnControl.x, btnControl.y);
        btnScreen.font.draw(batch, btnScreen.text, btnScreen.x, btnScreen.y);
        btnJoystick.font.draw(batch, btnJoystick.text, btnJoystick.x, btnJoystick.y);
        btnAccelerometer.font.draw(batch, btnAccelerometer.text, btnAccelerometer.x, btnAccelerometer.y);
        btnSound.font.draw(batch, btnSound.text, btnSound.x, btnSound.y);
        btnBack.font.draw(batch, btnBack.text, btnBack.x, btnBack.y);
        keyboard.draw(batch);
        batch.end();
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
        saveSettings();
    }

    @Override
    public void dispose() {
        keyboard.dispose();
    }

    private void saveSettings() {
        Preferences prefs = Gdx.app.getPreferences("PifPafSettings");
        prefs.putString("name", main.screenGame.player.name);
        prefs.putInteger("controls", controls);
        prefs.putBoolean("joystick", main.joystick.side);
        prefs.putBoolean("sound", isSound);
        prefs.flush();
    }

    private void loadSettings() {
        Preferences prefs = Gdx.app.getPreferences("PifPafSettings");
        main.screenGame.player.name = prefs.getString("name", "Noname");
        controls = prefs.getInteger("controls", SCREEN);
        main.joystick.setSide(prefs.getBoolean("joystick", RIGHT));
        isSound = prefs.getBoolean("sound", true);
    }

    private BitmapFont isActiveFont(int c){
        return controls == c? fontWhite : fontGray;
    }
}
