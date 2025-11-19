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
import com.gustavo.entities.Colisao;
import com.gustavo.entities.Paddle;

import java.util.LinkedList;

/**
 * <a href="https://www.youtube.com/watch?v=AMUv8KvVt08">Exemplo do jogo</a>
 * */
public class GameScreen implements Screen {

    private final BreakoutGame game;

    private final Bola bola;
    private final Paddle paddle;
    private final Colisao colisao;
    private final LinkedList<Bloco> blocos;

    public GameScreen(final BreakoutGame game) {
        this.game = game;

        this.bola = new Bola();
        this.paddle = new Paddle();

        this.blocos = new LinkedList<>();
        criaBlocos();
        this.colisao = new Colisao(game, this.bola, this.paddle);
    }

    private void input(){
        this.paddle.movePaddle();
    }

    private void logic(){
        float worldWidth = this.game.getViewport().getWorldWidth();
        float worldHeight = this.game.getViewport().getWorldHeight();

        this.paddle.impedePaddleDeSairDaTela(worldWidth);
        this.colisao.mudaDirecaoBolaDeAcordoComColisao();
        this.paddle.syncRectangle();
        this.bola.syncRectangle();
        this.colisao.colisaoBolaEPaddle();
        this.colisao.colisaoBolaEBloco(blocos);
        //        movimentoHorizontalBolaTeste();

    }

    private void draw(){
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        this.game.getViewport().apply();
        this.game.getBatch().setProjectionMatrix(this.game.getViewport().getCamera().combined);

        this.game.getShapeRenderer().setAutoShapeType(true);
        this.game.getShapeRenderer().begin();
//        desenhaGrade();
        this.game.getShapeRenderer().end();

        this.game.getBatch().begin();
        desenhaBlocos();
        desenhaBall();
        desenhaPaddle();
        this.game.getBatch().end();
    }



    private void desenhaGrade(){
        int xy1 = 20;
        int xy2 = 1;
        this.game.getShapeRenderer().setColor(Color.WHITE);
        //desenha 'grade'
        for (int i = 0; i < 80; i++){
            xy1 += 20;
            xy2 += 20;
            this.game.getShapeRenderer().line(0, xy1, Gdx.graphics.getWidth(), xy1);
            this.game.getShapeRenderer().line(xy2, 0, xy2, Gdx.graphics.getHeight());
        }
    }

    private void desenhaBlocos() {
        for (Bloco bloco : blocos){
            if (bloco == null) continue;
            this.game.getBatch().draw(bloco.getSprite(), bloco.getX(), bloco.getY(), bloco.getWidth(), bloco.getHeight());
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
                this.blocos.add(bloco);
                x += blockWidth + 0.03f; // Move para a direita
            }
            y += blockHeight + 0.03f; // Move para cima
            x = margemEsquerda; // Reseta a posição X
        }

    }

    private void desenhaPaddle() {
        this.paddle.getSprite().draw(this.game.getBatch()); // Apenas desenha o sprite
    }

    private void desenhaBall(){
        this.bola.getSprite().draw(this.game.getBatch());
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
