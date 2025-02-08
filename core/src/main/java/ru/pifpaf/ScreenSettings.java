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
import com.badlogic.gdx.utils.Align;

public class ScreenSettings implements Screen {
    SpriteBatch batch;
    OrthographicCamera camera;
    Vector3 touch;
    BitmapFont fontWhite, fontYellow;
    Main main;

    Texture imgBackGround;

    PifPafButton btnBack;


    ScreenSettings(Main main){
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;
        fontWhite = main.fontWhite;
        fontYellow = main.fontYellow;
        this.main = main;

        imgBackGround = new Texture("space1.png");
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

            if(btnBack.hit(touch.x, touch.y)){
                main.setScreen(main.screenMenu);
            }
        }

        // события
        // отрисовка
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        fontWhite.draw(batch, "Settings", 0, 1500, SCR_WIDTH, Align.center, true);
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
