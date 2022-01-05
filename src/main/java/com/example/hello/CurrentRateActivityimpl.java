package com.example.hello;

import io.temporal.activity.Activity;
import io.temporal.activity.ActivityExecutionContext;
import io.temporal.activity.ActivityInfo;
import io.temporal.client.ActivityCompletionClient;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.ext.web.client.WebClient;
import org.json.JSONObject;
import java.net.ConnectException;
import java.util.concurrent.ForkJoinPool;

public class CurrentRateActivityimpl implements CurrentRateActivity {
    private static final Logger log = LoggerFactory.getLogger(CurrentRateActivity.class);

    ActivityCompletionClient completionClient;
    Vertx vertx;

    CurrentRateActivityimpl(ActivityCompletionClient completionClient,Vertx vertx) {
        this.completionClient = completionClient;
        this.vertx = vertx;
    }


    @Override
    public double currentUsdToEgpRate() {

        ActivityExecutionContext ctx = Activity.getExecutionContext();
        ActivityInfo info = ctx.getInfo();

        log.info("namespace=" + info.getActivityNamespace());
        log.info("workflowId=" + info.getWorkflowId());
        log.info("runId=" + info.getRunId());
        log.info("activityId=" + info.getActivityId());
        log.info("activityTimeout=" + info.getScheduledTimestamp());

        WebClient client = WebClient.create(vertx);
        byte[] taskToken = ctx.getInfo().getTaskToken();


        client
                .get(8080, "freecurrencyapi.net", "/api/v2/latest?apikey=1b9dc950-67c2-11ec-818d-df835416afa6")
                .send()
                .onSuccess(response -> {
                    JSONObject obj = new JSONObject(response.body().toString("ISO-8859-1"));
                    obj = obj.getJSONObject("data");
                    System.out.println("Got HTTP response with status " + response.statusCode() + "with data " +
                            obj.getDouble("EGP"));
                    double EGPRate = obj.getDouble("EGP");
                    ForkJoinPool.commonPool().execute(() -> currentUsdToEgpRateAsync(taskToken, EGPRate));
                })
                .onFailure(fail -> {
                    failActivity(taskToken, new ConnectException());
                });

        ctx.doNotCompleteOnReturn();
        return -1;
    }

    void currentUsdToEgpRateAsync(byte[] taskToken, double result) {

        // Complete our workflow activity using ActivityCompletionClient
        this.completionClient.complete(taskToken, result);
    }

    void failActivity(byte[] taskToken, Exception failure) {
        completionClient.completeExceptionally(taskToken, failure);
    }
}

