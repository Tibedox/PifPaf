package ru.pifpaf;

import static ru.pifpaf.Main.SCR_WIDTH;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector3;

public class PifPafButton {
    float x, y;
    float width, height;
    String text;
    BitmapFont font;

    public PifPafButton(String text, BitmapFont font, float x, float y) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.font = font;
        GlyphLayout glyphLayout = new GlyphLayout(font, text);
        width = glyphLayout.width;
        height = glyphLayout.height;
    }

    public PifPafButton(String text, BitmapFont font, float y) {
        this.y = y;
        this.text = text;
        this.font = font;
        GlyphLayout glyphLayout = new GlyphLayout(font, text);
        width = glyphLayout.width;
        height = glyphLayout.height;
        this.x = SCR_WIDTH/2 - width/2;
    }

    public void changeFont(BitmapFont font){
        this.font = font;
        GlyphLayout glyphLayout = new GlyphLayout(font, text);
        width = glyphLayout.width;
        height = glyphLayout.height;
    }

    boolean hit(float tx, float ty){
        return x<tx && tx<x+width && y-height<ty && ty<y;
    }

    boolean hit(Vector3 t){
        return x<t.x && t.x<x+width && y-height<t.y && t.y<y;
    }
}
