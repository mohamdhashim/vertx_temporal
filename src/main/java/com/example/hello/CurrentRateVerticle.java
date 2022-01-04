package com.example.hello;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

public class CurrentRateVerticle extends AbstractVerticle {

  private static final Logger logger =
      LoggerFactory.getLogger(CurrentRateWorkflowimpl.class);

  public void start(Promise<Void> startPromise) throws Exception {

    WorkflowServiceStubs service = WorkflowServiceStubs.newInstance();

    // WorkflowClient can be used to start, signal, query, cancel, and terminate
    // Workflows.
    WorkflowClient client = WorkflowClient.newInstance(service);
    WorkflowOptions options =
        WorkflowOptions.newBuilder()
            .setTaskQueue(Shared.Currency_Converter_TASK_QUEUE)
            .build();

    CurrentRateWorkflow workflow =
        client.newWorkflowStub(CurrentRateWorkflow.class, options);
    double rate = workflow.currentUsdToEgpRate();

    EventBus eventBus = vertx.eventBus();
    eventBus.send("egp", rate);
  }
}
