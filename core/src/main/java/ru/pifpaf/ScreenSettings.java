package ru.pifpaf;

import static ru.pifpaf.Main.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class ScreenSettings implements Screen {
    SpriteBatch batch;
    OrthographicCamera camera;
    Vector3 touch;
    BitmapFont fontWhite, fontGray;
    Main main;

    Texture imgBackGround;

    PifPafButton btnSettings;
    PifPafButton btnControl;
    PifPafButton btnScreen;
    PifPafButton btnJoystick;
    PifPafButton btnAccelerometer;
    PifPafButton btnBack;

    ScreenSettings(Main main){
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;
        fontWhite = main.fontWhite;
        fontGray = main.fontGray;
        this.main = main;

        imgBackGround = new Texture("space1.png");
        btnSettings = new PifPafButton("Settings", fontWhite, 1500);
        btnControl = new PifPafButton("Control:", fontWhite, 100, 1200);
        btnScreen = new PifPafButton("Screen", fontWhite, 200, 1100);
        btnJoystick = new PifPafButton(main.joystick.text, fontGray, 200, 1000);
        btnAccelerometer = new PifPafButton("Accelerometer", fontGray, 200, 900);
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

            if(btnScreen.hit(touch)){
                btnScreen.setFont(fontWhite);
                btnJoystick.setFont(fontGray);
                btnAccelerometer.setFont(fontGray);
                controls = SCREEN;
            }
            if(btnJoystick.hit(touch)){
                btnScreen.setFont(fontGray);
                btnJoystick.setFont(fontWhite);
                btnAccelerometer.setFont(fontGray);
                if(controls == JOYSTICK){
                    main.joystick.setSide(!main.joystick.side);
                    btnJoystick.setText(main.joystick.text);
                }
                controls = JOYSTICK;
            }
            if(btnAccelerometer.hit(touch)){
                if(Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer)) {
                    btnScreen.setFont(fontGray);
                    btnJoystick.setFont(fontGray);
                    btnAccelerometer.setFont(fontWhite);
                    controls = ACCELEROMETER;
                }
            }
            if(btnBack.hit(touch)){
                main.setScreen(main.screenMenu);
            }
        }

        // события
        // отрисовка
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        btnSettings.font.draw(batch, btnSettings.text, btnSettings.x, btnSettings.y);
        btnControl.font.draw(batch, btnControl.text, btnControl.x, btnControl.y);
        btnScreen.font.draw(batch, btnScreen.text, btnScreen.x, btnScreen.y);
        btnJoystick.font.draw(batch, btnJoystick.text, btnJoystick.x, btnJoystick.y);
        btnAccelerometer.font.draw(batch, btnAccelerometer.text, btnAccelerometer.x, btnAccelerometer.y);
        btnBack.font.draw(batch, btnBack.text, btnBack.x, btnBack.y);
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

    }

    @Override
    public void dispose() {

    }
}
