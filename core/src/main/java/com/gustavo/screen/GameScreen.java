package com.gustavo.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
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
    private final Colisao colisao;
    private final LinkedList<Bloco> blocos;

    public GameScreen(final BreakoutGame game) {
        this.game = game;

        bola = new Bola();
        paddle = new Paddle();

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
//        desenhaGrade();
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
        float blockWidth = Bloco.getWidth();
        float blockHeight = Bloco.getHeight();

        float margemEsquerda = 4f;
        float x = margemEsquerda;
        float y = 10f;

        for (int c = 0; c < 8; c++) { // Colunas
            for (int l = 0; l < 6; l++) { // Linhas
                Bloco bloco = new Bloco(y, x);
                blocos.add(bloco);
                x += blockWidth + 0.03f; // Move para a direita
            }
            y += blockHeight + 0.03f; // Move para cima
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
        private boolean isIndoParaDireita = true;
        private boolean isIndoParaEsquerda = false;
        private boolean isIndoParaCima = false;
        private boolean isIndoParaBaixo = true;

        private void mudaDirecaoBolaDeAcordoComColisao(){
            float worldWidth = game.getViewport().getWorldWidth();
            float worldHeight = game.getViewport().getWorldHeight();
            float x = bola.getSprite().getX();
            float y = bola.getSprite().getY();

            if (y >= (worldHeight - bola.getSprite().getHeight())){
                isIndoParaCima = false;
                isIndoParaBaixo = true;
            } else if (y <= 0) {
                isIndoParaCima = true;
                isIndoParaBaixo = false;
            }

            if (x >= (worldWidth - bola.getSprite().getWidth())){
                isIndoParaDireita = false;
                isIndoParaEsquerda = true;
            } else if (x <= 0) {
                isIndoParaDireita = true;
                isIndoParaEsquerda = false;
            }

            if(isIndoParaCima){
                bola.moveParaCima();
            }

            if(isIndoParaBaixo){
                bola.moveParaBaixo();
            }

            if (isIndoParaEsquerda){
                bola.moveParaEsquerda();
            }

            if(isIndoParaDireita){
                bola.moveParaDireita();
            }

        }


        public void colisaoBolaEBloco(LinkedList<Bloco> blocos) {
            Iterator<Bloco> iterator = blocos.iterator();

            while (iterator.hasNext()) {
                Bloco bloco = iterator.next();
                if (bloco == null) continue;

                Rectangle bolaRect = bola.getSprite().getBoundingRectangle();
                Rectangle blocoRect = bloco.getRectangle();

                // 1 - detecta colisao
                if(bolaRect.overlaps(blocoRect)){

                    // 2 - calcular os centros:
                    //  posicaoX + largura / 2
                    //  posicaoY + altura / 2
                    float bolaCentroX = bolaRect.x + bolaRect.width / 2;
                    float bolaCentroY = bolaRect.y + bolaRect.height / 2;

                    float blocoCentroX = blocoRect.x + blocoRect.width / 2;
                    float blocoCentroY = blocoRect.y + blocoRect.height / 2;

                    //3 - calcular a distancias entre os centros
                    float dx = bolaCentroX - blocoCentroX;
                    float dy = bolaCentroY - blocoCentroY;

                    //4 - calcular a sobreposição (O quanto os dois objetos estão se sobrepondo em cada direção)
                    //sobreposicao horizontal (largura) = soma das metades das larguras - valor absoluto da distanciaX
                    //sobreposicao vertical (altura) = soma das metades das altura - valor absoluto da distanciaY
                    float larguraMedia = (bolaRect.width + blocoRect.width) / 2;
                    float alturaMedia = (bolaRect.height + blocoRect.height) / 2;

                    float larguraSobreposicao = larguraMedia - Math.abs(dx);
                    float alturaSobreposicao = alturaMedia - Math.abs(dy);

                    if (larguraSobreposicao < alturaSobreposicao){
                        // Colisão lateral
                        if (dx > 0) {
                            // Bateu no lado direito do bloco
                            deveIrParaDireita();
                        } else {
                            // Bateu no lado esquerdo do bloco
                            deveIrParaEsquerda();
                        }
                    } else {
                        // Colisão vertical
                        if (dy > 0) {
                            // Bateu por cima do bloco
                            deveIrParaCima();
                        } else {
                            // Bateu por baixo do bloco
                            deveIrParaBaixo();
                        }
                    }

                    mudaDirecaoBolaDeAcordoComColisao();
                    iterator.remove(); // Remove de forma segura
                }
            }
        }

        public void colisaoBolaEPaddle(){
            boolean result = paddle.getRectangle().overlaps(bola.getRectangle());

            if (result) {
                deveIrParaCima();
            }
        }

        private void deveIrParaBaixo(){
            isIndoParaBaixo = true;
            isIndoParaCima = false;
        }

        private void deveIrParaCima(){
            isIndoParaCima = true;
            isIndoParaBaixo = false;
        }

        private void deveIrParaEsquerda(){
            isIndoParaEsquerda = true;
            isIndoParaDireita = false;
        }

        private void deveIrParaDireita(){
            isIndoParaDireita = true;
            isIndoParaEsquerda = false;
        }

    }
}
