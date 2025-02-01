package ru.pifpaf;

import com.badlogic.gdx.utils.TimeUtils;

public class Ship extends SpaceObject{
    public int phase, nPhases = 12;
    private long timeLastPhase, timePhaseInterval = 33;

    public Ship(float x, float y) {
        super(x, y);
        width = 200;
        height = 200;
    }

    @Override
    public void move() {
        super.move();
        if(TimeUtils.millis()> timeLastPhase+timePhaseInterval) {
            if (++phase == nPhases) phase = 0;
            timeLastPhase = TimeUtils.millis();
        }
    }

    public float scrX(){
        return x-width/2;
    }

    public float scrY(){
        return y-height/2;
    }
}
