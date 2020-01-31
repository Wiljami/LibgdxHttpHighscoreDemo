package fi.tuni.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import java.util.ArrayList;

public class MyHighScoreManager {
    /**
     * url of where you get your high score data. What you get depends on the
     * server. In this democase you will receive a json file that will contain
     * up to 10 high score entries ordered by highest score first.
     */
    private static final String GETURL = "https://highscore-demo.herokuapp.com/get/";

    /**
     * url where you send your high score entries. The server will then handle
     * the entry data. You should not have to worry whether the new high score
     * gets to the top10 etc. Server takes care of that.
     */
    private static final String POSTURL = "https://highscore-demo.herokuapp.com/add/";


    public static void fetchHighScores(final HighScoreListener source) {
        Net.HttpRequest request = new Net.HttpRequest(HttpMethods.GET);
        request.setUrl(GETURL);
        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void handleHttpResponse (Net.HttpResponse httpResponse) {
                String result = httpResponse.getResultAsString();
                Json json = new Json();
                ArrayList<HighScoreEntry> highScores = json.fromJson(ArrayList.class, HighScoreEntry.class, result);
                source.receiveHighScore(highScores);
            }

            @Override
            public void failed (Throwable t) {
                Gdx.app.error("MyHighScoreManager", "GET: something went wrong");
                source.failedToRetrieveHighScores(t);
            }

            @Override
            public void cancelled () {
                Gdx.app.log("MyHighScoreManager", "GET: cancelled");
            }

        });
    }

    public static void sendNewHighScore(HighScoreEntry highscore, final HighScoreListener source) {
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
                source.receiveConfirmationOnSend();
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.error("MyHighScoreManager", "POST: something went wrong", t);
                source.failedToSendHighScore(t);
            }

            @Override
            public void cancelled() {
                Gdx.app.log("MyHighScoreManager", "POST: cancelled");
            }
        });
    }
}
