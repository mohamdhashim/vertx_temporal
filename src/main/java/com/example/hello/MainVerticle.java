package com.example.hello;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    System.out.println("Helloworld");
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new CurrentRateVerticle());
    vertx.deployVerticle(new ReverseRateVerticle());
    vertx.deployVerticle(new Worker());
  }
}
