package com.gustavo.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.gustavo.BreakoutGame;
import com.gustavo.entities.Bloco;
import com.gustavo.entities.Bola;
import com.gustavo.entities.Paddle;
import lombok.Data;

import java.util.Iterator;
import java.util.LinkedList;

public class GameScreen implements Screen {

    private final BreakoutGame game;

    private final Bola bola;
    private final Paddle paddle;
    private final Bloco blocoInicial;
    private final Colisao colisao;
    private final LinkedList<Bloco> blocos;

    public GameScreen(final BreakoutGame game) {
        this.game = game;

        bola = new Bola();
        paddle = new Paddle();
        blocoInicial = new Bloco();

        blocos = new LinkedList<>();
        criaBlocos();
        colisao = new Colisao(game, bola, paddle);
    }

    private void input(){
        movePaddle();
    }

    private void logic(){
        float worldWidth = game.getViewport().getWorldWidth();
        float worldHeight = game.getViewport().getWorldHeight();

        impedePaddleDeSairDaTela(worldWidth);
        colisao.mudaDirecaoBolaDeAcordoComColisao();
        paddle.syncRectangle();
        bola.syncRectangle();
        colisao.colisaoBolaEPaddle();
        colisao.colisaoBolaEBloco(blocos);
        //        movimentoHorizontalBolaTeste();

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
        desenhaBall();
        desenhaPaddle();
        game.getBatch().end();
    }

    private void impedePaddleDeSairDaTela(float worldWidth){
        float paddleWidth = paddle.getSprite().getWidth();
        paddle.getSprite().setX(MathUtils.clamp(paddle.getSprite().getX(), 0, worldWidth - paddleWidth));
    }

    private void desenhaGrade(){
        int xy1 = 20;
        int xy2 = 1;
        game.getShapeRenderer().setColor(Color.WHITE);
        //desenha 'grade'
        for (int i = 0; i < 80; i++){
            xy1 += 20;
            xy2 += 20;
            game.getShapeRenderer().line(0, xy1, Gdx.graphics.getWidth(), xy1);
            game.getShapeRenderer().line(xy2, 0, xy2, Gdx.graphics.getHeight());
        }
    }

    private void desenhaBlocos() {
        for (Bloco bloco : blocos){
            if (bloco == null) continue;
            game.getBatch().draw(bloco.getSprite(), bloco.getX(), bloco.getY(), bloco.getWidth(), bloco.getHeight());
        }
    }

    private void criaBlocos(){
        float blockWidth = ((float) blocoInicial.getTexture().getWidth() / Gdx.graphics.getWidth()) * game.getViewport().getWorldWidth(); // Converte para unidades do mundo
        float blockHeight = ((float) blocoInicial.getTexture().getHeight() / Gdx.graphics.getHeight()) * game.getViewport().getWorldHeight(); // Converte para unidades do mundo

        float margemEsquerda = 4f;
        float x = margemEsquerda;
        float y = 10f;

        for (int c = 0; c < 8; c++) { // Colunas
            for (int l = 0; l < 6; l++) { // Linhas
                Bloco bloco = new Bloco(blockHeight, blockWidth, y, x);
                blocos.add(bloco);
                x += blockWidth; // Move para a direita
            }
            y += blockHeight; // Move para cima
            x = margemEsquerda; // Reseta a posição X
        }

    }

    private void desenhaPaddle() {
        paddle.getSprite().draw(game.getBatch()); // Apenas desenha o sprite
    }

    private void desenhaBall(){
        bola.getSprite().draw(game.getBatch());
    }


    private void movePaddle(){
        float speed = 11f;
        float delta = Gdx.graphics.getDeltaTime();

        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            paddle.getSprite().translateX(speed * delta);
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)){
            paddle.getSprite().translateX(-speed * delta);
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

    @Data
    public static class Colisao{

        private final BreakoutGame game;
        private final Bola bola;
        private final Paddle paddle;
        private boolean deveIrDireita = true;
        private boolean deveIrEsquerda = false;
        private boolean deveIrCima = true;
        private boolean deveIrBaixo = false;

        private void mudaDirecaoBolaDeAcordoComColisao(){
            float worldWidth = game.getViewport().getWorldWidth();
            float worldHeight = game.getViewport().getWorldHeight();
            float x = bola.getSprite().getX();
            float y = bola.getSprite().getY();

            if (y >= (worldHeight - bola.getSprite().getHeight())){
                deveIrCima = false;
                deveIrBaixo = true;
            } else if (y <= 0) {
                deveIrCima = true;
                deveIrBaixo = false;
            }

            if (x >= (worldWidth - bola.getSprite().getWidth())){
                deveIrDireita = false;
                deveIrEsquerda = true;
            } else if (x <= 0) {
                deveIrDireita = true;
                deveIrEsquerda = false;
            }

            if(deveIrCima){
                bola.moveParaCima();
            }

            if(deveIrBaixo){
                bola.moveParaBaixo();
            }

            if (deveIrEsquerda){
                bola.moveParaEsquerda();
            }

            if(deveIrDireita){
                bola.moveParaDireita();
            }

        }


        public void colisaoBolaEBloco(LinkedList<Bloco> blocos) {
            Iterator<Bloco> iterator = blocos.iterator();

            while (iterator.hasNext()) {
                Bloco bloco = iterator.next();
                if (bloco == null) continue;

                boolean colisao = bola.getRectangle().overlaps(bloco.getRectangle());

                if (colisao) {
                    // TODO verificar onde a bola bateu para mudar a direção dela

                    mudaDirecaoBolaDeAcordoComColisao();
                    iterator.remove(); // Remove de forma segura
                }
            }
        }


        public void colisaoBolaEPaddle(){
            boolean result = paddle.getRectangle().overlaps(bola.getRectangle());

            if (result) {
                deveIrCima = true;
                deveIrBaixo = false;
            }
        }

    }
}
