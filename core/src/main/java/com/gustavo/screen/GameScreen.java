package com.gustavo.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.gustavo.BreakoutGame;
import com.gustavo.entities.Bloco;

import java.util.LinkedList;

public class GameScreen implements Screen {

    private final BreakoutGame game;

    private final Texture blockTexture;
    private final Texture paddleTexture;
    private final Sprite blockSprite;
    private final Sprite paddleSprite;
    private final LinkedList<Bloco> blocos;


    public GameScreen(final BreakoutGame game) {
        this.game = game;

        blockTexture = new Texture("TESTE.png");
        blockSprite = new Sprite(blockTexture);
        blockSprite.setSize(1, .5f);

        paddleTexture = new Texture("paddle.png");
        paddleSprite = new Sprite(paddleTexture);
        paddleSprite.setSize(2, .5f);
        paddleSprite.setPosition(9, 1); //posição inicial do paddle

        blocos = new LinkedList<>();
        criaBlocos();
    }

    private void input(){
        movePaddle();
    }

    private void logic(){
        float worldWidth = game.getViewport().getWorldWidth();
        float worldHeight = game.getViewport().getWorldHeight();

        impedePaddleDeSairDaTela(worldWidth);
    }

    private void draw(){
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        game.getViewport().apply();
        game.getBatch().setProjectionMatrix(game.getViewport().getCamera().combined);

        game.getShapeRenderer().setAutoShapeType(true);
        game.getShapeRenderer().begin();
        desenhaGrade();
        game.getShapeRenderer().end();

        game.getBatch().begin();
        desenhaBlocos();
        desenhaPaddle();
        game.getBatch().end();
    }

    private void impedePaddleDeSairDaTela(float worldWidth){
        float paddleWidth = paddleSprite.getWidth();
        paddleSprite.setX(MathUtils.clamp(paddleSprite.getX(), 0, worldWidth - paddleWidth));
    }

    private void desenhaGrade(){
        int xy1 = 20;
        int xy2 = 1;
        game.getShapeRenderer().setColor(Color.WHITE);
        //denha 'grade'
        for (int i = 0; i < 80; i++){
            xy1 += 20;
            xy2 += 20;
            game.getShapeRenderer().line(0, xy1, Gdx.graphics.getWidth(), xy1);
            game.getShapeRenderer().line(xy2, 0, xy2, Gdx.graphics.getHeight());
        }
    }

    private void desenhaBlocos() {
        for (Bloco bloco : blocos){
            game.getBatch().draw(blockTexture, bloco.getX(), bloco.getY(), bloco.getWidth(), bloco.getHeight());
        }
    }

    private void criaBlocos(){
        float blockWidth = ((float) blockTexture.getWidth() / Gdx.graphics.getWidth()) * game.getViewport().getWorldWidth(); // Converte para unidades do mundo
        float blockHeight = ((float) blockTexture.getHeight() / Gdx.graphics.getHeight()) * game.getViewport().getWorldHeight(); // Converte para unidades do mundo

        float margemEsquerda = 4f;
        float x = margemEsquerda;
        float y = 10f;

        for (int c = 0; c < 8; c++) { // Colunas
            for (int l = 0; l < 6; l++) { // Linhas
                Bloco bloco = new Bloco.Builder().x(x).y(y).width(blockWidth).height(blockHeight).build();
                blocos.add(bloco);
                x += blockWidth; // Move para a direita
            }
            y += blockHeight; // Move para cima
            x = margemEsquerda; // Reseta a posição X
        }

    }

    private void desenhaPaddle() {
        paddleSprite.draw(game.getBatch()); // Apenas desenha o sprite
    }

    private void movePaddle(){
        float speed = 10f;
        float delta = Gdx.graphics.getDeltaTime();

        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            paddleSprite.translateX(speed * delta);
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)){
            paddleSprite.translateX(-speed * delta);
        }

        // só para testes
//        else if(Gdx.input.isKeyPressed(Input.Keys.W)){
//            paddleSprite.translateY(speed * delta);
//        } else if(Gdx.input.isKeyPressed(Input.Keys.S)){
//            paddleSprite.translateY(-speed * delta);
//        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        input();
        logic();
        draw();
    }

    @Override
    public void resize(int width, int height) {
        game.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
