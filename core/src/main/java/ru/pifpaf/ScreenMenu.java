package ru.pifpaf;

import static ru.pifpaf.Main.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class ScreenMenu implements Screen {
    Main main;
    SpriteBatch batch;
    OrthographicCamera camera;
    Vector3 touch;
    BitmapFont font;

    Texture imgBackGround;

    PifPafButton btnPlay;
    PifPafButton btnSettings;
    PifPafButton btnRecords;
    PifPafButton btnAbout;
    PifPafButton btnExit;

    ScreenMenu(Main main){
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;
        font = main.fontWhite;
        this.main = main;

        imgBackGround = new Texture("space1.png");

        btnPlay = new PifPafButton("Play", font, 300, 1000);
        btnSettings = new PifPafButton("Settings", font, 300, 900);
        btnRecords = new PifPafButton("Records", font, 300, 800);
        btnAbout = new PifPafButton("About", font, 300, 700);
        btnExit = new PifPafButton("Exit", font, 300, 600);
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

            if(btnPlay.hit(touch.x, touch.y)){
                main.setScreen(main.screenGame);
            }
            if(btnSettings.hit(touch.x, touch.y)){
                main.setScreen(main.screenSettings);
            }
            if(btnRecords.hit(touch.x, touch.y)){
                main.setScreen(main.screenRecords);
            }
            if(btnAbout.hit(touch.x, touch.y)){
                main.setScreen(main.screenAbout);
            }
            if(btnExit.hit(touch.x, touch.y)){
                Gdx.app.exit();
            }
        }

        // события
        // отрисовка
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        btnPlay.font.draw(batch, btnPlay.text, btnPlay.x, btnPlay.y);
        btnSettings.font.draw(batch, btnSettings.text, btnSettings.x, btnSettings.y);
        btnRecords.font.draw(batch, btnRecords.text, btnRecords.x, btnRecords.y);
        btnAbout.font.draw(batch, btnAbout.text, btnAbout.x, btnAbout.y);
        btnExit.font.draw(batch, btnExit.text, btnExit.x, btnExit.y);
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
