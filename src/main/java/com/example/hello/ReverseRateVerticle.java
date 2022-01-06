package com.example.hello;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

public class ReverseRateVerticle extends AbstractVerticle {
  private static final Logger logger = LoggerFactory.getLogger(CurrentRateWorkflowimpl.class);

  public void start(Promise<Void> startPromise) throws Exception {

    WorkflowServiceStubs service = WorkflowServiceStubs.newInstance();

    // WorkflowClient can be used to start, signal, query, cancel, and terminate
    // Workflows.
    WorkflowClient client = WorkflowClient.newInstance(service);
    WorkflowOptions options = WorkflowOptions.newBuilder()
        .setTaskQueue(Shared.Currency_Converter_TASK_QUEUE)
        .build();

    final EventBus eventBus = vertx.eventBus();
    eventBus.consumer("egp", receivedMessage -> {
      logger.info("I have received a message: " + receivedMessage.body());
      receivedMessage.reply(receivedMessage.body());
      ReverseRateWorkflow workflow = client.newWorkflowStub(ReverseRateWorkflow.class, options);
      System.out.println("final Result is: " + workflow.EgpToUsdRate((double) receivedMessage.body(), 20.0));
    });
  }
}
