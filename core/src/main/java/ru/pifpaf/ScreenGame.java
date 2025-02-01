package ru.pifpaf;

import static ru.pifpaf.Main.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class ScreenGame implements Screen {
    Main main;
    SpriteBatch batch;
    OrthographicCamera camera;
    Vector3 touch;
    BitmapFont font;

    Texture imgBackGround;

    PifPafButton btnExit;

    Space[] space = new Space[2];

    ScreenGame(Main main){
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;
        font = main.font;
        this.main = main;

        imgBackGround = new Texture("space2.png");

        btnExit = new PifPafButton("Exit", font, 300, 600);

        space[0] = new Space(0, 0);
        space[1] = new Space(0, SCR_HEIGHT);
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

            if(btnExit.hit(touch.x, touch.y)){
                main.setScreen(main.screenMenu);
            }
        }

        // события
        for (Space s: space) s.move();

        // отрисовка
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for(Space s: space) batch.draw(imgBackGround, s.x, s.y, s.width, s.height);
        font.draw(batch, "Game", 400, 1000);
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
