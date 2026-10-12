package com.ficklebytes.request.card;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCardRequest {

  @JsonProperty(value = "amount", required = true)
  private int amount;

  @JsonProperty(value = "price", required = true)
  private int price;


}
