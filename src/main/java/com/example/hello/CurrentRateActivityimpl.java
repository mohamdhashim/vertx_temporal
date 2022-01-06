package com.example.hello;

import com.fasterxml.jackson.databind.JsonNode;
import io.temporal.activity.Activity;
import io.temporal.activity.ActivityExecutionContext;
import io.temporal.activity.ActivityInfo;
import io.temporal.client.ActivityCompletionClient;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.ext.web.client.WebClient;
import java.io.IOException;
import java.net.ConnectException;
import java.util.concurrent.ForkJoinPool;
import org.json.JSONObject;

public class CurrentRateActivityimpl implements CurrentRateActivity {
  private static final Logger log =
      LoggerFactory.getLogger(CurrentRateActivity.class);

  ActivityCompletionClient completionClient;
  Vertx vertx;

  CurrentRateActivityimpl(ActivityCompletionClient completionClient,
                          Vertx vertx) {
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
        .get(8080, "freecurrencyapi.net",
             "/api/v2/latest?apikey=1b9dc950-67c2-11ec-818d-df835416afa6")
        .send()
        .onSuccess(response -> {
          try {
            JsonNode json = Json.parse(response.body().toString("ISO-8859-1"));
            Rates rates = Json.fromJson(json.get("data"), Rates.class);
            ForkJoinPool.commonPool().execute(
                () -> currentUsdToEgpRateAsync(taskToken, rates.EGP));
          } catch (IOException e) {
            e.printStackTrace();
          }
        })
        .onFailure(
            fail -> { failActivity(taskToken, new ConnectException()); });

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
