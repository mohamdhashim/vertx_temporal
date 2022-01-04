package com.example.hello;

import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

public class ReverseRateWorkflowimpl implements ReverseRateWorkflow {
  private static final Logger logger = LoggerFactory.getLogger(ReverseRateWorkflow.class);

  @Override
  public double EgpToUsdRate(double rate, double count) {
    return count * (1 / rate);
  }
}
