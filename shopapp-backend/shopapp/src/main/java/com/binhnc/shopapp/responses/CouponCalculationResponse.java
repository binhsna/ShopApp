package com.binhnc.shopapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponCalculationResponse {
    @JsonProperty("result")
    private Double result;

    // errorCode?
    @JsonProperty("error_message")
    private String errorMessage;
}
