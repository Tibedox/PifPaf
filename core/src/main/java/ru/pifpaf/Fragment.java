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
        float a = MathUtils.random(0f, 360f);
        float v = MathUtils.random(1f, 10f);
        vx = v*MathUtils.sin(a);
        vy = v*MathUtils.cos(a);
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
