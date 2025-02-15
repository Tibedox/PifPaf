package ru.pifpaf;

import static ru.pifpaf.Main.RIGHT;
import static ru.pifpaf.Main.SCR_WIDTH;

import com.badlogic.gdx.math.Vector3;

public class Joystick {
    public float x, y;
    public float width, height;
    public boolean side;
    public String text;

    public Joystick(float diameter, boolean side) {
        width = height = diameter;
        setSide(side);
        y = height/2;
    }

    public void setSide(boolean side){
        this.side = side;
        if(side == RIGHT) {
            x = SCR_WIDTH - width / 2;
            text = "Joystick Right";
        }
        else {
            x = width / 2;
            text = "Joystick Left";
        }
    }

    public boolean isTouchInside(Vector3 t){
        return Math.pow(t.x-x, 2)+Math.pow(t.y-y, 2) <= Math.pow(width/2, 2);
    }

    public float scrX(){
        return x-width/2;
    }

    public float scrY(){
        return y-height/2;
    }
}
