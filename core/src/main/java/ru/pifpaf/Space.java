package ru.pifpaf;

import static ru.pifpaf.Main.*;

public class Space extends SpaceObject{
    public Space(float x, float y) {
        super(x, y);
        width = SCR_WIDTH;
        height = SCR_HEIGHT;
        vy = -5;
    }

    @Override
    public void move() {
        super.move();
        if(y<-SCR_HEIGHT) y = SCR_HEIGHT;
    }
}
