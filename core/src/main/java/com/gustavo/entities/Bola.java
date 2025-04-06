package com.gustavo.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import lombok.Getter;

@Getter
public class Bola {

    private final Texture texture;
    private final Sprite sprite;
    private static final float VELOCIDADE = .05f;
    private final Rectangle rectangle;

    public Bola () {
        texture = new Texture("ball.png");
        sprite = new Sprite(texture);
        sprite.setSize(.3f, .3f);
        sprite.setPosition(1, 9.8f);
        rectangle = new Rectangle();
    }

    public void moveParaCima(){
        sprite.setY(this.sprite.getY() + VELOCIDADE);
    }

    public void moveParaBaixo(){
        sprite.setY(this.sprite.getY() - VELOCIDADE);
    }

    public void moveParaEsquerda(){
        sprite.setX(this.sprite.getX() - VELOCIDADE);
    }

    public void moveParaDireita(){
        sprite.setX(this.sprite.getX() + VELOCIDADE);
    }

    public void syncRectangle(){
        rectangle.set(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }

}
