package com.example.hello;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;

public class CurrentRateWorkflowimpl implements CurrentRateWorkflow {
  private static final Logger logger = LoggerFactory.getLogger(CurrentRateWorkflowimpl.class);

  @Override
  public double currentUsdToEgpRate() {
    ActivityOptions options = ActivityOptions.newBuilder()
            .setScheduleToCloseTimeout(Duration.ofSeconds(2))
            .build();

    final CurrentRateActivity rate = Workflow.newActivityStub(CurrentRateActivity.class, options);

    return rate.currentUsdToEgpRate();
  }

}
