package ru.pifpaf;

import static ru.pifpaf.Main.*;

import com.badlogic.gdx.math.MathUtils;

public class Fragment extends SpaceObject{
    public int num;
    public float rotation;
    private float vRotation;

    public Fragment(float x, float y, int type) {
        super(x, y);
        this.type = type;
        num = MathUtils.random(0, 24);
        width = MathUtils.random(20f, 50f);
        height = MathUtils.random(20f, 50f);
        vRotation = MathUtils.random(-5f, 5f);
        vx = MathUtils.random(-10f, 10f);
        vy = MathUtils.random(-10f, 10f);
    }

    @Override
    public void move() {
        super.move();
        rotation += vRotation;
    }

    public boolean outOfScreen(){
        return x<-width/2 || x>SCR_WIDTH+width/2 || y<-height/2 || y>SCR_HEIGHT+height*5;
    }
}
