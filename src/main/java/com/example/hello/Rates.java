package com.example.hello;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Rates {

  @JsonProperty("EGP") public Double EGP;

  @JsonProperty("AUD") public Double AUD;
}
