package ru.pifpaf;

import static ru.pifpaf.Main.*;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class Enemy extends SpaceObject{
    public int phase, nPhases = 12;
    private long timeLastPhase, timePhaseInterval = 33;
    public int type;

    public Enemy() {
        width = 200;
        height = 200;
        x = MathUtils.random(width/2, SCR_WIDTH-width/2);
        y = MathUtils.random(SCR_HEIGHT+height, SCR_HEIGHT*2);
        vy = MathUtils.random(-8f, -4f);
        type = MathUtils.random(0, 3);
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
}
