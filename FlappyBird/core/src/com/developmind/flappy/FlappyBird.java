package com.developmind.flappy;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {

    private SpriteBatch batch;

    private Texture[] passaros;

    private Texture fundo;

    private Texture canoBaixo, canoTopo, gameOver, logo;

    private BitmapFont fonte, mensagem;

    private Circle passaroCirculo;

    private Rectangle retanguloCanoBaixo, retanguloCanoTopo;

    // private ShapeRenderer shape;

    private Random random;

    private OrthographicCamera camera;

    private Viewport viewport;

    private int estadoJogo = 0, pontuacao = 0;

    private float largura, altura, variacao = 0, velocidadeQueda = 0, posicaoInicialVertical, posicaoMovimentoCanoHorizontal, espacoCanos, deltaTime, alturaEntreCanos;

    private boolean marcouPonto;

    private final float VIRTUAL_WIDTH = 768;

    private final float VIRTUAL_HEIGHT = 1024;

    @Override
    public void create() {
        batch = new SpriteBatch();
        passaroCirculo = new Circle();
        // retanguloCanoBaixo = new Rectangle();
        // retanguloCanoTopo = new Rectangle();
        // shape = new ShapeRenderer();
        passaros = new Texture[]{new Texture("passaro1.png"), new Texture("passaro2.png"), new Texture("passaro3.png")};
        fundo = new Texture("fundo.png");
        canoBaixo = new Texture("cano_baixo_maior.png");
        canoTopo = new Texture("cano_topo_maior.png");
        gameOver = new Texture("game_over.png");
        logo = new Texture("logo.png");
        random = new Random();
        fonte = new BitmapFont();
        fonte.setColor(Color.WHITE);
        fonte.getData().setScale(6);
        mensagem = new BitmapFont();
        mensagem.setColor(Color.WHITE);
        mensagem.getData().setScale(3);
        largura = VIRTUAL_WIDTH;
        altura = VIRTUAL_HEIGHT;
        posicaoInicialVertical = altura / 2;
        posicaoMovimentoCanoHorizontal = largura;
        espacoCanos = 300;
        camera = new OrthographicCamera();
        camera.position.set(VIRTUAL_WIDTH / 2, VIRTUAL_HEIGHT / 2, 0);
        viewport = new StretchViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
    }

    @Override
    public void render() {
        camera.update();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        deltaTime = Gdx.graphics.getDeltaTime();
        variacao += deltaTime * 10;
        if (variacao > 2) {
            variacao = 0;
        }
        if (estadoJogo == 0) {
            if (Gdx.input.justTouched()) {
                estadoJogo = 1;
            }
        } else {
            velocidadeQueda++;
            if (posicaoInicialVertical > 0 || velocidadeQueda < 0) {
                posicaoInicialVertical -= velocidadeQueda;
            }
            if (estadoJogo == 1) {
                posicaoMovimentoCanoHorizontal -= deltaTime * 200;
                if (Gdx.input.justTouched()) {
                    velocidadeQueda = -20;
                }
                if (posicaoMovimentoCanoHorizontal < -canoTopo.getWidth()) {
                    posicaoMovimentoCanoHorizontal = largura;
                    alturaEntreCanos = random.nextInt(400) - 200;
                    marcouPonto = false;
                }
                if (posicaoMovimentoCanoHorizontal < 120) {
                    if (!marcouPonto) {
                        pontuacao++;
                        marcouPonto = true;
                    }
                }
            } else {
                if (Gdx.input.justTouched()) {
                    estadoJogo = 0;
                    pontuacao = 0;
                    velocidadeQueda = 0;
                    posicaoInicialVertical = altura / 2;
                    posicaoMovimentoCanoHorizontal = largura;
                }
            }
        }
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(fundo, 0, 0, largura, altura);
        batch.draw(canoTopo, posicaoMovimentoCanoHorizontal, altura / 2 + espacoCanos / 2 + alturaEntreCanos);
        batch.draw(canoBaixo, posicaoMovimentoCanoHorizontal, altura / 2 - canoBaixo.getHeight() - espacoCanos / 2 + alturaEntreCanos);
        batch.draw(passaros[(int) variacao], 120, posicaoInicialVertical);
        if(estadoJogo == 1){
            fonte.draw(batch, String.valueOf(pontuacao), largura / 2, altura - 50);
        }
        if(estadoJogo == 0) {
            batch.draw(logo, largura / 2 - 250, altura / 2 + 150, 500, 150);
            mensagem.draw(batch, "Toque para Iniciar!", largura / 2 - 180, altura / 2 - logo.getHeight() / 2);
        }
        if (estadoJogo == 2) {
            batch.draw(gameOver, largura / 2 - gameOver.getWidth() / 2, altura / 2);
            mensagem.draw(batch, "Toque para reiniciar!", largura / 2 - 200, altura / 2 - gameOver.getHeight() / 2);
        }
        batch.end();
        passaroCirculo.set(120 + passaros[0].getWidth() / 2, posicaoInicialVertical + passaros[0].getHeight() / 2, passaros[0].getWidth() / 2);
        retanguloCanoTopo = new Rectangle(posicaoMovimentoCanoHorizontal, altura / 2 + espacoCanos / 2 + alturaEntreCanos, canoTopo.getWidth(), canoTopo.getHeight());
        retanguloCanoBaixo = new Rectangle(posicaoMovimentoCanoHorizontal, altura / 2 - canoBaixo.getHeight() - espacoCanos / 2 + alturaEntreCanos, canoBaixo.getWidth(), canoBaixo.getHeight());
        // shape.begin(ShapeRenderer.ShapeType.Filled);
        // shape.circle(passaroCirculo.x, passaroCirculo.y, passaroCirculo.radius);
        // shape.rect(retanguloCanoTopo.x, retanguloCanoTopo.y, retanguloCanoTopo.getWidth(), retanguloCanoTopo.getHeight());
        // shape.rect(retanguloCanoBaixo.x, retanguloCanoBaixo.y, retanguloCanoBaixo.getWidth(), retanguloCanoBaixo.getHeight());
        // shape.end();
        if (Intersector.overlaps(passaroCirculo, retanguloCanoBaixo) || Intersector.overlaps(passaroCirculo, retanguloCanoTopo) || posicaoInicialVertical <= 0 || posicaoInicialVertical >= altura) {
            estadoJogo = 2;
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }
}
