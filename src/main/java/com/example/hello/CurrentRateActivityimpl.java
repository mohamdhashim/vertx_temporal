package com.example.hello;

import io.temporal.activity.Activity;
import io.temporal.activity.ActivityExecutionContext;
import io.temporal.activity.ActivityInfo;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CurrentRateActivityimpl implements CurrentRateActivity {
    private static final Logger log = LoggerFactory.getLogger(CurrentRateActivity.class);

    @Override
    public double currentUsdToEgpRate(){

        ActivityExecutionContext ctx = Activity.getExecutionContext();
        ActivityInfo info = ctx.getInfo();

        log.info("namespace=" +  info.getActivityNamespace());
        log.info("workflowId=" + info.getWorkflowId());
        log.info("runId=" + info.getRunId());
        log.info("activityId=" + info.getActivityId());
        log.info("activityTimeout=" + info.getScheduledTimestamp());

        try {
            String url = "https://freecurrencyapi.net/api/v2/latest?apikey=1b9dc950-67c2-11ec-818d-df835416afa6";
            URL urlForGetRequest = new URL(url);
            String readLine = null;
            HttpURLConnection connection = (HttpURLConnection) urlForGetRequest.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer response = new StringBuffer();
                while ((readLine = in.readLine()) != null) {
                    response.append(readLine);
                }

                in.close();
                JSONObject obj = new JSONObject(response.toString());
                obj = obj.getJSONObject("data");

                return(obj.getDouble("EGP"));

            } else {
                throw new Exception("Error in API Call");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        ctx.doNotCompleteOnReturn();
        return -1;
    }
}

