package fi.tuni.tiko;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.List;


public class MainClass extends ApplicationAdapter implements HighScoreListener {
	private SpriteBatch batch;
	private OrthographicCamera camera;

	private int height = 800;
	private int width = 600;

	@Override
	public void create () {
		//highScore.sendNewHighScore(new HighScoreEntry("libgdx", 9999999));
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, width, height);
		createFont();
		MyHighScoreManager.fetchHighScores(this);
	}

	private void createFont() {
		FreeTypeFontGenerator fontGenerator =
				new FreeTypeFontGenerator(Gdx.files.internal("CheyenneSansNovus-Regular.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter =
				new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 24;
		parameter.color = Color.BLACK;
		font = fontGenerator.generateFont(parameter);
	}


	private BitmapFont font;

	private String textToDraw = "";

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);

		batch.begin();

		font.draw(batch, textToDraw, 20, height-50);

		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
	}

	@Override
	public void receiveHighScore(List<HighScoreEntry> highScores) {
		StringBuilder string = new StringBuilder();
		string.append("Highscores:\n");
		if (highScores == null) {
			textToDraw = string.toString();
		} else {
			for (HighScoreEntry e : highScores) {
				string.append(e.getScore()).append("  ").append(e.getName()).append("\n");
			}
			textToDraw = string.toString();
		}
	}

	@Override
	public void receiveConfirmationOnSend(boolean success) {
		MyHighScoreManager.fetchHighScores(this);
	}
}
