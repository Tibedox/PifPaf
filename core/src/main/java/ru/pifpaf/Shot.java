package ru.pifpaf;

import static ru.pifpaf.Main.SCR_HEIGHT;
import static ru.pifpaf.Main.SCR_WIDTH;

public class Shot extends SpaceObject{
    public boolean isDead;

    public Shot(float x, float y) {
        super(x, y);
        width = 50;
        height = 150;
        vy = 20f;
    }

    @Override
    public void move() {
        super.move();
        outOfScreen();
    }

    private void outOfScreen(){
        isDead = x<-width/2 || x>SCR_WIDTH+width/2 || y<-height/2 || y>SCR_HEIGHT+height/2;
    }
}
