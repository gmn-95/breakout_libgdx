package com.gustavo.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import lombok.Getter;


@Getter
public class Paddle {

    private final Texture texture;
    private final Sprite sprite;
    private final Rectangle rectangle;

    public Paddle(){
        texture = new Texture("paddle.png");
        sprite = new Sprite(texture);
        sprite.setSize(2, .5f);
        sprite.setPosition(9, 1); //posição inicial do paddle
        rectangle = new Rectangle();
    }

    public void syncRectangle(){
        rectangle.set(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }
}
