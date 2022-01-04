package com.example.hello;

import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class CurrentRateWorkflowimpl implements CurrentRateWorkflow {
  private static final Logger logger =
      LoggerFactory.getLogger(CurrentRateWorkflowimpl.class);

  @Override
  public double currentUsdToEgpRate() {
    try {
      String url =
          "https://freecurrencyapi.net/api/v2/latest?apikey=1b9dc950-67c2-11ec-818d-df835416afa6";
      URL urlForGetRequest = new URL(url);
      String readLine = null;
      HttpURLConnection connection =
          (HttpURLConnection)urlForGetRequest.openConnection();
      connection.setRequestMethod("GET");
      int responseCode = connection.getResponseCode();
      if (responseCode == HttpURLConnection.HTTP_OK) {
        BufferedReader in = new BufferedReader(
            new InputStreamReader(connection.getInputStream()));
        StringBuffer response = new StringBuffer();
        while ((readLine = in.readLine()) != null) {
          response.append(readLine);
        }
        in.close();
        JSONObject obj = new JSONObject(response.toString());
        obj = obj.getJSONObject("data");

        return (obj.getDouble("EGP"));
      } else {
        throw new Exception("Error in API Call");
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return -1;
  }
}
