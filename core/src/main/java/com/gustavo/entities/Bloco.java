package com.gustavo.entities;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import lombok.Data;

import java.awt.*;

@Data
public class Bloco {
    private float x;
    private float y;
    private float width;
    private float height;
    private final Texture texture;
    private final Sprite sprite;
    private final Rectangle rectangle;

    public Bloco(float height, float width, float y, float x) {
        this.height = height;
        this.width = width;
        this.y = y;
        this.x = x;

        texture = new Texture("bloco.png");
        sprite = new Sprite(texture);
        sprite.setSize(1, .5f);
        rectangle = new Rectangle(x, y, width, height);


    }

    public Bloco() {
        texture = new Texture("bloco.png");
        sprite = new Sprite(texture);
        sprite.setSize(1, .5f);
        rectangle = new Rectangle();
    }


}

