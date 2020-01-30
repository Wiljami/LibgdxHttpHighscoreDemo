package fi.tuni.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import java.util.ArrayList;

public class MyHighScoreManager {
    private final String GETURL = "https://highscore-demo.herokuapp.com/get/";
    private final String POSTURL = "https://highscore-demo.herokuapp.com/add/";

    private String result = "";
    private ArrayList<HighScoreEntry> highScores;

    public void fetchHighScores() {
        Net.HttpRequest request = new Net.HttpRequest(HttpMethods.GET);
        request.setUrl(GETURL);
        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void handleHttpResponse (Net.HttpResponse httpResponse) {
                result = httpResponse.getResultAsString();
                Json json = new Json();
                highScores = json.fromJson(ArrayList.class, HighScoreEntry.class, result);
            }


            @Override
            public void failed (Throwable t) {
                Gdx.app.error("MyHighScoreManager", "GET: something went wrong", t);
            }

            @Override
            public void cancelled () {
                Gdx.app.log("MyHighScoreManager", "GET: cancelled");
            }

        });
    }

    public void sendNewHighScore(HighScoreEntry highscore) {
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);

        String content = json.toJson(highscore);

        Net.HttpRequest request = new Net.HttpRequest(HttpMethods.POST);
        request.setUrl(POSTURL);
        request.setHeader("Content-type", "application/json");
        request.setContent(content);

        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                Gdx.app.log("MyHighScoreManager", "POST: success");
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.error("MyHighScoreManager", "POST: something went wrong", t);

            }

            @Override
            public void cancelled() {
                Gdx.app.log("MyHighScoreManager", "POST: cancelled");
            }
        });
    }

    public ArrayList<HighScoreEntry> getHighScores() {
        return highScores;
    }
}
