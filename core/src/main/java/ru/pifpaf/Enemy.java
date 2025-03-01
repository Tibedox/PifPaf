package ru.pifpaf;

import static ru.pifpaf.Main.*;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class Enemy extends SpaceObject{
    public int phase, nPhases = 12;
    private long timeLastPhase, timePhaseInterval = 33;
    public int hp;

    public Enemy() {
        type = MathUtils.random(0, 3);
        settingsByType();
        x = MathUtils.random(width/2, SCR_WIDTH-width/2);
        y = MathUtils.random(SCR_HEIGHT+height, SCR_HEIGHT*2);
    }

    @Override
    public void move() {
        super.move();
        changePhase();
    }

    private void changePhase(){
        if(TimeUtils.millis()> timeLastPhase+timePhaseInterval) {
            if (++phase == nPhases) phase = 0;
            timeLastPhase = TimeUtils.millis();
        }
    }

    private void settingsByType(){
        switch (type){
            case 0:
                hp = 2;
                width = height = 200;
                vy = MathUtils.random(-8f, -6f);
                break;
            case 1:
                hp = 6;
                width = height = 300;
                vy = MathUtils.random(-6f, -4f);
                break;
            case 2:
                hp = 3;
                width = height = 250;
                vy = MathUtils.random(-7f, -5f);
                break;
            case 3:
                hp = 1;
                width = height = 160;
                vy = MathUtils.random(-9f, -7f);
                break;
        }
    }
}
