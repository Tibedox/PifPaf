package ru.pifpaf;

import static ru.pifpaf.Main.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
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
    Player[] players = new Player[10];
    public Player player;
    private long timeLastSpawnEnemy, timeIntervalSpawnEnemy = 2000;
    private long timeLastShoot, timeIntervalShoot = 1000;
    private int numFragments = 100;
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
        player = new Player();
        for (int i = 0; i < players.length; i++) {
            players[i] = new Player();
        }
        loadTableOfRecords();
    }

    @Override
    public void show() {
        try {
            Thread.sleep(500);
            Gdx.input.setInputProcessor(null);
        } catch (InterruptedException e) {

        }
        Gdx.input.setInputProcessor(new PifPafInputProcessor());
        gameStart();
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
                spawnFragments(enemies.get(i));
                enemies.remove(i);
                gameOver();
                break;
            }
            if(enemies.get(i).outOfScreen()){
                enemies.remove(i);
                if(!gameOver) gameOver();
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
                        player.score += enemies.get(j).price;
                        player.kills++;
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
        font50.draw(batch, "score: "+player.score, 10, 1590);
        btnExit.font.draw(batch, btnExit.text, btnExit.x, btnExit.y);
        if(gameOver){
            font120.draw(batch, "GAME OVER", 0, 1300, SCR_WIDTH, Align.center, false);
            font50.draw(batch, "score", 450, 1170, 200, Align.right, false);
            font50.draw(batch, "kills", 600, 1170, 200, Align.right, false);
            for (int i = 0; i < players.length; i++) {
                font80.draw(batch, i+1+"", 100, 1100-i*80);
                font80.draw(batch, players[i].name, 200, 1100-i*80);
                font80.draw(batch, players[i].score+"", 450, 1100-i*80, 200, Align.right, false);
                font80.draw(batch, players[i].kills+"", 600, 1100-i*80, 200, Align.right, false);
            }
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

    private void gameStart(){
        gameOver = false;
        ship = new Ship(SCR_WIDTH/2, 150);
        player.clear();
        enemies.clear();
        fragments.clear();
        shots.clear();
    }

    private void gameOver(){
        gameOver = true;
        if(isSound) sndExplosion.play();
        spawnFragments(ship);
        ship.dead();
        if(player.score >= players[players.length-1].score) {
            players[players.length-1].clone(player);
            sortTableOfRecords();
            saveTableOfRecords();
        }
    }

    private void sortTableOfRecords(){
        for(int j = 0; j < players.length; j++) {
            for (int i = 0; i < players.length - 1; i++) {
                if (players[i].score < players[i + 1].score) {
                    Player c = players[i];
                    players[i] = players[i + 1];
                    players[i + 1] = c;
                }
            }
        }
    }

    public void saveTableOfRecords(){
        Preferences prefs = Gdx.app.getPreferences("PifPafPrefs");
        for (int i = 0; i < players.length; i++) {
            prefs.putString("name"+i, players[i].name);
            prefs.putInteger("score"+i, players[i].score);
            prefs.putInteger("kills"+i, players[i].kills);
        }
        prefs.flush();
    }

    private void loadTableOfRecords(){
        Preferences prefs = Gdx.app.getPreferences("PifPafPrefs");
        for (int i = 0; i < players.length; i++) {
            players[i].name = prefs.getString("name"+i, "Noname");
            players[i].score = prefs.getInteger("score"+i, 0);
            players[i].kills = prefs.getInteger("kills"+i, 0);
        }
    }

    public void clearTableOfRecords(){
        for (Player p: players) {
            p.clone(new Player());
        }
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
