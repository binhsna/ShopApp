package com.binhnc.shopapp.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListMessageResponse {
    @JsonProperty("messages")
    private List<String> messages;
}
