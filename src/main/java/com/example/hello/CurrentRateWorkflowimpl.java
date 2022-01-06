package com.example.hello;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import java.time.Duration;

public class CurrentRateWorkflowimpl implements CurrentRateWorkflow {
  private static final Logger logger =
      LoggerFactory.getLogger(CurrentRateWorkflowimpl.class);

  @Override
  public double currentUsdToEgpRate() {
    ActivityOptions options =
        ActivityOptions.newBuilder()
            .setScheduleToCloseTimeout(Duration.ofSeconds(5))
            .build();

    final CurrentRateActivity rate =
        Workflow.newActivityStub(CurrentRateActivity.class, options);

    return rate.currentUsdToEgpRate();
  }
}
