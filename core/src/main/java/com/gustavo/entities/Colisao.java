package com.gustavo.entities;

import com.badlogic.gdx.math.Rectangle;
import com.gustavo.BreakoutGame;
import lombok.Data;

import java.util.Iterator;
import java.util.LinkedList;

@Data
public class Colisao {

    private final BreakoutGame game;
    private final Bola bola;
    private final Paddle paddle;

    /**
     * ‘Flags’ para indicar a direção da bola ao bater nos blocos
     * */
    private boolean isIndoParaDireita = true;
    private boolean isIndoParaEsquerda = false;
    private boolean isIndoParaCima = false;
    private boolean isIndoParaBaixo = true;

    public Colisao(BreakoutGame game, Bola bola, Paddle paddle) {
        this.game = game;
        this.bola = bola;
        this.paddle = paddle;
    }

    public void mudaDirecaoBolaDeAcordoComColisao(){
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

    //TODO dependendo de onde bater no paddle, é necessário devolver para a direção que veio
    public void colisaoBolaEPaddle(){
        boolean result = paddle.getRectangle().overlaps(bola.getRectangle());
//        paddle.getRectangle().

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
