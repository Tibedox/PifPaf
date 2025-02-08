package ru.pifpaf;

import static ru.pifpaf.Main.SCR_HEIGHT;
import static ru.pifpaf.Main.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
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
    PifPafButton btnBack;
    PifPafButton btnControl;
    PifPafButton btnScreen;
    PifPafButton btnJoystick;
    PifPafButton btnAccelerometer;

    ScreenSettings(Main main){
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;
        fontWhite = main.fontWhite;
        fontGray = main.fontYellow;
        this.main = main;

        imgBackGround = new Texture("space1.png");
        btnSettings = new PifPafButton("Settings", fontWhite, 1500);
        btnControl = new PifPafButton("Control:", fontWhite, 100, 1200);
        btnScreen = new PifPafButton("Screen", fontWhite, 200, 1100);
        btnJoystick = new PifPafButton("Joystick Right", fontGray, 200, 1000);
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
                btnScreen.changeFont(fontWhite);
                btnJoystick.changeFont(fontGray);
                btnAccelerometer.changeFont(fontGray);
            }
            if(btnJoystick.hit(touch)){
                btnScreen.changeFont(fontGray);
                btnJoystick.changeFont(fontWhite);
                btnAccelerometer.changeFont(fontGray);
            }
            if(btnAccelerometer.hit(touch)){
                btnScreen.changeFont(fontGray);
                btnJoystick.changeFont(fontGray);
                btnAccelerometer.changeFont(fontWhite);
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
