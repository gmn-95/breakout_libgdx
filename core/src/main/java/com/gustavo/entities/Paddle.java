package com.gustavo.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import lombok.Getter;


@Getter
public class Paddle {

    private final Texture texture;
    private final Sprite sprite;
    private final Rectangle rectangle;

    public Paddle() {
        texture = new Texture("paddle.png");
        sprite = new Sprite(texture);
        sprite.setSize(2, .5f);
        sprite.setPosition(9, 1); //posição inicial do paddle
        rectangle = new Rectangle();
    }

    public void syncRectangle(){
        rectangle.set(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }

    public void impedePaddleDeSairDaTela(float worldWidth) {
        float paddleWidth = this.getSprite().getWidth();
        this.getSprite().setX(MathUtils.clamp(this.getSprite().getX(), 0, worldWidth - paddleWidth));
    }

    public void movePaddle() {
        float speed = 11f;
        float delta = Gdx.graphics.getDeltaTime();

        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            this.getSprite().translateX(speed * delta);
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)){
            this.getSprite().translateX(-speed * delta);
        }

        // só para testes
//        else if(Gdx.input.isKeyPressed(Input.Keys.W)){
//            paddleSprite.translateY(speed * delta);
//        } else if(Gdx.input.isKeyPressed(Input.Keys.S)){
//            paddleSprite.translateY(-speed * delta);
//        }
    }


}
