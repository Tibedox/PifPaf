package ru.pifpaf;

public class Ship extends SpaceObject{
    public Ship(float x, float y) {
        super(x, y);
        width = 200;
        height = 200;
    }

    public float scrX(){
        return x-width/2;
    }

    public float scrY(){
        return y-height/2;
    }
}
