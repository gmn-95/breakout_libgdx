package com.gustavo.screen;

import com.badlogic.gdx.Screen;
import com.gustavo.BreakoutGame;

/**
 * Responsável pela tela de menu
 * */
public class MainMenuScreen implements Screen {

    private final BreakoutGame game;

    public MainMenuScreen(BreakoutGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        game.setScreen(new GameScreen(game));
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

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
