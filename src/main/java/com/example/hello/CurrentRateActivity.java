package com.example.hello;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import io.temporal.workflow.WorkflowInterface;

@ActivityInterface
public interface CurrentRateActivity {

    @ActivityMethod
    public double currentUsdToEgpRate();


}
