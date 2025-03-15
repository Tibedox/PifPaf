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

public class ScreenRecords implements Screen {
    SpriteBatch batch;
    OrthographicCamera camera;
    Vector3 touch;
    BitmapFont font80white, font50yellow;
    Main main;

    Texture imgBackGround;

    PifPafButton btnGlobalLocal;
    PifPafButton btnClear;
    PifPafButton btnBack;
    Player[] players;

    ScreenRecords(Main main){
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;
        font80white = main.font80White;
        font50yellow = main.font50Yellow;
        this.main = main;
        players = main.screenGame.players;

        imgBackGround = new Texture("space1.png");

        btnGlobalLocal = new PifPafButton("Local", font80white, 1350);
        btnClear = new PifPafButton("Clear", font80white, 350);
        btnBack = new PifPafButton("Back", font80white, 200);
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

            if(btnClear.hit(touch)){
                main.screenGame.clearTableOfRecords();
                main.screenGame.saveTableOfRecords();
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
        font80white.draw(batch, "Records", 0, 1500, SCR_WIDTH, Align.center, false);
        btnGlobalLocal.font.draw(batch, btnGlobalLocal.text, btnGlobalLocal.x, btnGlobalLocal.y);
        font50yellow.draw(batch, "score", 450, 1270, 200, Align.right, false);
        font50yellow.draw(batch, "kills", 600, 1270, 200, Align.right, false);
        for (int i = 0; i < players.length; i++) {
            font80white.draw(batch, i+1+"", 100, 1200-i*80);
            font80white.draw(batch, players[i].name, 200, 1200-i*80);
            font80white.draw(batch, players[i].score+"", 450, 1200-i*80, 200, Align.right, false);
            font80white.draw(batch, players[i].kills+"", 600, 1200-i*80, 200, Align.right, false);
        }
        btnClear.font.draw(batch, btnClear.text, btnClear.x, btnClear.y);
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
