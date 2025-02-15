package ru.pifpaf;

import static ru.pifpaf.Main.*;

public class Space extends SpaceObject{
    public Space(float x, float y) {
        super(x, y);
        width = SCR_WIDTH;
        height = SCR_HEIGHT+6;
        vy = -3;
    }

    @Override
    public void move() {
        super.move();
        if(y<-SCR_HEIGHT) y = SCR_HEIGHT;
    }
}
