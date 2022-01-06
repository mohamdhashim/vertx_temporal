package com.example.hello;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface CurrentRateActivity {

    @ActivityMethod
    public double currentUsdToEgpRate();


}
