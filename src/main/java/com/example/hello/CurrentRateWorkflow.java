package com.example.hello;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface CurrentRateWorkflow {

  // @WorkflowMethod
  // public double EgpToUsdRate(double EGP);

  @WorkflowMethod
  public double currentUsdToEgpRate();
}
