package com.gustavo.entities;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import lombok.Data;
import lombok.Getter;

import java.awt.*;

@Data
public class Bloco {
    private float x;
    private float y;
    private final Texture texture;
    private final Sprite sprite;
    private final Rectangle rectangle;
    @Getter
    private static final float width = 2f;
    @Getter
    private static final float height = .5f;

    public Bloco(float y, float x) {
        this.y = y;
        this.x = x;

        texture = new Texture("bloco.png");
        sprite = new Sprite(texture);
        sprite.setSize(width, height);
        rectangle = new Rectangle(x, y, width, height);


    }

    public Bloco() {
        texture = new Texture("bloco.png");
        sprite = new Sprite(texture);
        sprite.setSize(width, height);
        rectangle = new Rectangle();
    }


}

