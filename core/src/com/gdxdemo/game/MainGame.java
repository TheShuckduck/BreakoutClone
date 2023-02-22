package com.gdxdemo.game;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import java.util.ArrayList;
import java.util.Iterator;

public class MainGame extends ApplicationAdapter {
	private ShapeRenderer shapeRenderer;
	private Ball ball;
	private Paddle paddle;
	private boolean isGameOver;
	private Stage stage;
	private int lives;
	private ParticleEffect smokeEffect;
	private ParticleEffectPool smokePool;
	private Array<ParticleEffectPool.PooledEffect> smokeEffects;
	private SpriteBatch batch;
	private ArrayList<Block> blocks = new ArrayList<>();
	private BlockCreator blockCreator = new BlockCreator();
	private BitmapFont font;

	@Override
	public void create() {
		shapeRenderer = new ShapeRenderer();
		ball = new Ball(150, 200, 10, 7, 3);
		paddle = new Paddle(0, 0, 165, 15);
		lives = 3;
		isGameOver = false;
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		// Create font for displaying lives
		font = new BitmapFont();

		// Create UI Elements
		Skin skin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));
		Table table = new Table();
		table.setFillParent(true);
		table.center();
		Label gameOverLabel = new Label("Game Over", skin, "big");
		TextButton playAgainButton = new TextButton("Play Again", skin);
		TextButton exitButton = new TextButton("Exit", skin);
		playAgainButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				resetGame();
			}
		});
		exitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});
		table.add(gameOverLabel).padBottom(10);
		table.row();
		table.add(playAgainButton).padRight(10).padBottom(10);
		table.add(exitButton).padLeft(10).padBottom(10);
		stage.addActor(table);
		smokeEffects = new Array<ParticleEffectPool.PooledEffect>();
		batch = new SpriteBatch();

		// Creates the blocks by calling the BlockCreator method.
		blocks = blockCreator.createBlocks();

		// Create a new smoke effect and add it to the smokeEffects array  (I need to include both the Smoke.p file
		// AND the pp_circle.png file inside the asset folder).
		ParticleEffect smokeEffect = new ParticleEffect();
		smokeEffect.load(Gdx.files.internal("Particle Park Smoke.p"), Gdx.files.internal(""));
		ParticleEffectPool smokeEffectPool = new ParticleEffectPool(smokeEffect, 10, 100);
		ParticleEffectPool.PooledEffect smokeEffectInstance = smokeEffectPool.obtain();
		smokeEffectInstance.setPosition(50, 50); // set the position of the smoke effect
		smokeEffects.add(smokeEffectInstance);
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (isGameOver) {
			stage.act(Gdx.graphics.getDeltaTime());
			stage.draw();
		} else {
			updateGame();
			drawSmokeEffects();
		}
	}

	private void updateGame() {
		stage.act(Gdx.graphics.getDeltaTime());

		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		Iterator<Block> iterator = blocks.iterator();
		while (iterator.hasNext()) {
			Block block = iterator.next();
			block.draw(shapeRenderer);
			if (ball.collidesWith(block)) {
				ball.reverseY();
				iterator.remove();
			}
		}

		ball.update();
		ball.draw(shapeRenderer);
		paddle.update();
		paddle.draw(shapeRenderer);

		ball.checkCollision(paddle);
		if (ball.y - ball.radius < 0) {
			lives--;
			ball.reset();
			if (lives <= 0) {
				isGameOver = true;
			}
		}

		drawSmokeEffects();

		// Since we are using a shapeRenderer to draw the blocks, ball, and paddle this needs to be ended before
		// drawing the text displaying lives so the lives text will stay on top.
		shapeRenderer.end();

		font.setColor(0.9f, 0.2f, 0.1f, 1);
		batch.begin();
		font.draw(batch, "Lives: " + lives, 20, Gdx.graphics.getHeight() - 5);
		batch.end();
	}


	private void drawSmokeEffects() {
		batch.begin();  // Begin drawing with the SpriteBatch
		for (ParticleEffectPool.PooledEffect effect : smokeEffects) {
			effect.draw(batch);
			effect.update(Gdx.graphics.getDeltaTime());
			if (effect.isComplete()) {
				effect.free();
				smokeEffects.removeValue(effect, true);
			}
		}
		batch.end(); // call end() after all drawing is complete
	}

	public void resetGame() {
		lives = 3;
		isGameOver = false;
		ball.reset();
		paddle.reset();
		blocks.clear();
	}

	@Override
	public void dispose() {
		// ... other disposal code ...

		// Dispose of the smoke effect when the game is closed
		for (ParticleEffectPool.PooledEffect effect : smokeEffects) {
			effect.free();
		}
		smokeEffects.clear();

		// Dispose of the SpriteBatch when the game is closed
		batch.dispose();
	}
}