package com.example.hello;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface ReverseRateWorkflow {

  @WorkflowMethod public double EgpToUsdRate(double rate, double count);
}
