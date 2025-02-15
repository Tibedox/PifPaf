package ru.pifpaf;

import static ru.pifpaf.Main.SCR_WIDTH;

public class Joystick {
    private float x, y;
    public float width, height;
    public boolean isRight = false;

    public Joystick(float diameter, boolean isRight) {
        width = height = diameter;
        on(isRight);
        y = height/2;
    }

    public void on(boolean isRight){
        this.isRight = isRight;
        if(isRight) x = SCR_WIDTH-width/2;
        else x = width/2;
    }

    public boolean isTouchInside(float tx, float ty){
        return Math.pow(tx-x, 2)+Math.pow(ty-y, 2) <= Math.pow(width/2, 2);
    }

    public float scrX(){
        return x-width/2;
    }

    public float scrY(){
        return y-height/2;
    }
}
