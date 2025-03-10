package ru.pifpaf;

import static ru.pifpaf.Main.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class ScreenGame implements Screen {
    Main main;
    SpriteBatch batch;
    OrthographicCamera camera;
    Vector3 touch;
    BitmapFont font50, font80, font120;

    Texture imgJoystick;
    Texture imgBackGround;
    Texture imgShipsAtlas;
    Texture imgShotsAtlas;
    TextureRegion[] imgShip = new TextureRegion[12];
    TextureRegion[][] imgEnemy = new TextureRegion[4][12];
    TextureRegion[][] imgFragment = new TextureRegion[5][25];
    TextureRegion imgShot;

    Sound sndBlaster;
    Sound sndExplosion;

    PifPafButton btnExit;

    Space[] space = new Space[2];
    Ship ship;
    List<Enemy> enemies = new ArrayList<>();
    List<Shot> shots = new ArrayList<>();
    List<Fragment> fragments = new ArrayList<>();
    private long timeLastSpawnEnemy, timeIntervalSpawnEnemy = 2000;
    private long timeLastShoot, timeIntervalShoot = 500;
    private int numFragments = 100;
    private int score;
    private boolean gameOver;

    ScreenGame(Main main){
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;
        font50 = main.font50Yellow;
        font80 = main.font80White;
        font120 = main.font120White;
        this.main = main;

        imgJoystick = new Texture("joystick.png");
        imgBackGround = new Texture("space2.png");
        imgShipsAtlas = new Texture("ships_atlas.png");
        imgShotsAtlas = new Texture("shots.png");
        for (int i = 0; i < imgShip.length; i++) {
            imgShip[i] = new TextureRegion(imgShipsAtlas, (i<7?i:12-i)*400, 0, 400, 400);
        }
        for(int j = 0; j < imgEnemy.length; j++) {
            for (int i = 0; i < imgEnemy[j].length; i++) {
                imgEnemy[j][i] = new TextureRegion(imgShipsAtlas, (i < 7 ? i : 12 - i) * 400, (j+1)*400, 400, 400);
            }
        }
        int k = (int) Math.sqrt(imgFragment[0].length);
        int size = 400/k;
        for(int j = 0; j < imgFragment.length; j++) {
            for (int i = 0; i < imgFragment[j].length; i++) {
                if(j<4) imgFragment[j][i] = new TextureRegion(imgEnemy[j][0], i%k*size, i/k*size, size, size);
                else imgFragment[j][i] = new TextureRegion(imgShip[0], i%k*size, i/k*size, size, size);
            }
        }
        imgShot = new TextureRegion(imgShotsAtlas, 0, 0, 100, 350);

        sndBlaster = Gdx.audio.newSound(Gdx.files.internal("blaster.mp3"));
        sndExplosion = Gdx.audio.newSound(Gdx.files.internal("explosion.mp3"));

        btnExit = new PifPafButton("x", font50, 870, 1590);

        space[0] = new Space(0, 0);
        space[1] = new Space(0, SCR_HEIGHT);
        ship = new Ship(SCR_WIDTH/2, 150);
    }

    @Override
    public void show() {
        try {
            Thread.sleep(500);
            Gdx.input.setInputProcessor(null);
        } catch (InterruptedException e) {

        }
        Gdx.input.setInputProcessor(new PifPafInputProcessor());
    }

    @Override
    public void render(float delta) {
        // касания
        if(Gdx.input.justTouched()){
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);

            if(btnExit.hit(touch)){
                main.setScreen(main.screenMenu);
            }
        }
        if(controls == ACCELEROMETER){
            ship.vx = -Gdx.input.getAccelerometerX()*2;
            ship.vy = -Gdx.input.getAccelerometerY()*2;
        }

        // события
        for (Space s: space) s.move();
        spawnEnemy();
        if(!gameOver) {
            ship.move();
            spawnShots();
        }
        for (int i = enemies.size()-1; i>=0; i--) {
            enemies.get(i).move();
            if(enemies.get(i).overlap(ship)){
                if(isSound) sndExplosion.play();
                if(isSound) sndExplosion.play();
                spawnFragments(enemies.get(i));
                spawnFragments(ship);
                enemies.remove(i);
                ship.dead();
                gameOver = true;
                break;
            }
        }
        for (int i = shots.size()-1; i >= 0; i--) {
            shots.get(i).move();
            if(shots.get(i).outOfScreen()) {
                shots.remove(i);
                continue;
            }
            for (int j = enemies.size()-1; j >= 0; j--) {
                if(shots.get(i).readyToKill() && shots.get(i).overlap(enemies.get(j))){
                    shots.remove(i);
                    if(isSound) sndExplosion.play();
                    if(--enemies.get(j).hp == 0) {
                        score += enemies.get(j).price;
                        spawnFragments(enemies.get(j));
                        enemies.remove(j);
                    }
                    break;
                }
            }
        }
        for (int i = fragments.size()-1; i >= 0; i--) {
            fragments.get(i).move();
            if(fragments.get(i).outOfScreen()) fragments.remove(i);
        }

        // отрисовка
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for(Space s: space) batch.draw(imgBackGround, s.x, s.y, s.width, s.height);
        if(controls == JOYSTICK){
            batch.draw(imgJoystick, main.joystick.scrX(), main.joystick.scrY(), main.joystick.width, main.joystick.height);
        }
        for (Fragment f: fragments){
            batch.draw(imgFragment[f.type][f.num], f.scrX(), f.scrY(), f.width/2, f.height/2, f.width, f.height, 1, 1, f.rotation);
        }
        for (Enemy e: enemies){
            batch.draw(imgEnemy[e.type][e.phase], e.scrX(), e.scrY(), e.width, e.height);
        }
        for (Shot s: shots){
            batch.draw(imgShot, s.scrX(), s.scrY(), s.width, s.height);
        }
        batch.draw(imgShip[ship.phase], ship.scrX(), ship.scrY(), ship.width, ship.height);
        font50.draw(batch, "score: "+score, 10, 1590);
        btnExit.font.draw(batch, btnExit.text, btnExit.x, btnExit.y);
        if(gameOver){
            font120.draw(batch, "GAME OVER", 0, 1200, SCR_WIDTH, Align.center, true);
        }
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
        imgBackGround.dispose();
        imgShipsAtlas.dispose();
        imgShotsAtlas.dispose();
        imgJoystick.dispose();
        sndBlaster.dispose();
        sndExplosion.dispose();
    }

    private void spawnEnemy(){
        if(TimeUtils.millis()>timeLastSpawnEnemy+timeIntervalSpawnEnemy){
            enemies.add(new Enemy());
            timeLastSpawnEnemy = TimeUtils.millis();
        }
    }
    private void spawnShots(){
        if(TimeUtils.millis()>timeLastShoot+timeIntervalShoot){
            shots.add(new Shot(ship.x-60, ship.y));
            shots.add(new Shot(ship.x+60, ship.y));
            timeLastShoot = TimeUtils.millis();
            if(isSound) sndBlaster.play();
        }
    }

    private void spawnFragments(SpaceObject o){
        for (int i = 0; i < numFragments; i++) {
            fragments.add(new Fragment(o.x, o.y, o.type));
        }
    }

    class PifPafInputProcessor implements InputProcessor{

        @Override
        public boolean keyDown(int keycode) {
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            touch.set(screenX, screenY, 0);
            camera.unproject(touch);
            if(controls == SCREEN) {
                ship.touchScreen(touch);
            }
            if(controls == JOYSTICK) {
                if(main.joystick.isTouchInside(touch)){
                    ship.touchJoystick(touch, main.joystick);
                }
            }
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            touch.set(screenX, screenY, 0);
            camera.unproject(touch);
            if(controls == SCREEN) {
                ship.touchScreen(touch);
            }
            if(controls == JOYSTICK) {
                if(main.joystick.isTouchInside(touch)){
                    ship.touchJoystick(touch, main.joystick);
                }
            }
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(float amountX, float amountY) {
            return false;
        }
    }
}
