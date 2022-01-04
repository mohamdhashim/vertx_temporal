package com.example.hello;

import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.WorkerFactory;
import io.vertx.core.AbstractVerticle;

public class Worker extends AbstractVerticle {
  @Override
  public void start() {
    WorkflowServiceStubs service = WorkflowServiceStubs.newInstance();
    WorkflowClient client = WorkflowClient.newInstance(service);

    WorkerFactory factory = WorkerFactory.newInstance(client);
    io.temporal.worker.Worker worker =
        factory.newWorker(Shared.Currency_Converter_TASK_QUEUE);

    worker.registerWorkflowImplementationTypes(CurrentRateWorkflowimpl.class);
    worker.registerWorkflowImplementationTypes(ReverseRateWorkflowimpl.class);

    // Start polling the Task Queue.
    factory.start();
  }
}
