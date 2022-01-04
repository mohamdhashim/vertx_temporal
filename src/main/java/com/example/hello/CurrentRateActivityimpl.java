package com.example.hello;

import io.temporal.activity.Activity;
import io.temporal.activity.ActivityExecutionContext;
import io.temporal.activity.ActivityInfo;
import io.temporal.client.ActivityCompletionClient;
import io.vertx.core.Vertx;
import io.vertx.core.impl.TaskQueue;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.ext.web.client.WebClient;
import org.json.JSONObject;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

public class CurrentRateActivityimpl implements CurrentRateActivity {
    private static final Logger log = LoggerFactory.getLogger(CurrentRateActivity.class);

    ActivityCompletionClient completionClient;
    CurrentRateActivityimpl(ActivityCompletionClient completionClient){
        this.completionClient = completionClient;
    }


    @Override
    public double currentUsdToEgpRate(){

        ActivityExecutionContext ctx = Activity.getExecutionContext();
        ActivityInfo info = ctx.getInfo();

        log.info("namespace=" +  info.getActivityNamespace());
        log.info("workflowId=" + info.getWorkflowId());
        log.info("runId=" + info.getRunId());
        log.info("activityId=" + info.getActivityId());
        log.info("activityTimeout=" + info.getScheduledTimestamp());

        Vertx vertx = Vertx.vertx();
        WebClient client = WebClient.create(vertx);
        byte[] taskToken = ctx.getInfo().getTaskToken();


        try {
            client
                    .get(8080, "freecurrencyapi.net", "/api/v2/latest?apikey=1b9dc950-67c2-11ec-818d-df835416afa6")
                    .send(ar -> {
                        if (ar.succeeded()) {
                            HttpResponse<Buffer> response = ar.result();
                            JSONObject obj = new JSONObject(response.body().toString("ISO-8859-1"));
                            obj = obj.getJSONObject("data");
                            System.out.println("Got HTTP response with status " + response.statusCode() + " with data " +
                                    obj.getDouble("EGP"));

                            double finalObj = obj.getDouble("EGP");
                            ForkJoinPool.commonPool().execute(() -> currentUsdToEgpRateAsync(taskToken, finalObj));

                        } else {
                            System.out.println("faaaaaaaaiiiiiiiiiiiiiiilllll");
                        }
                    });
        }catch (Exception ex) {
            System.out.println("faaaaaaaaiiiiiiiiiiiiiiilllll");
        }

//        try {
//            String url = "https://freecurrencyapi.net/api/v2/latest?apikey=1b9dc950-67c2-11ec-818d-df835416afa6";
//            URL urlForGetRequest = new URL(url);
//            String readLine = null;
//            HttpURLConnection connection = (HttpURLConnection) urlForGetRequest.openConnection();
//            connection.setRequestMethod("GET");
//
//            int responseCode = connection.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                StringBuffer response = new StringBuffer();
//                while ((readLine = in.readLine()) != null) {
//                    response.append(readLine);
//                }
//
//                in.close();
//                JSONObject obj = new JSONObject(response.toString());
//                obj = obj.getJSONObject("data");
//
//                return(obj.getDouble("EGP"));
//
//            } else {
//                throw new Exception("Error in API Call");
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }


        ctx.doNotCompleteOnReturn();
        return -1;
    }

    void currentUsdToEgpRateAsync(byte[] taskToken, double result) {

        // Complete our workflow activity using ActivityCompletionClient
        this.completionClient.complete(taskToken, result);
    }
}

