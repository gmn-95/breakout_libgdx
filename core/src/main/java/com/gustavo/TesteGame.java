package com.gustavo;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.particles.values.LineSpawnShapeValue;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
/**
 * Game teste feito de acordo com a DOC: https://libgdx.com/wiki/start/a-simple-game
 * */
public class TesteGame extends ApplicationAdapter {

    private SpriteBatch spriteBatch;
    private FitViewport viewport;
    private Sprite bucketSprite;

    private Texture backGrounTexture;
    private Texture bucketTexture;
    private Texture dropTexture;
    private Sound dropSound;
    private Music music;

    private Vector2 touchPos;
    private Array<Sprite> dropSprites;

    private float dropTimer;

    private Rectangle bucketRectangle;
    private Rectangle dropRectangle;

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(8, 5);

        backGrounTexture = new Texture("gameteste/background.png");
        bucketTexture = new Texture("gameteste/bucket.png");
        dropTexture = new Texture("gameteste/drop.png");

        dropSound = Gdx.audio.newSound(Gdx.files.internal("gameteste/drop.mp3"));
        music = Gdx.audio.newMusic(Gdx.files.internal("gameteste/music.mp3"));

        bucketSprite = new Sprite(bucketTexture);
        bucketSprite.setSize(1, 1);

        touchPos = new Vector2();
        dropSprites = new Array<>();

        bucketRectangle = new Rectangle();
        dropRectangle = new Rectangle();

        music.setLooping(true);
        music.setVolume(.5f);
        music.play();
    }

    @Override
    public void render() {
        input();
        logic();
        draw();
    }

    private void input() {
        float speed = 4f;
        float delta = Gdx.graphics.getDeltaTime();

        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            bucketSprite.translateX(speed * delta);
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            bucketSprite.translateX(-speed * delta);
        }

        if(Gdx.input.isTouched()){
            touchPos.set(Gdx.input.getX(), Gdx.input.getY()); // Obtenha onde o toque aconteceu na tela
            viewport.unproject(touchPos); // converte a unidade para unidades do viewport
            bucketSprite.setCenterX(touchPos.x); // Altera a posição centralizada horizontalmente do balde
        }

    }

    private void logic() {
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        float bucketWidth = bucketSprite.getWidth();
        float bucketHeight = bucketSprite.getHeight();

        //limita o baude para nao sair da tela
        bucketSprite.setX(MathUtils.clamp(bucketSprite.getX(), 0, worldWidth - bucketWidth));

        float delta = Gdx.graphics.getDeltaTime();

        bucketRectangle.set(bucketSprite.getX(), bucketSprite.getY(), bucketWidth, bucketHeight);

        // loop para previnir vazemento de memoria
        for (int i = dropSprites.size - 1; i >= 0; i--) {
            Sprite dropSprite = dropSprites.get(i);
            float dropWidth = dropSprite.getWidth();
            float dropHeight = dropSprite.getHeight();

            dropSprite.translateY(-2f * delta);

            dropRectangle.set(dropSprite.getX(), dropSprite.getY(), dropWidth, dropHeight);

            // se já saiu da tela, removemos do array para limpar da memória
            if (dropSprite.getY() < -dropHeight){
                dropSprites.removeIndex(i);
            } else if (bucketRectangle.overlaps(dropRectangle)) {
                dropSprites.removeIndex(i);
                dropSound.play();
            }
        }

        dropTimer += delta; // Adiciona o delta atual ao temporizador
        if(dropTimer > 1f){ //checa se já passou mais de um segundo
            dropTimer = 0; //reset timer
            createDroplet();
        }
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        spriteBatch.begin();
        spriteBatch.draw(backGrounTexture, 0, 0, worldWidth, worldHeight); // desenha o fundo
        bucketSprite.draw(spriteBatch);

        for (Sprite dropSprite : dropSprites){
            dropSprite.draw(spriteBatch);
        }



        spriteBatch.end();

    }

    private void createDroplet(){
        float dropWidth = 1;
        float dropHeight = 1;
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        Sprite dropSprite = new Sprite(dropTexture);
        dropSprite.setSize(dropWidth, dropHeight);
        dropSprite.setX(MathUtils.random(0f, worldWidth - dropWidth)); //randomiza onde a gota cai
        dropSprite.setY(worldHeight);
        dropSprites.add(dropSprite);
    }


    @Override
    public void dispose() {
        spriteBatch.dispose();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
}
